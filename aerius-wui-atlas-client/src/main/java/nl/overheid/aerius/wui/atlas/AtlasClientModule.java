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
package nl.overheid.aerius.wui.atlas;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.PlaceHistoryHandler.Historian;
import com.google.inject.Singleton;

import nl.overheid.aerius.wui.atlas.factories.ContextWidgetFactory;
import nl.overheid.aerius.wui.atlas.factories.LayerLegendWidgetFactory;
import nl.overheid.aerius.wui.atlas.factories.LevelGuideFactory;
import nl.overheid.aerius.wui.atlas.factories.MainLegendWidgetFactory;
import nl.overheid.aerius.wui.atlas.factories.SelectableTextWidgetFactory;
import nl.overheid.aerius.wui.domain.auth.AuthContext;
import nl.overheid.aerius.wui.domain.auth.AuthContextImpl;
import nl.overheid.aerius.wui.domain.story.StoryContext;
import nl.overheid.aerius.wui.domain.story.StoryContextObservableImpl;
import nl.overheid.aerius.wui.history.HTML5Historian;

/**
 * Gin bindings for the Monitor Up application.
 */
public class AtlasClientModule extends AbstractGinModule {
  @Override
  protected void configure() {
    bind(Historian.class).to(HTML5Historian.class);

    // Bind contexts
    bind(StoryContext.class).to(StoryContextObservableImpl.class);
    bind(AuthContext.class).to(AuthContextImpl.class).in(Singleton.class);

    // Bind factories
    install(new GinFactoryModuleBuilder().build(ContextWidgetFactory.class));
    install(new GinFactoryModuleBuilder().build(LayerLegendWidgetFactory.class));
    install(new GinFactoryModuleBuilder().build(MainLegendWidgetFactory.class));
    install(new GinFactoryModuleBuilder().build(LevelGuideFactory.class));
    install(new GinFactoryModuleBuilder().build(SelectableTextWidgetFactory.class));
  }
}
