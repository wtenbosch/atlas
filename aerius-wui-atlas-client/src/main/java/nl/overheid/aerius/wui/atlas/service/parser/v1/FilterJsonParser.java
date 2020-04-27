package nl.overheid.aerius.wui.atlas.service.parser.v1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONArrayHandle;
import nl.overheid.aerius.wui.atlas.service.parser.CommonJson.JSONObjectHandle;
import nl.overheid.aerius.wui.atlas.util.UglyBoilerPlate;

public final class FilterJsonParser {
  private FilterJsonParser() {}

  public static void parse(final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback, final JSONObjectHandle object) {
    final LinkedHashMap<AreaGroupType, List<NatureArea>> map = new LinkedHashMap<>();
    final JSONArrayHandle array = object.getObject("areas").getArray("entities");

    for (int i = 0; i < array.size(); i++) {
      final JSONObjectHandle areaJson = array.getObject(i);

      final NatureArea area = new NatureArea();
      area.setName(areaJson.getString("name"));
      area.setId(areaJson.getString("area_id"));

      areaJson.getArrayOptional("authorities").ifPresent(prov -> {
        for (int j = 0; j < prov.size(); j++) {
          final JSONObjectHandle authority = prov.getObject(j).getObject("authority");

          final AreaGroupType type = AreaGroupType.valueOf(authority.getString("province"));
          if (type == null) {
            throw new NullPointerException("Unknown province type: " + authority.getString("province"));
          }

          if (!map.containsKey(type)) {
            map.put(type, new ArrayList<>());
          }
          final List<NatureArea> areas = map.get(type);
          areas.add(area);
          
          if (j == 0) {
            area.setAuthority(type);
          }
        }
      });
    }

    final Map<AreaGroupType, List<NatureArea>> sortedmap = UglyBoilerPlate.sortAreaGroupTypes(map);

    callback.onSuccess(sortedmap);
  }
}
