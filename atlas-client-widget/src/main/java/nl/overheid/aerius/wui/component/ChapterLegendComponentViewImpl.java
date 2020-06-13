package nl.overheid.aerius.wui.component;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.properties.LegendComponentProperties;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.util.LegacyWebComponentUtil;
import nl.overheid.aerius.wui.util.ReplacementAssistant;
import nl.overheid.aerius.wui.widget.EventComposite;

public class ChapterLegendComponentViewImpl extends EventComposite {
  private static final LegendComponentViewImplUiBinder UI_BINDER = GWT.create(LegendComponentViewImplUiBinder.class);

  interface LegendComponentViewImplUiBinder extends UiBinder<Widget, ChapterLegendComponentViewImpl> {}

  private final LegendComponentViewImplEventBinder EVENT_BINDER = GWT.create(LegendComponentViewImplEventBinder.class);

  interface LegendComponentViewImplEventBinder extends EventBinder<ChapterLegendComponentViewImpl> {}

  private final Map<String, String> selectors = new HashMap<>();
  private final Map<String, String> replaceables = new HashMap<>();

  private final LegendComponentProperties properties;

  @UiField SimplePanel targetPanel;

  private Element component;
  private final ReplacementAssistant replacer;

  @Inject
  public ChapterLegendComponentViewImpl(@Assisted final PanelContent content, @Assisted final EventBus eventBus,
      final ReplacementAssistant replacer) {
    this.replacer = replacer;
    this.properties = content.asLegendComponentProperties();

    setEventBus(eventBus);

    initWidget(UI_BINDER.createAndBindUi(this));

    LegacyWebComponentUtil.inject(properties, targetPanel, eventBus, c -> initComponentParameters(c));
  }

  public void initComponentParameters(final Element component) {
    this.component = component;

    properties.getParameters().entrySet().forEach(v -> addReplaceableAttribute(v.getKey(), v.getValue()));

    updateSelectorAttributes();
    updateReplaceables();
  }

  @EventHandler
  public void onSelectorEvent(final SelectorEvent e) {
    final Selector selector = e.getValue();

    if (selector.getValue().isPresent()) {
      selectors.put(selector.getType(), selector.getValue().get());
    } else {
      selectors.remove(selector.getType());
      if (component != null) {
        component.removeAttribute(selector.getType());
      }
    }

    if (component != null) {
      selector.getValue().ifPresent(v -> setFinalAttribute(selector.getType(), v));
      updateReplaceables();
    }
  }

  private void updateReplaceables() {
    replaceables.entrySet().forEach(v -> setReplaceableAttribute(v.getKey(), v.getValue()));
  }

  private void addReplaceableAttribute(final String key, final String value) {
    replaceables.put(key, value);
  }

  private void setReplaceableAttribute(final String key, final String value) {
    setFinalAttribute(key, replacer.replace(value));
  }

  private void setFinalAttribute(final String key, final String value) {
    component.setAttribute(key, value);
  }

  private void updateSelectorAttributes() {
    selectors.entrySet().forEach(v -> setFinalAttribute(v.getKey(), v.getValue()));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
