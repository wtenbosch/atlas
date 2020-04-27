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
import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.overheid.aerius.shared.domain.SelectorConfiguration;
import nl.overheid.aerius.wui.atlas.service.parser.SelectorJsonParser;
import nl.overheid.aerius.wui.util.ReplacementAssistant;

@Singleton
public class SelectorServiceAsyncImpl implements SelectorServiceAsync {
  private final ReplacementAssistant replacer;

  @Inject
  public SelectorServiceAsyncImpl(final ReplacementAssistant replacer) {
    this.replacer = replacer;
  }

  @Override
  public void getSelectorConfiguration(final String url, final AsyncCallback<SelectorConfiguration> callback) {
    final String correctedUrl = url;

    RequestUtil.doGet(replacer.replace(correctedUrl), v -> SelectorJsonParser.wrap(url, v), callback);
  }
}