package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.wui.atlas.event.LibraryChangeEvent;
import nl.overheid.aerius.wui.atlas.factories.LevelGuideFactory;
import nl.overheid.aerius.wui.widget.EventComposite;

public class BigLibraryViewImpl extends EventComposite implements BigLibraryView {
  private final BigLibraryViewerEventBinder EVENT_BINDER = GWT.create(BigLibraryViewerEventBinder.class);

  interface BigLibraryViewerEventBinder extends EventBinder<BigLibraryViewImpl> {}

  private static final BigLibraryViewerUiBinder UI_BINDER = GWT.create(BigLibraryViewerUiBinder.class);

  interface BigLibraryViewerUiBinder extends UiBinder<Widget, BigLibraryViewImpl> {}

  @UiField SimplePanel viewModeGuide;
//  @UiField FlowPanel container;

  private final LevelGuideFactory factory;

  @Inject
  public BigLibraryViewImpl(final LevelGuideFactory factory) {
    this.factory = factory;
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @EventHandler
  public void onLibraryChangeEvent(final LibraryChangeEvent e) {
    final NarrowLibrary library = e.getValue();

//    container.clear();
//    for (final StoryInformation story : library.values()) {
//      final BigStoryNavigationControl control = new BigStoryNavigationControl(story, eventBus);
//      container.add(control);
//    }

    // TODO Use
    updateLevelGuide();
  }

  private void updateLevelGuide() {
    viewModeGuide.setWidget(factory.createLocationGuide());
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
