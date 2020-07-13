package nl.overheid.aerius.wui.component;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.geo.command.MapResizeCommand;
import nl.overheid.aerius.geo.command.MapSetExtentCommand;
import nl.overheid.aerius.geo.domain.IsMapCohort;
import nl.overheid.aerius.geo.event.MapEventBus;
import nl.overheid.aerius.geo.wui.Map;
import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.atlas.event.MapActiveEvent;
import nl.overheid.aerius.wui.atlas.service.LayerServiceAsync;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.domain.map.MapContext;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.util.WidgetUtil;
import nl.overheid.aerius.wui.widget.HasEventBus;

public class ChapterWidgetMapDelegate extends BasicEventComponent implements PanelWidgetDelegate, HasEventBus, IsMapCohort {
  private final FlowPanel container;

  private final Map map;
  private final AcceptsOneWidget panel;

  private final MapContext mapContext;
  private final LayerServiceAsync bboxService;

  private boolean attached;
  private final StoryContext storyContext;
  private final MapSearchWidget searchWidget;

  @Inject
  public ChapterWidgetMapDelegate(final @Assisted AcceptsOneWidget panel, @Assisted final Chapter chapter, final Map map,
      final MapContext mapContext, final StoryContext storyContext,
      final LayerServiceAsync bboxService, final MapSearchWidget searchWidget) {
    this.panel = panel;
    this.map = map;

    container = new FlowPanel();
    container.getElement().getStyle().setPosition(Position.RELATIVE);
    container.addStyleName(AtlasR.css().flex());

    WidgetUtil.asWidgetIfWidget(map).addStyleName(AtlasR.css().grow());

    container.add(searchWidget);
    container.add(WidgetUtil.asWidgetIfWidget(map));

    this.mapContext = mapContext;
    this.storyContext = storyContext;
    this.bboxService = bboxService;
    this.searchWidget = searchWidget;

    map.setUniqueId(UglyBoilerPlate.generateUniqueMapId(storyContext.getStory(), chapter, PanelNames.PANEL_MAIN.getName()));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    map.setEventBus(eventBus);
    map.registerEventCohort(this);
    map.registerEventCohort(searchWidget);
  }

  @Override
  public void clear() {
    eventBus.fireEvent(new MapActiveEvent(false));
  }

  @Override
  public void show() {
    eventBus.fireEvent(new MapActiveEvent(true));
    mapContext.claimMapPrimacy(map);
    panel.setWidget(container);
    map.attach();

    eventBus.fireEvent(new MapResizeCommand());

    if (!attached) {
      // Zoom to extent
      if (UglyBoilerPlate.hasArea()) {
        bboxService.getBbox(storyContext.getDataset(), String.valueOf(UglyBoilerPlate.getAreaId()),
            v -> eventBus.fireEvent(new MapSetExtentCommand(v.getBbox())));
      }
    }

    attached = true;
  }

  @Override
  public void notifySelector(final Selector selector) {}

  @Override
  public void notifyMap(final Map map, final MapEventBus mapEventBus) {
    super.setEventBus(mapEventBus, searchWidget);
  }
}
