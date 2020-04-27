package nl.overheid.aerius.wui.atlas.factories;

import nl.overheid.aerius.wui.atlas.place.StoryPlace;
import nl.overheid.aerius.wui.atlas.ui.story.StoryActivity;

public interface AtlasActivityFactory {
  StoryActivity createStoryPresenter(StoryPlace place);
}
