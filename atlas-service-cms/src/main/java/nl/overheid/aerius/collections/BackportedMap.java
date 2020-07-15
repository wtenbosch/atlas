package nl.overheid.aerius.collections;


import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

public class BackportedMap {
  public static <K, V> Map.Entry<K, V> entry(final K key, final V value) {
    return new SimpleEntry<>(key, value);
  }
  
  public static <K, V> Map<K, V> of() {
    return new HashMap<>();
  }

  public static <K, V> Map<K, V> of(final K k1, final V v1) {
    final HashMap<K, V> map = new HashMap<>();
    map.put(k1, v1);
    return map;
  }

  public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2) {
    final HashMap<K, V> map = new HashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    return map;
  }

  public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
    final HashMap<K, V> map = new HashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    return map;
  }

  public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
    final HashMap<K, V> map = new HashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    return map;
  }

  public static <K, V> Map<K, V> of(final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5,
      final V v5) {
    final HashMap<K, V> map = new HashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    return map;
  }

  public static <K, V> Map<K, V> copyOf(final Map<K, V> copy) {
    return new HashMap<>(copy);
  }
}
