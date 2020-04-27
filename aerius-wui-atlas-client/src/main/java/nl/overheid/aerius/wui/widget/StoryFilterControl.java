package nl.overheid.aerius.wui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.StoryFilterOption;
import nl.overheid.aerius.wui.atlas.command.StoryFilterSelectionChangeCommand;
import nl.overheid.aerius.wui.i18n.M;

public class StoryFilterControl extends SimpleMaskedButton<StoryFilterOption> {
  private static final StoryFilterControlUiBinder UI_BINDER = GWT.create(StoryFilterControlUiBinder.class);

  interface StoryFilterControlUiBinder extends UiBinder<Widget, StoryFilterControl> {}

  public StoryFilterControl(final StoryFilterOption option, final EventBus eventBus) {
    super(option);

    setEventBus(eventBus);
  }

  @Override
  protected void onEnsureDebugId(final String baseID) {
    super.onEnsureDebugId(baseID);

    button.ensureDebugId(baseID + "-" + getLabel(option));
  }

  @Override
  protected void initWidget() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  protected void onSelect(final StoryFilterOption option) {
    eventBus.fireEvent(new StoryFilterSelectionChangeCommand(option));
  }

  @Override
  protected String getLabel(final StoryFilterOption option) {
    return M.messages().storyFilterOption(option);
  }
}
