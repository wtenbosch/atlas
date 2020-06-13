package nl.overheid.aerius.shared.domain;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ChapterText {
  public static Builder builder() {
    return new AutoValue_ChapterText.Builder();
  }

  public abstract String text();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder text(String value);

    public abstract ChapterText build();
  }
}
