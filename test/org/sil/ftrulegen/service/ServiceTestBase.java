/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.sil.ftrulegen.Constants;
import org.sil.ftrulegen.model.FLExTransRuleGenerator;

public abstract class ServiceTestBase {
	protected XmlBackEndProvider provider;
	protected FLExTransRuleGenerator ruleGenerator;
	private String TestDataDir;

	protected final String getTestDataDir() {
		return Constants.UNIT_TEST_DATA_DIRECTORY;
	}

	protected final void setTestDataDir(String value) {
		TestDataDir = value;
	}

	private String RuleGenFile;

	protected final String getRuleGenFile() {
		return RuleGenFile;
	}

	protected final void setRuleGenFile(String value) {
		RuleGenFile = value;
	}

	private String RuleGenExpected;

	protected final String getRuleGenExpected() {
		return RuleGenExpected;
	}

	protected final void setRuleGenExpected(String value) {
		RuleGenExpected = value;
	}
//	protected String expectedFileName = "RuleGenExpected.xml";
//	protected static final String kTestDir = "RuleGeneratorServiceTests";

	@Before
	public void setup() {
		ruleGenerator = new FLExTransRuleGenerator();
		ResourceBundle bundle = ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, new Locale("en"));
		provider = new XmlBackEndProvider(ruleGenerator, bundle);
		setRuleGenExpected(Constants.UNIT_TEST_DATA_FILE_EXPECTED);
		File file = new File(Constants.UNIT_TEST_DATA_FILE);
	}
}