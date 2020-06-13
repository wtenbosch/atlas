package nl.overheid.aerius.wui.component;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.MainContentType;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.wui.atlas.factories.ChapterWidgetFactory;

public class ChapterWidgetBuilderImpl implements ChapterWidgetBuilder {
  @Inject private ChapterWidgetFactory factory;

  @Override
  public PanelWidgetDelegate createChapterWidget(final MainContentType contentType, final Chapter chapter, final PanelContent config,
      final SimplePanel target) {
    PanelWidgetDelegate delegate;

    if (contentType == null) {
      delegate = factory.getErrorChapter(target, "No valid main content type.");
    } else {
      switch (contentType) {
      case COMPONENT:
        switch (config.asMainComponentProperties().getVersion()) {
        case 3:
          delegate = factory.getComponentChapter(target, config);
          break;
        case 1:
        default:
          delegate = factory.getLegacyComponentChapter(target, config);
          break;
        }
        break;
      case MAP:
        throw new RuntimeException("Type not available: " + contentType);
      case TEXT:
      default:
        delegate = factory.getTextChapter(target, chapter, config);
        break;
      }
    }

    return delegate;
  }
}
