package nl.overheid.aerius.wui.atlas.service;

@SuppressWarnings("serial")
public class HttpRequestException extends Exception {
  public HttpRequestException() {
    super();
  }

  public HttpRequestException(final String message) {
    super(message);
  }
}
