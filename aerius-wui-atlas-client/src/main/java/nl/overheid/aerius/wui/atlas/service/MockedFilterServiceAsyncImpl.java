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
package nl.overheid.aerius.wui.atlas.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.NatureArea;

/**
 * TODO De-mock
 */
public class MockedFilterServiceAsyncImpl implements FilterServiceAsync {
  @Override
  public void getNatura2000Areas(final AsyncCallback<Map<AreaGroupType, List<NatureArea>>> callback) {
    Scheduler.get().scheduleDeferred(() -> callback.onSuccess(getNatura2000Areas()));
  }

  public Map<AreaGroupType, List<NatureArea>> getNatura2000Areas() {
    final NatureArea a1 = new NatureArea();
    a1.setName("Gebied 1");
    a1.setId("1");
    final NatureArea a2 = new NatureArea();
    a2.setName("Gebied 2");
    a2.setId("2");
    final NatureArea a3 = new NatureArea();
    a3.setName("Gebied 3");
    a3.setId("3");
    final NatureArea a4 = new NatureArea();
    a4.setName("Gebied 4");
    a4.setId("4");
    final NatureArea a5 = new NatureArea();
    a5.setName("Gebied 5");
    a5.setId("5");
    final NatureArea a6 = new NatureArea();
    a6.setName("Gebied 6");
    a6.setId("6");

    final ArrayList<NatureArea> ass1 = new ArrayList<>();
    ass1.add(a1);
    ass1.add(a2);
    ass1.add(a3);

    final ArrayList<NatureArea> ass2 = new ArrayList<>();
    ass2.add(a4);
    ass2.add(a5);
    ass2.add(a6);

    final Map<AreaGroupType, List<NatureArea>> lst = new HashMap<>();
    for (final AreaGroupType type : AreaGroupType.values()) {
      lst.put(type, new ArrayList<>());
    }
    lst.put(AreaGroupType.DRENTHE, ass1);
    lst.put(AreaGroupType.FLEVOLAND, ass2);

    return lst;
  }
}
