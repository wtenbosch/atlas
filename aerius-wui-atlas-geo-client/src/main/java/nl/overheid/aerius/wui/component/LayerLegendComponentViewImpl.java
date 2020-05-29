package nl.overheid.aerius.wui.component;

import java.util.HashMap;
import java.util.List;
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

import nl.overheid.aerius.geo.domain.legend.ComponentLegendConfig;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.properties.ComponentProperties;
import nl.overheid.aerius.wui.atlas.command.ChapterReplacementCommand;
import nl.overheid.aerius.wui.atlas.event.SelectorEvent;
import nl.overheid.aerius.wui.util.LegacyWebComponentUtil;
import nl.overheid.aerius.wui.util.NativeSelectorDomEvents;
import nl.overheid.aerius.wui.util.ReplacementAssistant;
import nl.overheid.aerius.wui.util.SelectorUtil;
import nl.overheid.aerius.wui.widget.EventComposite;

public class LayerLegendComponentViewImpl extends EventComposite {
  private static final LegendComponentViewImplUiBinder UI_BINDER = GWT.create(LegendComponentViewImplUiBinder.class);

  interface LegendComponentViewImplUiBinder extends UiBinder<Widget, LayerLegendComponentViewImpl> {}

  private final LayerLegendComponentViewImplEventBinder EVENT_BINDER = GWT.create(LayerLegendComponentViewImplEventBinder.class);

  interface LayerLegendComponentViewImplEventBinder extends EventBinder<LayerLegendComponentViewImpl> {}

  private final Map<String, String> selectors = new HashMap<>();
  private final Map<String, String> replaceables = new HashMap<>();

  private final Map<String, String> parameters;

  @UiField SimplePanel targetPanel;

  private Element component;

  private List<String> selectables;

  private final ReplacementAssistant replacer;

  @Inject
  public LayerLegendComponentViewImpl(@Assisted final List<String> selectables, @Assisted final ComponentLegendConfig conf,
      final ReplacementAssistant replacer) {
    this.selectables = selectables;
    this.replacer = replacer;
    parameters = conf.getParameters();

    initWidget(UI_BINDER.createAndBindUi(this));

    LegacyWebComponentUtil.inject(new ComponentProperties() {
      @Override
      public String getComponentSource() {
        return conf.getComponentSource();
      }

      @Override
      public String getComponentName() {
        return conf.getComponentName();
      }

      @Override
      public String getUrl() {
        return getParameters().get("url");
      }

      @Override
      public Map<String, String> getParameters() {
        return conf.getParameters();
      }

    }, targetPanel, null, c -> initComponentParameters(c));
  }

  public void initComponentParameters(final Element component) {
    this.component = component;

    if (parameters != null) {
      parameters.forEach((k, v) -> addReplaceableAttribute(k, v));
    }

    NativeSelectorDomEvents.addEventListener(component, s -> eventBus.fireEvent(new SelectorEvent(s)));
    NativeSelectorDomEvents.addReplacementListener(component, s -> eventBus.fireEvent(new ChapterReplacementCommand(s)));

    updateSelectorAttributes();
    updateReplaceables();
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

  @EventHandler
  public void onSelectorEvent(final SelectorEvent e) {
    final Selector selector = e.getSelector();
    if (!SelectorUtil.matchesSmart(selector, selectables)) {
      return;
    }

    if (!selector.isSystem() && selectors.containsKey(selector.getType())
        && selectors.get(selector.getType()).equals(selector.getValue().orElse(null))) {
      return;
    }

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
      updateSelectorAttributes();
    }
  }

  private void setFinalAttribute(final String key, final String value) {
    component.setAttribute(key, value);
  }

  private void updateSelectorAttributes() {
    selectors.entrySet().forEach(v -> setFinalAttribute(v.getKey(), v.getValue()));
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    if (eventBus == null) {
      // FIXME Figure out when this happens (probably layer panel switcher)
      return;
    }

    super.setEventBus(eventBus, this, EVENT_BINDER);
  }
}
