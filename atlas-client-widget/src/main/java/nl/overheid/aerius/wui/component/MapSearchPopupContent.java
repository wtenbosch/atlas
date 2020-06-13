/*
 * Copyright Dutch Ministry of Agriculture, Nature and Food Quality
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package nl.overheid.aerius.wui.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.wui.widget.EventComposite;

public class MapSearchPopupContent extends EventComposite implements Consumer<SearchSuggestion> {
  private static final MapSearchPopupImplUiBinder UI_BINDER = GWT.create(MapSearchPopupImplUiBinder.class);

  interface MapSearchPopupImplUiBinder extends UiBinder<Widget, MapSearchPopupContent> {}

  private static final int DEATH_TIMER = 250;

  @UiField FlowPanel panel;

  private final Timer deathTimer = new Timer() {
    @Override
    public void run() {
      killStales();
    }
  };

  private final Set<SearchSuggestion> deathList = new HashSet<>();

  private final Map<SearchSuggestion, SearchSuggestionWidget> suggestions = new HashMap<>();
  private SearchSuggestionWidget selected;

  public MapSearchPopupContent() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  public void clear() {
    panel.clear();
  }

  public void clearSoftly() {

  }

  public void notifyChange() {
    deathTimer.schedule(DEATH_TIMER);
    deathList.clear();
    deathList.addAll(suggestions.keySet());
  }

  private void killStales() {
    deathList.forEach(v -> suggestions.computeIfPresent(v, (o, n) -> {
      n.removeFromParent();
      return null;
    }));
  }

  @Override
  public void accept(final SearchSuggestion t) {
    if (deathList.remove(t)) {
      return;
    }

    final SearchSuggestionWidget widg = new SearchSuggestionWidget(t);
    widg.setEventBus(eventBus);
    suggestions.put(t, widg);
    panel.add(widg);
  }

  public void confirm() {
    if (suggestions.isEmpty()) {
      return;
    }

    Optional.ofNullable(Optional.ofNullable(selected)
        .orElse(suggestions.entrySet().iterator().next().getValue()))
        .ifPresent(v -> v.confirm());
  }

  public void moveNext() {
    // Stub
  }

  public void movePrevious() {
    // Stub
  }
}
