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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * Global resource class, access resources via R.css(). or R.images().
 */
public final class R {

  /**
   * Aerius CssResources.
   */
  public interface ApplicationCssResource extends CssResource {
    String rootLayoutPanel();

    String glassPanel();

    String glassPanelFull();

    String elementMargin();

    String flex();

    String columns();

    String justify();

    String distribute();

    String spaceAround();

    String alignCenter();

    String stretch();

    String grow();

    String noShrink();

    String rowReverse();

    String wrap();

    String unicode();

    String clickableBackground();

    String loader();

    String focus();

    String overflow();

    String elementPadding();

    String printWidth();

    String scaledPrintWidth();

    String fullPrintWidth();

    String printBreak();

    String printBreakBeforeAvoid();

    String search();

    String marginAround();

    String infoText();

    String printBreakAfter();

    String ieSvg();
  }

  public interface ApplicationResource extends ClientBundle, ImageResources {
    @Source("strict.gss")
    ApplicationCssResource css();

    @NotStrict
    @Source("notstrict.css")
    CssResource notstrictcss();
  }

  private static final ApplicationResource RESOURCES = GWT.create(ApplicationResource.class);

  // Don't instantiate directly, use the static fields.
  private R() {}

  /**
   * Ensures css is injected. Should be called as soon as possible on startup.
   */
  public static void init() {
    RESOURCES.css().ensureInjected();
    RESOURCES.notstrictcss().ensureInjected();
  }

  /**
   * Access to css resources.
   */
  public static ApplicationCssResource css() {
    return RESOURCES.css();
  }

  /**
   * Access to image resources.
   */
  public static ImageResources images() {
    return RESOURCES;
  }
}
