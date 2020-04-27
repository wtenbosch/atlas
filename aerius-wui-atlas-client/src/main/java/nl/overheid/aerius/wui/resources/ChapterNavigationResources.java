package nl.overheid.aerius.wui.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.MimeType;

public interface ChapterNavigationResources extends ClientBundle {
  @Source("images/chapter/ch-diagram.svg")
  @MimeType("image/svg+xml")
  DataResource chapterDiagram();

  @Source("images/chapter/ch-map.svg")
  @MimeType("image/svg+xml")
  DataResource chapterMap();

  @Source("images/chapter/ch-table.svg")
  @MimeType("image/svg+xml")
  DataResource chapterTable();

  @Source("images/chapter/ch-textpage.svg")
  @MimeType("image/svg+xml")
  DataResource chapterTextPage();
}
