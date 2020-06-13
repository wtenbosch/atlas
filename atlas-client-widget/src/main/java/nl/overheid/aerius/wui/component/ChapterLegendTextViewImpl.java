package nl.overheid.aerius.wui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.wui.widget.EventComposite;

public class ChapterLegendTextViewImpl extends EventComposite {
  private static final TextLegendViewImplUiBinder UI_BINDER = GWT.create(TextLegendViewImplUiBinder.class);

  interface TextLegendViewImplUiBinder extends UiBinder<Widget, ChapterLegendTextViewImpl> {}

  @UiField(provided = true) String legendText;

  @Inject
  public ChapterLegendTextViewImpl(final @Assisted PanelContent config) {
    legendText = config.asLegendTextProperties().getText();

    initWidget(UI_BINDER.createAndBindUi(this));
  }
}
