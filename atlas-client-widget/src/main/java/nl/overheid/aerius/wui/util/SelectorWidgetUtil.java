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

import java.util.List;
import java.util.Optional;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.component.SelectorWidget;
import nl.overheid.aerius.wui.domain.selector.SelectorContext;

public final class SelectorWidgetUtil {
  private static SelectorContext selectorContext;

  private SelectorWidgetUtil() {}

  public static void initializeSelectorContext(final SelectorContext selectorContext) {
    SelectorWidgetUtil.selectorContext = selectorContext;
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
