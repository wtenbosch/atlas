package nl.overheid.aerius.wui.component;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.MapBuilder;
import nl.overheid.aerius.geo.command.MapResizeCommand;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.wui.atlas.event.StorySelectionChangeEvent;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.widget.HasEventBus;
import nl.overheid.aerius.wui.widget.SwitchPanel;

public class MapPanelSwitcher extends SwitchPanel implements HasEventBus {
  interface LayerPanelSwitcherEventBinder extends EventBinder<MapPanelSwitcher> {}

  private final LayerPanelSwitcherEventBinder EVENT_BINDER = GWT.create(LayerPanelSwitcherEventBinder.class);

  private final java.util.Map<String, Integer> panels = new HashMap<>();
  private final java.util.Map<String, Map> maps = new HashMap<>();

  private final MapBuilder mapBuilder;

  private EventBus eventBus;

  private final StoryContext storyContext;
  private final MapContext mapContext;

  private String activeKey;

  @Inject
  public MapPanelSwitcher(final MapBuilder mapBuilder, final StoryContext storyContext, final MapContext mapContext) {
    this.mapBuilder = mapBuilder;
    this.storyContext = storyContext;
    this.mapContext = mapContext;
  }

  @EventHandler
  public void onStoryChangeEvent(final StorySelectionChangeEvent e) {
    reset();
  }

  private void reset() {
    panels.clear();
    clear();
  }

  private void initializeMapPanel(final String key) {
    final SimplePanel panel = new SimplePanel();
    final Map map = mapBuilder.getMap();

    panel.setWidget(map);
    panels.put(key, getWidgetCount());
    maps.put(key, map);
    map.asWidget().getElement().getStyle().setWidth(100, Unit.PCT);
    map.asWidget().getElement().getStyle().setHeight(100, Unit.PCT);
    add(panel);

    map.setEventBus(eventBus);
    map.attach();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  public void show() {
    final String key = tryGetKey();
    if (key.equals(activeKey)) {
      return;
    }

    activeKey = key;

    if (!panels.containsKey(activeKey)) {
      initializeMapPanel(activeKey);
    }

    showWidget(panels.get(activeKey));

    final Map map = maps.get(activeKey);

    mapContext.claimMapPrimacy(map);
    eventBus.fireEvent(new MapResizeCommand());
  }

  private String tryGetKey() {
    if (storyContext.getStory() == null || storyContext.getChapter() == null) {
      return "";
    }

    return storyContext.getStory().uid() + "-" + storyContext.getChapter().uid();
  }
}
