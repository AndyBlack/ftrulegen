/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.model.*;

public class WebPageProducerTests extends ServiceTestBase
{
	private WebPageProducer producer;
	private String expectedResultFile = "";
	private String actualWebPageOutput = "";
	private FLExTransRuleGenerator ruleGenerator;
	private FLExTransRule rule;

	@Before
	@Override
	public void setup()
	{
		super.setup();
		producer = WebPageProducer.getInstance();
	}

	@Test
	public final void produceFromEx1aDefNounTest()
	{
		final String kFileName = "Ex1a_Def-Noun";
		setRuleGenExpected(Path.of(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
		expectedResultFile = Path.of(getTestDataDir(), kFileName + ".htm").toString();
		checkResult();
	}

	@Test
	public final void produceFromEx4bIndefAdjNounTest()
	{
		final String kFileName = "Ex4b_Indef-Adj-Noun";
		setRuleGenExpected(Path.of(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
		expectedResultFile = Path.of(getTestDataDir(), kFileName + ".htm").toString();
		checkResult();
	}

	private void checkResult()
	{
		ResourceBundle bundle = ResourceBundle.getBundle(org.sil.ftrulegen.Constants.RESOURCE_LOCATION, new Locale("en"));
		actualWebPageOutput = producer.produceWebPage(rule, bundle);
//		System.out.println("actual=\n" + actualWebPageOutput);
		try {
			String expectedWebPageOutput = new String(Files.readAllBytes(Paths.get(expectedResultFile)), StandardCharsets.UTF_8);
			expectedWebPageOutput = expectedWebPageOutput.replace("\r", "");
			assertEquals(expectedWebPageOutput, actualWebPageOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}