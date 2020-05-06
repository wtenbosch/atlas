package nl.overheid.aerius.templates.stories.helpers;

import nl.overheid.aerius.configuration.AtlasServices;

public class UrlHelper {
  private static AtlasServices services;

  public static void initSerivces(final AtlasServices services) {
    UrlHelper.services = services;
  }

  public static String getCms(final String path) {
    return services.getCms() + path;
  }

  public static String getAvailability(final String path) {
    return services.getLayer() + path;
  }

  public static String getSelector(final String path) {
    return services.getSelector() + path;
  }

  public static String getComponent(final String path) {
    return services.getComponents() + path;
  }
}
