/*
 * Copyright the State of the Netherlands
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
package nl.overheid.aerius.wui.config;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "AerConfig")
public class EnvironmentConfigurationWrapper {
  @JsProperty public String loginEndpoint;

  @JsProperty public String logoutEndpoint;

  @JsProperty public String healthchecksEndpoint;

  @JsProperty public String selectorEndpoint;

  @JsProperty public String layerEndpoint;

  @JsProperty public String cmsEndpoint;

  @JsProperty public String basicAuthEndpoint;

  @JsProperty public String authenticationEndpoint;

  @JsProperty public String authenticationClientId;

  @JsProperty public String authenticationClientSecret;

  @JsProperty public String searchEndpoint;

  @JsProperty public String basicAuthCredentials;

  @JsProperty public String applicationVersion;
}
