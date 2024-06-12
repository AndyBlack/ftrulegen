/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.model.Affix;
import org.sil.ftrulegen.model.FLExTransRule;
import org.sil.ftrulegen.model.FLExTransRuleGenerator;
import org.sil.ftrulegen.model.Feature;
import org.sil.ftrulegen.model.HeadValue;

/**
 * @author Andy Black
 *
 */
public class ValidityCheckerTest extends ServiceTestBase {

	ValidityChecker checker;
	FLExTransRule rule;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	@Override
	public void setup() {
		super.setup();
		checker = ValidityChecker.getInstance();
		ResourceBundle bundle = ResourceBundle.getBundle(org.sil.ftrulegen.Constants.RESOURCE_LOCATION,
				new Locale("en"));
		checker.setBundle(bundle);
		final String kFileName = "Ex1a_Def-Noun";
		setRuleGenExpected(Paths.get(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sourceWordsHaveCategoriesTest() {
		checker.setRule(rule);
		assertEquals(true, checker.checkSourceWordsHaveCategories());
		rule.getSource().getPhrase().getWords().get(0).deleteCategory();
		checker.setRule(rule);
		assertEquals(false, checker.checkSourceWordsHaveCategories());
		rule.getSource().getPhrase().getWords().get(1).deleteCategory();
		checker.setRule(rule);
		assertEquals(false, checker.checkSourceWordsHaveCategories());
	}

	@Test
	public void targetHasFeatureTest() {
		checker.setRule(rule);
		assertEquals(true, checker.checkTargetHasFeature());
		rule.getTarget().getPhrase().getWords().get(0).setFeatures(null);
		checker.setRule(rule);
		assertEquals(true, checker.checkTargetHasFeature());
		rule.getTarget().getPhrase().getWords().get(1).setFeatures(null);
		checker.setRule(rule);
		assertEquals(false, checker.checkTargetHasFeature());
		Affix affix = new Affix();
		rule.getTarget().getPhrase().getWords().get(0).getAffixes().add(affix);
		checker.setRule(rule);
		assertEquals(false, checker.checkTargetHasFeature());
		Feature feature = new Feature();
		feature.setLabel("gender");
		feature.setMatch("m");
		affix.getFeatures().add(feature);
		rule.getTarget().getPhrase().getWords().get(0).getAffixes().add(affix);
		checker.setRule(rule);
		assertEquals(true, checker.checkTargetHasFeature());
	}

	@Test
	public void targetWordMarkedAsHeadTest() {
		checker.setRule(rule);
		assertEquals(true, checker.checkTargetWordMarkedAsHead());
		rule.getTarget().getPhrase().getWords().get(1).setHead(HeadValue.no);
		checker.setRule(rule);
		assertEquals(false, checker.checkTargetWordMarkedAsHead());
	}

}
