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
package nl.overheid.aerius.wui.i18n;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

import nl.overheid.aerius.shared.domain.AreaGroupType;
import nl.overheid.aerius.shared.domain.LevelOption;
import nl.overheid.aerius.shared.domain.PanelNames;
import nl.overheid.aerius.shared.domain.PanelType;
import nl.overheid.aerius.shared.domain.SearchSuggestionType;
import nl.overheid.aerius.shared.domain.StoryFilterOption;
import nl.overheid.aerius.shared.domain.StoryIcon;
import nl.overheid.aerius.wui.util.ApplicationConstants;

/**
 * GWT interface to language specific static text.
 */
@DefaultLocale(ApplicationConstants.DEFAULT_LOCALE)
public interface ApplicationMessages extends Messages, ConfigurationMessages {
  String dateFormat();

  String dateFormatNullDefault();

  String nameNullDefault();

  @Description("A fatal error occurred, most likely a bug.")
  String errorInternalFatal();

  @Description("User has an older version of application and should refresh")
  String errorInternalApplicationOutdated();

  @Description("Problem with internet connection.")
  String errorConnection();

  @Description("Service is not available. Probably in maintenance.")
  String serviceUnavailable();

  String applicationReloadPageOnLogout();

  String contextNavigationOption(@Select PanelType option);

  String storyNavigationOption(@Select StoryIcon option);

  String filterNaturaDescription();

  String filterNationalDescription();

  String contextDataSetTitle();

  String contextDataSetDescription();

  String contextYearTitle();

  String contextYearDescription();

  String contextLibraryTitle();

  String contextLibraryDescription();

  String contextLayerPanelTitle();

  String contextPreferencesPanelTitle();

  String preferencesLanguageLabel();

  String preferencesDeveloperMode();

  String preferencesLoginMode();

  String contextInfoPanelTitle();

  String contextMetaPanelTitle();

  String levelOptionName(@Select LevelOption option);

  String searchPlaceHolder();
  
  String searchNature2000PlaceHolder();

  String searchResultText();

  String areaListNoResultText();

  String loginRequiredField();

  String loginWrongAuth();

  String loginClientError();

  String legendTitle();

  String documentsTitle();

  String areaGroupName(@Select AreaGroupType value);

  String noSelectorTextDefault();

  String selectorLoadFailure(@Select String type);

  String contextPanelCollapse(@Select PanelNames panel);

  String contextPanelOpen(@Select PanelNames panel);

  String logoutConfirmMessage();

  String storyFilterOption(@Select StoryFilterOption option);

  String searchSuggestionType(@Select SearchSuggestionType type);

  String exitStory();

  String exitStoryConfirm();

  String home();

  String applicationVersion(String applicationVersion);

}
