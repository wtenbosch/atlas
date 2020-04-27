package nl.overheid.aerius.shared.domain;

import java.util.Date;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class DocumentResource {
  public static Builder builder() {
    return new AutoValue_DocumentResource.Builder();
  }

  public abstract String url();

  public abstract String name();

  @Nullable
  public abstract Date date();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder url(String value);

    public abstract Builder name(String value);

    @Nullable
    public abstract Builder date(Date value);

    public abstract DocumentResource build();
  }
}
