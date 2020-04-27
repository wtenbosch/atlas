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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.service.parser.StoryJsonParser;
import nl.overheid.aerius.wui.domain.auth.AuthContext;
import nl.overheid.aerius.wui.domain.auth.AuthorizationInfo;
import nl.overheid.aerius.wui.util.FilterUtil;

@Singleton
public class StoryServiceAsyncImpl implements StoryServiceAsync {
  private static final String METHOD_NAME = "GetStoryFragments";
  private static final String NAME_PARAM_NAME = "id";

  private final AuthContext authContext;

  @Inject
  public StoryServiceAsyncImpl(final AuthContext authContext) {
    this.authContext = authContext;
  }

  @Override
  public void getStory(final String story, final AsyncCallback<Story> callback) {
    final Optional<AuthorizationInfo> authInfo = authContext.getAuthInfo();

    final List<Criterium> filters = new ArrayList<>();
    filters.add(Criterium.builder()
        .name(NAME_PARAM_NAME)
        .value(story)
        .build());
    final String[] formatFilters = FilterUtil.I.formatFilters(filters);

    if (authInfo.isPresent()) {
      RequestUtil.doAuthenticatedMethodGet(authInfo.get(), METHOD_NAME, v -> StoryJsonParser.wrap(v), callback, formatFilters);
      // RequestUtil.doMethodGet(METHOD_NAME, v -> StoryJsonParser.wrap(v), callback, formatFilters);
    } else {
      RequestUtil.doMethodGet(METHOD_NAME, v -> StoryJsonParser.wrap(v), callback, formatFilters);
    }
  }
}
