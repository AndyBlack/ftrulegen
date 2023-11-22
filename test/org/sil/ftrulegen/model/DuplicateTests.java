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

public class DuplicateTests
{
	private FLExTransRuleGenerator ruleGenerator;
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
		Phrase sourcePhrase = source.getPhrase();
		sourceWord = new Word();
		sourceWord.setId("Source 1");
		sourceWord.setCategory("Noun");
		sourceWord.setCategoryConstituent(new Category(sourceWord.getCategory()));
		sourceWord.setHead(HeadValue.yes);
		sourcePhrase.getWords().add(sourceWord);
		sourceWord2 = new Word();
		sourceWord2.setId("Source 2");
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
	}

	@Test
	public final void ruleDuplicateTest()
	{
		assert 1 == ruleGenerator.getFLExTransRules().size();
		FLExTransRule rule = ruleGenerator.getFLExTransRules().get(0);
		FLExTransRule rule2 = rule.duplicate();
		assert rule.getName() == rule2.getName();
		assert rule.getSource().getPhrase().getWords().size() == rule2.getSource().getPhrase().getWords().size();
		assert rule.getTarget().getPhrase().getWords().size() == rule2.getTarget().getPhrase().getWords().size();
	}

	@Test
	public final void wordDuplicateTest()
	{
		Word newWord = sourceWord.duplicate();
		assert "Source 1" == sourceWord.getId();
		assert "Source 1" == newWord.getId();
		assert "Noun" == sourceWord.getCategory();
		assert "Noun" == newWord.getCategory();
		Category sourceCat = sourceWord.getCategoryConstituent();
		Category newCat = newWord.getCategoryConstituent();
		assert "Noun" == sourceCat.getName();
		assert "Noun" == newCat.getName();
		assert 0 == sourceWord.getAffixes().size();
		assert 0 == newWord.getAffixes().size();
		assert 0 == sourceWord.getFeatures().size();
		assert 0 == newWord.getFeatures().size();
	}

	@Test
	public final void affixDuplicateTest()
	{
		final String kLabel = "gender";
		final String kMatch = "alpha";
		Affix affix = new Affix();
		affix.setType(AffixType.prefix);
		Feature feature = new Feature();
		feature.setLabel(kLabel);
		feature.setMatch(kMatch);
		affix.getFeatures().add(feature);

		Affix newAffix = affix.duplicate();
		assert AffixType.prefix == newAffix.getType();
		assert 1 == newAffix.getFeatures().size();
		Feature newFeature = affix.getFeatures().get(0);
		assert kLabel == newFeature.getLabel();
		assert kMatch == newFeature.getMatch();
	}
}