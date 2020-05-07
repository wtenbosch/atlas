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

import nl.overheid.aerius.wui.atlas.service.parser.ChapterTextJsonParser;
import nl.overheid.aerius.wui.domain.auth.AuthContext;
import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;

@Singleton
public class ChapterTextServiceAsyncImpl implements ChapterTextServiceAsync {
  private final AuthContext authContext;

  @Inject
  public ChapterTextServiceAsyncImpl(final AuthContext authContext) {
    this.authContext = authContext;
  }

  @Override
  public void getChapterContent(final String url, final AsyncCallback<String> callback) {
    final Optional<AuthorizationInfo> authInfo = authContext.getAuthInfo();

    if (authInfo.isPresent()) {
      LegacyRequestUtil.doGet(url, v -> ChapterTextJsonParser.wrap(v), callback);
      throw new RuntimeException("Authenticated text gets are not yet implemented.");
    } else {
      LegacyRequestUtil.doGet(url, v -> ChapterTextJsonParser.wrap(v), callback);
    }
  }
}
