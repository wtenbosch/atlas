package nl.overheid.aerius.wui.atlas.daemon.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.wui.atlas.command.ConfigurationPropertyChangeCommand;
import nl.overheid.aerius.wui.atlas.util.GWTList;
import nl.overheid.aerius.wui.atlas.util.TimeUtil;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.event.BasicEventComponent;

@Singleton
public class ConfigurationDaemonImpl extends BasicEventComponent implements ConfigurationDaemon {
  static final String MONITOR_CONFIGURATIONS_START_KEY = "monitor-configurations_";

  private static final ConfigurationDaemonImplEventBinder EVENT_BINDER = GWT.create(ConfigurationDaemonImplEventBinder.class);

  interface ConfigurationDaemonImplEventBinder extends EventBinder<ConfigurationDaemonImpl> {}

  private final Map<String, String> configurations = new HashMap<>();

  private final Map<String, List<Consumer<String>>> handlers = new HashMap<>();

  @Inject
  public ConfigurationDaemonImpl(final ConfigurationDefaults defaults) {
    findConfigurations(defaults.get());
  }

  private void findConfigurations(final Map<String, String> defaults) {
    // Insert the default configurations first.
    configurations.putAll(defaults);
//    GWTProd.log("CFG", "Default config: " + configurations);

    // Overwrite with remembered defaults
    configurations.putAll(Cookies.getCookieNames().stream()
        .filter(v -> v.startsWith(MONITOR_CONFIGURATIONS_START_KEY))
        .collect(Collectors.toMap(v -> v.substring(MONITOR_CONFIGURATIONS_START_KEY.length()), v -> Cookies.getCookie(v))));

//    GWTProd.log("CFG", "  Final config: " + configurations);
  }

  @EventHandler
  public void onConfigurationPropertyChangeCommand(final ConfigurationPropertyChangeCommand c) {
    persistConfigurationChange(c.getKey(), c.getValue());
  }

  @Override
  public void persistConfigurationChange(final String key, final Object value) {
    persistConfigurationChange(key, String.valueOf(value));
  }

  @Override
  public void persistConfigurationChange(final String key, final String value) {
    final String ret = configurations.put(key, value);
    if (ret != null && value.equals(ret)) {
      return;
    } else {
      persistCookie(key, value);
    }

    Optional.ofNullable(handlers.get(key)).ifPresent(lst -> lst.forEach(v -> {
      v.accept(value);
    }));
  }

  private void persistCookie(final String key, final String value) {
    Cookies.setCookie(MONITOR_CONFIGURATIONS_START_KEY + key, value, new Date(System.currentTimeMillis() + TimeUtil.WEEK));
  }

  @Override
  public void registerAsBoolean(final String key, final Consumer<Boolean> consumer) {
    register(key, v -> consumer.accept(Boolean.parseBoolean(v)));
  }

  @Override
  public void register(final String key, final Consumer<String> consumer) {
    if (configurations.containsKey(key)) {
      consumer.accept(configurations.get(key));
    } else {
      GWTProd.warn("CFG", "No default configuration value available for: " + key);
    }

    handlers.merge(key, GWTList.of(consumer), GWTList::concat);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }

  @Override
  public String getConfig(final String key) {
    return configurations.get(key);
  }
}
