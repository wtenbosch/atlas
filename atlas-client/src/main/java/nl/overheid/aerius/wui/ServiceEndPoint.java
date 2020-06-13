package nl.overheid.aerius.wui;

import com.google.gwt.core.client.GWT;

public class ServiceEndPoint {
  public static final ServiceEndPoint I = GWT.create(ServiceEndPoint.class);

  static class Aerius extends ServiceEndPoint {
    @Override
    public String get() {
      return "https://test-monitor.aerius.nl/v1/";
    }
  }

  static class Debug extends ServiceEndPoint {
    @Override
    public String get() {
      return "https://test-monitor.aerius.nl/v1/";
    }
  }

  static class Production extends ServiceEndPoint {
    @Override
    public String get() {
      return "https://monitorup.aerius.nl/v1/";
    }
  }

  public String get() {
    throw new RuntimeException("Required binding [ServiceEndPoint] unbound.");
  }

  public String format(final String methodName) {
    return get() + methodName;
  }

  public String getUser() {
    return null;
  }

  public String getPassword() {
    return null;
  }
}
