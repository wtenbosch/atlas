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
package nl.overheid.aerius.wui;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.bootstrapper.BootstrapView;
import nl.overheid.aerius.wui.bootstrapper.FancyBootstrapLoadingView;
import nl.overheid.aerius.wui.dev.GWTProd;

public class Bootstrap implements EntryPoint {
  private static final boolean DISPLAY_BOOTSTRAP_VIEW = false;

  private BootstrapView bootstrapLoadingView;
  private RootPanel bootstrapContainer;

  @Override
  public void onModuleLoad() {
    displayBootstrapView();

    GWTProd.log("Hello! Welcome to the AERIUS Console!");
    GWTProd.log("Request href: " + Window.Location.getHref());
    GWTProd.log(new Date().getTime() + " <Bootstrap start time.");

    GWT.runAsync(new RunAsyncCallback() {
      @Override
      public void onFailure(final Throwable caught) {
        GWTProd.log("Bootstrap failed. " + caught.getMessage());
      }

      @Override
      public void onSuccess() {
        GWTProd.log(new Date().getTime() + " <Application load start time.");

        onFinish(() -> {
          UglyBoilerPlate.setDevelopmentRerouter();
          Application.A.create(() -> {
            scrub();
            clearBootstrapView();
          });
        });

        GWTProd.log(new Date().getTime() + " <Application load complete time.");
      }

      private void onFinish(final Runnable runner) {
        if (DISPLAY_BOOTSTRAP_VIEW) {
          bootstrapLoadingView.onApplicationReady(runner);
        } else {
          runner.run();
        }
      }
    });
  }

  protected void clearBootstrapView() {
    if (!DISPLAY_BOOTSTRAP_VIEW) {
      return;
    }

    bootstrapContainer.getElement().getStyle().setProperty("transitionDelay", "0.4s");
    bootstrapContainer.getElement().getStyle().setProperty("top", "-100vh");
    Scheduler.get().scheduleFixedDelay(() -> {
      bootstrapContainer.clear();
      return false;
    }, 1200);
  }

  private void displayBootstrapView() {
    if (!DISPLAY_BOOTSTRAP_VIEW) {
      return;
    }

    bootstrapLoadingView = new FancyBootstrapLoadingView();
    bootstrapContainer = RootPanel.get("bootstrap");
    if (bootstrapContainer != null) {
      bootstrapContainer.add(bootstrapLoadingView);
      bootstrapContainer.getElement().getStyle().setTop(0, Unit.PX);
    }
  }

  private void scrub() {
    final BodyElement body = Document.get().getBody();

    for (int i = body.getChildCount() - 1; i >= 0; i--) {
      final Node child = body.getChild(i);

      final Element e = child.cast();
      final String className = e.getClassName();

      if (className.contains("scrub-me")) {
        e.setInnerHTML("");
        e.setClassName("");
      }
    }
  }
}
