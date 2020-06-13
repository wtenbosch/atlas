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
package nl.overheid.aerius.wui.domain.auth;

import java.util.Optional;

import com.google.gwt.user.client.Cookies;
import com.google.inject.Singleton;

@Singleton
public class AuthContextImpl implements AuthContext {

  private boolean authorized;

  private AuthorizationInfo authInfo;

  @Override
  public boolean isAuthenticated() {
    return authorized;
  }

  @Override
  public void setAuthorized(final boolean authorized) {
    this.authorized = authorized;
  }

  @Override
  public Optional<AuthorizationInfo> getAuthInfo() {
    return Optional.ofNullable(authInfo);
  }

  @Override
  public void setAuthInfo(final AuthorizationInfo authInfo) {
    this.authInfo = authInfo;
  }

  @Override
  public boolean loggedInRecently() {
    return "true".equals(Cookies.getCookie(AERIUS_LOGGED_IN_RECENTLY));
  }
}
