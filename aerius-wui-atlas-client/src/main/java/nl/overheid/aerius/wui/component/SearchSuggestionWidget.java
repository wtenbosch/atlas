package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.wui.atlas.event.MapSearchSuggestionEvent;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.widget.EventComposite;

public class SearchSuggestionWidget extends EventComposite {
  private static final SearchSuggestionWidgetUiBinder UI_BINDER = GWT.create(SearchSuggestionWidgetUiBinder.class);

  interface SearchSuggestionWidgetUiBinder extends UiBinder<Widget, SearchSuggestionWidget> {}

  private final SearchSuggestion suggestion;

  @UiField Label typeField;
  @UiField Label titleField;

  public SearchSuggestionWidget(final SearchSuggestion suggestion) {
    this.suggestion = suggestion;

    initWidget(UI_BINDER.createAndBindUi(this));

    titleField.setText(suggestion.title());
    typeField.setText(M.messages().searchSuggestionType(suggestion.type()));
  }

  @UiHandler("panel")
  public void onPanelClick(final ClickEvent e) {
    confirm();
  }

  public void confirm() {
    eventBus.fireEvent(new MapSearchSuggestionEvent(suggestion));
  }
}
