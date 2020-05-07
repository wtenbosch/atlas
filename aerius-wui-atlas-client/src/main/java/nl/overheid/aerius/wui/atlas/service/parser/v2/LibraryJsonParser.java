package nl.overheid.aerius.wui.atlas.service.parser.v2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.DocumentResource;
import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.shared.domain.StoryInformation;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson;
import nl.overheid.aerius.wui.atlas.service.parser.JSONObjectHandle;

public final class LibraryJsonParser extends CommonJson {
  private LibraryJsonParser() {}

  public static void parse(final AsyncCallback<NarrowLibrary> callback, final JSONObjectHandle result) {
    final NarrowLibrary library = new NarrowLibrary();

    result.getArray("library").forEach(storyJson -> {
      final StoryInformation story = StoryInformationJsonParser.parse(storyJson);

      library.put(story.uid().toLowerCase(), story);
    });

    final List<DocumentResource> documents = new ArrayList<>();
    result.getArrayOptional("documents").ifPresent(docs -> docs.forEach(docJson -> {
      final DocumentResource doc = DocumentResource.builder()
          .name(String.valueOf(docJson.getString("name")))
          .url(String.valueOf(docJson.getString("url")))
          .date(new Date(docJson.getLong("date")))
          .build();

      documents.add(doc);
    }));
    library.setDocuments(documents);

    callback.onSuccess(library);
  }
}
