package nl.overheid.aerius.collections;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BackportedList {
  public static <T> List<T> of(final T... items) {
    final List<T> lst = new ArrayList<>();
    lst.addAll(Arrays.asList(items));
    return lst;
  }

  public static <T> List<T> copyOf(final Collection<T> cop) {
    return new ArrayList<>(cop);
  }
}
