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
package nl.overheid.aerius.wui.atlas.future.library;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;

import nl.overheid.aerius.shared.domain.Criterium;
import nl.overheid.aerius.shared.domain.NarrowLibrary;
import nl.overheid.aerius.wui.future.Oracle;

@ImplementedBy(LibraryOracleImpl.class)
public interface LibraryOracle extends Oracle<NarrowLibrary> {
  boolean getStories(List<Criterium> filters, AsyncCallback<NarrowLibrary> callback);
  
  boolean getStories(Map<String, String> filters, AsyncCallback<NarrowLibrary> callback);
}
