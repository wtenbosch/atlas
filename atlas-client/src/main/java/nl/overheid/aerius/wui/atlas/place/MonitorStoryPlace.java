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
package nl.overheid.aerius.wui.atlas.place;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;
import nl.overheid.aerius.wui.place.ApplicationPlace;
import nl.overheid.aerius.wui.place.PlaceTokenizer;
import nl.overheid.aerius.wui.place.TokenizerUtils;

public class MonitorStoryPlace extends ApplicationPlace {
  private static final String RECEPTOR = UglyBoilerPlate.RECEPTOR_ID;
  public static final String STORY_TOKEN = UglyBoilerPlate.ASSESSMENT_AREA_ID;

  private static final String CONTEXT = "deel";
  private static final String STORY = "onderwerp";
  private static final String CHAPTER = "hoofdstuk";
  private static final String DATASET = "dataset";

  private String receptorId;

  private PanelNames panel;
  private String story;
  private String chapter;
  private String dataset;

  private Map<String, String> filters = new HashMap<>();

  public MonitorStoryPlace() {
    super(new Tokenizer());
  }

  /**
   * Tokenizer for {@link MonitorStoryPlace}.
   */
  public static class Tokenizer implements PlaceTokenizer<MonitorStoryPlace> {

    @Override
    public MonitorStoryPlace getPlace(final String token) {
      final Map<String, String> parameters = TokenizerUtils.find(token);

      final MonitorStoryPlace place = new MonitorStoryPlace();
      place.setPanel(PanelNames.fromTitle(parameters.getOrDefault(CONTEXT, "")));
      place.setStory(parameters.getOrDefault(STORY, null));
      place.setChapter(parameters.getOrDefault(CHAPTER, null));
      place.setDataset(parameters.getOrDefault(DATASET, null));
      place.setReceptorId(parameters.getOrDefault(RECEPTOR, null));

      parameters.remove(CONTEXT);
      parameters.remove(STORY);
      parameters.remove(CHAPTER);
      parameters.remove(DATASET);
      parameters.remove(RECEPTOR);

      place.getFilters().putAll(parameters);

      return place;
    }

    @Override
    public String getToken(final MonitorStoryPlace place) {
      final Map<String, String> filters = new HashMap<>(place.getFilters());

      final Map<String, String> pairs = new LinkedHashMap<>();
      filters.computeIfPresent(UglyBoilerPlate.ASSESSMENT_AREA_ID, (k, v) -> {
        pairs.put(UglyBoilerPlate.ASSESSMENT_AREA_ID, v);
        return null;
      });
      pairs.put(STORY, Optional.ofNullable(place.getStory()).orElse(""));
      pairs.put(CHAPTER, place.getChapter());

      final Map<String, String> composites = new LinkedHashMap<>();

      composites.putAll(filters);

      composites.put(DATASET, place.getDataset());
      composites.put(CONTEXT, place.getPanel() == null ? null : place.getPanel().getTitle());

      composites.remove(UglyBoilerPlate.LEVEL);

      return TokenizerUtils.format(pairs, composites);
    }
  }

  public MonitorStoryPlace copy() {
    return copyTo(new MonitorStoryPlace());
  }

  public <E extends MonitorStoryPlace> E copyTo(final E copy) {
    copy.setPanel(panel);
    copy.setStory(story);
    copy.setChapter(chapter);
    copy.setDataset(dataset);
    copy.setReceptorId(receptorId);
    copy.setFilters(filters);
    return super.copyTo(copy);
  }

  public void putFilter(final String key, final String value) {
    filters.put(key, value);
  }

  public Map<String, String> getFilters() {
    return filters;
  }

  public void setFilters(final Map<String, String> filters) {
    this.filters = filters;
  }

  public PanelNames getPanel() {
    return panel;
  }

  public String getStory() {
    return story;
  }

  public String getChapter() {
    return chapter;
  }

  public String getDataset() {
    return dataset;
  }

  public void setPanel(final PanelNames panel) {
    this.panel = panel;
  }

  public void setStory(final String story) {
    this.story = story;
  }

  public void setChapter(final String chapter) {
    this.chapter = chapter;
  }

  public void setDataset(final String dataset) {
    this.dataset = dataset;
  }

  public String getReceptorId() {
    return receptorId;
  }

  public void setReceptorId(final String receptorId) {
    this.receptorId = receptorId;
  }

  @Override
  public String toString() {
    return "StoryPlace [receptorId=" + receptorId + ", panel=" + panel + ", story=" + story + ", chapter=" + chapter + ", dataset=" + dataset
        + ", filters=" + filters + "]";
  }
}
