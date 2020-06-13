package nl.overheid.aerius.wui.atlas.future;

public interface ProvidesConcurrencyToken {
  String getStateToken(String identifier);
}
