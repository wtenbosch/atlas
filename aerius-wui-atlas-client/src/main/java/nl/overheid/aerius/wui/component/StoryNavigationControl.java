package nl.overheid.aerius.wui.component;

import com.google.gwt.resources.client.DataResource;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.command.LibraryItemSelectionCommand;
import nl.overheid.aerius.wui.atlas.command.StorySelectionChangeCommand;
import nl.overheid.aerius.wui.util.StoryNavigationImageUtil;
import nl.overheid.aerius.wui.widget.SimpleMaskedButton;

public class StoryNavigationControl extends SimpleMaskedButton<StoryInformation> {
  public StoryNavigationControl(final StoryInformation option, final EventBus eventBus) {
    super(option);

    setEventBus(eventBus);
  }

  @Override
  protected void onEnsureDebugId(final String baseID) {
    super.onEnsureDebugId(baseID);

    button.ensureDebugId(baseID + "-" + getLabel(option));
  }

  @Override
  protected DataResource getImage(final StoryInformation story) {
    return StoryNavigationImageUtil.getImageResource(story.icon());
  }

  @Override
  protected void onSelect(final StoryInformation story) {
    eventBus.fireEvent(new LibraryItemSelectionCommand(story));
  }

  @Override
  protected void onUnselect(final StoryInformation option) {
    eventBus.fireEvent(new StorySelectionChangeCommand(null));
  }

  @Override
  protected String getLabel(final StoryInformation story) {
    return story.name();
  }
}
