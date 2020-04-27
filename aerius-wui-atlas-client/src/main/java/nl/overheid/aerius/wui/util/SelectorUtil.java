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
package nl.overheid.aerius.wui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.EditableContentConfig;
import nl.overheid.aerius.shared.domain.HasSelectorType;
import nl.overheid.aerius.shared.domain.IsSelector;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.component.SelectorWidget;
import nl.overheid.aerius.wui.domain.selector.SelectorContext;

public final class SelectorUtil {
  private static final Collection<String> APPLICATION_TYPES = new ArrayList<>();
  static {
    APPLICATION_TYPES.add(Selector.DEFAULT_TYPE_YEAR);
  }

  private static SelectorContext selectorContext;

  private SelectorUtil() {}

  public static void initializeSelectorContext(final SelectorContext selectorContext) {
    SelectorUtil.selectorContext = selectorContext;
  }

  public static boolean matchesSmart(final Selector selector, final List<String> selectorTypes) {
    if (selector.isSystem()) {
      return true;
    }

    return matchesStrict(selector.getType(), selectorTypes);
  }

  public static boolean matchesStrict(final Selector selector, final List<String> selectorTypes) {
    return matchesStrict(selector.getType(), selectorTypes);
  }

  public static boolean matchesStrict(final String type, final IsSelector e) {
    return matches(type, e.getSelector().getType());
  }

  public static boolean matchesStrict(final String type, final HasSelectorType e) {
    return matches(type, e.getType());
  }

  public static boolean matchesStrict(final String type, final Collection<String> types) {
    for (final String typeB : types) {
      if (matches(type, typeB)) {
        return true;
      }
    }

    return false;
  }

  public static boolean matches(final String typeA, final String typeB) {
    return typeB != null && typeA.equals(typeB);
  }

  static boolean isMatch(final Collection<Selector> criteria, final Collection<Selector> selections) {
    // If the set of options contain anything not in the selections, there cannot be a match
    if (criteria.stream().anyMatch(v -> !containsSelector(v.getType(), selections))) {
      return false;
    }

    // First, take the options, filter out those we don't have selections for
    final List<Selector> possiblyMatchingOptions = criteria.stream().filter(v -> containsSelector(v.getType(), selections)).sorted().collect(
        Collectors.toList());

    // Second, take the selections, filter out those we don't have options for
    final List<Selector> possiblyMatchingSelections = selections.stream().filter(
        v -> containsSelector(v.getType(), possiblyMatchingOptions)).sorted().collect(Collectors.toList());

    // If the resultant lists are a complete match, it's a [full outer] match
    return isCompleteMatch(possiblyMatchingOptions, possiblyMatchingSelections);
  }

  static boolean containsSelector(final String type, final Collection<Selector> selectors) {
    return selectors.stream().filter(e -> e.getType().equals(type)).count() > 0;
  }

  static boolean isCompleteMatch(final Collection<Selector> options, final Collection<Selector> selections) {
    final List<Selector> optionsSorted = options.stream().sorted().collect(Collectors.toList());
    final List<Selector> selectionsSorted = selections.stream().sorted().collect(Collectors.toList());
    return Objects.deepEquals(optionsSorted, selectionsSorted);
  }

  /**
   *
   * @param challenger
   *          Candidate that is challenging the status-quo
   * @param old
   *          The status-quo that is being challenged
   * @param selections
   *          The criteria which define the victor
   * @return true if the candidate is superior
   */
  static boolean isBetterMatch(final Collection<Selector> challenger, final Collection<Selector> old, final Collection<Selector> selections) {
    if (old == null) {
      return true;
    }

    final long challengerMatches = selections.stream().filter(v -> containsSelector(v.getType(), challenger)).count();
    final long oldMatches = selections.stream().filter(v -> containsSelector(v.getType(), old)).count();

    return challengerMatches > oldMatches;
  }

  public static boolean isApplicationType(final String type) {
    return APPLICATION_TYPES.contains(type);
  }

  public static EditableContentConfig findEditableContent(final List<EditableContentConfig> collection, final Collection<Selector> values) {
    for (final EditableContentConfig conf : collection) {
      if (isCompleteMatch(conf.getSelectorCombination(), values)) {
        return conf;
      }
    }

    return null;
  }

  public static void populateSelectorWidget(final FlowPanel container, final List<String> selectables, final EventBus eventBus) {
    if (selectables == null) {
      return;
    }

    // For each selector subscription, display a widget
    for (final String selectable : selectables) {
      if (SelectorUtil.isApplicationType(selectable)) {
        continue;
      }

      final SelectorWidget widg = new SelectorWidget(selectable);
      container.add(widg);
      widg.setEventBus(eventBus);

      Optional.ofNullable(selectorContext.getSelectors().get(selectable)).ifPresent(widg::setSelector);
    }
  }
}
