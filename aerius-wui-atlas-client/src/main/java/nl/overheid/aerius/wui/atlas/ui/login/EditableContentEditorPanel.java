package nl.overheid.aerius.wui.atlas.ui.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.EditableContentConfig;
import nl.overheid.aerius.shared.domain.EditableContentConfigCollection;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.atlas.command.ReloadChapterCommand;
import nl.overheid.aerius.wui.atlas.event.PanelSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.widget.EventComposite;

public class EditableContentEditorPanel extends EventComposite {
  private static final EditableContentEditorPanelUiBinder UI_BINDER = GWT.create(EditableContentEditorPanelUiBinder.class);

  interface EditableContentEditorPanelUiBinder extends UiBinder<Widget, EditableContentEditorPanel> {}

  private final EditableContentEditorPanelEventBinder EVENT_BINDER = GWT.create(EditableContentEditorPanelEventBinder.class);

  interface EditableContentEditorPanelEventBinder extends EventBinder<EditableContentEditorPanel> {}

  public interface CustomStyle extends CssResource {
    String disabled();
  }

  @UiField Label selectionText;

  @UiField CustomStyle style;

  @UiField FocusPanel openButton;
  @UiField FocusPanel documentsButton;
  @UiField FocusPanel refreshButton;

  private List<String> selectables;
  private final Map<String, Selector> selectors = new HashMap<>();

  private final PanelContent panel;

  private String openLink;
  private String resourceLink;

  private PanelNames type;

  public EditableContentEditorPanel() {
    this(null, null);
  }

  public EditableContentEditorPanel(final PanelNames type, final PanelContent panel) {
    this.type = type;
    this.panel = panel;

    initWidget(UI_BINDER.createAndBindUi(this));

    if (panel == null) {
      openButton.setStyleName(style.disabled());
      documentsButton.setStyleName(style.disabled());
      refreshButton.setStyleName(style.disabled());
      selectionText.setText("Geen bewerkbare inhoud.");
      return;
    }

    openButton.ensureDebugId(AtlasTestIDs.BUTTON_OPEN + "_" + type.getName());
    documentsButton.ensureDebugId(AtlasTestIDs.BUTTON_DOCUMENTS + "_" + type.getName());
    refreshButton.ensureDebugId(AtlasTestIDs.BUTTON_REFRESH + "_" + type.getName());

    openButton.removeStyleName(style.disabled());
    documentsButton.removeStyleName(style.disabled());
    refreshButton.removeStyleName(style.disabled());

    selectables = panel.selectables();
    updateActiveSelectorText();
  }

  @EventHandler
  public void onPanelChangeEvent(final PanelSelectionChangeEvent e) {
    setSelectedPanel(e.getValue());
  }

  @EventHandler
  public void onSelectorEvent(final SelectorEvent e) {
    final Selector selector = e.getSelector();
    if (!SelectorUtil.matchesStrict(selector, selectables)) {
      return;
    }

    selectors.put(selector.getType(), selector);

    updateActiveSelectorText();
    updateEditPanel();
  }

  @UiHandler("openButton")
  public void onOpenButton(final ClickEvent e) {
    Window.open(openLink, "_blank", "");
  }

  @UiHandler("documentsButton")
  public void onDocumentsButton(final ClickEvent e) {
    Window.open(resourceLink, "_blank", "");
  }

  @UiHandler("refreshButton")
  public void onRefreshClick(final ClickEvent e) {
    eventBus.fireEvent(new ReloadChapterCommand());
  }

  private void updateEditPanel() {
    final EditableContentConfigCollection conf = panel.editableContentConfig();

    final EditableContentConfig editableContent = SelectorUtil.findEditableContent(conf.getCollection(), selectors.values());
    if (editableContent == null) {
      openLink = EditorUtil.formatAddEditableTextLink(conf.getAddNode(), selectors.values());
    } else {
      openLink = EditorUtil.formatEditEditableTextLink(editableContent.getEditNode());
    }
  }

  private void updateActiveSelectorText() {
    final String collect = selectors.values().stream()
        .map(v -> v.getSelector().getTitle().orElse(""))
        .collect(Collectors.joining(","));
    selectionText.setText(collect);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);

    updateEditPanel();
  }

  public void setSelectedPanel(final PanelNames selectedPanel) {
    setVisible(PanelNames.PANEL_MAIN == type || type == selectedPanel);
  }
}
