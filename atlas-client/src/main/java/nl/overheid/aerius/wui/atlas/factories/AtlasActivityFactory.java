package nl.overheid.aerius.wui.atlas.factories;

import nl.overheid.aerius.wui.atlas.place.StoryPlace;
import nl.overheid.aerius.wui.atlas.ui.story.WidgetStoryActivity;

public interface AtlasActivityFactory {
  WidgetStoryActivity createStoryPresenter(StoryPlace place);
}
