/*
 * Copyright Dutch Ministry of Agriculture, Nature and Food Quality
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package nl.overheid.aerius.wui.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.DataResource.MimeType;
import com.google.gwt.resources.client.ImageResource;

import nl.overheid.aerius.geo.resources.GeoResources;

/**
 * Image Resource interface to all images.
 */
public interface ImageResources extends ClientBundle, AdditionalNavigationResources, StoryNavigationResources, ChapterNavigationResources,
    GeoResources, AreaGroupResources, EditorResources, ProvinceOverlays {
  @Source("images/AERIUS-Monitor.svg")
  @MimeType("image/svg+xml")
  DataResource monitorLogo();

  @Source("images/circle-grey-arrow-right-white.svg")
  @MimeType("image/svg+xml")
  DataResource flipButton();

  @Source("images/waiting-animation.gif")
  ImageResource waitingAnimation();

  @Source("images/provinces.svg")
  @MimeType("image/svg+xml")
  DataResource areaWithoutLabels();

  @Source("images/provinces-label-overlay.svg")
  @MimeType("image/svg+xml")
  DataResource areaWithLabels();

  @Source("images/error.svg")
  @MimeType("image/svg+xml")
  DataResource error();

  @Source("images/icons/baseline-account_box-24px.svg")
  @MimeType("image/svg+xml")
  DataResource accountLoginIcon();

  @Source("images/icons/author.svg")
  @MimeType("image/svg+xml")
  DataResource authorIcon();

  @Source("images/icons/last-edited.svg")
  @MimeType("image/svg+xml")
  DataResource lastEditedIcon();

  @Source("images/icons/st-filter.svg")
  @MimeType("image/svg+xml")
  DataResource filterIcon();

  @Source("images/icons/an-pdf.svg")
  @MimeType("image/svg+xml")
  DataResource pdfIcon();

  @Source("images/ch-close-col.svg")
  @MimeType("image/svg+xml")
  DataResource contextPanelCollapseButton();

  @Source("images/icons/an-search.svg")
  @MimeType("image/svg+xml")
  DataResource buttonSearchNormal();
}
