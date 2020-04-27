/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package nl.overheid.aerius.wui.place;

/**
 * Implemented by objects responsible for text serialization and deserialization of Place objects.
 *
 * @param <P> a subtype of {@link ApplicationPlace}
 */
public interface PlaceTokenizer<P extends ApplicationPlace> {

  /**
   * Returns the {@link ApplicationPlace} associated with the given token.
   *
   * @param token a String token
   * @return a {@link ApplicationPlace} of type P
   */
  P getPlace(String token);

  /**
   * Returns the token associated with the given {@link ApplicationPlace}.
   *
   * @param place a {@link ApplicationPlace} of type P
   * @return a String token
   */
  String getToken(P place);
}
