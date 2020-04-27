package nl.overheid.aerius.wui.atlas.future.search;

import java.util.function.Consumer;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import nl.overheid.aerius.geo.domain.Point;
import nl.overheid.aerius.geo.domain.ReceptorPoint;
import nl.overheid.aerius.geo.util.ReceptorUtil;
import nl.overheid.aerius.shared.domain.SearchSuggestion;
import nl.overheid.aerius.shared.domain.SearchSuggestionType;

public final class ReceptorCoordinateSearchUtil {
  // Regex used to identify whether a Receptor ID was entered as search term.
  private static final RegExp SEARCH_TERM_RECEPTOR_ID_REGEX = RegExp.compile("^[1-9][0-9]{0,7}$");

  // Regex used to identify whether an RDNew XY coordinate was entered as search
  // term.
  private static final RegExp SEARCH_TERM_COORDINATE_REGEX = RegExp
      .compile("^\\s*(x\\:)?\\s*([1-2]?\\d{5})\\s*[,;\\s]\\s*(y\\:)?\\s*([3-6]\\d{5})\\s*$", "i");

  // The relevant groups in the above regular expression that identify the X and Y
  // coordinates respectively.
  private static final int SEARCH_TERM_COORDINATE_REGEX_GROUP_X = 2;
  private static final int SEARCH_TERM_COORDINATE_REGEX_GROUP_Y = 4;

  private ReceptorCoordinateSearchUtil() {}

  public static void search(final ReceptorUtil receptorUtil, final String query, final Consumer<SearchSuggestion> callback) {
    handleCoordinate(receptorUtil, query, callback);
    handleReceptorId(receptorUtil, query, callback);
  }

  private static void handleCoordinate(final ReceptorUtil receptorUtil, final String query, final Consumer<SearchSuggestion> callback) {
    final MatchResult coordinateMatch = SEARCH_TERM_COORDINATE_REGEX.exec(query);
    if (coordinateMatch == null) {
      return;
    }

    final int x = Integer.parseInt(coordinateMatch.getGroup(SEARCH_TERM_COORDINATE_REGEX_GROUP_X));
    final int y = Integer.parseInt(coordinateMatch.getGroup(SEARCH_TERM_COORDINATE_REGEX_GROUP_Y));

    callback.accept(createCoordinateSuggestion(x, y).build());

    final ReceptorPoint receptor = receptorUtil.createReceptorIdFromPoint(new Point(x, y));
    if (!receptorUtil.isPointWithinBoundingBox(receptor)) {
      return;
    }

    final int receptorX = Math.round((float) receptor.getX());
    final int receptorY = Math.round((float) receptor.getY());

    callback.accept(createReceptorSuggestion(receptor.getId(), receptorX, receptorY)
        .payload(receptor)
        .build());
  }

  private static void handleReceptorId(final ReceptorUtil receptorUtil, final String searchText, final Consumer<SearchSuggestion> callback) {
    final MatchResult receptorIdMatch = SEARCH_TERM_RECEPTOR_ID_REGEX.exec(searchText);
    if (receptorIdMatch == null) {
      return;
    }

    final int id = Integer.parseInt(receptorIdMatch.getGroup(0));
    final ReceptorPoint receptor = receptorUtil.createReceptorPointFromId(id);
    if (!receptorUtil.isPointWithinBoundingBox(receptor)) {
      return;
    }

    final int x = Math.round((float) receptor.getX());
    final int y = Math.round((float) receptor.getY());

    callback.accept(createReceptorSuggestion(receptor.getId(), x, y)
        .payload(receptor)
        .build());
  }

  private static SearchSuggestion.Builder createReceptorSuggestion(final int receptorId, final int x, final int y) {
    return SearchSuggestion.builder()
        .type(SearchSuggestionType.RECEPTOR)
        .title(receptorId + " (x:" + x + " y:" + y + ")")
        .extent("POINT(" + x + " " + y + ")");
  }

  private static SearchSuggestion.Builder createCoordinateSuggestion(final int x, final int y) {
    return SearchSuggestion.builder()
        .type(SearchSuggestionType.COORDINATE)
        .title("x:" + x + " y:" + y)
        .extent("POINT(" + x + " " + y + ")");
  }
}
