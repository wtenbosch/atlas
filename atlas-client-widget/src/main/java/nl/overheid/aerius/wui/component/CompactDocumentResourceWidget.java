package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.DocumentResource;
import nl.overheid.aerius.wui.resources.AtlasR;
import nl.overheid.aerius.wui.util.FormatUtil;
import nl.overheid.aerius.wui.util.SvgUtil;

public class CompactDocumentResourceWidget extends Composite {
  private static final DocumentResourceWidgetUiBinder UI_BINDER = GWT.create(DocumentResourceWidgetUiBinder.class);

  interface DocumentResourceWidgetUiBinder extends UiBinder<Widget, CompactDocumentResourceWidget> {}

  @UiField Label name;
  @UiField Label date;

  @UiField SimplePanel icon;

  private final DocumentResource resource;

  public CompactDocumentResourceWidget(final DocumentResource resource) {
    this.resource = resource;
    initWidget(UI_BINDER.createAndBindUi(this));

    SvgUtil.I.setSvg(icon, AtlasR.images().pdfIcon());

    name.setText(resource.name());
    date.setText(FormatUtil.formatDate(resource.date()));
  }

  @UiHandler("link")
  public void onLinkClick(final ClickEvent e) {
    Window.open(resource.url(), "_blank", "");
  }
}
