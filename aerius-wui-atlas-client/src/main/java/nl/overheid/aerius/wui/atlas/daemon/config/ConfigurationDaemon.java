package nl.overheid.aerius.wui.atlas.daemon.config;

import java.util.function.Consumer;

import com.google.inject.ImplementedBy;

import nl.overheid.aerius.wui.daemon.Daemon;

@ImplementedBy(ConfigurationDaemonImpl.class)
public interface ConfigurationDaemon extends Daemon {
  void register(String key, Consumer<String> consumer);

  void registerAsBoolean(String key, Consumer<Boolean> consumer);

  void persistConfigurationChange(String key, String value);

  void persistConfigurationChange(String key, Object value);

  String getConfig(String key);
}
