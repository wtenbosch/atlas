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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.command.ContextPanelCollapseCommand;
import nl.overheid.aerius.wui.atlas.command.ContextPanelOpenCommand;
import nl.overheid.aerius.wui.atlas.event.PanelSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.atlas.util.HoverSelectionUtil;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.resources.R;

public class PanelVisibilityButton extends SVGButton {
  private final PanelVisibilityButtonEventBinder EVENT_BINDER = GWT.create(PanelVisibilityButtonEventBinder.class);

  interface PanelVisibilityButtonEventBinder extends EventBinder<PanelVisibilityButton> {}

  private boolean open = true;

  private PanelNames panel;

  @Override
  protected DataResource getImage() {
    return R.images().contextPanelCollapseButton();
  }

  @Override
  protected void onEnsureDebugId(final String baseID) {
    super.onEnsureDebugId(baseID);
    button.ensureDebugId(baseID + "-" + AtlasTestIDs.BUTTON_PANEL_VISIBILITY);
  }

  @Override
  protected void onSelect() {
    eventBus.fireEvent(open ? new ContextPanelCollapseCommand() : new ContextPanelOpenCommand());
  }

  @EventHandler
  public void onPanelSelectionChangeEvent(final PanelSelectionChangeEvent e) {
    panel = e.getValue();
  }

  @Override
  protected void onHover() {
    HoverSelectionUtil.displayRight(this, open ? M.messages().contextPanelCollapse(panel) : M.messages().contextPanelOpen(panel));
  }

  public void setClosed() {
    this.open = false;

    button.getElement().getStyle().setProperty("transform", "rotate(180deg)");
  }

  public void setOpen() {
    this.open = true;

    button.getElement().getStyle().setProperty("transform", "rotate(0deg)");
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
