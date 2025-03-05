/**
 * Copyright (c) 2023-2025 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen;

import java.util.prefs.Preferences;

import org.sil.utility.ApplicationPreferencesUtilities;
import org.sil.utility.StringUtilities;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 
 */
public class ApplicationPreferences extends ApplicationPreferencesUtilities {

	static final String LAST_LOCALE_LANGUAGE = "lastLocaleLanguage";
	// Not trying to be anglo-centric, but we have to start with something...
	static final String DEFAULT_LOCALE_LANGUAGE = "en";

	// Window parameters to remember
	static final String POSITION_X = "PositionX";
	static final String POSITION_Y = "PositionY";
	static final String WIDTH = "Width";
	static final String HEIGHT = "Height";
	static final String MAXIMIZED = "Maximized";
	// Window parameters for main window and various dialogs
	public static final String LAST_WINDOW = "lastWindow";
	public static final String LAST_SPLIT_PANE_POSITION = "lastSplitPanePosition";

	public static final String LAST_CATEGORY_CHOOSER = "lastCategoryChooser";
	public static final String LAST_FEATURE_CHOOSER = "lastFeatureChooser";
	public static final String LAST_SELECTED_RULE = "lastSelectedRule";
	public static final String LAST_SELECTED_DISJOINT_FEATURE_SET = "lastSelectedDisjointFeatureSet";
	public static final String DISJOINT_FEATURE_EDITOR = "DISJOINT_FEATURE_EDITOR_";

	private Preferences prefs;
	
	private static final ApplicationPreferences instance = new ApplicationPreferences();

	public static ApplicationPreferences getInstance() {
		return instance;
	}

	public ApplicationPreferences() {
	}

	public void setObject(Object app) {
		prefs = Preferences.userNodeForPackage(app.getClass());
	}

	public String getLastLocaleLanguage() {
		return prefs.get(LAST_LOCALE_LANGUAGE, DEFAULT_LOCALE_LANGUAGE);
	}

	public void setLastLocaleLanguage(String lastLocaleLanguage) {
		setPreferencesKey(LAST_LOCALE_LANGUAGE, lastLocaleLanguage);
	}

	public int getLastSelectedRule() {
		return prefs.getInt(LAST_SELECTED_RULE, 0);
	}

	public void setLastSelectedRule(int lastSelectedRule) {
		prefs.putInt(LAST_SELECTED_RULE, lastSelectedRule);
	}

	public int getLastDisjointFeatureSetItemUsed() {
		return prefs.getInt(LAST_SELECTED_DISJOINT_FEATURE_SET, 0);
	}

	public void setLastDisjointFeatureSetItemUsed(int lastSelectedRule) {
		prefs.putInt(LAST_SELECTED_DISJOINT_FEATURE_SET, lastSelectedRule);
	}

	public Stage getLastWindowParameters(String sWindow, Stage stage, Double defaultHeight,
			Double defaultWidth) {
		double dHeight = prefs.getDouble(sWindow + HEIGHT, defaultHeight);
		double dWidth = prefs.getDouble(sWindow + WIDTH, defaultWidth);
		double dX = prefs.getDouble(sWindow + POSITION_X, 10);
		double dY = prefs.getDouble(sWindow + POSITION_Y, 10);
		if (!isXOnAScreen(dX) || !isYOnAScreen(dY)) {
			dX = 10;
			dY = 10;
			dHeight = defaultHeight;
			dWidth = defaultWidth;
		}
		stage.setHeight(dHeight);
		stage.setWidth(dWidth);
		stage.setX(dX);
		stage.setY(dY);
		boolean fValue = prefs.getBoolean(sWindow + MAXIMIZED, false);
		stage.setMaximized(fValue);
		return stage;
	}

	public void setLastWindowParameters(String sWindow, Stage stage) {
		boolean isMaximized = stage.isMaximized();
		if (!isMaximized) {
			setPreferencesKey(sWindow + HEIGHT, stage.getHeight());
			setPreferencesKey(sWindow + WIDTH, stage.getWidth());
			setPreferencesKey(sWindow + POSITION_X, stage.getX());
			setPreferencesKey(sWindow + POSITION_Y, stage.getY());
		}
		setPreferencesKey(sWindow + MAXIMIZED, stage.isMaximized());
	}

	boolean isXOnAScreen(double dX) {
		for (Screen screen : Screen.getScreens()) {
			Rectangle2D bounds = screen.getVisualBounds();
			if (dX >= bounds.getMinX() && dX <= bounds.getMaxX()) {
				return true;
			}
		}
		return false;
	}

	boolean isYOnAScreen(double dY) {
		for (Screen screen : Screen.getScreens()) {
			Rectangle2D bounds = screen.getVisualBounds();
			if (dY >= bounds.getMinY() && dY <= bounds.getMaxY()) {
				return true;
			}
		}
		return false;
	}

	public double getLastSplitPaneDividerPosition() {
		return prefs.getDouble(LAST_SPLIT_PANE_POSITION, 0.5);
	}

	public void setLastSplitPaneDividerPosition(double position) {
		setPreferencesKey(LAST_SPLIT_PANE_POSITION, position);
	}

	public void setPreferencesKey(String key, boolean value) {
		if (!StringUtilities.isNullOrEmpty(key)) {
			if (key != null) {
				prefs.putBoolean(key, value);

			} else {
				prefs.remove(key);
			}
		}
	}

	public void setPreferencesKey(String key, Double value) {
		if (!StringUtilities.isNullOrEmpty(key)) {
			if (key != null && value != null) {
				prefs.putDouble(key, value);

			} else {
				prefs.remove(key);
			}
		}
	}

	public void setPreferencesKey(String key, String value) {
		if (!StringUtilities.isNullOrEmpty(key) && !StringUtilities.isNullOrEmpty(value)) {
			prefs.put(key, value);

		} else {
			prefs.remove(key);
		}
	}

	@Override
	public String getLastOpenedDirectoryPath() {
		// Not needed since the file is given
		return null;
	}

	@Override
	public void setLastOpenedDirectoryPath(String arg0) {
		// Not needed since the file is given
	}

	public boolean getBooleanValue(String sKey, boolean defaultValue) {
		return prefs.getBoolean(sKey, defaultValue);
	}

	public Double getDoubleValue(String sKey, Double defaultValue) {
		return prefs.getDouble(sKey, defaultValue);
	}

	public int getIntegerValue(String sKey, int defaultValue) {
		return prefs.getInt(sKey, defaultValue);
	}

	public String getStringValue(String sKey, String defaultValue) {
		return prefs.get(sKey, defaultValue);
	}

}
