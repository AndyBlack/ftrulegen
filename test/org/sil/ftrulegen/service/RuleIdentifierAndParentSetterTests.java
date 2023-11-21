/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import static org.junit.Assert.*;

import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.model.*;

public class RuleIdentifierAndParentSetterTests extends ServiceTestBase
{
	private RuleIdentifierAndParentSetter idSetter;
	private FLExTransRuleGenerator ruleGenerator;
	private FLExTransRule rule;
	private Phrase phrase;
	private Word word;
	private Category category;
	private Feature feature;
	private Affix affix;

	@Before
	@Override
	public void setup()
	{
		super.setup();
		idSetter = RuleIdentifierAndParentSetter.getInstance();
	}

	@Test
	public final void identifiersForEx1aDefNounTest()
	{
		final String kFileName = "Ex1a_Def-Noun";
		setRuleGenExpected(Path.of(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
		idSetter.setIdentifiersAndParents(rule);
		phrase = rule.getSource().getPhrase();
		assert 1 == phrase.getIdentifier();
		assert 2 == phrase.getWords().get(0).getIdentifier();
		assert 3 == phrase.getWords().get(0).getCategoryConstituent().getIdentifier();
		assert 4 == phrase.getWords().get(1).getIdentifier();
		assert 5 == phrase.getWords().get(1).getCategoryConstituent().getIdentifier();
		phrase = rule.getTarget().getPhrase();
		assert 6 == phrase.getIdentifier();
		assert 7 == phrase.getWords().get(0).getIdentifier();
		assert 8 == phrase.getWords().get(0).getCategoryConstituent().getIdentifier();
		assert 9 == phrase.getWords().get(0).getFeatures().get(0).getIdentifier();
		assert 10 == phrase.getWords().get(1).getIdentifier();
		assert 11 == phrase.getWords().get(1).getCategoryConstituent().getIdentifier();
		assert 12 == phrase.getWords().get(1).getFeatures().get(0).getIdentifier();
	}

	@Test
	public final void identifiersForEx4bIndefAdjNounTest()
	{
		final String kFileName = "Ex4b_Indef-Adj-Noun";
		setRuleGenExpected(Path.of(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
		idSetter.setIdentifiersAndParents(rule);
		phrase = rule.getSource().getPhrase();
		assert 1 == phrase.getIdentifier();
		assert 2 == phrase.getWords().get(0).getIdentifier();
		assert 3 == phrase.getWords().get(0).getCategoryConstituent().getIdentifier();
		assert 4 == phrase.getWords().get(1).getIdentifier();
		assert 5 == phrase.getWords().get(1).getCategoryConstituent().getIdentifier();
		assert 6 == phrase.getWords().get(2).getIdentifier();
		assert 7 == phrase.getWords().get(2).getCategoryConstituent().getIdentifier();
		phrase = rule.getTarget().getPhrase();
		assert 8 == phrase.getIdentifier();
		assert 9 == phrase.getWords().get(0).getIdentifier();
		assert 10 == phrase.getWords().get(0).getCategoryConstituent().getIdentifier();
		assert 11 == phrase.getWords().get(0).getAffixes().get(0).getIdentifier();
		assert 12 == phrase.getWords().get(0).getAffixes().get(0).getFeatures().get(0).getIdentifier();
		assert 13 == phrase.getWords().get(0).getAffixes().get(1).getIdentifier();
		assert 14 == phrase.getWords().get(0).getAffixes().get(1).getFeatures().get(0).getIdentifier();
		assert 15 == phrase.getWords().get(1).getIdentifier();
		assert 16 == phrase.getWords().get(1).getCategoryConstituent().getIdentifier();
		assert 17 == phrase.getWords().get(1).getFeatures().get(0).getIdentifier();
		assert 18 == phrase.getWords().get(1).getAffixes().get(0).getIdentifier();
		assert 19 == phrase.getWords().get(1).getAffixes().get(0).getFeatures().get(0).getIdentifier();
		assert 20 == phrase.getWords().get(2).getIdentifier();
		assert 21 == phrase.getWords().get(2).getCategoryConstituent().getIdentifier();
		assert 22 == phrase.getWords().get(2).getAffixes().get(0).getIdentifier();
		assert 23 == phrase.getWords().get(2).getAffixes().get(0).getFeatures().get(0).getIdentifier();
		assert 24 == phrase.getWords().get(2).getAffixes().get(1).getIdentifier();
		assert 25 == phrase.getWords().get(2).getAffixes().get(1).getFeatures().get(0).getIdentifier();
	}

	@Test
	public final void parentsForEx1aDefNounTest()
	{
		final String kFileName = "Ex1a_Def-Noun";
		setRuleGenExpected(Path.of(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
		idSetter.setIdentifiersAndParents(rule);
		phrase = rule.getSource().getPhrase();
		assert rule == phrase.getParent();
		word = phrase.getWords().get(0);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();
		word = phrase.getWords().get(1);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();

		phrase = rule.getTarget().getPhrase();
		word = phrase.getWords().get(0);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();
		feature = word.getFeatures().get(0);
		assert word == feature.getParent();
		word = phrase.getWords().get(1);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();
		feature = word.getFeatures().get(0);
		assert word == feature.getParent();
	}

	@Test
	public final void parentsForEx4bIndefAdjNounTest()
	{
		final String kFileName = "Ex4b_Indef-Adj-Noun";
		setRuleGenExpected(Path.of(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
		idSetter.setIdentifiersAndParents(rule);

		phrase = rule.getSource().getPhrase();
		assert rule == phrase.getParent();
		word = phrase.getWords().get(0);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();

		word = phrase.getWords().get(1);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();

		word = phrase.getWords().get(2);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();

		phrase = rule.getTarget().getPhrase();
		assert rule == phrase.getParent();
		word = phrase.getWords().get(0);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();
		affix = word.getAffixes().get(0);
		assert word == affix.getParent();
		feature = affix.getFeatures().get(0);
		assert affix == feature.getParent();
		affix = word.getAffixes().get(1);
		assert word == affix.getParent();
		feature = affix.getFeatures().get(0);
		assert affix == feature.getParent();

		word = phrase.getWords().get(1);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();
		feature = word.getFeatures().get(0);
		assert word == feature.getParent();
		affix = word.getAffixes().get(0);
		assert word == affix.getParent();
		feature = affix.getFeatures().get(0);
		assert affix == feature.getParent();

		word = phrase.getWords().get(2);
		assert phrase == word.getParent();
		category = word.getCategoryConstituent();
		assert word == category.getParent();
		affix = word.getAffixes().get(0);
		assert word == affix.getParent();
		feature = affix.getFeatures().get(0);
		assert affix == feature.getParent();
		affix = word.getAffixes().get(1);
		assert word == affix.getParent();
		feature = affix.getFeatures().get(0);
		assert affix == feature.getParent();
	}
}