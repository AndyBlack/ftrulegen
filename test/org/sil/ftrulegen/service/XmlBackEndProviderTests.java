/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.Constants;
import org.sil.ftrulegen.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class XmlBackEndProviderTests extends ServiceTestBase {

	@Test
	public final void loadEx1aDefNounTest() {
		setRuleGenExpected(Paths.get(getTestDataDir(), "Ex1a_Def-Noun.xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		ruleGenerator = provider.getRuleGenerator();
		assertNotNull(ruleGenerator);
		assertEquals(1, ruleGenerator.getFLExTransRules().size());
		FLExTransRule ftRule = ruleGenerator.getFLExTransRules().get(0);
		assertEquals("Definite - Noun", ftRule.getName());

		Source source = ftRule.getSource();
		assertNotNull(source);
		Phrase sourcePhrase = source.getPhrase();
		assert PhraseType.source == sourcePhrase.getType();
		List<Word> words = sourcePhrase.getWords();
		assertEquals(2, words.size());
		Word word = words.get(0);
		checkWordAttributes(word, "1", "def", HeadValue.no);
		assert 0 == word.getAffixes().size();
		assert 0 == word.getFeatures().size();
		word = words.get(1);
		checkWordAttributes(word, "2", "n", HeadValue.no);
		assert 0 == word.getAffixes().size();
		assert 0 == word.getFeatures().size();

		Target target = ftRule.getTarget();
		assertNotNull(target);
		Phrase targetPhrase = target.getPhrase();
		assert PhraseType.target == targetPhrase.getType();
		words = targetPhrase.getWords();
		assert 2 == words.size();
		word = words.get(0);
		checkWordAttributes(word, "1", "", HeadValue.no);
		assert 0 == word.getAffixes().size();
		assert 1 == word.getFeatures().size();
		Feature feature = word.getFeatures().get(0);
		checkFeatureAttributes(feature, "gender", "\u03b1");
		word = words.get(1);
		checkWordAttributes(word, "2", "", HeadValue.yes);
		assert 0 == word.getAffixes().size();
		assert 1 == word.getFeatures().size();
		feature = word.getFeatures().get(0);
		checkFeatureAttributes(feature, "gender", "\u03b1");
	}

	@Test
	public final void loadEx4bIndefAdjNounTest() {
		setRuleGenExpected(Paths.get(getTestDataDir(), "Ex4b_Indef-Adj-Noun.xml").toString());
		provider.loadDataFromFile(getRuleGenExpected());
		FLExTransRuleGenerator ruleGenerator = provider.getRuleGenerator();
		assertNotNull(ruleGenerator);
		assert 1 == ruleGenerator.getFLExTransRules().size();
		FLExTransRule ftRule = ruleGenerator.getFLExTransRules().get(0);
		assertEquals("Indefinite - Adjective - Noun", ftRule.getName());

		Source source = ftRule.getSource();
		assertNotNull(source);
		Phrase sourcePhrase = source.getPhrase();
		List<Word> words = sourcePhrase.getWords();
		assert 3 == words.size();
		Word word = words.get(0);
		checkWordAttributes(word, "1", "indef", HeadValue.no);
		assert 0 == word.getAffixes().size();
		assert 0 == word.getFeatures().size();
		word = words.get(1);
		checkWordAttributes(word, "2", "adj", HeadValue.no);
		assert 0 == word.getAffixes().size();
		assert 0 == word.getFeatures().size();
		word = words.get(2);
		checkWordAttributes(word, "3", "n", HeadValue.no);
		assert 0 == word.getAffixes().size();
		assert 0 == word.getFeatures().size();

		Target target = ftRule.getTarget();
		assertNotNull(target);
		Phrase targetPhrase = target.getPhrase();
		words = targetPhrase.getWords();
		assert 3 == words.size();
		word = words.get(0);
		checkWordAttributes(word, "1", "", HeadValue.no);
		assert 2 == word.getAffixes().size();
		assert 0 == word.getFeatures().size();
		Affix affix = word.getAffixes().get(0);
		assert AffixType.suffix == affix.getType();
		assert 1 == affix.getFeatures().size();
		Feature feature = affix.getFeatures().get(0);
		checkFeatureAttributes(feature, "gender", "\u03b1");
		affix = word.getAffixes().get(1);
		assert AffixType.suffix == affix.getType();
		assert 1 == affix.getFeatures().size();
		feature = affix.getFeatures().get(0);
		checkFeatureAttributes(feature, "number", "\u03b2");

		word = words.get(1);
		checkWordAttributes(word, "3", "", HeadValue.yes);
		assert 1 == word.getAffixes().size();
		assert 1 == word.getFeatures().size();
		feature = word.getFeatures().get(0);
		checkFeatureAttributes(feature, "gender", "\u03b1");
		affix = word.getAffixes().get(0);
		assert AffixType.suffix == affix.getType();
		assert 1 == affix.getFeatures().size();
		feature = affix.getFeatures().get(0);
		checkFeatureAttributes(feature, "number", "\u03b2");

		word = words.get(2);
		checkWordAttributes(word, "2", "", HeadValue.no);
		assert 2 == word.getAffixes().size();
		assert 0 == word.getFeatures().size();
		affix = word.getAffixes().get(0);
		assert AffixType.suffix == affix.getType();
		assert 1 == affix.getFeatures().size();
		feature = affix.getFeatures().get(0);
		checkFeatureAttributes(feature, "gender", "\u03b1");
		affix = word.getAffixes().get(1);
		assert AffixType.prefix == affix.getType();
		assert 1 == affix.getFeatures().size();
		feature = affix.getFeatures().get(0);
		checkFeatureAttributes(feature, "number", "\u03b2");
	}

	protected final void checkWordAttributes(Word word, String id, String category, HeadValue head) {
		assertNotNull(word);
		assertEquals(id, word.getId());
		assertEquals(category, word.getCategory());
		assert head == word.getHead();
	}

	protected final void checkFeatureAttributes(Feature feature, String label, String match) {
		assertNotNull(feature);
		assertEquals(label, feature.getLabel());
		assertEquals(match, feature.getMatch());
	}

	@Test
	public final void saveTest() {
		FLExTransRuleGenerator ruleGenerator = new FLExTransRuleGenerator();
		FLExTransRule ftRule = new FLExTransRule();
		ftRule.setName("Indefinite - Adjective - Noun");
		Source source = new Source();
		Phrase sourcePhrase = new Phrase();
		Word word1 = makeWordAttributes("1", "indef", HeadValue.no);
		sourcePhrase.getWords().add(word1);
		Word word2 = makeWordAttributes("2", "adj", HeadValue.no);
		sourcePhrase.getWords().add(word2);
		Word word3 = makeWordAttributes("3", "n", HeadValue.no);
		sourcePhrase.getWords().add(word3);
		source.setPhrase(sourcePhrase);
		ftRule.setSource(source);

		Target target = new Target();
		Phrase targetPhrase = new Phrase();
		word1 = makeWordAttributes("1", "", HeadValue.no);
		ArrayList<Feature> features1 = new ArrayList<Feature>();
		features1.add(makeFeatureAttributes("gender", "\u03b1"));
		Affix affix1 = makeAffix(AffixType.suffix, features1);
		word1.getAffixes().add(affix1);
		ArrayList<Feature> features2 = new ArrayList<Feature>();
		features2.add(makeFeatureAttributes("number", "\u03b2"));
		Affix affix2 = makeAffix(AffixType.suffix, features2);
		word1.getAffixes().add(affix2);
		targetPhrase.getWords().add(word1);

		word2 = makeWordAttributes("3", "", HeadValue.yes);
		features2 = new ArrayList<Feature>();
		features2.add(makeFeatureAttributes("number", "\u03b2"));
		affix2 = makeAffix(AffixType.suffix, features2);
		word2.getAffixes().add(affix2);
		ArrayList<Feature> wordFeatures = new ArrayList<Feature>();
		wordFeatures.add(makeFeatureAttributes("gender", "\u03b1"));
		word2.setFeatures(wordFeatures);
		targetPhrase.getWords().add(word2);

		word3 = makeWordAttributes("2", "", HeadValue.no);
		ArrayList<Feature> features31 = new ArrayList<Feature>();
		features31.add(makeFeatureAttributes("gender", "\u03b1"));
		Affix affix3 = makeAffix(AffixType.suffix, features31);
		word3.getAffixes().add(affix3);
		ArrayList<Feature> features32 = new ArrayList<Feature>();
		features32.add(makeFeatureAttributes("number", "\u03b2"));
		affix2 = makeAffix(AffixType.suffix, features32);
		word3.getAffixes().add(affix2);
		targetPhrase.getWords().add(word3);
		target.setPhrase(targetPhrase);
		ftRule.setTarget(target);
		ruleGenerator.getFLExTransRules().add(ftRule);

		provider.setRuleGenerator(ruleGenerator);
		File xmlFile = null;
		try {
			xmlFile = File.createTempFile("RuleGen", ".xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// comment off the following three lines to see the temp file
		if (xmlFile != null) {
			xmlFile.deleteOnExit();
		}
		provider.saveDataToFile(xmlFile.getPath());
		String sProduced = null;
		String sExpected = null;
		try {
			sProduced = new String(Files.readAllBytes(xmlFile.toPath()), StandardCharsets.UTF_8);
			sExpected = new String(Files.readAllBytes(Paths.get(getRuleGenExpected())), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(sExpected, sProduced);
	}

	protected final Word makeWordAttributes(String id, String category, HeadValue head) {
		Word word = new Word();
		word.setId(id);
		word.setCategory(category);
		word.setHead(head);
		return word;
	}

	protected final Affix makeAffix(AffixType type, ArrayList<Feature> features) {
		Affix affix = new Affix();
		affix.setType(type);
		affix.setFeatures(features);
		return affix;
	}

	protected final Feature makeFeatureAttributes(String label, String match) {
		Feature feature = new Feature();
		feature.setLabel(label);
		feature.setMatch(match);
		return feature;
	}

	@Test
	public final void loadNonExistingFileTest() {
		try {
			ruleGenerator = new FLExTransRuleGenerator();
			ResourceBundle bundle = ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, new Locale("en"));
			provider = new XmlBackEndProvider(ruleGenerator, bundle);
			String sMissingFile = Constants.UNIT_TEST_MISSING_DATA_FILE;
			Path missingPath = Paths.get(sMissingFile);
			Files.deleteIfExists(missingPath);
			provider.loadDataFromFile(sMissingFile);
			File missingFile = new File(sMissingFile);
			assertEquals(true, missingFile.exists());
			FLExTransRuleGenerator ftGen = provider.getRuleGenerator();
			assertEquals(1, ftGen.getFLExTransRules().size());
			FLExTransRule rule = ftGen.getFLExTransRules().get(0);
			assertEquals("", rule.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}