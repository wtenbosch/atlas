package nl.overheid.aerius.wui.resources;

import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.MimeType;

public interface EditorResources {
  @Source("images/editor/ed-open.svg")
  @MimeType("image/svg+xml")
  DataResource editorOpen();

  @Source("images/editor/ed-reload.svg")
  @MimeType("image/svg+xml")
  DataResource editorReload();

  @Source("images/editor/ed-attachments.svg")
  @MimeType("image/svg+xml")
  DataResource editorDocuments();

  @Source("images/editor/ed-log-out.svg")
  @MimeType("image/svg+xml")
  DataResource editorLogout();
}
