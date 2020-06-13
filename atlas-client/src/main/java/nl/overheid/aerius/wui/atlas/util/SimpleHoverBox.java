package nl.overheid.aerius.wui.atlas.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SimpleHoverBox extends Composite {
  private static final int MARGIN_DESTROY = 10;
  private static final int MARGIN_AWAY = 5;
  private static final SimpleHoverBoxUiBinder UI_BINDER = GWT.create(SimpleHoverBoxUiBinder.class);

  interface SimpleHoverBoxUiBinder extends UiBinder<Widget, SimpleHoverBox> {}

  @UiField(provided = true) String text;

  public SimpleHoverBox(final String text) {
    this.text = text == null ? "" : text;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  public void destroy() {
    new Timer() {
      @Override
      public void run() {
        if (getElement().getParentElement() != null) {
          getElement().getParentElement().removeChild(getElement());
          removeFromParent();
        }
      };
    }.schedule(200);
  }

  public void appearRight() {
    getElement().getStyle().setMarginLeft(MARGIN_AWAY, Unit.PX);
    appear();
  }

  public void appearLeft() {
    getElement().getStyle().setMarginRight(MARGIN_AWAY, Unit.PX);
    appear();
  }

  public void appearTop() {
    getElement().getStyle().setMarginBottom(MARGIN_AWAY, Unit.PX);
    appear();
  }

  public void appearBottom() {
    getElement().getStyle().setMarginTop(MARGIN_AWAY, Unit.PX);
    appear();
  }

  private void appear() {
    getElement().getStyle().setOpacity(1D);
  }

  private void disappear() {
    getElement().getStyle().setOpacity(0D);
  }

  public void destroyRight() {
    getElement().getStyle().setMarginLeft(MARGIN_DESTROY, Unit.PX);
    disappear();
  }

  public void destroyLeft() {
    getElement().getStyle().setMarginRight(MARGIN_DESTROY, Unit.PX);
    disappear();
  }

  public void destroyTop() {
    getElement().getStyle().setMarginBottom(MARGIN_DESTROY, Unit.PX);
    disappear();
  }

  public void destroyBottom() {
    getElement().getStyle().setMarginTop(MARGIN_DESTROY, Unit.PX);
    disappear();
  }
}
