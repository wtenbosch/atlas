package nl.overheid.aerius.wui.component;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.geo.command.LayerHiddenCommand;
import nl.overheid.aerius.geo.command.LayerOpacityCommand;
import nl.overheid.aerius.geo.command.LayerVisibleCommand;
import nl.overheid.aerius.geo.domain.IsLayer;
import nl.overheid.aerius.geo.domain.LayerInfo;
import nl.overheid.aerius.geo.resources.GeoResources;
import nl.overheid.aerius.wui.atlas.daemon.config.ConfigurationDaemon;
import nl.overheid.aerius.wui.atlas.daemon.config.Configurations;
import nl.overheid.aerius.wui.atlas.factories.LayerLegendBuilder;
import nl.overheid.aerius.wui.atlas.test.AtlasTestIDs;
import nl.overheid.aerius.wui.util.MathUtil;
import nl.overheid.aerius.wui.util.ObservingReplacementAssistant;
import nl.overheid.aerius.wui.widget.EventComposite;
import nl.overheid.aerius.wui.widget.FlipButton;
import nl.overheid.aerius.wui.widget.HasEventBus;
import nl.overheid.aerius.wui.widget.SliderBar;

public class LayerPanelRadioItem extends EventComposite implements LayerPanelItem {
  private static final LayerPanelRadioItemUiBinder UI_BINDER = GWT.create(LayerPanelRadioItemUiBinder.class);

  interface LayerPanelRadioItemUiBinder extends UiBinder<Widget, LayerPanelRadioItem> {}

  public interface CustomStyle extends CssResource {
    String clustered();
  }

  @UiField CustomStyle style;

  @UiField SimplePanel innerCluster;
  @UiField SimplePanel hideButtonContainer;

  @UiField(provided = true) FlipButton flipButton;
  @UiField(provided = true) ToggleButton hideButton;

  @UiField ListBox layerListBox;

  @UiField FlowPanel selectorContainer;

  @UiField(provided = true) SliderBar opacitySlider;
  @UiField Widget inner;
  @UiField AcceptsOneWidget legend;

  private boolean hidden;
  private final EventBus mapEventBus;

  private final Map<String, IsLayer<?>> layers = new HashMap<>();
  private IsLayer<?> selectedLayer;

  private final LayerLegendBuilder legendFactory;

  private boolean autoExpand;

  private boolean deferredVisible;

  private boolean deferred;

  public LayerPanelRadioItem(final EventBus mapEventBus, final GeoResources res, final ObservingReplacementAssistant replacer,
      final LayerLegendBuilder legendFactory, final ConfigurationDaemon configDaemon) {
    this.mapEventBus = mapEventBus;
    this.legendFactory = legendFactory;
    this.flipButton = new FlipButton(v -> {
      inner.setVisible(v);
    });
    flipButton.setClosedRotation(FlipButton.EAST);

    hideButton = new ToggleButton(new Image(res.toggleLayerOn().getSafeUri()), new Image(res.toggleLayerOff().getSafeUri()));

    opacitySlider = new SliderBar(o -> onSetLayerOpacity(o));
    configDaemon.registerAsBoolean(Configurations.LAYER_OPACITY, v -> {
      opacitySlider.setVisible(v);
    });
    configDaemon.registerAsBoolean(Configurations.LAYER_EXPAND, v -> {
      autoExpand = v;
    });

    initWidget(UI_BINDER.createAndBindUi(this));
    flipButton.init(autoExpand);

    opacitySlider.ensureDebugId(AtlasTestIDs.SLIDER_OPACITY);
    hideButton.ensureDebugId(AtlasTestIDs.BUTTON_COLLAPSE_LAYER);
    flipButton.ensureDebugId(AtlasTestIDs.BUTTON_FLIP);
  }

  @Override
  public void setClustered(final boolean clustered) {
    hideButtonContainer.setStyleName(style.clustered(), clustered);
    innerCluster.setStyleName(style.clustered(), clustered);
  }

  @Override
  public void setLayerVisible(final IsLayer<?> layer, final boolean visible) {
    if (layer != selectedLayer) {
      if (visible) {
        selectLayer(layer);
        setListBoxSelection(layer);
        setHideButton(visible);
      }
      return;
    }

    setHideButton(visible);
  }

  private void setHideButton(final boolean visible) {
    hideButton.setDown(!visible);
    if (autoExpand) {
      scheduleSetVisible(visible);
    }
    this.hidden = !visible;
  }

  private void scheduleSetVisible(final boolean visible) {
    deferredVisible = visible;
    if (deferred) {
      return;
    }

    deferred = true;
    Scheduler.get().scheduleDeferred(() -> {
      flipButton.setState(!deferredVisible);
      deferred = false;
    });
  }

  @UiHandler("hideButton")
  public void onRemoveButtonClick(final ClickEvent e) {
    mapEventBus.fireEvent(hidden ? new LayerVisibleCommand(selectedLayer) : new LayerHiddenCommand(selectedLayer));
  }

  public void onSetLayerOpacity(final double o) {
    if (hideButton.isDown()) {
      mapEventBus.fireEvent(new LayerVisibleCommand(selectedLayer));
    }

    layers.values().forEach(v -> mapEventBus.fireEvent(new LayerOpacityCommand(v, o)));
  }

  @UiHandler("layerListBox")
  public void onScrollEvent(final MouseWheelEvent e) {
    if (e.getDeltaY() < 0) {
      selectPrevious();
    } else if (e.getDeltaY() > 0) {
      selectNext();
    }
  }

  private void selectPrevious() {
    selectIdx(layerListBox.getSelectedIndex() - 1);
  }

  private void selectNext() {
    selectIdx(layerListBox.getSelectedIndex() + 1);
  }

  private void selectIdx(final int idx) {
    layerListBox.setSelectedIndex(MathUtil.positiveMod(idx, layerListBox.getItemCount()));
    DomEvent.fireNativeEvent(Document.get().createChangeEvent(), layerListBox);
  }

  @UiHandler("layerListBox")
  public void onSelectionChangeEvent(final ChangeEvent e) {
    final IsLayer<?> selection = layers.get(layerListBox.getSelectedValue());

    selectLayer(selection);
  }

  private void selectLayer(final IsLayer<?> selection) {
    if (selectedLayer != null) {
      mapEventBus.fireEvent(new LayerHiddenCommand(selectedLayer));
    }

    setSelectedLayer(selection);

    mapEventBus.fireEvent(new LayerVisibleCommand(selection));

  }

  private void setListBoxSelection(final IsLayer<?> selection) {
    for (int i = 0; i < layerListBox.getItemCount(); i++) {
      if (layers.get(layerListBox.getValue(i)) == selection) {
        layerListBox.setSelectedIndex(i);
        return;
      }
    }
  }

  @Override
  public void remove(final IsLayer<?> isLayer) {
    removeFromParent();
  }

  @Override
  public void setLayerOpacity(final double opacity) {
    opacitySlider.setValue(opacity);
  }

  public void setLegendPanel(final Widget legendWidget) {
    legend.setWidget(legendWidget);
  }

  public void add(final IsLayer<?> isLayer) {
    if (selectedLayer == null) {
      setSelectedLayer(isLayer);
    }

    final LayerInfo info = isLayer.getInfo().get();

    layerListBox.addItem(info.getTitle(), info.getName());
    layers.put(info.getName(), isLayer);
  }

  private void setSelectedLayer(final IsLayer<?> isLayer) {
    selectedLayer = isLayer;

    final Widget legendWidget = legendFactory.getLegendWidget(isLayer.getInfo().get());
    if (legendWidget instanceof HasEventBus) {
      ((HasEventBus) legendWidget).setEventBus(eventBus);
    }

    setLegendPanel(legendWidget);
  }
}
