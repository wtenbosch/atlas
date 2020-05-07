package nl.overheid.aerius.wui.atlas.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.shared.domain.DocumentResource;
import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.service.LegacyRequestUtil;
import nl.overheid.aerius.wui.atlas.service.RequestUtil;
import nl.overheid.aerius.wui.atlas.service.parser.JSONObjectHandle;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.util.NotificationUtil;
import nl.overheid.aerius.wui.util.TemplatedString;

/**
 * A repository for ugly code that shouldn't exist.
 *
 * Code contained herein ought to be supplemented with best-effort commentary
 * for its curious, rebellious and esoteric existence. This is not a rule
 * strictly enforced; it is boilerplate, after all.
 */
@Deprecated
public class UglyBoilerPlate {
  /**
   * Some simple default layers for background
   */
  public static final List<String> DEFAULT_LAYERS = new ArrayList<>();
  static {
    DEFAULT_LAYERS.add("location-natura2000-area-natura2000-directive-areas-all");
    DEFAULT_LAYERS.add("location-natura2000-area-included-receptors-all");
  }

  @Deprecated
  public static void selectorSanityWarnings(final EventBus eventBus, final Chapter chapter) {
    final List<String> chapterSelectableTypes = chapter.selectables().stream().map(v -> v.type())
        .collect(Collectors.toList());

    final Set<String> registeredSelectableTypes = chapter.panels().values().stream()
        .map(v -> v.selectables().stream()).flatMap(v -> v).collect(Collectors.toSet());

    registeredSelectableTypes.removeAll(chapterSelectableTypes);
    if (!registeredSelectableTypes.isEmpty()) {
      NotificationUtil.broadcastError(eventBus,
          "WARNING: selectables are registered but not provided as chapter_selectables: " + registeredSelectableTypes
              + " - as a result, these selectables won't have values.");
    }
  }

  public static void addSystemSelectors(final Set<String> activeSelectors) {
    activeSelectors.add("unit");
    activeSelectors.add("year");
  }

  @Deprecated
  public static void populateDefaults(final Map<String, String> rememberedDefaults, final Map<String, Selector> selectors) {
    if (rememberedDefaults.containsKey("unit")) {
      selectors.put("unit", new Selector("unit", rememberedDefaults.get("unit"), true));
    } else {
      selectors.put("unit", new Selector("unit", "kg", true));
    }
  }

  public static String convertUrlIfOld(final String url) {
    final TemplatedString replacer = new TemplatedString(url);

    replacer.replace("sector", "{sectorCode}");
    replacer.replace("sector-group", "{sectorgroupCode}");
    replacer.replace("habitat-type", "{habitatTypeCode}");
    replacer.replace("species", "{speciesCode}");
    replacer.replace("other-deposition-code", "{otherDepositionType}");
    replacer.replace("segment", "{segmentRegister}");
    replacer.replace("segment-integrated", "{segmentRegister}");
    replacer.replace("rehabilitation-strategy", "{rehabilitationStrategyCode}");
    replacer.replace("assessment-area", "{natura2000AreaCode}");
    replacer.replace("comparing-dataset", "{comparingDataset}");
    replacer.replace("sectorGroupCode", "{sectorgroupCode}");

    if (!replacer.toString().equals(url)) {
      GWTProd.warn("Incorrectly configured selector: [" + url + "] should be [" + replacer
          + "] -- recovered automatically, however, please adjust config ASAP.");
    }

    return replacer.toString();
  }

  public static String convertTypeIfOld(final String type) {
    String converted;

    switch (type) {
    case "sector-group":
      converted = "sectorgroupCode";
      break;
    case "sector":
      converted = "sectorCode";
      break;
    case "habitat-type":
      converted = "habitatTypeCode";
      break;
    case "species":
      converted = "speciesCode";
      break;
    case "segment":
      converted = "segmentRegister";
      break;
    case "segment-integrated":
      converted = "segmentRegister";
      break;
    case "other-deposition-code":
      converted = "otherDepositionType";
      break;
    case "rehabilitation-strategy":
      converted = "rehabilitationStrategyCode";
      break;
    case "comparing-dataset":
      converted = "comparingDataset";
      break;
    case "assessment-area":
      converted = "natura2000AreaCode";
      break;
    case "sectorGroupCode":
      converted = "sectorgroupCode";
      break;
    default:
      converted = type;
      break;
    }

    if (!converted.equals(type)) {
      GWTProd.warn("Incorrectly configured selector: [" + type + "] should be [" + converted
          + "] -- recovered automatically, however, please adjust config ASAP.");
    }

    return converted;
  }

  public static void injectPriorityProjectCode(final Story story, final JSONObjectHandle storyJson) {
    storyJson.getStringOptional("property_project_code").ifPresent(v -> story.info().properties().put("priorityProjectCode", v));
    storyJson.getStringOptional("priority_project_code").ifPresent(v -> story.info().properties().put("priorityProjectCode", v));
  }

  /**
   * Polymer caches the Polymer instance based on URL. If a URL exists duplicably
   * on a page from multiple sources (which can happen when a URL is malformed),
   * the entire instance will crash.
   */
  public static String sanitizeComponentSource(final String componentSource) {
    // Please look away
    final String correctedSource = componentSource
        .replaceFirst("//", "HTTP_SLASHES")
        .replace("//", "/")
        .replace("HTTP_SLASHES", "//");

    if (!correctedSource.equals(componentSource)) {
      GWTProd.warn("Dirty source URL discovered: " + componentSource);
    }

    return correctedSource;
  }

  public static String generateUniqueMapId(final Chapter chapter, final String panel) {
    return "openlayers-3-map-" + chapter.uid() + "-" + panel;
  }

  public static String generateUniqueMapId(final Story story, final Chapter chapter, final String panel) {
    return "openlayers-3-map-" + story.info().uid() + "-" + chapter.uid() + "-" + panel;
  }

  /**
   * The default dataset currently cannot be set through drupal response (needs
   * spec, but easy to implement)
   *
   * In the interim, we'll prefer setting its default to M18 in client code.
   */
  public static DatasetConfiguration findDefaultDataset(final Set<DatasetConfiguration> dataSets) {
    DatasetConfiguration datasetDefault;

    final DatasetConfiguration m18 = DatasetConfiguration.builder()
        .code("M18")
        .build()
        .label("M18");

    if (dataSets.contains(m18)) {
      datasetDefault = m18;
    } else {
      datasetDefault = dataSets.iterator().next();
    }

    return datasetDefault;
  }

  public static String fixAreaId(final String imageSource) {
    final String replaced = imageSource.replace("area.area_id", "natura2000AreaCode");
    if (!imageSource.equals(replaced)) {
      GWTProd.warn("Outdated replacement code discovered in image source: " + imageSource);
    }

    return replaced;
  }

  public static void injectDocuments(final int areaId, final NarrowLibrary library) {
    final List<DocumentResource> docs = new ArrayList<>();

    if (areaId == 0) {
      // Nothing
    } else if (areaId == 999) {
      // final DocumentResource res1 = new DocumentResource();
      // res1.setDate(new Date());
      // res1.setName("landelijke-monitoringsrapportage-stikstof_STUB.pdf");
      // res1.setUrl("https://aerius.nl");
      // docs.add(res1);
      // } else {
      // final DocumentResource res1 = new DocumentResource();
      // res1.setDate(new Date());
      // res1.setName("gebieds-analyse.pdf");
      // res1.setUrl("https://test-monitor.aerius.nl/downloads/gebiedsanalyses/" +
      // areaId + ".pdf");
      // final DocumentResource res2 = new DocumentResource();
      // res2.setDate(new Date());
      // res2.setName("gebieds-rapportage.pdf");
      // res2.setUrl("https://test-monitor.aerius.nl/downloads/gebiedsrapportages/" +
      // areaId + ".pdf");
      //
      // docs.add(res1);
      // docs.add(res2);
    }

    library.setDocuments(docs);
  }

  public static void guardSelectorDeprecation(final String origin, final JSONObjectHandle result) {
    result.getObjectOptional("meta").ifPresent(meta -> {
      if ("true".equals(meta.getStringOrDefault("deprecated", "false"))) {
        GWTProd.warn("Selector configuration was deprecated. Please update configuration: " + origin);
      }
    });
  }

  public static void addDevStrings(final Map<String, String> map) {
    if (GWT.getHostPageBaseURL().startsWith("http://localhost")) {
      map.put("layer_service_host", "http://localhost:8080/aerius-service-layers/");
      map.put("layer_service_group_url", "http://localhost:8080/aerius-service-layers/getLayerGroup=");
    }
  }

  public static DatasetConfiguration findDataset(final String dataset) {
    GWTProd.warn("Finding a probably incorrect dataset: " + dataset);
    if (dataset == null) {
      return null;
    }

    return DatasetConfiguration.builder()
        .code(dataset)
        .build();
  }

  public static void notifyReroute(final EventBus eventBus) {
    final String rerouter = LegacyRequestUtil.getRerouter();
    if (rerouter != null) {
      NotificationUtil.broadcastWarning(eventBus, "Note: Entered development mode. Rerouting backend requests to " + rerouter);
    }
  }

  public static void setDevelopmentRerouter() {
    final String rerouter = Location.getParameter("reroute");
    if (rerouter != null) {
      LegacyRequestUtil.rerouteCmsRequests(rerouter);
      RequestUtil.rerouteCmsRequests(rerouter);
    }
  }
  
  public static final String LEVEL = "level";
  public static final String ASSESSMENT_AREA_ID = "gebied";
  public static final String RECEPTOR_ID = "rec";

  @Deprecated
  public static boolean hasArea() {
    return Window.Location.getParameterMap().containsKey(ASSESSMENT_AREA_ID);
  }

  @Deprecated
  public static String getAreaId() {
    return Window.Location.getParameter(ASSESSMENT_AREA_ID);
  }
}
