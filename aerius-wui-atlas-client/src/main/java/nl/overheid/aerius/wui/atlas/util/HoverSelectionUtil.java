package nl.overheid.aerius.wui.atlas.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class HoverSelectionUtil {
  private static Map<Object, HandlerRegistration> mouseRegisters = new HashMap<>();
  private static Map<Object, HandlerRegistration> touchRegisters = new HashMap<>();

  public static Runnable displayLeft(final Widget target, final String text) {
    final SimpleHoverBox killBox = display(target, text, box -> attachLeft(box, target.getElement()), box -> box.destroyLeft());
    return () -> Optional.ofNullable(killBox).ifPresent(v -> v.destroyLeft());
  }

  public static Runnable displayRight(final Widget target, final String text) {
    final SimpleHoverBox killBox = display(target, text, box -> attachRight(box, target.getElement()), box -> box.destroyRight());
    return () -> Optional.ofNullable(killBox).ifPresent(v -> v.destroyRight());
  }

  public static Runnable displayTop(final Widget target, final String text) {
    final SimpleHoverBox killBox = display(target, text, box -> attachTop(box, target.getElement()), box -> box.destroyTop());
    return () -> Optional.ofNullable(killBox).ifPresent(v -> v.destroyTop());
  }

  public static Runnable displayBottom(final Widget target, final String text) {
    final SimpleHoverBox killBox = display(target, text, box -> attachBottom(box, target.getElement()), box -> box.destroyBottom());
    return () -> Optional.ofNullable(killBox).ifPresent(v -> v.destroyBottom());
  }

  public static Runnable displayLeft(final Widget target, final Supplier<String> text) {
    final SimpleHoverBox killBox = display(target, text, box -> attachLeft(box, target.getElement()), box -> box.destroyLeft());
    return () -> Optional.ofNullable(killBox).ifPresent(v -> v.destroyLeft());
  }

  public static Runnable displayRight(final Widget target, final Supplier<String> text) {
    final SimpleHoverBox killBox = display(target, text, box -> attachRight(box, target.getElement()), box -> box.destroyRight());
    return () -> Optional.ofNullable(killBox).ifPresent(v -> v.destroyRight());
  }

  public static Runnable displayTop(final Widget target, final Supplier<String> text) {
    final SimpleHoverBox killBox = display(target, text, box -> attachTop(box, target.getElement()), box -> box.destroyTop());
    return () -> Optional.ofNullable(killBox).ifPresent(v -> v.destroyTop());
  }

  public static Runnable displayBottom(final Widget target, final Supplier<String> text) {
    final SimpleHoverBox killBox = display(target, text, box -> attachBottom(box, target.getElement()), box -> box.destroyBottom());
    return () -> Optional.ofNullable(killBox).ifPresent(v -> v.destroyBottom());
  }

  private static SimpleHoverBox display(final Widget target, final Supplier<String> text, final Consumer<SimpleHoverBox> attachConsumer,
      final Consumer<SimpleHoverBox> destroyConsumer) {
    return display(target, text.get(), attachConsumer, destroyConsumer);
  }

  private static SimpleHoverBox display(final Widget target, final String text, final Consumer<SimpleHoverBox> attachConsumer,
      final Consumer<SimpleHoverBox> destroyConsumer) {
    if (text == null || text.isEmpty()) {
      return null;
    }

    final SimpleHoverBox box = new SimpleHoverBox(text);

    Document.get().getBody().appendChild(box.getElement());

    Scheduler.get().scheduleDeferred(() -> attachConsumer.accept(box));

    registerMouse(target, target.addDomHandler(e -> kill(box, destroyConsumer, target), MouseOutEvent.getType()));
    registerTouch(target, target.addDomHandler(e -> kill(box, destroyConsumer, target), TouchEndEvent.getType()));

    // Add decay
    Scheduler.get().scheduleFixedDelay(() -> {
      if (box.getElement().getParentElement() != null) {
        kill(box, destroyConsumer, target);
      }
      return false;
    }, 3500);

    return box;
  }

  private static void kill(final SimpleHoverBox box, final Consumer<SimpleHoverBox> destroyConsumer, final Widget target) {
    box.destroy();
    destroyConsumer.accept(box);
    mouseUnregister(target);
    touchUnregister(target);
  }

  private static void registerMouse(final Widget target, final HandlerRegistration register) {
    mouseUnregister(target);
    mouseRegisters.put(target, register);
  }

  private static void registerTouch(final Widget target, final HandlerRegistration register) {
    touchUnregister(target);
    touchRegisters.put(target, register);
  }

  private static void mouseUnregister(final Widget target) {
    final HandlerRegistration registration = mouseRegisters.get(target);
    if (registration != null) {
      registration.removeHandler();
    }
  }

  private static void touchUnregister(final Widget target) {
    final HandlerRegistration registration = touchRegisters.get(target);
    if (registration != null) {
      registration.removeHandler();
    }
  }

  public static void attachLeft(final SimpleHoverBox box, final Element target) {
    box.getElement().getStyle().setLeft(target.getAbsoluteLeft() - box.getOffsetWidth(), Unit.PX);
    box.getElement().getStyle().setTop(target.getAbsoluteTop() + target.getOffsetHeight() / 2 - box.getOffsetHeight() / 2, Unit.PX);

    box.appearLeft();
  }

  public static void attachRight(final SimpleHoverBox box, final Element target) {
    box.getElement().getStyle().setLeft(target.getAbsoluteLeft() + target.getOffsetWidth(), Unit.PX);
    box.getElement().getStyle().setTop(target.getAbsoluteTop() + target.getOffsetHeight() / 2 - box.getOffsetHeight() / 2, Unit.PX);

    box.appearRight();
  }

  public static void attachTop(final SimpleHoverBox box, final Element target) {
    attachTop(box, target, target.getOffsetWidth());
  }

  public static void attachTop(final SimpleHoverBox box, final Element target, final int targetWidth) {
    box.getElement().getStyle().setLeft(target.getAbsoluteLeft() + targetWidth / 2 - box.getOffsetWidth() / 2, Unit.PX);
    box.getElement().getStyle().setBottom(Window.getClientHeight() - target.getAbsoluteTop(), Unit.PX);
    box.getElement().getStyle().clearTop();

    box.appearTop();
  }

  public static void attachBottom(final SimpleHoverBox box, final Element target) {
    box.getElement().getStyle().setLeft(target.getAbsoluteLeft() + target.getOffsetWidth() / 2 - box.getOffsetWidth() / 2, Unit.PX);
    box.getElement().getStyle().setTop(target.getAbsoluteTop() + target.getOffsetHeight(), Unit.PX);

    box.appearBottom();
  }

  public static HandlerRegistration register(final Widget widget, final String text, final BiConsumer<Widget, String> consumer) {
    return widget.addDomHandler(e -> consumer.accept(widget, text), MouseOverEvent.getType());
  }

  public static HandlerRegistration register(final Widget widget, final Supplier<String> text, final BiConsumer<Widget, Supplier<String>> consumer) {
    return widget.addDomHandler(e -> consumer.accept(widget, text), MouseOverEvent.getType());
  }

  public static HandlerRegistration registerLeft(final Widget widget, final String text) {
    return register(widget, text, (e, t) -> HoverSelectionUtil.displayLeft(e, t));
  }

  public static HandlerRegistration registerRight(final Widget widget, final String text) {
    return register(widget, text, (e, t) -> HoverSelectionUtil.displayRight(e, t));
  }

  public static HandlerRegistration registerTop(final Widget widget, final String text) {
    return register(widget, text, (e, t) -> HoverSelectionUtil.displayTop(e, t));
  }

  public static HandlerRegistration registerBottom(final Widget widget, final String text) {
    return register(widget, text, (e, t) -> HoverSelectionUtil.displayBottom(e, t));
  }

  public static HandlerRegistration registerLeft(final Widget widget, final Supplier<String> text) {
    return register(widget, text, (e, t) -> HoverSelectionUtil.displayLeft(e, t));
  }

  public static HandlerRegistration registerRight(final Widget widget, final Supplier<String> text) {
    return register(widget, text, (e, t) -> HoverSelectionUtil.displayRight(e, t));
  }

  public static HandlerRegistration registerTop(final Widget widget, final Supplier<String> text) {
    return register(widget, text, (e, t) -> HoverSelectionUtil.displayTop(e, t));
  }

  public static HandlerRegistration registerBottom(final Widget widget, final Supplier<String> text) {
    return register(widget, text, (e, t) -> HoverSelectionUtil.displayBottom(e, t));
  }
}
