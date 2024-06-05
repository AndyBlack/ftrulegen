/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.model.*;

public class ConstituentFinderTests extends ServiceTestBase {
	private RuleIdentifierAndParentSetter idSetter;
	private FLExTransRuleGenerator ruleGenerator;
	private ConstituentFinder finder;
	private FLExTransRule rule;
	private Phrase phrase;
	private Word word;
	private Category category;
	private Feature feature;
	private Affix affix;
	private RuleConstituent constituent;

	@Before
	@Override
	public void setup() {
		super.setup();
		idSetter = RuleIdentifierAndParentSetter.getInstance();
		finder = ConstituentFinder.getInstance();
	}

	@Test
	public final void setForEx1aDefNounTest() {
		final String kFileName = "Ex1a_Def-Noun";
		setRuleGenExpected(Paths.get(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
		idSetter.setIdentifiersAndParents(rule);

		constituent = finder.findConstituent(rule, 0);
		assert constituent == null;
		constituent = finder.findConstituent(rule, 1);
		phrase = (Phrase) ((constituent instanceof Phrase) ? constituent : null);
		assert phrase != null;
		constituent = finder.findConstituent(rule, 2);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 3);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assertEquals("def", category.getName());

		constituent = finder.findConstituent(rule, 4);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 5);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assertEquals("n", category.getName());

		constituent = finder.findConstituent(rule, 6);
		phrase = (Phrase) ((constituent instanceof Phrase) ? constituent : null);
		assert phrase != null;
		constituent = finder.findConstituent(rule, 7);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 8);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assert "" == category.getName();
		constituent = finder.findConstituent(rule, 9);
		feature = (Feature) ((constituent instanceof Feature) ? constituent : null);
		assert feature != null;
		assertEquals("gender", feature.getLabel());
		assertEquals("α", feature.getMatch());
		assertEquals("", feature.getValue());
		assertEquals("α", feature.getMatchOrValue());

		constituent = finder.findConstituent(rule, 10);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 11);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assertEquals("", category.getName());
		constituent = finder.findConstituent(rule, 12);
		feature = (Feature) ((constituent instanceof Feature) ? constituent : null);
		assert feature != null;
		assertEquals("gender", feature.getLabel());
		assertEquals("α", feature.getMatch());
		assertEquals("", feature.getValue());
		assertEquals("α", feature.getMatchOrValue());

		constituent = finder.findConstituent(rule, 13);
		assert constituent == null;
	}

	@Test
	public final void setForEx4bIndefAdjNounTest() {
		final String kFileName = "Ex4b_Indef-Adj-Noun";
		setRuleGenExpected(Paths.get(getTestDataDir(), kFileName + ".xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		rule = ruleGenerator.getFLExTransRules().get(0);
		idSetter.setIdentifiersAndParents(rule);

		idSetter.setIdentifiersAndParents(rule);

		constituent = finder.findConstituent(rule, 0);
		assert constituent == null;
		constituent = finder.findConstituent(rule, 1);
		phrase = (Phrase) ((constituent instanceof Phrase) ? constituent : null);
		assert phrase != null;
		constituent = finder.findConstituent(rule, 2);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 3);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assertEquals("indef", category.getName());

		constituent = finder.findConstituent(rule, 4);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 5);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assertEquals("adj", category.getName());

		constituent = finder.findConstituent(rule, 6);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 7);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		// Assert.AreEqual("n", category.Name);

		constituent = finder.findConstituent(rule, 8);
		phrase = (Phrase) ((constituent instanceof Phrase) ? constituent : null);
		assert phrase != null;
		constituent = finder.findConstituent(rule, 9);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 10);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assertEquals("", category.getName());
		constituent = finder.findConstituent(rule, 11);
		affix = (Affix) ((constituent instanceof Affix) ? constituent : null);
		assert affix != null;
		constituent = finder.findConstituent(rule, 12);
		feature = (Feature) ((constituent instanceof Feature) ? constituent : null);
		assert feature != null;
		assertEquals("gender", feature.getLabel());
		assertEquals("α", feature.getMatch());
		constituent = finder.findConstituent(rule, 13);
		affix = (Affix) ((constituent instanceof Affix) ? constituent : null);
		assert affix != null;
		constituent = finder.findConstituent(rule, 14);
		feature = (Feature) ((constituent instanceof Feature) ? constituent : null);
		assert feature != null;
		assertEquals("number", feature.getLabel());
		assertEquals("β", feature.getMatch());

		constituent = finder.findConstituent(rule, 15);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 16);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assertEquals("", category.getName());
		constituent = finder.findConstituent(rule, 17);
		feature = (Feature) ((constituent instanceof Feature) ? constituent : null);
		assert feature != null;
		assertEquals("gender", feature.getLabel());
		assertEquals("α", feature.getMatch());
		constituent = finder.findConstituent(rule, 18);
		affix = (Affix) ((constituent instanceof Affix) ? constituent : null);
		assert affix != null;
		constituent = finder.findConstituent(rule, 19);
		feature = (Feature) ((constituent instanceof Feature) ? constituent : null);
		assert feature != null;
		assertEquals("number", feature.getLabel());
		assertEquals("β", feature.getMatch());

		constituent = finder.findConstituent(rule, 20);
		word = (Word) ((constituent instanceof Word) ? constituent : null);
		assert word != null;
		constituent = finder.findConstituent(rule, 21);
		category = (Category) ((constituent instanceof Category) ? constituent : null);
		assert category != null;
		assert "" == category.getName();
		constituent = finder.findConstituent(rule, 22);
		affix = (Affix) ((constituent instanceof Affix) ? constituent : null);
		assert affix != null;
		constituent = finder.findConstituent(rule, 23);
		feature = (Feature) ((constituent instanceof Feature) ? constituent : null);
		assert feature != null;
		assertEquals("gender", feature.getLabel());
		assertEquals("α", feature.getMatch());
		constituent = finder.findConstituent(rule, 24);
		affix = (Affix) ((constituent instanceof Affix) ? constituent : null);
		assert affix != null;
		constituent = finder.findConstituent(rule, 25);
		feature = (Feature) ((constituent instanceof Feature) ? constituent : null);
		assert feature != null;
		assertEquals("number", feature.getLabel());
		assertEquals("β", feature.getMatch());
	}
}