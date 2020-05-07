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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.geo.GeoInitializer;
import nl.overheid.aerius.wui.activity.ActivityManager;
import nl.overheid.aerius.wui.atlas.event.RequestClientLoadFailureEvent;
import nl.overheid.aerius.wui.atlas.event.RequestConnectionFailureEvent;
import nl.overheid.aerius.wui.atlas.event.RequestServerLoadFailureEvent;
import nl.overheid.aerius.wui.atlas.event.UserAuthorizationChangedEvent;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.bootstrapper.BootstrapView;
import nl.overheid.aerius.wui.daemon.DaemonBootstrapper;
import nl.overheid.aerius.wui.dev.DevelopmentObserver;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.history.HistoryManager;
import nl.overheid.aerius.wui.i18n.AtlasM;
import nl.overheid.aerius.wui.service.RequestAeriusException;
import nl.overheid.aerius.wui.service.RequestBlockedException;
import nl.overheid.aerius.wui.service.RequestClientException;
import nl.overheid.aerius.wui.service.RequestServerException;
import nl.overheid.aerius.wui.util.NotificationUtil;
import nl.overheid.aerius.wui.util.WebUtil;
import nl.overheid.aerius.wui.widget.HasEventBus;

/**
 * Root of the application logic.
 */
public class ApplicationRoot extends BasicEventComponent {
  private final ApplicationRootEventBinder EVENT_BINDER = GWT.create(ApplicationRootEventBinder.class);

  interface ApplicationRootEventBinder extends EventBinder<ApplicationRoot> {}

  @Inject private RootPanelFactory rootPanelFactory;

  @Inject private HistoryManager historyManager;

  @Inject private ActivityManager activityManager;

  @Inject private EventBus eventBus;

  @SuppressWarnings("unused") @Inject private DevelopmentObserver development;

  @Inject DaemonBootstrapper daemonBootstrapper;

  @Inject GeoInitializer geoInitializer;

  private ApplicationRootView appDisplay;

  private BootstrapView bootstrapper;

  /**
   * Starts the application.
   *
   * @param bootstrapper
   * @param loginFirst
   */
  public void startUp(final ApplicationRootView appDisplay, final BootstrapView bootstrapper) {
    this.appDisplay = appDisplay;
    this.bootstrapper = bootstrapper;

    final String root = Document.get().getElementsByTagName("base").getItem(0).getAttribute("href");
    WebUtil.setAbsoluteRoot(root);

    setUncaughtExceptionHandler();

    daemonBootstrapper.setEventBus(eventBus);

    if (appDisplay instanceof HasEventBus) {
      ((HasEventBus) appDisplay).setEventBus(eventBus);
    }

    activityManager.setPanel(appDisplay);

    onFinishStartup();
  }

  private void onFinishStartup() {
    rootPanelFactory.getPanel().clear();
    rootPanelFactory.getPanel().add(appDisplay);

    historyManager.handleCurrentHistory();

    UglyBoilerPlate.notifyReroute(eventBus);
  }

  @EventHandler
  public void onUserAuthorizationChangedEvent(final UserAuthorizationChangedEvent e) {
    if (e.getValue() != null) {
      onFinishStartup();
    } else {
      bootstrapper.error();
    }
  }

  /**
   * Hides the main application display, if attached.
   */
  public void hideDisplay() {
    if (appDisplay != null && appDisplay.asWidget().isAttached()) {
      // destroy panel. unrecoverable error
      appDisplay.asWidget().removeFromParent();
    }
  }

  private void setUncaughtExceptionHandler() {
    GWT.setUncaughtExceptionHandler(e -> {
      final Throwable cause = findCause(e);

      if (cause instanceof IncompatibleRemoteServiceException && Window.confirm(AtlasM.messages().errorInternalApplicationOutdated())) {
        Window.Location.reload();
        return;
      }

      if (!isKnownException(cause)) {
        final String message = cause.getMessage();
        Logger.getLogger("UncaughtExceptionHandler").log(Level.SEVERE, message, cause);
        GWTProd.log("Application encountered an error it could not recover from: " + message);
        NotificationUtil.broadcastError(eventBus, message);
      } else {
        GWTProd.error("Request failed: " + cause);

        if (cause instanceof RequestClientException) {
          eventBus.fireEvent(new RequestClientLoadFailureEvent(true));
        } else if (cause instanceof RequestServerException) {
          eventBus.fireEvent(new RequestServerLoadFailureEvent(true));
        } else if (cause instanceof RequestBlockedException) {
          eventBus.fireEvent(new RequestConnectionFailureEvent(true));
        } else if (cause instanceof RequestAeriusException) {
          NotificationUtil.broadcastError(eventBus, cause.getMessage());
        }
      }
    });
  }

  private Throwable findCause(final Throwable e) {
    if (e == null) {
      return null;
    }

    if (e.getCause() != null) {
      return findCause(e.getCause());
    } else {
      return e;
    }
  }

  private boolean isKnownException(final Throwable e) {
    return e instanceof IncompatibleRemoteServiceException
        || e instanceof InvocationException
        || e instanceof StatusCodeException
        || e instanceof RequestException;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
