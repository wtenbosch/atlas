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
package nl.overheid.aerius.wui.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import nl.overheid.aerius.shared.domain.Selector;

public class SelectorUtilTest {
  @Test
  public void testIsMatch() {
    final ArrayList<Selector> lstFulla = new ArrayList<>();
    lstFulla.add(new Selector("year", "2015"));
    lstFulla.add(new Selector("habitat-type-name", "H1234"));

    final ArrayList<Selector> lstFullb = new ArrayList<>();
    lstFullb.add(new Selector("year", "2015"));
    lstFullb.add(new Selector("habitat-type-name", "H1234"));

    final ArrayList<Selector> lstFullAlta = new ArrayList<>();
    lstFullAlta.add(new Selector("year", "2016"));
    lstFullAlta.add(new Selector("habitat-type-name", "H1234"));

    final ArrayList<Selector> lstFullAltb = new ArrayList<>();
    lstFullAltb.add(new Selector("year", "2016"));
    lstFullAltb.add(new Selector("habitat-type-name", "H1234"));

    final ArrayList<Selector> lst2015a = new ArrayList<>();
    lst2015a.add(new Selector("year", "2015"));
    final ArrayList<Selector> lst2015b = new ArrayList<>();
    lst2015b.add(new Selector("year", "2015"));

    final ArrayList<Selector> lst2016a = new ArrayList<>();
    lst2016a.add(new Selector("year", "2016"));
    final ArrayList<Selector> lst2016b = new ArrayList<>();
    lst2016b.add(new Selector("year", "2016"));

    final ArrayList<Selector> lstEmpty = new ArrayList<>();

    assertTrue("An empty array should match with a universal default", SelectorUtil.isMatch(lstEmpty, lstEmpty));

    assertTrue("The universal default should match with any filled selector list", SelectorUtil.isMatch(lstEmpty, lstFulla));

    assertFalse("An empty array should not match with a specific default", SelectorUtil.isMatch(lst2015a, lstEmpty));

    assertTrue("A perfectly matching year selector and no habitat should match with year default", SelectorUtil.isMatch(lst2015a, lst2015b));

    assertTrue("A matching year selector and non-matching habitat selector should match with the year default",
        SelectorUtil.isMatch(lst2015a, lstFullb));

    assertFalse("A full spec should not match with not-full spec", SelectorUtil.isMatch(lstFulla, lst2015a));

    assertFalse("A year specific option list should not match with an empty selector list", SelectorUtil.isMatch(lst2015a, lstEmpty));

    assertTrue("A full match should match", SelectorUtil.isMatch(lstFulla, lstFullb));
    assertTrue("A full match should match", SelectorUtil.isMatch(lst2015a, lst2015b));
  }

  @Test
  public void testIsBetterMatch() {}

  @Test
  public void testIsCompleteMatch() {
    final ArrayList<Selector> lstFulla = new ArrayList<>();
    lstFulla.add(new Selector("year", "2015"));
    lstFulla.add(new Selector("habitat-type-name", "H1234"));

    final ArrayList<Selector> lstFullb = new ArrayList<>();
    lstFullb.add(new Selector("year", "2015"));
    lstFullb.add(new Selector("habitat-type-name", "H1234"));

    final ArrayList<Selector> lst2015a = new ArrayList<>();
    lst2015a.add(new Selector("year", "2015"));
    final ArrayList<Selector> lst2015b = new ArrayList<>();
    lst2015b.add(new Selector("year", "2015"));

    final ArrayList<Selector> lst2016 = new ArrayList<>();
    lst2016.add(new Selector("year", "2016"));

    final ArrayList<Selector> lstEmpty = new ArrayList<>();

    assertFalse("Full list and partial list are not a complete match.", SelectorUtil.isCompleteMatch(lstFulla, lst2015a));
    assertFalse("Full list and empty list are not a complete match.", SelectorUtil.isCompleteMatch(lstFulla, lstEmpty));

    assertFalse("2015 list and 2016 list are not a complete match.", SelectorUtil.isCompleteMatch(lst2015a, lst2016));

    assertTrue("2015 [a] list and 2015 [b] list are not a complete match.", SelectorUtil.isCompleteMatch(lst2015a, lst2015b));
    assertTrue("2015 [a] list and 2015 [b] list are not a complete match.", SelectorUtil.isCompleteMatch(lstFulla, lstFullb));
  }

  @Test
  public void testContainsSelector() {
    final Collection<Selector> selectors = new ArrayList<>();
    final Selector c1 = new Selector("foo", "whatever");
    final Selector c2 = new Selector("bar", "whatever");
    selectors.add(c1);
    selectors.add(c2);

    assertFalse("Selectors doesn't contain one typed 'test', so should be false.", SelectorUtil.containsSelector("test", selectors));
    assertTrue("The set of selectors should have a type named 'foo'", SelectorUtil.containsSelector("foo", selectors));

    assertFalse("The set of selectors should return false if the list of selectors is empty",
        SelectorUtil.containsSelector("foo", new ArrayList<>()));
  }
}
