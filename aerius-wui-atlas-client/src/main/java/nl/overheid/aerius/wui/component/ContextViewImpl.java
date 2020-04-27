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

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.command.ActivateBigContextCommand;
import nl.overheid.aerius.wui.atlas.command.ActivateSmallContextCommand;
import nl.overheid.aerius.wui.atlas.command.CompactModeActivationCommand;
import nl.overheid.aerius.wui.atlas.command.CompactModeDeactivationCommand;
import nl.overheid.aerius.wui.atlas.command.ContextPanelCollapseCommand;
import nl.overheid.aerius.wui.atlas.command.ContextPanelOpenCommand;
import nl.overheid.aerius.wui.atlas.command.PanelSelectionChangeCommand;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.ContextCompositionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.ContextPanelCollapseEvent;
import nl.overheid.aerius.wui.atlas.event.ContextPanelOpenEvent;
import nl.overheid.aerius.wui.atlas.event.PanelConfigurationChangeEvent;
import nl.overheid.aerius.wui.atlas.event.PanelSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.factories.ContextWidgetFactory;
import nl.overheid.aerius.wui.atlas.ui.context.MonitorUpPanel;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.HasEventBus;

@Singleton
public class ContextViewImpl extends EventComposite implements ContextView {
  private static final String COLLAPSED_WIDTH = "50px";
  private static final String DEFAULT_SMALL_WIDTH = "35%";
  private static final String DEFAULT_BIG_WIDTH = "55%";
  private static final int WIDTH_MIN = 455;
  private boolean big = false;

  private final ContextViewEventBinder EVENT_BINDER = GWT.create(ContextViewEventBinder.class);

  interface ContextViewEventBinder extends EventBinder<ContextViewImpl> {}

  private static final ContextViewUiBinder UI_BINDER = GWT.create(ContextViewUiBinder.class);

  interface ContextViewUiBinder extends UiBinder<Widget, ContextViewImpl> {}

  public interface CustomStyle extends CssResource {}

  @UiField CustomStyle style;
  @UiField(provided = true) FlowPanel container;

  private final LinkedHashMap<PanelNames, MonitorUpPanel> panels = new LinkedHashMap<>();

  private IsWidget selected;
  private final ContextWidgetFactory factory;

  private boolean open = true;
  private String fullWidth = getActualFullWidth();
  private HandlerRegistration resizeHandler;
  private boolean compactMode;
  private boolean updateScheduled;

  @Inject
  public ContextViewImpl(final ContextWidgetFactory factory) {
    this.factory = factory;
    this.container = new FlowPanel();

    initWidget(UI_BINDER.createAndBindUi(this));

    setPanelOpen();
  }

  private String getActualFullWidth() {
    return big ? DEFAULT_BIG_WIDTH : DEFAULT_SMALL_WIDTH;
  }

  @EventHandler
  public void onActivateBigContextCommand(final ActivateBigContextCommand c) {
    setPanelSizeBig(true);
  }

  @EventHandler
  public void onActivateSmallContextCommand(final ActivateSmallContextCommand c) {
    setPanelSizeBig(false);
  }

  private void setPanelSizeBig(final boolean big) {
    this.big = big;
    this.fullWidth = getActualFullWidth();
    updateWidth();
    refreshPanelWidth();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    EVENT_BINDER.bindEventHandlers(this, eventBus);

    super.setEventBus(eventBus);

    notifyContextWidth();
  }

  @EventHandler
  public void onContextOptionsChangedEvent(final PanelConfigurationChangeEvent e) {
    panels.values().forEach(MonitorUpPanel::retire);
    panels.clear();
    container.clear();

    e.getValue().entrySet().forEach(v -> addPanel(v.getValue()));
  }

  @EventHandler
  public void onChapterChangeEvent(final ChapterSelectionChangeEvent e) {
    final Map<PanelNames, PanelContent> panelContentMap = e.getValue().panels();
    panels.forEach((k, v) -> {
      if (v.supportsPanelContent(k)) {
        v.setPanelContent(panelContentMap.get(k));
      }
    });
  }

  @EventHandler
  public void onPanelSelectionChangeCommand(final PanelSelectionChangeCommand e) {
    MonitorUpPanel view;
    if (!panels.containsKey(e.getValue())) {
      view = findDefaultPanel();
    } else {
      view = panels.get(e.getValue());
    }

    if (e.getValue() != null && view.equals(selected)) {
      if (open && e.getValue() != PanelNames.PANEL_LAYER) {
        eventBus.fireEvent(new ContextPanelCollapseCommand());
      } else {
        eventBus.fireEvent(new ContextPanelOpenCommand());
      }
    } else {
      eventBus.fireEvent(new ContextPanelOpenCommand());
    }
  }

  @EventHandler
  public void onPanelSelectionChangeEvent(final PanelSelectionChangeEvent e) {
    MonitorUpPanel view;
    if (!panels.containsKey(e.getValue())) {
      view = findDefaultPanel();
    } else {
      view = panels.get(e.getValue());
    }

    if (view == null) {
      return;
    }

    if (e.getValue() == null && compactMode) {
      eventBus.fireEvent(new ContextPanelCollapseCommand());
    } else if (view != selected) {
      container.add(view);
      select(view);
    }
  }

  @EventHandler
  public void onContextPanelCollapseEvent(final ContextPanelCollapseEvent e) {
    setPanelOpen(false);
  }

  @EventHandler
  public void onContextPanelOpenEvent(final ContextPanelOpenEvent e) {
    setPanelOpen(true);
  }

  private void refreshPanelWidth() {
    setPanelOpen(open);
  }

  private void setPanelOpen(final boolean open) {
    if (open) {
      setPanelOpen();
    } else {
      setPanelClosed();
    }

    this.open = open;
  }

  private void setPanelOpen() {
    if (selected != null) {
      new Timer() {
        @Override
        public void run() {
          if (open) {
            selected.asWidget().getElement().getStyle().clearWidth();
          }
        }
      }.schedule(300);
    }

    updateWidth();
  }

  private void updateWidth() {
    notifyContextWidth();
    Scheduler.get().scheduleFixedDelay(() -> {
      notifyContextWidth();
      return false;
    }, 300);

    getElement().getStyle().setProperty("width", fullWidth);
    getElement().getStyle().setProperty("minWidth", WIDTH_MIN, Unit.PX);
    getElement().getStyle().setOpacity(1);
  }

  private void notifyContextWidth() {
    if (eventBus != null) {
      eventBus.fireEvent(new ContextCompositionChangeEvent(container.getOffsetWidth()));
    }
  }

  private void setPanelClosed() {
    if (selected != null) {
      selected.asWidget().getElement().getStyle().setProperty("width", container.getOffsetWidth(), Unit.PX);
    }

    notifyContextWidth();
    Scheduler.get().scheduleFixedDelay(() -> {
      notifyContextWidth();
      return false;
    }, 300);

    getElement().getStyle().setProperty("width", COLLAPSED_WIDTH);
    getElement().getStyle().clearProperty("minWidth");
    Scheduler.get().scheduleFixedDelay(() -> {
      getElement().getStyle().setOpacity(0);
      return false;
    }, 250);
  }

  private MonitorUpPanel findDefaultPanel() {
    return panels.values().stream()
        .filter(v -> v.hasPanelContent())
        .findFirst()
        .orElse(null);
  }

  private void addPanel(final PanelConfiguration conf) {
    if (conf.getParent() != null) {
      addPanel(conf.getParent());

      panels.put(PanelNames.fromName(conf.getName()), panels.get(PanelNames.fromName(conf.getParent().getName())));
      return;
    }

    final MonitorUpPanel panel = panels.containsKey(PanelNames.fromName(conf.getName())) ? null : getContextPanel(conf);
    if (panel == null) {
      return;
    }

    panels.put(PanelNames.fromName(conf.getName()), panel);
    container.add(panel);
    panel.asWidget().getElement().getStyle().setOpacity(0);
  }

  @EventHandler
  public void onCompactModeActivationCommand(final CompactModeActivationCommand c) {
    compactMode = true;
    resizeHandler = Window.addResizeHandler(e -> {
      updateFullSizeWindowMode();
    });
    updateFullSizeWindowMode();
  }

  private void updateFullSizeWindowMode() {
    if (!compactMode) {
      return;
    }

    if (!updateScheduled) {
      updateScheduled = true;
      Scheduler.get().scheduleDeferred(() -> {
        if (!compactMode) {
          return;
        }

        fullWidth = Math.max(Window.getClientWidth(), WIDTH_MIN) + "px";
        setPanelOpen(open);
        updateScheduled = false;
      });
    }
  }

  @EventHandler
  public void onCompactModeDeactivationCommand(final CompactModeDeactivationCommand c) {
    compactMode = false;
    resizeHandler.removeHandler();
    fullWidth = getActualFullWidth();
    setPanelOpen(open);
  }

  private MonitorUpPanel getContextPanel(final PanelConfiguration conf) {
    MonitorUpPanel panel;

    switch (conf.asConfigurationProperties().getPanelType()) {
    case EXPORT:
      panel = factory.getContextExportView(conf);
      break;
    case INFO:
      panel = factory.getContextInfoView(conf);
      break;
    case META:
      panel = factory.getContextMetaView(conf);
      break;
    case LAYERS:
      panel = factory.getContextLayerView(conf);
      break;
    case MAP:
      panel = factory.getContextMapView(conf);
      break;
    case LOCATION:
      panel = factory.getContextLocationView(conf);
      break;
    case PREFERENCES:
      panel = factory.getContextPreferencesView(conf);
      break;
    default:
      throw new RuntimeException("Option not implemented: " + conf.asConfigurationProperties().getPanelType());
    }

    if (panel instanceof HasEventBus) {
      ((HasEventBus) panel).setEventBus(eventBus);
    }

    return panel;
  }

  private void select(final MonitorUpPanel view) {
    if (view == null) {
      final int width = container.getOffsetWidth();
      retractExcept(width, null);
    }

    Scheduler.get().scheduleFixedDelay(() -> {
      final int width = container.getOffsetWidth();

      view.asWidget().getElement().getStyle().setLeft(0, Unit.PX);
      view.asWidget().getElement().getStyle().setOpacity(1);
      this.selected = view;
      view.show();

      retractExcept(width, view);
      return false;
    }, 20);
  }

  private void retractExcept(final int width, final MonitorUpPanel view) {
    for (final IsWidget context : panels.values()) {
      if (context.equals(view)) {
        continue;
      }

      delayedSnapback(width, context);
    }
  }

  private void delayedSnapback(final int width, final IsWidget context) {
    Scheduler.get().scheduleFixedDelay(() -> {
      if (context.equals(selected)) {
        return false;
      }

      context.asWidget().getElement().getStyle().setLeft(-width, Unit.PX);
      context.asWidget().getElement().getStyle().setOpacity(0);
      return false;
    }, 200);
  }
}
