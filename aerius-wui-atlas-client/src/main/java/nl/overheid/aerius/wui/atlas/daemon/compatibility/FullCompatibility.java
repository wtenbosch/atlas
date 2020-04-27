package nl.overheid.aerius.wui.atlas.daemon.compatibility;

public class FullCompatibility {
  public static class LimitedCompatibility extends FullCompatibility {
    @Override
    protected String getMessage() {
      return "U maakt gebruikt van een browser waarop deze applicatie nog niet volledig is getest, mochten er zich problemen voordoen, dan wordt het aangeraden gebruik te maken van een recente versie van Google Chrome of Chromium.";
    }
  }

  protected String getMessage() {
    return null;
  }
}
