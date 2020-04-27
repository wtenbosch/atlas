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
package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.geo.command.LayerHiddenCommand;
import nl.overheid.aerius.geo.command.LayerOpacityCommand;
import nl.overheid.aerius.geo.command.LayerVisibleCommand;
import nl.overheid.aerius.geo.domain.IsLayer;
import nl.overheid.aerius.geo.resources.GeoResources;
import nl.overheid.aerius.wui.atlas.daemon.config.ConfigurationDaemon;
import nl.overheid.aerius.wui.atlas.daemon.config.Configurations;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.FlipButton;
import nl.overheid.aerius.wui.widget.SliderBar;

public class LayerPanelSimpleItem extends EventComposite implements LayerPanelItem {
  private static final LayerPanelItemUiBinder UI_BINDER = GWT.create(LayerPanelItemUiBinder.class);

  interface LayerPanelItemUiBinder extends UiBinder<Widget, LayerPanelSimpleItem> {}

  public interface CustomStyle extends CssResource {
    String clustered();
  }

  @UiField CustomStyle style;

  @UiField SimplePanel innerCluster;
  @UiField SimplePanel hideButtonContainer;

  @UiField(provided = true) FlipButton flipButton;
  @UiField(provided = true) ToggleButton hideButton;
  @UiField Label name;

  @UiField FlowPanel selectorContainer;

  @UiField(provided = true) SliderBar opacitySlider;
  @UiField Widget inner;
  @UiField AcceptsOneWidget legend;

  private final IsLayer<?> layer;

  private boolean hidden;
  private final EventBus mapEventBus;

  private boolean autoExpand;

  public LayerPanelSimpleItem(final IsLayer<?> layer, final EventBus mapEventBus, final GeoResources res,
      final ObservingReplacementAssistant replacer, final ConfigurationDaemon configDaemon) {
    this.layer = layer;
    this.mapEventBus = mapEventBus;
    this.flipButton = new FlipButton(v -> {
      inner.setVisible(v);
      name.getElement().getStyle().setFontWeight(v ? FontWeight.BOLD : FontWeight.NORMAL);
    });
    flipButton.setClosedRotation(FlipButton.EAST);

    hideButton = new ToggleButton(new Image(res.toggleLayerOn().getSafeUri()), new Image(res.toggleLayerOff().getSafeUri()));

    opacitySlider = new SliderBar(o -> onSetLayerOpacity(o));
    configDaemon.registerAsBoolean(Configurations.LAYER_OPACITY, v -> {
      opacitySlider.setVisible(v);
    });
    configDaemon.registerAsBoolean(Configurations.LAYER_EXPAND, v -> {
      autoExpand = v;
    });

    initWidget(UI_BINDER.createAndBindUi(this));
    flipButton.init();

    layer.getInfo().ifPresent(info -> {
      replacer.register(info.getTitle(), v -> name.setText(v));
      // Programmatic naming.
      // replacer.register(info.getFriendlyName().orElse("n/a"), name::setText);
    });

    name.addClickHandler(e -> flipButton.toggle());

    setLayerVisible(layer, true);
  }

  @Override
  public void setClustered(final boolean clustered) {
    hideButtonContainer.setStyleName(style.clustered(), clustered);
    innerCluster.setStyleName(style.clustered(), clustered);
  }

  @Override
  public void setLayerVisible(final IsLayer<?> layer, final boolean visible) {
    if (autoExpand) {
      flipButton.setState(!visible);
    }

    hideButton.setDown(!visible);
    this.hidden = !visible;
  }

  @UiHandler("hideButton")
  public void onRemoveButtonClick(final ClickEvent e) {
    mapEventBus.fireEvent(hidden ? new LayerVisibleCommand(layer) : new LayerHiddenCommand(layer));
  }

  public void onSetLayerOpacity(final double o) {
    if (hideButton.isDown()) {
      eventBus.fireEvent(new LayerVisibleCommand(layer));
    }

    mapEventBus.fireEvent(new LayerOpacityCommand(layer, o));
  }

  @Override
  public void remove(final IsLayer<?> isLayer) {
    removeFromParent();
  }

  @Override
  public void setLayerOpacity(final double opacity) {
    opacitySlider.setValue(opacity);
  }

  public void setLegendPanel(final Widget legendWidget) {
    legend.setWidget(legendWidget);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    SelectorUtil.populateSelectorWidget(selectorContainer, layer.getInfo().get().getSelectables(), eventBus);
  }
}
