package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;

public class LayerLegendLabelViewImpl extends Composite {
  private static final LegendLabelViewImplUiBinder UI_BINDER = GWT.create(LegendLabelViewImplUiBinder.class);

  interface LegendLabelViewImplUiBinder extends UiBinder<Widget, LayerLegendLabelViewImpl> {}

  @UiField Label label;

  @Inject
  public LayerLegendLabelViewImpl(@Assisted final String text, final ObservingReplacementAssistant replacer) {
    initWidget(UI_BINDER.createAndBindUi(this));

    replacer.register(text, v -> label.setText(v));
  }
}
