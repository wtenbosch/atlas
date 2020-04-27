package nl.overheid.aerius.wui.atlas.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class GWTList {
  private GWTList() {}

  @SafeVarargs
  public static <T> List<T> of(final T... items) {
    final ArrayList<T> lst = new ArrayList<>();
    Stream.of(items)
        .forEach(v -> lst.add(v));
    return lst;
  }

  public static <T> List<Consumer<T>> add(final List<Consumer<T>> lst, final Consumer<T> item) {
    lst.add(item);
    return lst;
  }

  public static <T> List<T> concat(final List<T> a, final List<T> b) {
    final ArrayList<T> lst = new ArrayList<T>(a);
    lst.addAll(b);
    return lst;
  }
}
