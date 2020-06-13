package nl.overheid.aerius.wui.atlas.ui.story;

import com.google.web.bindery.event.shared.EventBus;

public class GeoStoryCommandRouterNoOp implements GeoStoryCommandRouter {
  @Override
  public void setEventBus(final EventBus eventBus) {}
}
