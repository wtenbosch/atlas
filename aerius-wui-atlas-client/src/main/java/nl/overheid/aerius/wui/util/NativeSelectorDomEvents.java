package nl.overheid.aerius.wui.util;

import java.util.function.Consumer;

import com.google.gwt.dom.client.Element;

import nl.overheid.aerius.shared.domain.Replacement;
import nl.overheid.aerius.shared.domain.Selector;

/**
 * For registering Java {@link Runnable runnables} as event listeners on GWT DOM elements.
 * <p>
 * This class allows for registering listeners for all DOM events including those not supported by GWT.
 */
public class NativeSelectorDomEvents {

  public static native void addEventListener(Element element, Consumer<Selector> listener) /*-{
    var eventListener = function(e) {
      var selector = @nl.overheid.aerius.shared.domain.Selector::new(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(e.detail.type, String(e.detail.value), String(e.detail.title));
      listener.@java.util.function.Consumer::accept(Ljava/lang/Object;)(selector);
    };

    if (element.addEventListener) {
      element.addEventListener("selector", eventListener, false);
    } else if (element.attachEvent) {
      element.attachEvent('on' + "selector", eventListener);
    }
  }-*/;

  public static native void addReplacementListener(final Element element, final Consumer<Replacement> listener) /*-{
    var eventListener = function(e) {
      var replacement = @nl.overheid.aerius.shared.domain.Replacement::new(Ljava/lang/String;Ljava/lang/String;)(e.key, e.value);
      listener.@java.util.function.Consumer::accept(Ljava/lang/Object;)(replacement);
    };

    if (element.addEventListener) {
      element.addEventListener("replacement", eventListener, true);
    } else if (element.attachEvent) {
      element.attachEvent('on' + "replacement", eventListener);
    }
  }-*/;

  public static native void addSelectionListener(final Element element, final Consumer<String> listener) /*-{
    var eventListener = function(e) {
      var selection = e.value;
      listener.@java.util.function.Consumer::accept(Ljava/lang/Object;)(selection);
    };

    if (element.addEventListener) {
      element.addEventListener("selection", eventListener, true);
    } else if (element.attachEvent) {
      element.attachEvent('on' + "selection", eventListener);
    }
  }-*/;
}