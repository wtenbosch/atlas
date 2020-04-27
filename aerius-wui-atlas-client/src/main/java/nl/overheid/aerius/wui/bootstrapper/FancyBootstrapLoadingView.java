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
package nl.overheid.aerius.wui.bootstrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class FancyBootstrapLoadingView extends Composite implements BootstrapView {
  private static final FancyBootstrapLoadingViewUiBinder UI_BINDER = GWT.create(FancyBootstrapLoadingViewUiBinder.class);

  interface FancyBootstrapLoadingViewUiBinder extends UiBinder<Widget, FancyBootstrapLoadingView> {}

  private static final int MIN_DELAY = 850;

  private boolean loading;
  private boolean starting;

  private Runnable finish;

  @UiField ImageElement monitorButton;
  @UiField DivElement monitorContainer;

  public FancyBootstrapLoadingView() {
    initWidget(UI_BINDER.createAndBindUi(this));

    Event.sinkEvents(monitorButton, Event.ONCLICK);
    Event.setEventListener(monitorButton, event -> {
      if (Event.ONCLICK == event.getTypeInt()) {
        startCalculator();
      }
    });
  }

  public void startCalculator() {
    loading = true;
    monitorContainer.getStyle().setDisplay(Display.FLEX);
    tryStart();
  }

  @Override
  public void onApplicationReady(final Runnable finish) {
    this.finish = finish;
    tryStart();

    if (!Window.Location.getParameterMap().containsKey("halt")) {
      startCalculator();
    }
  }

  public void tryStart() {
    if (loading && finish != null && !starting) {
      starting = true;
      Scheduler.get().scheduleFixedDelay(() -> {
        finish.run();
        return false;
      }, MIN_DELAY);
    }
  }

  @Override
  public void error() {}

  public void clear() {}
}
