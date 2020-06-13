package nl.overheid.aerius.wui.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.MimeType;

public interface StoryNavigationResources extends ClientBundle {
  @Source("images/story/st-room-and-utilization.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationRoomAndUtilization();

  @Source("images/story/st-priority-projects.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationPriorityProjects();

  @Source("images/story/st-permits.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationPermits();

  @Source("images/story/st-n-emission.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationEmission();

  @Source("images/story/st-n-deposition.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationDeposition();

  @Source("images/story/st-n-deposition-and-nature.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationDepositionAndNature();

  @Source("images/story/st-measurements.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationMeasurements();

  @Source("images/story/st-local-measures.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationLocalMeasures();

  @Source("images/story/st-basic-information.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationBasicInformation();

  @Source("images/story/st-analysis.svg")
  @MimeType("image/svg+xml")
  DataResource storyNavigationAnalysis();

  @Source("images/story/st-nature.svg")
  @MimeType("image/svg+xml")
  DataResource storyNatureInformation();
}
