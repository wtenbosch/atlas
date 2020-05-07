package nl.overheid.aerius.wui.atlas.service.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.shared.domain.SearchSuggestion.Builder;
import nl.overheid.aerius.shared.domain.SearchSuggestionType;
import nl.overheid.aerius.wui.future.JsonAsyncCallback;

public final class SearchQueryResultJsonParser implements AsyncCallback<JSONValue> {
  private final AsyncCallback<List<SearchSuggestion>> callback;

  public SearchQueryResultJsonParser(final AsyncCallback<List<SearchSuggestion>> callback) {
    this.callback = callback;
  }

  @Override
  public void onSuccess(final JSONValue result) {
    parse(new JSONArrayHandle(result.isArray()));
  }

  private void parse(final JSONArrayHandle arr) {
    final List<SearchSuggestion> lst = new ArrayList<>();

    arr.forEach(v -> {
      final Builder bldr = SearchSuggestion.builder()
          .type(SearchSuggestionType.N2000_AREA)
          .payload(v.getString("payload"))
          .title(v.getString("title"));

      v.getStringOptional("extent").ifPresent(ext -> bldr.extent(ext));

      lst.add(bldr.build());
    });

    callback.onSuccess(lst);
  }

  @Override
  public void onFailure(final Throwable caught) {
    callback.onFailure(caught);
  }

  public static AsyncCallback<JSONValue> convert(final AsyncCallback<List<SearchSuggestion>> callback) {
    return new SearchQueryResultJsonParser(callback);
  }

  public static RequestCallback wrap(final AsyncCallback<List<SearchSuggestion>> callback) {
    return JsonAsyncCallback.create(convert(callback));
  }
}
