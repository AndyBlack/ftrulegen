/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.Constants;
import org.sil.ftrulegen.service.RuleIdentifierAndParentSetter;

public class DuplicateTests {
	private FLExTransRuleGenerator ruleGenerator;
	private FLExTransRule rule;
	private Word sourceWord;
	private Word sourceWord2;
	ResourceBundle bundle;

	@Before
	public final void setup() {
		ruleGenerator = new FLExTransRuleGenerator();
		rule = new FLExTransRule();
		rule.setName("Rule 1");
		rule.setDescription("test 1");
		ruleGenerator.getFLExTransRules().add(rule);
		Source source = rule.getSource();
		Phrase sourcePhrase = source.getPhrase();
		sourceWord = new Word();
		sourceWord.setId("1");
		sourceWord.setCategory("Noun");
		sourceWord.setCategoryConstituent(new Category(sourceWord.getCategory()));
		sourceWord.setHead(HeadValue.yes);
		sourcePhrase.getWords().add(sourceWord);
		sourceWord2 = new Word();
		sourceWord2.setId("2");
		sourceWord2.setCategory("Det");
		sourceWord2.setCategoryConstituent(new Category(sourceWord2.getCategory()));
		sourceWord2.setHead(HeadValue.no);
		sourcePhrase.getWords().add(sourceWord2);
		Target target = rule.getTarget();
		Phrase targetPhrase = target.getPhrase();
		Word targetWord = new Word();
		targetWord.setId("Target 1");
		targetWord.setCategory("Det");
		targetWord.setHead(HeadValue.no);
		targetPhrase.getWords().add(targetWord);
		Word targetWord2 = new Word();
		targetWord2.setId("Target 2");
		targetWord2.setCategory("Noun");
		targetWord2.setHead(HeadValue.yes);
		targetPhrase.getWords().add(targetWord2);
		bundle = ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, new Locale("en"));
	}

	@Test
	public final void ruleDuplicateTest() {
		assert 1 == ruleGenerator.getFLExTransRules().size();
		FLExTransRule rule = ruleGenerator.getFLExTransRules().get(0);
		rule.setPermutations(PermutationsValue.with_head);
		FLExTransRule rule2 = rule.duplicate();
		assertEquals(rule.getName() + bundle.getString("model.ruleduplicated"), rule2.getName());
		assertEquals(rule.getDescription(), rule2.getDescription());
		assert rule.getPermutations() == rule2.getPermutations();
		assertEquals(PhraseType.target, rule2.getTarget().getPhrase().getType());
		assertEquals(rule.getSource().getPhrase().getWords().size(),
				rule2.getSource().getPhrase().getWords().size());
		assertEquals(rule.getTarget().getPhrase().getWords().size(),
				rule2.getTarget().getPhrase().getWords().size());
		Word wordT1 = rule.getTarget().getPhrase().getWords().get(0);
		Word wordT2 = rule.getTarget().getPhrase().getWords().get(1);
		Word word2T1 = rule2.getTarget().getPhrase().getWords().get(0);
		Word word2T2 = rule2.getTarget().getPhrase().getWords().get(1);
		assertEquals(wordT1, word2T1);
		assertEquals(wordT2, word2T2);
		assertEquals("Det", wordT1.getCategory());
		assertEquals("Noun", wordT2.getCategory());
		assertEquals("Det", word2T1.getCategory());
		assertEquals("Noun", word2T2.getCategory());
		assertEquals("Target 1", wordT1.getId());
		assertEquals("Target 2", wordT2.getId());
		assertEquals("Target 1", word2T1.getId());
		assertEquals("Target 2", word2T2.getId());
	}

	@Test
	public final void wordDuplicateTest() {
		RuleIdentifierAndParentSetter setter = RuleIdentifierAndParentSetter.getInstance();
		setter.setIdentifiersAndParents(rule);
		Word newWord = sourceWord.duplicate(true);
		assertEquals("1", sourceWord.getId());
		assertEquals("3", newWord.getId());
		assertEquals("Noun", sourceWord.getCategory());
		assertEquals("Noun", newWord.getCategory());
		Category sourceCat = sourceWord.getCategoryConstituent();
		Category newCat = newWord.getCategoryConstituent();
		assertEquals("Noun", sourceCat.getName());
		assertEquals("Noun", newCat.getName());
		assertEquals(0, sourceWord.getAffixes().size());
		assertEquals(0, newWord.getAffixes().size());
		assertEquals(0, sourceWord.getFeatures().size());
		assertEquals(0, newWord.getFeatures().size());
		newWord = sourceWord.duplicate(false);
		assertEquals("1", sourceWord.getId());
		assertEquals("1", newWord.getId());
	}

	@Test
	public final void affixDuplicateTest() {
		final String kLabel = "gender";
		final String kMatch = "alpha";
		final String kValue = "";
		final String kDefault = "m";
		final int kRanking = 2;
		Affix affix = new Affix();
		affix.setType(AffixType.prefix);
		Feature feature = new Feature();
		feature.setLabel(kLabel);
		feature.setMatch(kMatch);
		feature.setValue(kValue);
		feature.setUnmarked(kDefault);
		feature.setRanking(kRanking);
		affix.getFeatures().add(feature);

		Affix newAffix = affix.duplicate();
		assert AffixType.prefix == newAffix.getType();
		assert 1 == newAffix.getFeatures().size();
		Feature newFeature = newAffix.getFeatures().get(0);
		assertEquals(kLabel, newFeature.getLabel());
		assertEquals(kMatch, newFeature.getMatch());
		assertEquals(kValue, newFeature.getValue());
		assertEquals(kDefault, newFeature.getUnmarked());
		assertEquals(kRanking, newFeature.getRanking());
	}
}