package nl.overheid.aerius.wui.atlas.service.parser.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONObjectHandle;

public final class FilterJsonParser {
  private FilterJsonParser() {}

  public static void parse(final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback, final JSONObjectHandle object) {
    // Parse as returned by service
    final Map<NatureArea, List<AreaGroupType>> map = new HashMap<>();
    object.getArray("areas").forEach(areaJson -> {
      final NatureArea area = new NatureArea();
      area.setId(areaJson.getString("id"));
      area.setName(areaJson.getString("name"));
      area.setCenterOfMass(areaJson.getString("centroid"));
      area.setAuthority(AreaGroupType.fromName(areaJson.getString("authority")));

      final List<AreaGroupType> authorities = new ArrayList<>();
      areaJson.getArray("provinces").forEachString(authority -> {
        authorities.add(AreaGroupType.fromName(authority));
      });

      map.put(area, authorities);
    });

    // Re-map to the desired form (grouped by province)
    final LinkedHashMap<AreaGroupType, List<NatureArea>> finalMap = new LinkedHashMap<>();
    map.forEach((k, v) -> {
      v.forEach(authority -> {
        finalMap.computeIfAbsent(authority, e -> new ArrayList<>());
        finalMap.computeIfPresent(authority, (v1, v2) -> {
          v2.add(k);
          return v2;
        });
      });
    });

    // Sort based on client-preference
    final Map<AreaGroupType, List<NatureArea>> sortedmap = sortAreaGroupTypes(finalMap);

    callback.onSuccess(sortedmap);
  }

  public static Map<AreaGroupType, List<NatureArea>> sortAreaGroupTypes(final LinkedHashMap<AreaGroupType, List<NatureArea>> map) {
//    final Map<AreaGroupType, List<NatureArea>> result = new TreeMap<>((a, b) -> a == null || b == null ? 0 : a.getName().compareTo(
    final Map<AreaGroupType, List<NatureArea>> result = new LinkedHashMap<>();

    result.put(AreaGroupType.RIJKSOVERHEID, new ArrayList<>());

    result.put(AreaGroupType.DRENTHE, new ArrayList<>());
    result.put(AreaGroupType.FLEVOLAND, new ArrayList<>());
    result.put(AreaGroupType.FRIESLAND, new ArrayList<>());
    result.put(AreaGroupType.GELDERLAND, new ArrayList<>());
    result.put(AreaGroupType.GRONINGEN, new ArrayList<>());
    result.put(AreaGroupType.LIMBURG, new ArrayList<>());
    result.put(AreaGroupType.NOORDBRABANT, new ArrayList<>());
    result.put(AreaGroupType.NOORDHOLLAND, new ArrayList<>());
    result.put(AreaGroupType.OVERIJSSEL, new ArrayList<>());
    result.put(AreaGroupType.UTRECHT, new ArrayList<>());
    result.put(AreaGroupType.ZEELAND, new ArrayList<>());
    result.put(AreaGroupType.ZUIDHOLLAND, new ArrayList<>());

//    result.putAll(map);

    map.forEach((k, v) -> result.computeIfPresent(k, (o, n) -> v));

    return result;
  }
}
