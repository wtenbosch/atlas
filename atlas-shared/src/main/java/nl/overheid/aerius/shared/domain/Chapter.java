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
package nl.overheid.aerius.shared.domain;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

import nl.overheid.aerius.shared.util.HasUid;

@AutoValue
public abstract class Chapter implements HasUid, Comparable<Chapter> {
  public static Builder builder() {
    return new AutoValue_Chapter.Builder();
  }

  @Nullable
  public abstract String identifier();

  public abstract String title();

  public abstract ChapterIcon icon();

  public abstract Map<PanelNames, PanelContent> panels();

  public abstract List<SelectorResource> selectables();

  public abstract int sortId();

  public PanelContent getMainPanel() {
    return panels().get(PanelNames.PANEL_MAIN);
  }

  @Deprecated
  public void setMainPanel(final PanelContent panels) {
    setPanel(PanelNames.PANEL_MAIN, panels);
  }

  @Deprecated
  public void setPanel(final PanelNames panel, final PanelContent conf) {
    panels().put(panel, conf);
  }

  @Override
  public int compareTo(final Chapter o) {
    return Integer.compare(sortId(), o.sortId());
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder uid(String value);

    public abstract Builder identifier(String value);

    public abstract Builder title(String value);

    public abstract Builder icon(ChapterIcon value);

    public abstract Builder panels(Map<PanelNames, PanelContent> value);

    public abstract Builder selectables(List<SelectorResource> value);

    public abstract Builder sortId(int sortId);

    public abstract Chapter build();
  }
}
