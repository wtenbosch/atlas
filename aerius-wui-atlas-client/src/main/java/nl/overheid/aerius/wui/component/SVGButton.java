package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.util.SvgUtil;
import nl.overheid.aerius.wui.widget.EventComposite;

public abstract class SVGButton extends EventComposite {
  private static final SVGButtonUiBinder UI_BINDER = GWT.create(SVGButtonUiBinder.class);

  interface SVGButtonUiBinder extends UiBinder<Widget, SVGButton> {}

  protected @UiField FlowPanel control;
  protected @UiField SimplePanel button;

  public SVGButton() {
    initWidget(UI_BINDER.createAndBindUi(this));
    ensureDebugId(AtlasTestIDs.BUTTON_SVG);
  }

  @Override
  protected void initWidget(final Widget widget) {
    super.initWidget(widget);

    final DataResource image = getImage();
    if (image == null) {
      button.removeFromParent();
    } else {
      SvgUtil.I.setSvg(button, image);
    }

    control.addDomHandler(event -> {
      onSelect();
    }, ClickEvent.getType());

    control.addDomHandler(event -> {
      onHover();
    }, MouseOverEvent.getType());

  }

  protected abstract DataResource getImage();

  protected abstract void onSelect();

  protected abstract void onHover();
}
