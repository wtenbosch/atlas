package nl.overheid.aerius.wui.widget;

import java.util.Arrays;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.StoryFilterOption;
import nl.overheid.aerius.wui.atlas.event.StoryFilterSelectionChangeEvent;
import nl.overheid.aerius.wui.component.StoryFilterPopup;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.resources.R;

public class StoryFilterWidget extends CompactMenuItemListWidget<StoryFilterOption> {
  interface StoryFilterWidgetEventBinder extends EventBinder<StoryFilterWidget> {}

  private final StoryFilterWidgetEventBinder EVENT_BINDER = GWT.create(StoryFilterWidgetEventBinder.class);

  public StoryFilterWidget() {
    super(new StoryFilterPopup());
  }

  @Override
  protected DataResource getImage(final StoryFilterOption selection) {
    return R.images().filterIcon();
  }

  @Override
  protected String getName(final StoryFilterOption selection) {
    return M.messages().storyFilterOption(selection);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);
    super.setEventBus(eventBus, this, EVENT_BINDER);

    setList(Arrays.asList(StoryFilterOption.values()));
  }

  @EventHandler
  public void onStoryFilterSelectionChangeEvent(final StoryFilterSelectionChangeEvent e) {
    setSelected(e.getValue());
  }

  @Override
  public void onSelect(final StoryFilterOption option) {
    throw new RuntimeException("This function cannot be called for this component. [StoryFilterOption change in StoryFilterWidget]");
  }
}
