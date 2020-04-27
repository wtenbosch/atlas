/*
 * Copyright Dutch Ministry of Agriculture, Nature and Food Quality
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package nl.overheid.aerius.wui.atlas.ui.context.preferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import nl.overheid.aerius.wui.atlas.daemon.config.ConfigurationDaemon;
import nl.overheid.aerius.wui.atlas.daemon.config.Configurations;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.ui.context.info.BasicPanelComposite;
import nl.overheid.aerius.wui.config.EnvironmentConfiguration;
import nl.overheid.aerius.wui.i18n.M;

public class ContextPreferencesViewImpl extends BasicPanelComposite implements ContextPreferencesView {
  private static final ContextPreferencesViewImplUiBinder UI_BINDER = GWT.create(ContextPreferencesViewImplUiBinder.class);

  interface ContextPreferencesViewImplUiBinder extends UiBinder<Widget, ContextPreferencesViewImpl> {}

  public interface CustomStyle extends CssResource {
    String active();
  }

  @UiField CustomStyle style;

  @UiField Button kgActivate;
  @UiField Button molActivate;

  @UiField Button labelsActivate;
  @UiField Button labelsDeactivate;

  @UiField Button autoLayersActivate;
  @UiField Button autoLayersDeactivate;

  @UiField Button layerOpacityActivate;
  @UiField Button layerOpacityDeactivate;

  @UiField Label applicationVersion;

  private final ConfigurationDaemon configDaemon;

  @Inject
  public ContextPreferencesViewImpl(final ConfigurationDaemon configDaemon, final EnvironmentConfiguration cfg) {
    this.configDaemon = configDaemon;
    initWidget(UI_BINDER.createAndBindUi(this));

    configDaemon.registerAsBoolean(Configurations.NUMBER_IN_MAP, v -> setLabelsActive(v));
    configDaemon.register(Configurations.UNIT, v -> {
      switch (v) {
      case "kg":
        setKgActive(true);
        break;
      case "mol":
        setKgActive(false);
        break;
      }
    });
    configDaemon.registerAsBoolean(Configurations.LAYER_EXPAND, v -> setExpandActive(v));
    configDaemon.registerAsBoolean(Configurations.LAYER_OPACITY, v -> setOpacityActive(v));
    kgActivate.ensureDebugId(AtlasTestIDs.BUTTON_KG_ACTIVATE);
    molActivate.ensureDebugId(AtlasTestIDs.BUTTON_MOL_ACTIVATE);
    labelsActivate.ensureDebugId(AtlasTestIDs.BUTTON_LABELS_ACTIVATE);
    labelsDeactivate.ensureDebugId(AtlasTestIDs.BUTTON_LABELS_DEACTIVATE);
    autoLayersActivate.ensureDebugId(AtlasTestIDs.BUTTON_AUTO_LAYERS_ACTIVATE);
    autoLayersDeactivate.ensureDebugId(AtlasTestIDs.BUTTON_AUTO_LAYERS_DEACTIVATE);
    layerOpacityActivate.ensureDebugId(AtlasTestIDs.BUTTON_LAYER_OPACITY_ACTIVATE);
    layerOpacityDeactivate.ensureDebugId(AtlasTestIDs.BUTTON_LAYER_OPACITY_DEACTIVATE);

    applicationVersion.setText(M.messages().applicationVersion(cfg.getApplicationVersion() == null ? "-" : cfg.getApplicationVersion()));
  }

  @UiHandler("kgActivate")
  public void onKgActivate(final ClickEvent e) {
    configDaemon.persistConfigurationChange(Configurations.UNIT, "kg");
  }

  @UiHandler("molActivate")
  public void onMolActivate(final ClickEvent e) {
    configDaemon.persistConfigurationChange(Configurations.UNIT, "mol");
  }

  @UiHandler("labelsActivate")
  public void onLabelsActivate(final ClickEvent e) {
    configDaemon.persistConfigurationChange(Configurations.NUMBER_IN_MAP, true);
  }

  @UiHandler("labelsDeactivate")
  public void onLabelsDeactivate(final ClickEvent e) {
    configDaemon.persistConfigurationChange(Configurations.NUMBER_IN_MAP, false);
  }

  @UiHandler("autoLayersActivate")
  public void onAutoLayersActivate(final ClickEvent e) {
    configDaemon.persistConfigurationChange(Configurations.LAYER_EXPAND, true);
  }

  @UiHandler("autoLayersDeactivate")
  public void onAutoLayersDeactivate(final ClickEvent e) {
    configDaemon.persistConfigurationChange(Configurations.LAYER_EXPAND, false);
  }

  @UiHandler("layerOpacityActivate")
  public void onLayerOpacityActivate(final ClickEvent e) {
    configDaemon.persistConfigurationChange(Configurations.LAYER_OPACITY, true);
  }

  @UiHandler("layerOpacityDeactivate")
  public void onLayerOpacityDeactivate(final ClickEvent e) {
    configDaemon.persistConfigurationChange(Configurations.LAYER_OPACITY, false);
  }

  private void setKgActive(final boolean active) {
    kgActivate.setStyleName(style.active(), active);
    molActivate.setStyleName(style.active(), !active);
  }

  private void setLabelsActive(final boolean active) {
    labelsActivate.setStyleName(style.active(), active);
    labelsDeactivate.setStyleName(style.active(), !active);
  }

  private void setExpandActive(final boolean active) {
    autoLayersActivate.setStyleName(style.active(), active);
    autoLayersDeactivate.setStyleName(style.active(), !active);
  }

  private void setOpacityActive(final boolean active) {
    layerOpacityActivate.setStyleName(style.active(), active);
    layerOpacityDeactivate.setStyleName(style.active(), !active);
  }

  @Override
  public boolean hasPanelContent() {
    return true;
  }

  @Override
  public void show() {

  }
}
