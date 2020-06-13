package nl.overheid.aerius.wui.util;

import java.util.function.Function;

import nl.overheid.aerius.shared.domain.Story;

public interface StoryTitleProvider extends Function<Story, String> {}
