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

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

import nl.overheid.aerius.shared.util.HasUid;

@AutoValue
public abstract class StoryInformation implements HasUid, Comparable<StoryInformation> {
  public static Builder builder() {
    return new AutoValue_StoryInformation.Builder()
        .orderId(0);
  }

  @Override
  public abstract String uid();

  public abstract Map<String, String> properties();

  public abstract int orderId();

  public abstract String name();

  public abstract StoryIcon icon();

  @Nullable
  public abstract String authorName();

  public abstract Date creationDate();

  public abstract Date changedDate();

  public Optional<String> property(final String key) {
    return Optional.ofNullable(properties().get(key));
  }

  @Override
  public int compareTo(final StoryInformation o) {
    return Integer.compare(orderId(), o.orderId());
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder uid(String value);

    public abstract Builder properties(Map<String, String> value);

    public abstract Builder orderId(int value);

    public abstract Builder name(String value);

    public abstract Builder icon(StoryIcon value);

    public abstract Builder authorName(String name);

    public abstract Builder creationDate(Date value);

    public abstract Builder changedDate(Date value);

    public abstract StoryInformation build();
  }
}
