package nl.overheid.aerius.wui.atlas.daemon.ads;

import java.util.Optional;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.atlas.command.HealthcheckRequestCommand;
import nl.overheid.aerius.wui.atlas.event.AdblockerDetectedEvent;
import nl.overheid.aerius.wui.atlas.event.RequestClientLoadFailureEvent;
import nl.overheid.aerius.wui.atlas.event.RequestServerLoadFailureEvent;
import nl.overheid.aerius.wui.atlas.service.RequestUtil;
import nl.overheid.aerius.wui.atlas.util.FakeCallback;
import nl.overheid.aerius.wui.config.EnvironmentConfiguration;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.cache.CacheContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.future.AppAsyncCallback;
import nl.overheid.aerius.wui.service.RequestBlockedException;
import nl.overheid.aerius.wui.service.RequestClientException;
import nl.overheid.aerius.wui.service.RequestServerException;

public class AdblockInterceptorDaemonImpl extends BasicEventComponent implements AdblockInterceptorDaemon {
  private static final String ADS_ELEMENT_ID = "AERIUS-ADS";

  interface AdblockInterceptorDaemonImplEventBinder extends EventBinder<AdblockInterceptorDaemonImpl> {}

  private final AdblockInterceptorDaemonImplEventBinder EVENT_BINDER = GWT.create(AdblockInterceptorDaemonImplEventBinder.class);

  private final CacheContext cacheContext;

  @Inject EnvironmentConfiguration cfg;

  @Inject
  public AdblockInterceptorDaemonImpl(final CacheContext cacheContext) {
    this.cacheContext = cacheContext;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);

    // doAdsTest();
    doHealthChecks();
  }

  private void doAdsTest() {
    final Element elem = Document.get().getElementById(ADS_ELEMENT_ID);
    if (elem == null) {
      eventBus.fireEvent(new AdblockerDetectedEvent(true));
    }
  }

  @EventHandler
  public void onHealthcheckRequestCommand(final HealthcheckRequestCommand c) {
    doHealthChecks();
  }

  private void doHealthChecks() {
    cacheContext.clear();
    // TODO Support multiple endpoints when the success state does not conflict
    // Stream.of(ApplicationConfig.SERVICE_ENDPOINT_HEALTHCHECKS.split(",")).forEach(this::doHealthCheck);
    Optional.ofNullable(cfg.getHealthchecksEndpoint())
        .ifPresent(this::doHealthCheck);
  }

  private void doHealthCheck(final String endpoint) {
    if (endpoint.isEmpty()) {
      return;
    }

    RequestUtil.doGet(endpoint, FakeCallback.create(""),
        AppAsyncCallback.create(v -> succeedRequest(v), t -> failRequest(t)));
  }

  private void succeedRequest(final String v) {
    eventBus.fireEvent(new AdblockerDetectedEvent(false));
    eventBus.fireEvent(new RequestServerLoadFailureEvent(false));
    eventBus.fireEvent(new RequestClientLoadFailureEvent(false));
  }

  private void failRequest(final Throwable t) {
    GWTProd.error("Request failed: " + t);

    if (t instanceof RequestBlockedException) {
      eventBus.fireEvent(new AdblockerDetectedEvent(true));
    } else if (t instanceof RequestClientException) {
      eventBus.fireEvent(new RequestClientLoadFailureEvent(true));
    } else if (t instanceof RequestServerException) {
      eventBus.fireEvent(new RequestServerLoadFailureEvent(true));
    }
  }
}
