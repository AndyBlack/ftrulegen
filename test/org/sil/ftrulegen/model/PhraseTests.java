/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.flexmodel.FLExData;
import org.sil.ftrulegen.flexmodel.FLExFeature;
import org.sil.ftrulegen.flexmodel.FLExFeatureValue;
import org.sil.ftrulegen.service.RuleIdentifierAndParentSetter;
import org.sil.ftrulegen.service.ServiceTestBase;
import org.sil.ftrulegen.service.XMLFLExDataBackEndProvider;

public class PhraseTests extends ServiceTestBase {
	private FLExTransRuleGenerator ruleGenerator;
	private Phrase sourcePhrase;
	private Word sourceWord;
	private Word sourceWord2;
	private Phrase targetPhrase;
	private Word targetWord;
	private Word targetWord2;

	@Before
	public final void setup() {
		ruleGenerator = new FLExTransRuleGenerator();
		FLExTransRule rule = new FLExTransRule();
		rule.setName("Rule 1");
		ruleGenerator.getFLExTransRules().add(rule);
		Source source = rule.getSource();
		sourcePhrase = source.getPhrase();
		sourceWord = new Word();
		sourceWord.setId("1");
		sourceWord.setCategory("Noun");
		sourceWord.getCategoryConstituent().setName("Noun");
		sourceWord.setHead(HeadValue.yes);
		sourcePhrase.getWords().add(sourceWord);
		sourceWord2 = new Word();
		sourceWord2.setId("2");
		sourceWord2.setCategory("Det");
		sourceWord2.getCategoryConstituent().setName("Det");
		sourceWord2.setHead(HeadValue.no);
		sourcePhrase.getWords().add(sourceWord2);
		sourcePhrase.setParent(rule);
		Target target = rule.getTarget();
		targetPhrase = target.getPhrase();
		targetWord = new Word();
		targetWord.setId("1");
		targetWord.setCategory("Det");
		targetWord.setHead(HeadValue.no);
		targetPhrase.getWords().add(targetWord);
		targetWord2 = new Word();
		targetWord2.setId("2");
		targetWord2.setCategory("Noun");
		targetWord2.setHead(HeadValue.yes);
		targetPhrase.getWords().add(targetWord2);
		targetPhrase.setParent(rule);
		source.setParent(rule);
		target.setParent(rule);
	}

	@Test
	public final void deleteWordAtTest() {
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.deleteWordAt(-1); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.deleteWordAt(2); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());

		sourcePhrase.deleteWordAt(1);
		assertEquals(1, sourcePhrase.getWords().size());
		assertEquals("1", sourcePhrase.getWords().get(0).getId());
		sourcePhrase.deleteWordAt(0);
		assertEquals(0, sourcePhrase.getWords().size());
	}

	@Test
	public final void insertWordAtTest() {
		assertEquals(2, sourcePhrase.getWords().size());
		Word word3 = new Word();
		word3.setId("3");
		sourcePhrase.insertWordAt(word3, -1); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.insertWordAt(word3, 3); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.insertWordAt(word3, 1);
		assertEquals(3, sourcePhrase.getWords().size());
		assertEquals("3", sourcePhrase.getWords().get(1).getId());
		Word word4 = new Word();
		word4.setId("4");
		sourcePhrase.insertWordAt(word4, 0);
		assertEquals(4,  sourcePhrase.getWords().size());
		assertEquals("4", sourcePhrase.getWords().get(0).getId());
		Word word5 = new Word();
		word5.setId("5");
		sourcePhrase.insertWordAt(word5, sourcePhrase.getWords().size());
		assertEquals(5,  sourcePhrase.getWords().size());
		assertEquals("5", sourcePhrase.getWords().get(4).getId());
	}

	@Test
	public final void insertNewWordAtTest() {
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.insertNewWordAt(-1); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.insertNewWordAt(3); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.insertNewWordAt(1);
		assertEquals(3, sourcePhrase.getWords().size());
		assertEquals("3", sourcePhrase.getWords().get(1).getId());
		sourcePhrase.insertNewWordAt(0);
		assertEquals(4, sourcePhrase.getWords().size());
		assertEquals("4", sourcePhrase.getWords().get(0).getId());
	}

	@Test
	public final void insertNewWordAtNonSequentialWordIdsTest() {
		sourceWord.setId("2");
		sourceWord2.setId("3");
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.insertNewWordAt(0);
		assertEquals(3, sourcePhrase.getWords().size());
		assertEquals("1", sourcePhrase.getWords().get(0).getId());
		sourcePhrase.insertNewWordAt(0);
		assertEquals(4, sourcePhrase.getWords().size());
		assertEquals("4", sourcePhrase.getWords().get(0).getId());
		sourcePhrase.getWords().get(2).setId("5");
		sourcePhrase.insertNewWordAt(0);
		assertEquals(5, sourcePhrase.getWords().size());
		assertEquals("2", sourcePhrase.getWords().get(0).getId());
	}

	@Test
	public final void swapPositionOfWordsTest() {
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.swapPositionOfWords(-1, 0); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.swapPositionOfWords(2, 0); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.swapPositionOfWords(0, -1); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.swapPositionOfWords(0, 2); // is a no-op
		assertEquals(2, sourcePhrase.getWords().size());

		sourcePhrase.swapPositionOfWords(0, 1);
		assertEquals("2", sourcePhrase.getWords().get(0).getId());
		assertEquals("1", sourcePhrase.getWords().get(1).getId());
		sourcePhrase.swapPositionOfWords(1, 0);
		assertEquals("1", sourcePhrase.getWords().get(0).getId());
		assertEquals("2", sourcePhrase.getWords().get(1).getId());
	}

	@Test
	public final void changeIdOfWordTest() {
		assertEquals(2, sourcePhrase.getWords().size());
		sourcePhrase.changeIdOfWord(-1, "1", "2"); // is a no-op
		assertEquals("1", sourcePhrase.getWords().get(0).getId());
		assertEquals("2", sourcePhrase.getWords().get(1).getId());
		sourcePhrase.changeIdOfWord(2, "1", "2"); // is a no-op
		assertEquals("1", sourcePhrase.getWords().get(0).getId());
		assertEquals("2", sourcePhrase.getWords().get(1).getId());
		sourcePhrase.changeIdOfWord(0, "1", "2");
		assertEquals("2", sourcePhrase.getWords().get(0).getId());
		assertEquals("1", sourcePhrase.getWords().get(1).getId());
		sourcePhrase.changeIdOfWord(0, "2", "1");
		assertEquals("1", sourcePhrase.getWords().get(0).getId());
		assertEquals("2", sourcePhrase.getWords().get(1).getId());
	}

	@Test
	public final void markWordAsHeadTest() {
		assertEquals(HeadValue.yes, sourcePhrase.getWords().get(0).getHead());
		assertEquals(HeadValue.no, sourcePhrase.getWords().get(1).getHead());
		sourcePhrase.markWordAsHead(sourceWord2);
		assertEquals(HeadValue.no, sourcePhrase.getWords().get(0).getHead());
		assertEquals(HeadValue.yes, sourcePhrase.getWords().get(1).getHead());
	}

	@Test
	public final void getPhraseFromCategoryTest() {
		setRuleIdsAndParents();
		Category cat = sourceWord.getCategoryConstituent();
		Phrase phrase = cat.getPhrase();
		assertEquals(sourcePhrase, phrase);
	}

	@Test
	public final void getFeaturesInUseTest() {
		List<FLExFeature> featuresInUse = targetPhrase.getFeaturesInUse();
		assertEquals(0, featuresInUse.size());
		targetWord.insertNewFeature("gender", "m");
		featuresInUse = targetPhrase.getFeaturesInUse();
		assertEquals(1, featuresInUse.size());
		FLExFeature ff = featuresInUse.get(0);
		assertEquals("gender", ff.getName());
		assertEquals(1, ff.getValues().size());
		FLExFeatureValue fv = ff.getValues().get(0);
		assertEquals("m", fv.getAbbreviation());

		targetWord.insertNewFeature("number", "sg");
		featuresInUse = targetPhrase.getFeaturesInUse();
		assertEquals(2, featuresInUse.size());
		ff = featuresInUse.get(0);
		assertEquals("gender", ff.getName());
		assertEquals(1, ff.getValues().size());
		fv = ff.getValues().get(0);
		assertEquals("m", fv.getAbbreviation());
		ff = featuresInUse.get(1);
		assertEquals("number", ff.getName());
		assertEquals(1, ff.getValues().size());
		fv = ff.getValues().get(0);
		assertEquals("sg", fv.getAbbreviation());

		targetWord.insertNewFeature("gender", "m");
		featuresInUse = targetPhrase.getFeaturesInUse();
		assertEquals(2, featuresInUse.size());
		ff = featuresInUse.get(0);
		assertEquals("gender", ff.getName());
		assertEquals(1, ff.getValues().size());
		fv = ff.getValues().get(0);
		assertEquals("m", fv.getAbbreviation());
		ff = featuresInUse.get(1);
		assertEquals("number", ff.getName());
		assertEquals(1, ff.getValues().size());
		fv = ff.getValues().get(0);
		assertEquals("sg", fv.getAbbreviation());
	}

	@Test
	public final void getFeaturesInUseForCategoryTest() {
		XMLFLExDataBackEndProvider providerFLExData;
		FLExData flexData = new FLExData();
		providerFLExData = new XMLFLExDataBackEndProvider(flexData, new Locale("en"));
		setRuleGenExpected(Paths.get(getTestDataDir(), "FLExDataFrenchSpan.xml").toString());
		providerFLExData.loadFLExDataFromFile(getRuleGenExpected());
		flexData = providerFLExData.getFLExData();
		assertNotNull(flexData);
		targetWord.insertNewFeature("gender", "m");
		targetWord.insertNewFeature("number", "sg");

		Category cat = new Category("Noun");
		List<FLExFeature> featuresForCategory = targetPhrase.getFeaturesInUseForCategory(flexData
				.getTargetData().getCategories(), cat);
		assertNotNull(featuresForCategory);
		assertEquals(2, featuresForCategory.size());
		checkFeature(featuresForCategory, 0, "gender", 1);
		checkFeature(featuresForCategory, 1, "number", 1);

		targetWord2.insertNewFeature("case", "acc");
		assertEquals(1, targetWord2.getFeatures().size());
		featuresForCategory = targetPhrase.getFeaturesInUseForCategory(flexData
				.getTargetData().getCategories(), cat);
		assertNotNull(featuresForCategory);
		assertEquals(3, featuresForCategory.size());
		checkFeature(featuresForCategory, 0, "gender", 1);
		checkFeature(featuresForCategory, 1, "number", 1);
		checkFeature(featuresForCategory, 2, "case", 1);

		targetWord.setCategory("adj");
		cat.setName("adj");
		featuresForCategory = targetPhrase.getFeaturesInUseForCategory(flexData
				.getTargetData().getCategories(), cat);
		assertNotNull(featuresForCategory);
		assertEquals(2, featuresForCategory.size());
		checkFeature(featuresForCategory, 0, "gender", 1);
		checkFeature(featuresForCategory, 1, "number", 1);
	}

	protected void checkFeature(List<FLExFeature> featuresForCategory, int index, String name,
			int valueSize) {
		FLExFeature flexFeature = featuresForCategory.get(index);
		assertEquals(name, flexFeature.getName());
		assertEquals(valueSize, flexFeature.getValues().size());
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

	@Test
	public final void getCategoryOfWordWithIdTest() {
		Category cat = sourcePhrase.getCategoryOfWordWithId("1");
		assertNotNull(cat);
		assertEquals("Noun", cat.getName());
		cat = targetPhrase.getCategoryOfWordWithId("1");
		assertNotNull(cat);
		assertEquals("Noun", cat.getName());
		cat = targetPhrase.getCategoryOfWordWithId("2");
		assertNotNull(cat);
		assertEquals("Det", cat.getName());
	}

	private void setRuleIdsAndParents() {
		RuleIdentifierAndParentSetter ruleIdAndParent = RuleIdentifierAndParentSetter.getInstance();
		ruleIdAndParent.setIdentifiersAndParents(ruleGenerator.getFLExTransRules().get(0));
	}
}