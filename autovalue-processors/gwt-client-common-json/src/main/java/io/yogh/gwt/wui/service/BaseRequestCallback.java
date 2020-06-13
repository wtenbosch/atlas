package io.yogh.gwt.wui.service;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import io.yogh.gwt.wui.service.exception.RequestBlockedException;
import io.yogh.gwt.wui.service.exception.RequestClientException;
import io.yogh.gwt.wui.service.exception.RequestServerException;

public abstract class BaseRequestCallback implements RequestCallback {
  public boolean validateResponse(final Request request, final Response response) {
    if (response.getStatusCode() == 200) {
      return false;
    }

    final String responseCodeString = String.valueOf(response.getStatusCode());

    if (responseCodeString.equals("0")) {
      onError(request, new RequestBlockedException("Client blocked request."));
    } else if (responseCodeString.startsWith("4")) {
      onError(request, new RequestClientException(responseCodeString + " " + response.getStatusText()));
    } else if (responseCodeString.startsWith("5")) {
      onError(request, new RequestServerException(responseCodeString + " " + response.getStatusText()));
    }

    return true;
  }
}
