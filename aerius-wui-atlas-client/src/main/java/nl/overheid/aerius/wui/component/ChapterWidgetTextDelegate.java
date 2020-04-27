package nl.overheid.aerius.wui.component;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.wui.atlas.factories.SelectableTextWidgetFactory;
import nl.overheid.aerius.wui.event.BasicEventComponent;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.util.ReplacementAssistant;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.widget.HasEventBus;
import nl.overheid.aerius.wui.widget.HeadingWidget;

public class ChapterWidgetTextDelegate extends BasicEventComponent implements PanelWidgetDelegate, HasEventBus {
  private static final ChapterWidgetTextDelegateUiBinder UI_BINDER = GWT.create(ChapterWidgetTextDelegateUiBinder.class);

  interface ChapterWidgetTextDelegateUiBinder extends UiBinder<Widget, ChapterWidgetTextDelegate> {}

  interface ChapterWidgetTextDelegateEventBinder extends EventBinder<ChapterWidgetTextDelegate> {}

  private final ChapterWidgetTextDelegateEventBinder EVENT_BINDER = GWT.create(ChapterWidgetTextDelegateEventBinder.class);

  @UiField FlowPanel selectorContainer;

  @UiField HeadingWidget chapterField;

  private final Map<String, SelectorWidget> selectables = new HashMap<>();
  @UiField(provided = true) SelectableTextWidget selectableText;

  private final PanelContent content;

  private final Chapter chapter;

  @Inject
  public ChapterWidgetTextDelegate(final @Assisted AcceptsOneWidget panel, @Assisted final Chapter chapter, final @Assisted PanelContent content,
      final ReplacementAssistant replacer, final ObservingReplacementAssistant observableReplacer, final SelectableTextWidgetFactory fact) {
    this.chapter = chapter;
    this.content = content;

    selectableText = fact.createSelectableTextWidget();

    panel.setWidget(UI_BINDER.createAndBindUi(this));

    chapterField.setText(chapter.title());
  }

  @Override
  public void notifySelector(final Selector selector) {}

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);

    final String referenceName = chapter.uid() + "-" + content.asMainTextProperties().getReferenceName();

    // For each selector subscription, display a widget
    for (final String selectable : content.selectables()) {
      if (SelectorUtil.isApplicationType(selectable)) {
        continue;
      }

      if (selectables.containsKey(selectable)) {
        continue;
      }

      final SelectorWidget widg = new SelectorWidget(selectable);
      selectorContainer.add(widg);
      selectables.put(selectable, widg);
      widg.setEventBus(eventBus);
    }

    selectableText.setTypes(content.selectables());
    selectableText.setContentTextUrl(referenceName);

    selectableText.setEventBus(eventBus);
  }
}
