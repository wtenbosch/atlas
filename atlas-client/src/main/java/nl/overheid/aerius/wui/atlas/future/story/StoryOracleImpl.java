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
package nl.overheid.aerius.wui.atlas.future.story;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.future.StaggeredOracle;
import nl.overheid.aerius.wui.atlas.service.StoryServiceAsync;
import nl.overheid.aerius.wui.domain.cache.CacheContext;

@Singleton
public class StoryOracleImpl extends StaggeredOracle<Story> implements StoryOracle {
  private final StoryServiceAsync storyService;

  @Inject
  public StoryOracleImpl(final StoryServiceAsync storyService, final StoryCache cache, final CacheContext cacheContext) {
    super(cacheContext, cache);

    this.storyService = storyService;
  }

  @Override
  public boolean getStory(final String uid, final AsyncCallback<Story> callback) {
    return ask(cache, uid, c -> storyService.getStory(uid, c), callback);
  }
}
