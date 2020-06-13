package nl.overheid.aerius.wui.service;

import com.google.gwt.http.client.RequestException;

@SuppressWarnings("serial")
public class RequestAeriusException extends RequestException {
  public RequestAeriusException(final String msg) {
    super(msg);
  }
}
