package nl.overheid.aerius.wui.service;

import com.google.gwt.http.client.RequestException;

@SuppressWarnings("serial")
public class RequestServerException extends RequestException {
  public RequestServerException(final String msg) {
    super(msg);
  }
}
