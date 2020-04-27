package nl.overheid.aerius.wui.service;

import com.google.gwt.http.client.RequestException;

@SuppressWarnings("serial")
public class RequestClientException extends RequestException {
  public RequestClientException(final String msg) {
    super(msg);
  }
}
