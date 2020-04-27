package nl.overheid.aerius.shared.domain;

import org.checkerframework.checker.nullness.qual.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SearchSuggestion {
  public static Builder builder() {
    return new AutoValue_SearchSuggestion.Builder();
  }

  public abstract SearchSuggestionType type();

  @Nullable
  public abstract Object payload();

  public abstract String title();

  @Nullable
  public abstract String extent();

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder type(SearchSuggestionType value);

    @Nullable
    public abstract Builder payload(Object value);

    public abstract Builder title(String value);

    @Nullable
    public abstract Builder extent(String value);

    public abstract SearchSuggestion build();
  }
}
