package nl.overheid.aerius.shared.service;

import java.util.List;
import java.util.Map;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;

public interface FilterService {
  Map<AreaGroupType, List<NatureArea>> getNatura2000Areas();
}
