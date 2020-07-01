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

import com.google.inject.Singleton;

@Singleton
public class EnvironmentConfigurationImpl implements EnvironmentConfiguration {
  private final EnvironmentConfigurationWrapper wrapper = new EnvironmentConfigurationWrapper();

  @Override
  public String getLoginEndpoint() {
    return wrapper.loginEndpoint;
  }

  @Override
  public String getLogoutEndpoint() {
    return wrapper.logoutEndpoint;
  }

  @Override
  public String getHealthchecksEndpoint() {
    return wrapper.healthchecksEndpoint;
  }

  @Override
  public String getSelectorEndpoint() {
    return wrapper.selectorEndpoint;
  }

  @Override
  public String getLayerEndpoint() {
    return wrapper.layerEndpoint;
  }

  @Override
  public String getCmsEndpoint() {
    return wrapper.cmsEndpoint;
  }

  @Override
  public String getBasicAuthEndpoint() {
    return wrapper.basicAuthEndpoint;
  }

  @Override
  public String getAuthenticationEndpoint() {
    return wrapper.authenticationEndpoint;
  }

  @Override
  public String getAuthenticationClientId() {
    return wrapper.authenticationClientId;
  }

  @Override
  public String getAuthenticationClientSecret() {
    return wrapper.authenticationClientSecret;
  }

  @Override
  public String getSearchEndpoint() {
    return wrapper.searchEndpoint;
  }

  @Override
  public String getBasicAuthCredentials() {
    return wrapper.basicAuthCredentials;
  }

  @Override
  public String getApplicationVersion() {
    return wrapper.applicationVersion;
  }

  @Override
  public String getComponentsEndpoint() {
    return wrapper.componentsEndpoint;
  }
}
