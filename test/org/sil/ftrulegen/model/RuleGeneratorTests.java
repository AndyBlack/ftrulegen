package org.sil.ftrulegen.model;

/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RuleGeneratorTests {
	private FLExTransRuleGenerator ruleGenerator;

	@Before
	public final void setup() {
		ruleGenerator = new FLExTransRuleGenerator();
		ruleGenerator.setLocale(new Locale("en"));
	}

	@Test
	public final void newOperationTest() {
		assertEquals(0, ruleGenerator.getFLExTransRules().size());

		FLExTransRule rule = new FLExTransRule();
		ruleGenerator.getFLExTransRules().add(rule);
		assertEquals(1, ruleGenerator.getFLExTransRules().size());
		PermutationsValue createPermutations = rule.getPermutations();
		assertEquals(PermutationsValue.no, createPermutations);
		Source source = rule.getSource();
		Phrase sourcePhrase = source.getPhrase();
		assertNotNull(source);
		Target target = rule.getTarget();
		Phrase targetPhrase = target.getPhrase();
		assertNotNull(target);
		assertEquals(0, sourcePhrase.getWords().size());
		assertEquals(PhraseType.source, sourcePhrase.getType());
		assert PhraseType.target == targetPhrase.getType();

		Word sourceWord = new Word();
		source.getPhrase().getWords().add(sourceWord);
		assert 1 == source.getPhrase().getWords().size();
		assert "" == sourceWord.getId();
		assert "" == sourceWord.getCategory();
		assert HeadValue.no == sourceWord.getHead();
		assert 0 == sourceWord.getAffixes().size();
		assert 0 == sourceWord.getFeatures().size();
		assert 0 == target.getPhrase().getWords().size();

		Word targetWord = new Word();
		target.getPhrase().getWords().add(targetWord);
		assert 1 == target.getPhrase().getWords().size();
		assert "" == targetWord.getId();
		assert "" == targetWord.getCategory();
		assert HeadValue.no == targetWord.getHead();
		assert 0 == targetWord.getAffixes().size();
		assert 0 == targetWord.getFeatures().size();

		Affix affix = new Affix();
		sourceWord.getAffixes().add(affix);
		assert 1 == sourceWord.getAffixes().size();
		assert AffixType.suffix == affix.getType();
		assert 0 == affix.getFeatures().size();
		Feature affixFeature = new Feature();
		affix.getFeatures().add(affixFeature);
		assert 1 == affix.getFeatures().size();
		assert "" == affixFeature.getMatch();
		assert "" == affixFeature.getValue();
		assert "" == affixFeature.getLabel();
	}
}