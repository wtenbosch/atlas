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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.Story;
import nl.overheid.aerius.wui.atlas.service.parser.StoryJsonParser;
import nl.overheid.aerius.wui.util.RequestUtil;

@Singleton
public class StoryServiceAsyncImpl implements StoryServiceAsync {
  private static final String METHOD_NAME_NEW = "story/{uid}";

  @Override
  public void getStory(final String story, final AsyncCallback<Story> callback) {
    final String uri = RequestUtil.prepareUri(METHOD_NAME_NEW, "uid", story);
    
    RequestUtil.doGet(uri, v -> StoryJsonParser.convert(v), callback);
  }
}
