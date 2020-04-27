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
package nl.overheid.aerius.wui.atlas.command;

import java.util.List;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.event.AreaGroupChangeEvent;
import nl.overheid.aerius.wui.command.SimpleGenericCommand;

public class AreaGroupChangeCommand extends SimpleGenericCommand<AreaGroupType, AreaGroupChangeEvent> {
  private List<NatureArea> assessmentAreas;

  public AreaGroupChangeCommand() {
    super(null);
  }

  public AreaGroupChangeCommand(final AreaGroupType value, final List<NatureArea> assessmentAreas) {
    super(value);

    this.assessmentAreas = assessmentAreas;
  }

  public List<NatureArea> getAssessmentAreas() {
    return assessmentAreas;
  }

  public void setAssessmentAreas(final List<NatureArea> assessmentAreas) {
    this.assessmentAreas = assessmentAreas;
  }

  @Override
  protected AreaGroupChangeEvent createEvent(final AreaGroupType value) {
    return new AreaGroupChangeEvent(value, assessmentAreas);
  }
}
