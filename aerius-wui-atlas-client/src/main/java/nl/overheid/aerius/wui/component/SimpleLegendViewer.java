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

import java.util.Optional;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.MainContentType;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.properties.LegendProperties;
import nl.overheid.aerius.wui.atlas.command.LegendDisplayCommand;
import nl.overheid.aerius.wui.atlas.command.LegendHiddenCommand;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.factories.MainLegendWidgetFactory;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.widget.AnimatedFlowPanel;
import nl.overheid.aerius.wui.widget.EventComposite;

public class SimpleLegendViewer extends EventComposite implements LegendViewer {
  private static final String PANEL_NAME = "panel_legend";

  interface SimpleLegendViewerEventBinder extends EventBinder<SimpleLegendViewer> {}

  private final SimpleLegendViewerEventBinder EVENT_BINDER = GWT.create(SimpleLegendViewerEventBinder.class);

  private static final SimpleLegendViewerUiBinder UI_BINDER = GWT.create(SimpleLegendViewerUiBinder.class);

  interface SimpleLegendViewerUiBinder extends UiBinder<Widget, SimpleLegendViewer> {}

  private final MainLegendWidgetFactory legendWidgetFactory;

  @UiField Label legendTitleField;

  @UiField LegendToggleButton toggleButton;
  @UiField AnimatedFlowPanel targetPanel;

  private boolean visible;

  @Inject
  public SimpleLegendViewer(final MainLegendWidgetFactory legendWidgetFactory) {
    this.legendWidgetFactory = legendWidgetFactory;

    initWidget(UI_BINDER.createAndBindUi(this));

    toggleButton.ensureDebugId(AtlasTestIDs.BUTTON_TOGGLE_LEGEND);
  }

  @EventHandler
  public void onChapterSelectionChangedEvent(final ChapterSelectionChangeEvent e) {
    final PanelContent panelContent = e.getValue().panels().get(PanelNames.PANEL_LEGEND);

    Optional<Widget> delegate;
    if (panelContent != null) {
      final LegendProperties legendProperties = panelContent.asLegendProperties();
      switch (legendProperties.getContentType()) {
      case COMPONENT:
        delegate = Optional.of(legendWidgetFactory.getComponentLegend(panelContent, eventBus));
        break;
      case TEXT:
        delegate = Optional.of(legendWidgetFactory.getTextLegend(panelContent));
        break;
      default:
        delegate = Optional.empty();
        break;
      }
    } else {
      delegate = Optional.empty();
    }

    // Hide if the main panel is a Map (and there is no legend)
    setVisible(delegate.isPresent() || !e.getValue().getMainPanel().asMainProperties().getContentType().equals(MainContentType.MAP));

    // Set the legend content
    setLegend(delegate);
  }

  private void setLegend(final Optional<Widget> delegate) {
    targetPanel.clear();
    delegate.ifPresent(d -> targetPanel.add(d));

    toggleButton.setEnabled(delegate.isPresent());

    setLegendVisible(visible && delegate.isPresent(), false, false);
  }

  @EventHandler
  public void onLegendDisplayCommand(final LegendDisplayCommand c) {
    setLegendVisible(true);
  }

  @EventHandler
  public void onLegendHiddenCommand(final LegendHiddenCommand c) {
    setLegendVisible(false);
  }

  private void setLegendVisible(final boolean visible) {
    setLegendVisible(visible, true);
  }

  private void setLegendVisible(final boolean visible, final boolean persist) {
    setLegendVisible(visible, true, true);
  }

  private void setLegendVisible(final boolean visible, final boolean persist, final boolean animate) {
    if (persist) {
      this.visible = visible;
    }

    legendTitleField.setText(visible ? M.messages().legendTitle() : null);
    targetPanel.setVisible(visible, animate);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER, toggleButton);
  }
}
