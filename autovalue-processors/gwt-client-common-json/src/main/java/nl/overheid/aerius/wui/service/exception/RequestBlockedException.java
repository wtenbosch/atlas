package nl.overheid.aerius.wui.service.exception;

import com.google.gwt.http.client.RequestException;

@SuppressWarnings("serial")
public class RequestBlockedException extends RequestException {
  public RequestBlockedException(final String message) {
    super(message);
  }
}
