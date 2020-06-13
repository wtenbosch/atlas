package nl.overheid.aerius.templates.stories.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.ChapterIcon;
import nl.overheid.aerius.shared.domain.DocumentResource;
import nl.overheid.aerius.shared.domain.LayerNames;
import nl.overheid.aerius.shared.domain.LegendContentType;
import nl.overheid.aerius.shared.domain.MainContentType;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelContent.Builder;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.SelectorResource;
import nl.overheid.aerius.shared.domain.properties.BasicParameterizedProperties;
import nl.overheid.aerius.shared.domain.properties.InfoProperties;

public class ChapterBuilder {
//  private static final Logger LOG = LoggerFactory.getLogger(ChapterBuilder.class);

  private final Chapter.Builder bldr = Chapter.builder();

  private final Map<PanelNames, PanelContent.Builder> panels = new HashMap<>();

  private final List<String> layers = new ArrayList<>();

  private final Set<String> layerSelectables = new LinkedHashSet<>();
  private final Set<String> mapSelectables = new LinkedHashSet<>();
  private final Set<String> mainSelectables = new LinkedHashSet<>();

  private final Map<PanelNames, List<String>> panelSelectables = new HashMap<>();

  private final Map<String, SelectorResource.Builder> selectors = new LinkedHashMap<>();

  private ComponentConfigurationBuilder mainComponent;

  private int sortId;

  private final List<DocumentResource> exportLinks = new ArrayList<>();
  private String exportText;

  private boolean noAutoAccumulate;

  private ComponentConfigurationBuilder legendComponent;

  private BiFunction<String, String, String> infoUri;

  private String uid;

  /**
   * The UID for this chapter. Typically not set in the template specification.
   */
  public ChapterBuilder uid(final String uid) {
    this.uid = uid;
    bldr.uid(uid);
    return this;
  }

  /**
   * Skip the auto-accumulation step while creating the export panel, leaving only
   * explicitly defined document resources.
   */
  public ChapterBuilder noAutoAccumulateLinks() {
    noAutoAccumulate = true;
    return this;
  }

  /**
   * The name for this chapter.
   */
  public ChapterBuilder identifier(final String identifier) {
    bldr.identifier(identifier);
    return this;
  }

  /**
   * The title for this chapter.
   */
  public ChapterBuilder title(final String title) {
    bldr.title(title);
    return this;
  }

  /**
   * The icon for this chapter.
   */
  public ChapterBuilder icon(final ChapterIcon icon) {
    bldr.icon(icon);
    return this;
  }

  /**
   * The sort ID for this chapter. If displayed in a list with other chapters,
   * this ID will be used to sort them.
   *
   * IDs can be skipped, be negative, be duplicated, or not be present at all
   * (will default to chapter index, as implemented by the DatasetBuilder)
   */
  public ChapterBuilder sortId(final int sortId) {
    this.sortId = sortId;
    return this;
  }

  int sortId() {
    return sortId;
  }

  /**
   * A description for this chapter. The description will be put into the
   * chapter's "meta" panel. Can contain HTML tags.
   */
  public ChapterBuilder description(final String description) {
    panels.put(PanelNames.PANEL_META, PanelContent.builder()
        .properties(Map.of(
            "text", Map.of("value", description))));
    return this;
  }

  /**
   * A description for this chapter. The description will be put into the
   * chapter's "meta" panel. Can contain HTML tags.
   */
  public ChapterBuilder description(final TextBuilder bldr) {
    return description(bldr.build());
  }

  /**
   * A description for this chapter. The description will be put into the
   * chapter's "export" panel. Can contain HTML tags.
   */
  public ChapterBuilder export(final String export, final DocumentResource... links) {
    this.exportText = export;
    exportLinks.addAll(Arrays.asList(links));
    return this;
  }

  /**
   * A description for this chapter. The description will be put into the
   * chapter's "export" panel. Can contain HTML tags.
   */
  public ChapterBuilder export(final TextBuilder bldr, final DocumentResource... links) {
    return export(bldr.build(), links);
  }

  /**
   * Specify a text legend for this chapter. Will occupy the chapter's "legend"
   * panel.
   */
  public ChapterBuilder legend(final String text) {
    panels.put(PanelNames.PANEL_LEGEND, PanelContent.builder()
        .properties(Map.of("content_type", LegendContentType.TEXT.name().toLowerCase(),
            "text", text)));
    return this;
  }

  /**
   * Specify a text legend for this chapter. Will occupy the chapter's "legend"
   * panel.
   */
  public ChapterBuilder legend(final TextBuilder text) {
    panels.put(PanelNames.PANEL_LEGEND, PanelContent.builder()
        .properties(Map.of("content_type", LegendContentType.TEXT.name().toLowerCase(),
            "text", text.build())));
    return this;
  }

  /**
   * Specify a legend configuration for this chapter using a component builder.
   * Will occupy the chapter's "legend" panel. See
   * {@link ComponentsWithSelectable} for known components.
   */
  public ChapterBuilder legend(final ComponentConfigurationBuilder bldr) {
    this.legendComponent = bldr.copy();
    return this;
  }

  /**
   * Defines a component for this chapter. Will occupy the chapter's "main" panel.
   * Also pass the given dictionary of parameters as a property. These properties
   * will be present on the component element. Any parameter value can contain
   * replacement tags.
   */
  public ChapterBuilder legend(final ComponentConfigurationBuilder bldr, final Map<String, String> params) {
    legend(bldr);
    params.forEach((k, v) -> legendComponent.parameter(k, v));
    return this;
  }

  /**
   * Defines a component for this chapter. Will occupy the chapter's "legend"
   * panel.
   */
  public ChapterBuilder legend(final ComponentType type) {
    legend(new ComponentConfigurationBuilder()
        .component(type));
    return this;
  }

  /**
   * Defines a component for this chapter. Will occupy the chapter's "legend"
   * panel. Also pass the given dictionary of parameters as a property. These
   * properties will be present on the component element. Any parameter value can
   * contain replacement tags.
   */
  public ChapterBuilder legend(final ComponentType type, final Map<String, String> params) {
    legend(new ComponentConfigurationBuilder()
        .component(type), params);
    return this;
  }

  /**
   * Specify a legend configuration for this chapter using a component builder.
   * Will occupy the chapter's "legend" panel. See
   * {@link ComponentsWithSelectable} for known components.
   */
  public ChapterBuilder component(final ComponentConfigurationBuilder bldr) {
    this.mainComponent = bldr.copy();
    bldr.chapterSelectables().forEach(v -> selectable(v));
    return this;
  }

  public ChapterBuilder component(final ComponentConfigurationBuilder bldr, final String url) {
    component(bldr);
    mainComponent.url(url);
    return this;
  }

  /**
   * Defines a component for this chapter. Will occupy the chapter's "main" panel.
   */
  public ChapterBuilder component(final ComponentType type) {
    component(new ComponentConfigurationBuilder()
        .component(type));
    return this;
  }

  /**
   * Defines a component for this chapter. Will occupy the chapter's "main" panel.
   * Also pass the given url (a commonly occuring property) as a component
   * parameter. The URL can contain replacement tags.
   */
  public ChapterBuilder component(final ComponentType type, final String url) {
    component(new ComponentConfigurationBuilder()
        .component(type), url);
    return this;
  }

  /**
   * Defines a component for this chapter. Will occupy the chapter's "main" panel.
   * Also pass the given url (a commonly occuring property) as a component
   * parameter. The URL can contain replacement tags.
   */
  public ChapterBuilder component(final ComponentType type, final String url, final Map<String, String> params) {
    component(new ComponentConfigurationBuilder()
        .component(type)
        .parameters(params), url);
    return this;
  }
  
  

  /**
   * Defines a component for this chapter with the given name and component source
   * code (uri).
   */
  public ChapterBuilder component(final String name, final String source) {
    return component(new ComponentConfigurationBuilder()
        .component(name, source));
  }

  /**
   * Defines a map for this chapter, occupying the chapter's "main" panel. Can
   * contain a list of layer names to add to this chapter.
   *
   * See {@link LayerNames} for a list of known layers.
   */
  public ChapterBuilder mainMap(final String... layers) {
    panels.put(PanelNames.PANEL_MAIN, PanelContent.builder()
        .properties(Map.of(
            "content_type", MainContentType.MAP.name().toLowerCase())));

    Stream.of(layers)
        .forEach(v -> layer(v));

    return this;
  }

  /**
   * Defines a map for this chapter, occupying the chapter's "map" panel (on the
   * left). Can contain a list of layer names to add to this chapter.
   *
   * See {@link LayerNames} for a list of known layers.
   */
  public ChapterBuilder contextMap(final String... layers) {
    panels.put(PanelNames.PANEL_MAP, PanelContent.builder()
        .properties(Map.of()));

    Stream.of(layers)
        .forEach(v -> layer(v));

    return this;
  }

  /**
   * Add a selectable to this chapter. The given uri in the builder will be
   * templated to the preconfigured selector service endpoint.
   */
  public ChapterBuilder selectable(final SelectorResourceBuilder conf) {
    return selectable(conf, true);
  }

  /**
   * Add a selectable to this chapter. The given uri in the builder will be
   * templated to the preconfigured selector service endpoint. Also pass whether
   * to show this layer by default.
   */
  public ChapterBuilder selectable(final SelectorResourceBuilder conf, final boolean alwaysShow) {
    return selectable(conf.type(), UrlHelper.getSelector(conf.uri()), alwaysShow);
  }

  /**
   * Add a selectable to this chapter by type and fully-qualified uri
   */
  public ChapterBuilder selectable(final String type, final String url) {
    return selectable(SelectorResource.builder()
        .type(type)
        .url(url));
  }

  /**
   * Add a selectable to this chapter by type and fully-qualified uri. Also pass
   * whether to show this layer by default.
   */
  public ChapterBuilder selectable(final String type, final String url, final boolean alwaysShow) {
    return selectable(SelectorResource.builder()
        .type(type)
        .url(url), alwaysShow);
  }

  /**
   * Add a preconfigured chapter selectable to this chapter.
   */
  public ChapterBuilder selectable(final SelectorResource.Builder selectorResource) {
    return selectable(selectorResource, true);
  }

  /**
   * Add a preconfigured chapter selectable to this chapter.
   */
  public ChapterBuilder selectable(final SelectorResource.Builder selectorResource, final boolean alwaysShow) {
    if (alwaysShow) {
      mainSelectables.add(selectorResource.build().type());
    }

    selectors.put(selectorResource.build().type(), selectorResource);
    return this;
  }

  /**
   * Set the panel selectables for the given panel. Overrides all other
   * selectables which are configured by default.
   */
  public ChapterBuilder panelSelectables(final PanelNames name, final List<String> selectables) {
    panelSelectables.put(name, List.copyOf(selectables));

    return this;
  }

  /**
   * Add a single layer to this chapter by name.
   */
  public ChapterBuilder layer(final String layer) {
    if (layer == null) {
      throw new RuntimeException("Null layers are not allowed.");
    }

    layers.add(layer);

    return this;
  }

  /**
   * Add a pre-configured layer spec to this chapter, which may include selectors
   * and the like.
   */
  public ChapterBuilder layer(final LayerConfigurationBuilder layer) {
    return layer(layer, false);
  }

  /**
   * Add a pre-configured layer spec to this chapter, which may include selectors
   * and the like. Also pass whether to display this layer's selectables in the
   * layer panel.
   */
  public ChapterBuilder layer(final LayerConfigurationBuilder layer, final boolean displaySelectables) {
    if (layer == null) {
      throw new RuntimeException("Null layers are not allowed.");
    }

    layers.add(layer.layer());

    if (displaySelectables) {
      layer.selectors().forEach(this::layerSelectable);
    }
    layer.selectors().forEach(this::mapSelectable);

    layer.chapterSelectables().forEach(v -> selectable(v, false));

    return this;
  }

  /**
   * Add a selectable to the layer panel (i.e. on natura level)
   */
  public ChapterBuilder layerSelectable(final String selectable) {
    layerSelectables.add(selectable);
    return this;
  }

  /**
   * Remove a selectable from the layer panel (i.e. on natura level)
   */
  public ChapterBuilder removeLayerSelectable(final String selectable) {
    layerSelectables.remove(selectable);
    return this;
  }

  /**
   * Add a selectable to the map panel (i.e. location level)
   */
  public ChapterBuilder mapSelectable(final String selectable) {
    mapSelectables.add(selectable);
    return this;
  }

  /**
   * Remove a selectable from the map panel (i.e. location level)
   */
  public ChapterBuilder removeMapSelectable(final String selectable) {
    mapSelectables.remove(selectable);
    return this;
  }

  /**
   * Specify that the chapter's "main" panel be of the Text type, which will fetch
   * text out of the 'Chapter content' call using a default reference.
   *
   * NOTE: Chapter content is not yet fully supported.
   */
  public ChapterBuilder mainInfo() {
    return mainInfo("main");
  }

  /**
   * Specify that the chapter's "main" panel be of the Text type, which will fetch
   * text out of the 'Chapter content' call using the specified reference.
   *
   * NOTE: Chapter content is not yet fully supported.
   */
  public ChapterBuilder mainInfo(final String reference) {
    panels.put(PanelNames.PANEL_MAIN, PanelContent.builder()
        .properties(Map.of(
            "content_type", MainContentType.TEXT.name().toLowerCase(),
            "reference_name", reference)));
    return this;
  }

  /**
   * Specify that the chapter's "main" panel be of the Properties type, which is
   * typically used for development purposes.
   */
  public ChapterBuilder properties() {
    panels.put(PanelNames.PANEL_MAIN, PanelContent.builder()
        .properties(Map.of("content_type", MainContentType.PROPERTIES.name().toLowerCase())));
    return this;
  }

  /**
   * Define an image to show in the chapter's "info" panel.
   */
  public ChapterBuilder image(final String url) {
    panels.put(PanelNames.PANEL_INFO, PanelContent.builder()
        .properties(Map.of("image_source", url)));
    return this;
  }

  /**
   * Define a reference text to show in the chapter's "info" panel.
   */
  public ChapterBuilder info(final BiFunction<String, String, String> infoUri) {
//    this.infoUri = infoUri;
    return this;
  }

  /**
   * Build a Chapter out of this chapter spec. Typically called by a generator and
   * not a spec developer.
   */
  public Chapter build(final String story) {
    return bldr
        .sortId(sortId)
        .selectables(buildSelectors())
        .panels(buildPanels(story, uid))
        .build();
  }

  private List<SelectorResource> buildSelectors() {
    return selectors.values().stream()
        .map(SelectorResource.Builder::build)
        .collect(Collectors.toList());
  }

  private Map<PanelNames, PanelContent> buildPanels(final String story, final String chapter) {
    final Map<PanelNames, PanelContent.Builder> newPanels = new HashMap<>(panels);

    buildInfoPanel(newPanels, story, chapter);
    buildMainPanel(newPanels);
    buildLegendPanel(newPanels);
    buildExportPanel(newPanels);

    // For simplicity, add main selectables to each panel by default.
    newPanels.forEach((k, v) -> {
      v.selectables(List.copyOf(mainSelectables));
    });

    buildLayerPanel(newPanels);

    panelSelectables.forEach((k, v) -> {
      newPanels.computeIfPresent(k, (panel, bldr) -> bldr.selectables(v));
    });

    return newPanels.entrySet().stream()
        .collect(Collectors.toMap(k -> k.getKey(), e -> e.getValue().build()));
  }

  private void buildInfoPanel(final Map<PanelNames, Builder> newPanels, final String story, final String chapter) {
    if (infoUri != null) {
      newPanels.put(PanelNames.PANEL_INFO, PanelContent.builder()
          .properties(Map.of(
              InfoProperties.CONTENT_TEXT, infoUri.apply(story, chapter))));
    }
  }

  private void buildExportPanel(final Map<PanelNames, Builder> newPanels) {
    if (exportText != null || !exportLinks.isEmpty()) {
      newPanels.put(PanelNames.PANEL_EXPORT, PanelContent.builder()
          .properties(Map.of(
              "export_text", Map.of("value", exportText),
              "links", accumulateExportLinks())));
    }
  }

  private List<DocumentResource> accumulateExportLinks() {
    final List<DocumentResource> lst = new ArrayList<>(exportLinks);
    if (noAutoAccumulate) {
      return lst;
    }

    if (mainComponent != null) {
      lst.addAll(mainComponent.getParameters()
          .values().stream()
          .filter(v -> v.startsWith("http"))
          // Split on ; because some components have multiple urls in a single component
          .flatMap(v -> Stream.of(v.split(";")))
          .map(v -> DocumentResource.builder()
              .name(v)
              .url(v)
              .build())
          .collect(Collectors.toList()));
    }

    return lst;
  }

  private void buildLayerPanel(final Map<PanelNames, Builder> newPanels) {
    // If there's a layer panel, do a special action (and override main selectables)
    if (!layers.isEmpty()) {
      final List<String> newLayers = new ArrayList<>(layers);
      Collections.reverse(newLayers);

      newPanels.put(PanelNames.PANEL_LAYER, PanelContent.builder()
          .selectables(List.copyOf(layerSelectables))
          .properties(Map.of(
              "layer_names", newLayers)));

      newPanels.computeIfPresent(PanelNames.PANEL_MAP, (panel, bldr) -> {
        bldr.selectables(List.copyOf(mapSelectables));
        return bldr;
      });
    }
  }

  private void buildLegendPanel(final Map<PanelNames, Builder> newPanels) {
    if (legendComponent != null) {
      if (newPanels.containsKey(PanelNames.PANEL_LEGEND)) {
        throw new RuntimeException("Legend panel is doubly defined. Incorrect config.");
      }

      newPanels.put(PanelNames.PANEL_LEGEND, PanelContent.builder()
          .selectables(List.copyOf(legendComponent.selectors()))
          .properties(Map.of(
              BasicParameterizedProperties.PARAMETERS, legendComponent.getParameters(),
              "content_type", LegendContentType.COMPONENT.name().toLowerCase(),
              "component_version", legendComponent.getVersion(),
              "component_name", legendComponent.getName(),
              "component_source", UrlHelper.getComponent(legendComponent.getSource()))));
    }

    if (!newPanels.containsKey(PanelNames.PANEL_MAIN)) {
      throw new RuntimeException("No main panel found in " + newPanels + ", which would produce an invalid chapter.");
    }
  }

  private void buildMainPanel(final Map<PanelNames, Builder> newPanels) {
    if (mainComponent != null) {
      if (newPanels.containsKey(PanelNames.PANEL_MAIN)) {
        throw new RuntimeException("Main panel is doubly defined. Incorrect config.");
      }

      newPanels.put(PanelNames.PANEL_MAIN, PanelContent.builder()
          .selectables(List.copyOf(mainComponent.selectors()))
          .properties(Map.of(
              BasicParameterizedProperties.PARAMETERS, mainComponent.getParameters(),
              "content_type", MainContentType.COMPONENT.name().toLowerCase(),
              "component_version", mainComponent.getVersion(),
              "component_name", mainComponent.getName(),
              "component_source", UrlHelper.getComponent(mainComponent.getSource()))));
    }

    if (!newPanels.containsKey(PanelNames.PANEL_MAIN)) {
      throw new RuntimeException("No main panel found in " + newPanels + ", which would produce an invalid chapter.");
    }
  }
}
