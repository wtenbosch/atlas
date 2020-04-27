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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.ContextOptionsChangedEvent;
import nl.overheid.aerius.wui.atlas.event.PanelConfigurationChangeEvent;
import nl.overheid.aerius.wui.atlas.event.PanelSelectionChangeEvent;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.widget.HasEventBus;
import nl.overheid.aerius.wui.widget.MaskedButtonPanel;

public class PanelNavigation extends MaskedButtonPanel<PanelConfiguration, ContextNavigationControl>
    implements HasEventBus {
  private final ContextNavigationEventBinder EVENT_BINDER = GWT.create(ContextNavigationEventBinder.class);

  interface ContextNavigationEventBinder extends EventBinder<PanelNavigation> {}

  private EventBus eventBus;

  private Map<PanelNames, PanelConfiguration> panels;

  @Override
  protected Object getControlKey(final PanelConfiguration option) {
    return option == null ? null : option.getName();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    this.eventBus = eventBus;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  public void setPanelSelection(final PanelNames type, final boolean value) {
    if (panels == null || !panels.containsKey(type)) {
      GWTProd.warn("Selection made that does not exist: " + type);
      return;
    }

    if (value) {
      selectOption(panels.get(type));
    } else {
      deselect(panels.get(type));
    }
  }

  @EventHandler
  public void onContextSelectionChangeEvent(final PanelSelectionChangeEvent e) {
    selectOption(panels.get(e.getValue()));
  }

  @EventHandler
  public void onContextOptionsChangedEvent(final PanelConfigurationChangeEvent e) {
    setPanels(e.getValue());
  }

  public void setPanels(final Map<PanelNames, PanelConfiguration> panels) {
    this.panels = panels;

    setControls(panels.values());
  }

  @EventHandler
  public void onOptionSelectionChanged(final ContextOptionsChangedEvent e) {
    panels.clear();
    e.getValue().forEach(v -> panels.put(PanelNames.fromName(v.getName()), v));

    setControls(e.getValue());
  }

  @EventHandler
  public void onChapterChangedEvent(final ChapterSelectionChangeEvent e) {
    final Chapter chapter = e.getValue();

    final List<PanelNames> activePanels = chapter.panels().keySet().stream().collect(Collectors.toList());
    activePanels.add(PanelNames.PANEL_PREFERENCES);

    panels.entrySet().stream().forEach(v -> {
      final boolean active = activePanels.contains(v.getKey());

      controls.get(getControlKey(panels.get(v.getKey()))).setDisabled(!active);
    });
  }

  @Override
  protected ContextNavigationControl createControl(final PanelConfiguration value) {
    return new ContextNavigationControl(value, eventBus);
  }
}
