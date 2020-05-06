package nl.overheid.aerius.service.v2.serialization;

import java.util.LinkedHashMap;
import java.util.List;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;

public class AreaGroupConfiguration extends LinkedHashMap<NatureArea, List<AreaGroupType>> {
  private static final long serialVersionUID = 4975253827358299112L;
}
