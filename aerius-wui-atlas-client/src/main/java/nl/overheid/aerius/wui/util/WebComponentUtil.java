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

import java.util.function.Consumer;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.properties.ComponentProperties;
import nl.overheid.aerius.wui.widget.IframeWebComponent;

public final class WebComponentUtil {
  private WebComponentUtil() {}

  public static void inject(final ComponentProperties component, final AcceptsOneWidget panel, final EventBus eventBus,
      final Consumer<Element> func) {
    inject(component.getComponentName(), component.getComponentSource(), eventBus, panel, func);
  }

  public static void inject(final String componentName, final String componentSource, final EventBus eventBus, final AcceptsOneWidget panel,
      final Consumer<Element> func) {

    final IframeWebComponent webComponent = new IframeWebComponent(componentName, componentSource, eventBus, func);
    panel.setWidget(webComponent);
  }
}
