package nl.overheid.aerius.wui.atlas.ui.login;

import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.command.UserAuthorizationChangedCommand;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.PanelSelectionChangeEvent;
import nl.overheid.aerius.wui.event.PlaceChangeEvent;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.widget.EventComposite;

public class EditorPanel extends EventComposite {
  interface AdminPanelEventBinder extends EventBinder<EditorPanel> {}

  private final AdminPanelEventBinder EVENT_BINDER = GWT.create(AdminPanelEventBinder.class);

  interface EditorPanelUiBinder extends UiBinder<Widget, EditorPanel> {}

  private static final EditorPanelUiBinder UI_BINDER = GWT.create(EditorPanelUiBinder.class);

  @UiField FlowPanel editableContentPanels;

  @UiField Label chapterText;

  private HandlerRegistration registration;

  private PanelNames selectedPanel;

  public EditorPanel() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @UiHandler("logoutButton")
  public void onLogoutButton(final ClickEvent e) {
    if (Window.confirm(M.messages().logoutConfirmMessage())) {
      eventBus.fireEvent(new UserAuthorizationChangedCommand(null));
    }
  }

  @EventHandler
  public void onPanelSelectionChangeEvent(final PanelSelectionChangeEvent e) {
    selectedPanel = e.getValue();
  }

  @EventHandler
  public void onChapterChangeEvent(final ChapterSelectionChangeEvent e) {
    final Chapter value = e.getValue();
    editableContentPanels.clear();

//    chapterText.setText("Pagina " + value.getUid());

    for (final Entry<PanelNames, PanelContent> entry : value.panels().entrySet()) {
      if (entry.getValue().editableContentConfig() == null) {
        continue;
      }

      final EditableContentEditorPanel editablePanel = new EditableContentEditorPanel(entry.getKey(), entry.getValue());
      editablePanel.setSelectedPanel(selectedPanel);
      editableContentPanels.add(editablePanel);
      editablePanel.setEventBus(eventBus);
    }

    if (editableContentPanels.getWidgetCount() == 0) {
      editableContentPanels.add(new EditableContentEditorPanel());
    }
  }

  @EventHandler
  public void onPlaceChangeEvent(final PlaceChangeEvent e) {
    editableContentPanels.clear();
    editableContentPanels.add(new EditableContentEditorPanel());
    chapterText.setText("");
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    if (eventBus == null) {
      if (registration != null) {
        registration.removeHandler();
      }
      return;
    }

    registration = super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
