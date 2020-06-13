package nl.overheid.aerius.wui.component;

import java.util.Optional;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.command.LibraryItemSelectionCommand;
import nl.overheid.aerius.wui.atlas.daemon.library.LibraryStatusChangedEvent;
import nl.overheid.aerius.wui.resources.R;
import nl.overheid.aerius.wui.util.FormatUtil;
import nl.overheid.aerius.wui.util.StoryNavigationImageUtil;
import nl.overheid.aerius.wui.util.SvgUtil;
import nl.overheid.aerius.wui.widget.MaskedButton;

public class BigStoryNavigationControl extends MaskedButton<StoryInformation> {
  private static final BigStoryNavigationControlUiBinder UI_BINDER = GWT.create(BigStoryNavigationControlUiBinder.class);

  interface BigStoryNavigationControlUiBinder extends UiBinder<Widget, BigStoryNavigationControl> {}

  private final BigStoryNavigationControlEventBinder EVENT_BINDER = GWT.create(BigStoryNavigationControlEventBinder.class);

  interface BigStoryNavigationControlEventBinder extends EventBinder<BigStoryNavigationControl> {}

  private final EventBus eventBus;

  @UiField SimplePanel authorIcon;
  @UiField Label authorName;
  @UiField SimplePanel creationIcon;
  @UiField Label creationDate;

  public BigStoryNavigationControl(final StoryInformation option, final EventBus eventBus) {
    super(option);

    initWidget(UI_BINDER.createAndBindUi(this));

    final Optional<String> author = Optional.ofNullable(option.authorName());
    if (author.isPresent()) {
      authorName.setText(author.get());
    } else {
      authorIcon.setVisible(false);
    }

    creationDate.setText(FormatUtil.formatDate(option.creationDate()));

    SvgUtil.I.setSvg(authorIcon, R.images().authorIcon());
    SvgUtil.I.setSvg(creationIcon, R.images().lastEditedIcon());

    this.eventBus = eventBus;
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onLibraryStatusChangedEvent(final LibraryStatusChangedEvent e) {
    if (e.getValue() == option) {
      // TODO Use
//      setDisabled(!e.isAvailable());
    }
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
  protected String getLabel(final StoryInformation story) {
    return story.name();
  }
}
