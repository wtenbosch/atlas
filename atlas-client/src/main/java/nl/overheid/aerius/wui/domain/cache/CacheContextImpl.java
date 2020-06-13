package nl.overheid.aerius.wui.domain.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.inject.Singleton;

@Singleton
public class CacheContextImpl implements CacheContext {
  private final List<Collection<?>> colCaches = new ArrayList<>();
  private final List<Map<Object, ?>> mapCaches = new ArrayList<>();

  private boolean caching = true;

  @Override
  public void setCaching(final boolean caching) {
    this.caching = caching;
  }

  @Override
  public boolean isCaching() {
    return caching;
  }

  @Override
  public void clear() {
    mapCaches.forEach(v -> v.clear());
    colCaches.forEach(v -> v.clear());
  }

  @Override
  public void register(final Collection<?> cache) {
    colCaches.add(cache);
  }

  @Override
  public void register(final Map<Object, ?> cache) {
    mapCaches.add(cache);
  }
}
