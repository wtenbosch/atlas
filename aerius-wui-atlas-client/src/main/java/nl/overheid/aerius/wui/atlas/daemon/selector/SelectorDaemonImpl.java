package nl.overheid.aerius.wui.atlas.daemon.selector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.SelectorResource;
import nl.overheid.aerius.wui.atlas.command.BroadcastSelectorsCommand;
import nl.overheid.aerius.wui.atlas.command.ReloadSelectorsCommand;
import nl.overheid.aerius.wui.atlas.command.SelectorCommand;
import nl.overheid.aerius.wui.atlas.event.ChapterSelectionChangeEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationChangeEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorConfigurationReloadEvent;
import nl.overheid.aerius.wui.atlas.event.SelectorLoadFailureEvent;
import nl.overheid.aerius.wui.atlas.future.selector.SelectorOracle;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.domain.selector.SelectorContext;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.future.AppAsyncCallback;
import nl.overheid.aerius.wui.util.NotificationUtil;
import nl.overheid.aerius.wui.util.ReplacementAssistant;

public class SelectorDaemonImpl extends BasicEventComponent implements SelectorDaemon {
  interface SelectorDaemonImplEventBinder extends EventBinder<SelectorDaemonImpl> {}

  private final SelectorDaemonImplEventBinder EVENT_BINDER = GWT.create(SelectorDaemonImplEventBinder.class);

  private final SelectorOracle selectorOracle;

  private final Map<String, SelectorResource> selectorResources = new HashMap<>();
  private final Map<String, String> loaded = new HashMap<>();

  private final ReplacementAssistant replacer;

  private boolean reloadScheduled;

  private final SelectorContext selectors;

  @Inject
  public SelectorDaemonImpl(final SelectorOracle selectorOracle, final ReplacementAssistant replacer, final SelectorContext selectors) {
    this.selectorOracle = selectorOracle;
    this.replacer = replacer;
    this.selectors = selectors;
  }

  @EventHandler
  public void onBroadcastSelectorsCommand(final BroadcastSelectorsCommand c) {
    loaded.clear();
    reloadSelectors();
  }

  @EventHandler
  public void onSelectorCommand(final ReloadSelectorsCommand e) {
    reloadSelectors();
  }

  @EventHandler
  public void onSelectorCommand(final SelectorCommand e) {
    Scheduler.get().scheduleDeferred(() -> reloadSelectors());
  }

  @EventHandler
  public void onChapterSelectionChangeEvent(final ChapterSelectionChangeEvent e) {
    final Chapter chapter = e.getValue();
    final List<SelectorResource> selectables = chapter.selectables();

    UglyBoilerPlate.selectorSanityWarnings(eventBus, chapter);

    selectorResources.clear();
    for (final SelectorResource resource : selectables) {
      selectorResources.put(resource.type(), resource);
    }

    if (!selectorResources.isEmpty()) {
      loaded.clear();
      reloadSelectors();
    }
  }

  public void reloadSelectors() {
    if (selectorResources.isEmpty()) {
      return;
    }

    selectorResources.forEach((k, v) -> {
      try {
        final String requestUrl = replacer.replaceStrict(v.url());
        if (loaded.containsKey(k) && loaded.get(k).equals(requestUrl)) {
          return;
        }
        eventBus.fireEvent(new SelectorConfigurationReloadEvent(k));
        loaded.remove(k);

        final String requestedType = v.type();
        selectorOracle.getSelectorContent(requestUrl, AppAsyncCallback.create(r -> {
          final String reportedType = r.getType();
          if (requestedType == null || !requestedType.equals(reportedType)) {
            NotificationUtil.broadcastError(eventBus, "WARNING: Retrieved selector type does not match requested selector type. Requested: ["
                + requestedType + "] Retrieved: [" + reportedType + "] -> " + v.url());
          }

          eventBus.fireEvent(new SelectorConfigurationChangeEvent(r));

          loaded.put(k, requestUrl);
          scheduleReloadSelectors();
        }, e -> {
          eventBus.fireEvent(new SelectorLoadFailureEvent(requestedType));
        }));
      } catch (final IllegalStateException e) {
        GWTProd.log("NOTE: Selector URL could not be fully resolved: " + e.getMessage());
        return;
      }
    });

    if (loaded.size() == selectorResources.size()) {
      selectors.broadcast();
    }
  }

  private void scheduleReloadSelectors() {
    if (reloadScheduled) {
      return;
    }

    reloadScheduled = true;
    Scheduler.get().scheduleDeferred(() -> {
      reloadSelectors();
      reloadScheduled = false;
    });
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
