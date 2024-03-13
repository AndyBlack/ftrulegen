/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import java.io.File;
import java.util.Locale;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
		provider = new XmlBackEndProvider(ruleGenerator, new Locale("en"));
		setRuleGenExpected(Constants.UNIT_TEST_DATA_FILE_EXPECTED);
		File file = new File(Constants.UNIT_TEST_DATA_FILE);
	}
}