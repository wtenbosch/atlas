package nl.overheid.aerius.wui.component;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import nl.overheid.aerius.shared.domain.DocumentResource;

public class CompactDocumentsPanel extends Composite {
  private static final CompactDocumentsPanelUiBinder UI_BINDER = GWT.create(CompactDocumentsPanelUiBinder.class);

  interface CompactDocumentsPanelUiBinder extends UiBinder<Widget, CompactDocumentsPanel> {}

  @UiField FlowPanel container;

  public CompactDocumentsPanel() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  public void setDocuments(final List<DocumentResource> documents) {
    clear();
    if (documents == null) {
      return;
    }

    for (final DocumentResource res : documents) {
      final CompactDocumentResourceWidget widg = new CompactDocumentResourceWidget(res);
      container.add(widg);
    }
  }

  public void clear() {
    container.clear();
  }
}
