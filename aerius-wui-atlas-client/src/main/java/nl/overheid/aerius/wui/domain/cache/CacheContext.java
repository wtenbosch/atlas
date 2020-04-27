package nl.overheid.aerius.wui.domain.cache;

import java.util.Collection;
import java.util.Map;

import com.google.inject.ImplementedBy;

@ImplementedBy(CacheContextImpl.class)
public interface CacheContext {
  boolean isCaching();

  void setCaching(boolean caching);

  void clear();

  void register(Collection<?> cache);

  void register(Map<Object, ?> cache);
}
