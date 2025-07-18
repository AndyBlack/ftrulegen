/**
 * Copyright (c) 2023-2025 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen;

public class Constants {
	public static final String VERSION_NUMBER = "1.4.2";

	public static final String RESOURCE_LOCATION = "org.sil.ftrulegen.resources.RuleGen";
	public static final String RESOURCE_SOURCE_LOCATION = "src/org/sil/ftrulegen/";
	public static final String APPLICATION_ICON_RESOURCE = "file:resources/images/FLExTransIcon.png";
	public static final String CSS_FILES_LOCATION = "ftrulegenCSSFiles";
	public static final String HTML_FILE_NAME = "FLExTransRule.html";
	public static final String[] GREEK_VARIABLES = { "α", "β", "γ", "δ", "ε", "ζ", "η", "θ", "ι", "κ", "μ", "ν" };

	// Unit Testing constants
	public static final String UNIT_TEST_DATA_FILE_NAME = "test/org/sil/ftrulegen/testdata/Ex1a_Def-Noun.";
	public static final String UNIT_TEST_DATA_FILE = "test/org/sil/ftrulegen/testdata/Ex1a_Def-Noun.xml";
	public static final String UNIT_TEST_DATA_DIRECTORY = "test/org/sil/ftrulegen/testdata";
	public static final String UNIT_TEST_DATA_FILE_EXPECTED = "test/org/sil/ftrulegen/testdata/RuleGenExpected.xml";
	public static final String UNIT_TEST_MISSING_DATA_FILE = "test/org/sil/ftrulegen/testdata/IDoNotExist.xml";

}
