package nl.overheid.aerius.wui.component;

import com.google.gwt.resources.client.DataResource;
import com.google.web.bindery.event.shared.EventBus;

import nl.overheid.aerius.shared.domain.PanelConfiguration;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.command.PanelSelectionChangeCommand;
import nl.overheid.aerius.wui.i18n.M;
import nl.overheid.aerius.wui.util.ContextNavigationImageUtil;
import nl.overheid.aerius.wui.widget.SimpleMaskedButton;

public class ContextNavigationControl extends SimpleMaskedButton<PanelConfiguration> {
  private final EventBus eventBus;

  public ContextNavigationControl(final PanelConfiguration option, final EventBus eventBus) {
    super(option);
    this.eventBus = eventBus;
  }

  @Override
  protected void onEnsureDebugId(final String baseID) {
    super.onEnsureDebugId(baseID);

    button.ensureDebugId(baseID + "-" + getLabel(option));
  }

  @Override
  protected DataResource getImage(final PanelConfiguration conf) {
    return ContextNavigationImageUtil.getImageResource(conf.asConfigurationProperties().getPanelType());
  }

  @Override
  protected void onSelect(final PanelConfiguration conf, final boolean userInitiated) {
    super.onSelect(conf, userInitiated);

    if (userInitiated) {
      eventBus.fireEvent(new PanelSelectionChangeCommand(PanelNames.fromName(conf.getName())));
    }
  }

  @Override
  protected void onDeselect(final PanelConfiguration conf, final boolean spontaneous) {
    if (spontaneous) {
      eventBus.fireEvent(new PanelSelectionChangeCommand(null));
    }
  }

  @Override
  protected String getLabel(final PanelConfiguration conf) {
    return M.messages().contextNavigationOption(conf.asConfigurationProperties().getPanelType());
  }
}
