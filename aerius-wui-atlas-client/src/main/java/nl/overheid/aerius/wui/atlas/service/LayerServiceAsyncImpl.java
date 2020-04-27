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
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.geo.domain.layer.LayerConfig;
import nl.overheid.aerius.shared.domain.DatasetConfiguration;
import nl.overheid.aerius.wui.atlas.service.parser.BboxContainer;
import nl.overheid.aerius.wui.atlas.service.parser.BboxJsonParser;
import nl.overheid.aerius.wui.atlas.service.parser.LayerJsonParser;
import nl.overheid.aerius.wui.config.EnvironmentConfiguration;

@Singleton
public class LayerServiceAsyncImpl extends StaggeredService implements LayerServiceAsync {

  private final EnvironmentConfiguration cfg;

  @Inject
  public LayerServiceAsyncImpl(final EnvironmentConfiguration cfg) {
    this.cfg = cfg;
  }

  @Override
  public void getLayer(final String layerUrl, final AsyncCallback<List<LayerConfig>> callback) {
    final String correctedUrl = layerUrl.startsWith("http") ? layerUrl : cfg.getLayerEndpoint() + "GetLayers?name=" + layerUrl;

    request(correctedUrl, v -> LayerJsonParser.wrap(v), callback);
  }

  @Override
  public void getBbox(final DatasetConfiguration dataset, final String area, final AsyncCallback<BboxContainer> callback) {
    RequestUtil.doGet(cfg.getLayerEndpoint() + "GetBbox?dataset=" + dataset.code() + "&areaId=" + area,
        v -> BboxJsonParser.wrap(v),
        callback);
  }

  @Override
  public void getBbox(final String area, final AsyncCallback<BboxContainer> callback) {
    RequestUtil.doGet(cfg.getLayerEndpoint() + "GetBbox?&areaId=" + area,
        v -> BboxJsonParser.wrap(v),
        callback);
  }
}
