/*
 * Copyright Dutch Ministry of Economic Affairs
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

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.ImplementedBy;

import nl.overheid.aerius.geo.domain.layer.LayerConfig;
import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.wui.atlas.service.parser.BboxContainer;

@ImplementedBy(LayerServiceAsyncImpl.class)
public interface LayerServiceAsync {
  void getLayer(String layerUrl, AsyncCallback<List<LayerConfig>> callback);

  void getBbox(DatasetConfiguration dataset, String area, AsyncCallback<BboxContainer> callback);

  void getBbox(String area, AsyncCallback<BboxContainer> callback);
}
