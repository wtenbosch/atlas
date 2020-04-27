package nl.overheid.aerius.wui.atlas.service.parser.v1;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.shared.domain.StoryIcon;
import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.dev.GWTProd;
import nl.overheid.aerius.wui.util.GWTAtomicInteger;

public final class LibraryJsonParser extends CommonJson {
  private LibraryJsonParser() {}

  public static void parse(final AsyncCallback<NarrowLibrary> callback, final JSONObjectHandle result) {
    final JSONArrayHandle groups = result.getObject("data").getArray("stories");
    final NarrowLibrary library = new NarrowLibrary();

    final GWTAtomicInteger areaId = new GWTAtomicInteger();

    groups.forEach(storyJson -> {
      if (storyJson == null) {
        GWTProd.warn("Unexpected json output (story is null)");
        return;
      }

      final StoryInformation.Builder bldr = StoryInformation.builder()
          .uid(storyJson.getString("story_id"))
          .name(storyJson.getString("name"))
          .icon(StoryIcon.valueOf(storyJson.getString("icon")));

      storyJson.getObjectOptional("author").ifPresent(v -> bldr.authorName(v.getString("name")));

      final Map<String, String> props = new HashMap();

      bldr.creationDate(new Date(storyJson.getLong("created")))
          .changedDate(new Date(storyJson.getLong("last_changed")));
      // .datasets(storyJson.getArrayOptional("story_fragments").map(fragments -> {
      // final HashSet<String> datasets = new HashSet<>();
      // for (int j = 0; j < fragments.size(); j++) {
      // datasets.add(fragments.getObject(j).getObject("entity").getObject("dataset").getObject("entity").getString("dataset_code"));
      // }
      // return datasets;
      // }).orElse(new HashSet<>()));

      storyJson.getIntegerOptional("order_id").ifPresent(v -> bldr.orderId(v));

      // Fetch the first area id available
      if (areaId.get() == 0) {
        final Optional<JSONObjectHandle> propertiesOptional = storyJson.getObjectOptional("properties");
        if (propertiesOptional.isPresent()) {
          areaId.incrementAndGet(Integer.parseInt(propertiesOptional.get().getObject("entity").getString("natura2000AreaCode")));
        }
      }

      injectCustomProperties(storyJson.getArray("custom_properties"), props);

      bldr.properties(props);

      final StoryInformation story = bldr.build();
      library.put(story.uid().toLowerCase(), story);
    });

    UglyBoilerPlate.injectDocuments(areaId.get(), library);

    callback.onSuccess(library);
  }

  private static void injectCustomProperties(final JSONArrayHandle propsJson, final Map<String, String> properties) {
    for (int i = 0; i < propsJson.size(); i++) {
      final JSONObjectHandle prop = propsJson.getObject(i);

      properties.computeIfAbsent(prop.getString("key"), v -> prop.getString("value"));
    }
  }

}
