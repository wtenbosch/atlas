package nl.overheid.aerius.wui.atlas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.wui.atlas.service.parser.ProxiedCallback;

public abstract class StaggeredService {
  private boolean busy;
  private final List<Runnable> straglers = new ArrayList<>();

  protected <T> void request(final String url, final Function<AsyncCallback<T>, RequestCallback> parser, final AsyncCallback<T> callback) {
    straglers.add(() -> RequestUtil.doGet(url, parser, ProxiedCallback.wrap(callback, () -> requestNext())));

    if (!busy) {
      busy = true;
      requestNext();
    }
  }

  private void requestNext() {
    if (!straglers.isEmpty()) {
      straglers.remove(0).run();
    } else {
      busy = false;
    }
  }
}
