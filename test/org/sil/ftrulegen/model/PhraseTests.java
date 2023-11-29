/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.service.RuleIdentifierAndParentSetter;

public class PhraseTests
{
	private FLExTransRuleGenerator ruleGenerator;
	private Phrase sourcePhrase;
	private Word sourceWord;
	private Word sourceWord2;

	@Before
	public final void setup()
	{
		ruleGenerator = new FLExTransRuleGenerator();
		FLExTransRule rule = new FLExTransRule();
		rule.setName("Rule 1");
		ruleGenerator.getFLExTransRules().add(rule);
		Source source = rule.getSource();
		sourcePhrase = source.getPhrase();
		sourceWord = new Word();
		sourceWord.setId("Source 1");
		sourceWord.setCategory("Noun");
		sourceWord.setHead(HeadValue.yes);
		sourcePhrase.getWords().add(sourceWord);
		sourceWord2 = new Word();
		sourceWord2.setId("Source 2");
		sourceWord2.setCategory("Det");
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
	}

	@Test
	public final void deleteWordAtTest()
	{
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.deleteWordAt(-1); // is a no-op
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.deleteWordAt(2); // is a no-op
		assert 2 == sourcePhrase.getWords().size();

		sourcePhrase.deleteWordAt(1);
		assert 1 == sourcePhrase.getWords().size();
		assert "Source 1" == sourcePhrase.getWords().get(0).getId();
		sourcePhrase.deleteWordAt(0);
		assert 0 == sourcePhrase.getWords().size();
	}

	@Test
	public final void insertNewWordAtTest()
	{
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.insertNewWordAt(-1); // is a no-op
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.insertNewWordAt(3); // is a no-op
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.insertNewWordAt(1);
		assert 3 == sourcePhrase.getWords().size();
		assertEquals("3", sourcePhrase.getWords().get(1).getId());
		sourcePhrase.insertNewWordAt(0);
		assert 4 == sourcePhrase.getWords().size();
		assertEquals("4", sourcePhrase.getWords().get(0).getId());
	}

	@Test
	public final void swapPositionOfWordsTest()
	{
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.swapPositionOfWords(-1, 0); // is a no-op
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.swapPositionOfWords(2, 0); // is a no-op
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.swapPositionOfWords(0, -1); // is a no-op
		assert 2 == sourcePhrase.getWords().size();
		sourcePhrase.swapPositionOfWords(0, 2); // is a no-op
		assert 2 == sourcePhrase.getWords().size();

		sourcePhrase.swapPositionOfWords(0, 1);
		assert "Source 2" == sourcePhrase.getWords().get(0).getId();
		assert "Source 1" == sourcePhrase.getWords().get(1).getId();
		sourcePhrase.swapPositionOfWords(1, 0);
		assert "Source 1" == sourcePhrase.getWords().get(0).getId();
		assert "Source 2" == sourcePhrase.getWords().get(1).getId();
	}

	@Test
	public final void markWordAsHeadTest()
	{
		assert HeadValue.yes == sourcePhrase.getWords().get(0).getHead();
		assert HeadValue.no == sourcePhrase.getWords().get(1).getHead();
		sourcePhrase.markWordAsHead(sourceWord2);
		assert HeadValue.no == sourcePhrase.getWords().get(0).getHead();
		assert HeadValue.yes == sourcePhrase.getWords().get(1).getHead();
	}

	@Test
	public final void getPhraseFromCategoryTest() {
		setRuleIdsAndParents();
		Category cat = sourceWord.getCategoryConstituent();
		Phrase phrase = cat.getPhrase();
		assertEquals(sourcePhrase, phrase);
	}

	@Test
	public final void getPhraseFromFeatureTest() {
		Feature wordFeature = sourceWord.insertNewFeature("gender", "alpha");
		sourceWord2.insertNewAffixAt(AffixType.prefix, 0);
		Affix affix = sourceWord2.getAffixes().get(0);
		Feature affixFeature = affix.insertNewFeature("number", "beta");
		setRuleIdsAndParents();
		Phrase phrase = wordFeature.getPhrase();
		assertEquals(sourcePhrase, phrase);
		phrase = affixFeature.getPhrase();
		assertEquals(sourcePhrase, phrase);
	}

	private void setRuleIdsAndParents() {
		RuleIdentifierAndParentSetter ruleIdAndParent = RuleIdentifierAndParentSetter.getInstance();
		ruleIdAndParent.setIdentifiersAndParents(ruleGenerator.getFLExTransRules().get(0));
	}
}