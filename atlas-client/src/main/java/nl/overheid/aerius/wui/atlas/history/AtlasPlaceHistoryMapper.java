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
package nl.overheid.aerius.wui.atlas.history;

import nl.overheid.aerius.wui.atlas.place.StoryPlace;
import nl.overheid.aerius.wui.history.PlaceHistoryMapper;
import nl.overheid.aerius.wui.place.TokenizedPlace;

public class AtlasPlaceHistoryMapper implements PlaceHistoryMapper {
  @Override
  public String getToken(final TokenizedPlace value) {
    String token = "";

    if (value instanceof StoryPlace) {
      token = new StoryPlace.Tokenizer().getToken((StoryPlace) value);
    }

    return token;
  }

  @Override
  public TokenizedPlace getPlace(final String token) {
    return new StoryPlace.Tokenizer().getPlace(token);
  }
}
