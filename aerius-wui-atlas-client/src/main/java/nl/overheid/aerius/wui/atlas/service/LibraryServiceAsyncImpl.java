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
package nl.overheid.aerius.wui.atlas.service;

import java.util.Optional;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.wui.atlas.service.parser.LibraryJsonParser;
import nl.overheid.aerius.wui.domain.auth.AuthContext;
import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;

@Singleton
public class LibraryServiceAsyncImpl implements LibraryServiceAsync {
  private static final String METHOD_NAME = "library";

  private final AuthContext authContext;

  @Inject
  public LibraryServiceAsyncImpl(final AuthContext authContext) {
    this.authContext = authContext;
  }

  @Override
  public void getStories(final AsyncCallback<NarrowLibrary> callback, final String[] fltr) {
    final Optional<AuthorizationInfo> authInfo = authContext.getAuthInfo();

    if (authInfo.isPresent()) {
      LegacyRequestUtil.doAuthenticatedMethodGet(authInfo.get(), METHOD_NAME, v -> LibraryJsonParser.wrap(v), callback, fltr);
      // RequestUtil.doMethodGet(METHOD_NAME, v -> LibraryJsonParser.wrap(v), callback, fltr);
    } else {
      LegacyRequestUtil.doMethodGet(METHOD_NAME, v -> LibraryJsonParser.wrap(v), callback, fltr);
    }
  }
}
