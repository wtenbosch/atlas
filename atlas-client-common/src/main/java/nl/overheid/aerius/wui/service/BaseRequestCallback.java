package nl.overheid.aerius.wui.service;

import org.apache.http.HttpStatus;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

import nl.overheid.aerius.wui.dev.GWTProd;

public abstract class BaseRequestCallback implements RequestCallback {
  public boolean validateResponse(final Request request, final Response response) {
    if (response.getStatusCode() == HttpStatus.SC_OK) {
      return false;
    }

    final String responseCodeString = String.valueOf(response.getStatusCode());

    if (responseCodeString.equals("0")) {
      onError(request, new RequestBlockedException("Client blocked request."));
    } else if (responseCodeString.startsWith("4")) {
      if (!tryParseErrorMessage(request, response.getText())) {
        onError(request, new RequestClientException(responseCodeString + " " + response.getStatusText()));
      }
    } else if (responseCodeString.startsWith("5")) {
      if (!tryParseErrorMessage(request, response.getText())) {
        onError(request, new RequestServerException(responseCodeString + " " + response.getStatusText()));
      }
    }

    return true;
  }

  /**
   * Try parsing an AERIUS-interpretable error, returning true if succesful.
   */
  private boolean tryParseErrorMessage(final Request request, final String text) {
    try {
      final JSONValue parsed = JSONParser.parseStrict(text);
      if (parsed.isObject() != null) {
        final JSONObject obj = parsed.isObject();
        if (obj.keySet().contains("success") && "false".equals(obj.get("success").isString().stringValue())) {
          failOverToFailure(request, obj);
          return true;
        }
      }
    } catch (final Exception e) {
      // Can't parse it, return reporting failure
      return false;
    }

    return false;
  }

  private void failOverToFailure(final Request request, final JSONObject obj) {
    GWTProd.error("SELECTOR", "Failure while fetching selector: "
        + obj.get("type").isString().stringValue()
        + " > "
        + obj.get("message").isString().stringValue());
    Scheduler.get().scheduleDeferred(() -> {
      onError(request, new RequestAeriusException(obj.get("message").isString().stringValue()));
    });
  }
}
