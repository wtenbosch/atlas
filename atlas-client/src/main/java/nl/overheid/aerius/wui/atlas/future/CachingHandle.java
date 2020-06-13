package nl.overheid.aerius.wui.atlas.future;

import java.util.Arrays;

public class CachingHandle {
  private final Object[] objects;

  public CachingHandle(final Object... objects) {
    this.objects = objects;
  }

  public static Object create(final Object... objects) {
    return new CachingHandle(objects);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(objects);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CachingHandle other = (CachingHandle) obj;
    if (!Arrays.equals(objects, other.objects)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CachingHandle [objects=" + Arrays.toString(objects) + "]";
  }
}
