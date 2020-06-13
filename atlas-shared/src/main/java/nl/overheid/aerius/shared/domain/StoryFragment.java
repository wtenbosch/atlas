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

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class StoryFragment {
  public static final Builder builder() {
    return new AutoValue_StoryFragment.Builder();
  }

  public abstract DatasetConfiguration dataset();

  public abstract Map<String, Chapter> chapters();

  public Optional<Chapter> chapter(final String uid) {
    return Optional.ofNullable(chapters().get(uid));
  }

  public abstract String viewMode();

  @Nullable
  public abstract Map<PanelNames, PanelConfiguration> panels();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder dataset(DatasetConfiguration value);

    public abstract Builder chapters(Map<String, Chapter> value);

    public abstract Builder viewMode(String value);

    @Nullable
    public abstract Builder panels(Map<PanelNames, PanelConfiguration> value);

    public abstract StoryFragment build();
  }
}
