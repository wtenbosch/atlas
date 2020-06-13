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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.wui.widget.EventComposite;

@Singleton
public class ChapterNavigationPanel extends EventComposite {
  private static final ChapterNavigationPanelUiBinder UI_BINDER = GWT.create(ChapterNavigationPanelUiBinder.class);

  interface ChapterNavigationPanelUiBinder extends UiBinder<Widget, ChapterNavigationPanel> {}

  @UiField(provided = true) ChapterNavigation chapterNavigation;

  @Inject
  public ChapterNavigationPanel(final ChapterNavigation chapterNavigation) {
    this.chapterNavigation = chapterNavigation;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, chapterNavigation);
  }
}
