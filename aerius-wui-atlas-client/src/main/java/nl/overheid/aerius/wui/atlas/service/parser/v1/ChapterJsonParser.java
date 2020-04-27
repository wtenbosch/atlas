package nl.overheid.aerius.wui.atlas.service.parser.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.overheid.aerius.shared.domain.Chapter;
import nl.overheid.aerius.shared.domain.ChapterIcon;
import nl.overheid.aerius.shared.domain.EditableContentConfig;
import nl.overheid.aerius.shared.domain.EditableContentConfigCollection;
import nl.overheid.aerius.shared.domain.PanelContent;
import nl.overheid.aerius.shared.domain.PanelContent.Builder;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.Selector;
import nl.overheid.aerius.shared.domain.SelectorResource;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.dev.GWTProd;

public final class ChapterJsonParser extends CommonJson {
  public static Map<String, Chapter> parse(final JSONArrayHandle chapters) {
    final Map<String, Chapter> map = new LinkedHashMap<>();

    for (int i = 0; i < chapters.size(); i++) {
      final Optional<JSONObjectHandle> chapterOptional = chapters.getObjectOptional(i);
      if (!chapterOptional.isPresent()) {
        GWTProd.warn("Unpublished/'null' chapter at index " + i);
      }

      chapterOptional.ifPresent(chapterJson -> {
        final Chapter.Builder bldr = Chapter.builder()
            .uid(String.valueOf(chapterJson.getNumber("chapter_id").intValue()))
            .sortId(chapterJson.getNumberOptional("sort_id").orElse(0D).intValue())
            .title(chapterJson.getString("name"))
            .icon(ChapterIcon.safeValueOf(chapterJson.getString("icon")));

        final Map<PanelNames, PanelContent> panels = new HashMap<>();
        final JSONArrayHandle panelsJson = chapterJson.getArray("panels");
        for (int j = 0; j < panelsJson.size(); j++) {
          final JSONObjectHandle panelJson = panelsJson.getObject(j).getObject("panel");

          final String panelName = panelJson.getObject("panel_properties").getObject("entity").getString("panel_type");
          final Map<String, Object> panelProperties = new HashMap<>();
          final JSONObjectHandle panelConfigurationJson = panelJson.getObject("panel_properties").getObject("entity");
          for (final String key : panelConfigurationJson.keySet()) {
            parseProperty(panelProperties, key, panelConfigurationJson.get(key));
          }
          final Builder panelContentBldr = PanelContent.builder()
              .properties(panelProperties);


          final List<String> configurationSelectorTypes = new ArrayList<>();
          panelJson.getArrayOptional("panel_selectables").ifPresent(arr -> {
            for (int k = 0; k < arr.size(); k++) {
              configurationSelectorTypes
                  .add(UglyBoilerPlate.convertTypeIfOld(arr.getObject(k).getObject("entity").getString("selector_type")));
            }
          });
          panelContentBldr.selectables(configurationSelectorTypes);

          final double panelId = panelJson.getNumber("panel_id");
          panelJson.getBooleanOrDefault("has_editable_text", false).ifPresent(v -> {
            if (v) {
              parseEditableContent(panelId, panelContentBldr, panelJson);
            }
          });

          final PanelContent panelContent = panelContentBldr.build();
          panels.put(PanelNames.fromName(panelName), panelContent);
        }

        bldr.panels(panels);

        final List<SelectorResource> selectors = new ArrayList<>();
        if (!chapterJson.get("chapter_selectables").isNull()) {
          final JSONArrayHandle selectorsJson = chapterJson.getArray("chapter_selectables");
          for (int j = 0; j < selectorsJson.size(); j++) {
            final JSONObjectHandle selectorResourceJson = selectorsJson.getObject(j).getObject("entity");
            final SelectorResource resource = SelectorResource.builder()
                .type(UglyBoilerPlate.convertTypeIfOld(selectorResourceJson.getObject("selector_type")
                    .getObject("entity")
                    .getString("selector_type")))
                .url(UglyBoilerPlate.convertUrlIfOld(selectorResourceJson.getString("selector_url")))
                .build();

            selectors.add(resource);
          }

          bldr.selectables(selectors);
        }

        final Chapter chapter = bldr.build();
        map.put(chapter.uid(), chapter);
      });
    }

    return map.entrySet()
        .stream()
        .sorted((a, b) -> Integer.compare(a.getValue().sortId(), b.getValue().sortId()))
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
  }

  private static void parseEditableContent(final double panelId, final PanelContent.Builder panelConfiguration,
      final JSONObjectHandle panelJson) {
    final Map<String, EditableContentConfigCollection> editableConfig = new HashMap<>();
    final String referenceName = panelJson.getObject("panel_properties").getObject("entity")
        .getString("reference_name");
    final EditableContentConfigCollection configCollection;
    if (!editableConfig.containsKey(referenceName)) {
      configCollection = new EditableContentConfigCollection();
      configCollection.setReferenceName(referenceName);
      configCollection.setCollection(new ArrayList<>());
      configCollection.setAddNode(panelId);
      editableConfig.put(referenceName, configCollection);
    } else {
      configCollection = editableConfig.get(referenceName);
    }
    panelConfiguration.editableContentConfig(configCollection);

    final JSONArrayHandle array = panelJson.getArray("editable_text_entities");
    for (int i = 0; i < array.size(); i++) {
      final JSONObjectHandle item = array.getObject(i);
      final Optional<JSONObjectHandle> editableTextEntity = item.getObjectOptional("entity");
      if (!editableTextEntity.isPresent()) {
        GWTProd.warn("Editable Entity is null which is unexpected.");
      }

      editableTextEntity.ifPresent(textEntity -> {

        final EditableContentConfig conf = new EditableContentConfig();
        final JSONObjectHandle object = textEntity.getObject("is_editable");
        conf.setEditNode(object.getString("path"));

        final Optional<JSONArrayHandle> selectorsJson = textEntity.getArrayOptional("field_selectors");
        final List<Selector> selectors = new ArrayList<>();
        selectorsJson.ifPresent(arr -> {
          for (int j = 0; j < arr.size(); j++) {
            final Selector selector = new Selector();
            final JSONObjectHandle selectorEntity = arr.getObject(j);

            selector.setType(selectorEntity.getString("key"));
            selector.setValue(selectorEntity.getString("value"));

            selectors.add(selector);
          }
        });

        conf.setSelectorCombination(selectors);
        configCollection.add(conf);
      });
    }
  }

  private static void parseProperty(final Map<String, Object> panelProperties, final String key,
      final JSONValueHandle valueHandle) {
    if (key.startsWith("param_")) {
      final Object object = panelProperties.get("params");
      if (object == null) {
        panelProperties.put("params", new HashMap<String, String>());
      }
      @SuppressWarnings("unchecked")
      final Map<String, String> map = (Map<String, String>) panelProperties.get("params");

      // Parse away the 'param_' bits and take the remainder for a property key
      if (!valueHandle.isNull()) {
        map.put(key.substring(6), valueHandle.asString());
      }
    } else {
      panelProperties.put(key, parseProperty(valueHandle));
    }
  }

  private static Object parseProperty(final JSONValueHandle property) {
    if (property.isObject()) {
      final Map<String, Object> innerProperties = new HashMap<>();
      final JSONObjectHandle objectHandle = property.asObjectHandle();
      for (final String key : objectHandle.keySet()) {
        innerProperties.put(key, parseProperty(objectHandle.get(key)));
      }
      return innerProperties;
    }

    if (property.isArray()) {
      final JSONArrayHandle arrayValue = property.asArray();
      final List<Object> innerProperties = new ArrayList<>();
      for (int i = 0; i < arrayValue.size(); i++) {
        innerProperties.add(parseProperty(arrayValue.get(i)));
      }
      return innerProperties;
    }

    if (property.isString()) {
      return property.asString();
    }

    return null;
  }
}
