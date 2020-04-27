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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.properties.ComponentProperties;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.widget.WebComponent;

public final class LegacyWebComponentUtil {
  private static final Map<String, List<Runnable>> loading = new HashMap<>();
  private static final Map<String, Element> loaded = new HashMap<>();

  private LegacyWebComponentUtil() {}

  public static void inject(final ComponentProperties component, final AcceptsOneWidget panel, final EventBus eventBus,
      final Consumer<Element> func) {
    inject(component.getComponentName(), component.getComponentSource(), eventBus, panel, func);
  }

  public static void inject(final String componentName, final String componentSource, final EventBus eventBus, final AcceptsOneWidget panel,
      final Consumer<Element> func) {
    final Runnable initComponent = () -> attach(panel, func, eventBus, componentName);

    panel.setWidget(null);

    load(componentName, componentSource, initComponent);
  }

  // private static void unloadAll() {
  // loaded.forEach((k, v) -> unload(v));
  // loaded.clear();
  // }
  //
  // private static void unload(final Element comp) {
  // comp.removeFromParent();
  // }

  private static void load(final String componentName, final String componentSource, final Runnable finish) {
    if (loaded.containsKey(componentName)) {
      complete(finish);
      return;
    }

    if (loading.containsKey(componentName)) {
      loading.get(componentName).add(() -> {
        complete(finish);
      });
      return;
    }

    final Element link = Document.get().createElement("link");
    link.setAttribute("rel", "import");
    link.setAttribute("href", UglyBoilerPlate.sanitizeComponentSource(componentSource) + "?" + System.currentTimeMillis());

    final ArrayList<Runnable> onload = new ArrayList<Runnable>();
    Event.sinkEvents(link, Event.ONLOAD);
    Event.setEventListener(link, event -> {
      if (Event.ONLOAD == event.getTypeInt()) {
        onload.forEach(Runnable::run);
        loading.remove(componentName);
        loaded.put(componentName, link);
      }
    });

    onload.add(() -> complete(finish));
    loading.put(componentName, onload);

    Document.get().getHead().appendChild(link);
  }

  private static void complete(final Runnable finish) {
    Scheduler.get().scheduleDeferred(() -> finish.run());
  }

  private static void attach(final AcceptsOneWidget panel, final Consumer<Element> func, final EventBus eventBus, final String component) {
    final WebComponent webComponent = new WebComponent(component, eventBus, func);
    panel.setWidget(webComponent);
  }
}
