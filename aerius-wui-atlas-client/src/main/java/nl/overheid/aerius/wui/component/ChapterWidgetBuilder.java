package nl.overheid.aerius.wui.component;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.ImplementedBy;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.MainContentType;
import nl.overheid.aerius.shared.domain.PanelContent;

@ImplementedBy(ChapterWidgetBuilderImpl.class)
public interface ChapterWidgetBuilder {
  PanelWidgetDelegate createChapterWidget(MainContentType contentType, Chapter chapter, PanelContent config, SimplePanel target);
}
