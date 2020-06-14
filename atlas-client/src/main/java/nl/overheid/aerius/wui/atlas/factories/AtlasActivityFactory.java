package nl.overheid.aerius.wui.atlas.factories;

import nl.overheid.aerius.wui.atlas.place.MonitorStoryPlace;
import nl.overheid.aerius.wui.atlas.ui.story.WidgetStoryActivity;

public interface AtlasActivityFactory {
  WidgetStoryActivity createStoryPresenter(MonitorStoryPlace place);
}
