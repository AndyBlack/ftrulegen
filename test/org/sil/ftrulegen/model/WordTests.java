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

public class WordTests {
	private Word word;

	@Before
	public final void setup() {
		word = new Word();
		word.setId("Source 1");
		word.setCategory("Noun");
		word.setCategoryConstituent(new Category(word.getCategory()));
		word.setHead(HeadValue.yes);
	}

	@Test
	public final void deleteCategoryTest() {
		assert "Noun" == word.getCategory();
		assertNotNull(word.getCategoryConstituent());
		assert "Noun" == word.getCategoryConstituent().getName();
		word.deleteCategory();
		assert "" == word.getCategory();
		assertNotNull(word.getCategoryConstituent());
		assert "" == word.getCategoryConstituent().getName();
	}

	@Test
	public final void insertCategoryTest() {
		word.deleteCategory();
		assert "" == word.getCategory();
		assertNotNull(word.getCategoryConstituent());
		assert "" == word.getCategoryConstituent().getName();
		word.insertCategory("verb");
		assert "verb" == word.getCategory();
		assertNotNull(word.getCategoryConstituent());
		assert "verb" == word.getCategoryConstituent().getName();
	}

	@Test
	public final void deleteAffixAtTest() {
		createTwoAffixes();

		word.deleteAffixAt(0);
		assert 1 == word.getAffixes().size();
		assert AffixType.suffix == word.getAffixes().get(0).getType();

		word.insertNewAffixAt(AffixType.prefix, 1);
		assert 2 == word.getAffixes().size();
		assert AffixType.suffix == word.getAffixes().get(0).getType();
		assert AffixType.prefix == word.getAffixes().get(1).getType();

		word.deleteAffixAt(1);
		assert 1 == word.getAffixes().size();
		assert AffixType.suffix == word.getAffixes().get(0).getType();

		word.deleteAffixAt(0);
		assert 0 == word.getAffixes().size();
	}

	private void createTwoAffixes() {
		word.insertNewAffixAt(AffixType.prefix, 0);
		word.insertNewAffixAt(AffixType.suffix, 1);
		assert 2 == word.getAffixes().size();
		assert AffixType.prefix == word.getAffixes().get(0).getType();
		assert AffixType.suffix == word.getAffixes().get(1).getType();
	}

	@Test
	public final void insertNewAffixAtTest() {
		assert 0 == word.getAffixes().size();
		word.insertNewAffixAt(AffixType.prefix, 0);
		assert 1 == word.getAffixes().size();
		assert AffixType.prefix == word.getAffixes().get(0).getType();
		word.insertNewAffixAt(AffixType.suffix, 0);
		assert 2 == word.getAffixes().size();
		assertEquals(AffixType.suffix, word.getAffixes().get(0).getType());
		word.insertNewAffixAt(AffixType.prefix, 2);
		assert 3 == word.getAffixes().size();
		assertEquals(AffixType.prefix, word.getAffixes().get(2).getType());
	}

	@Test
	public final void insertAffixAtTest() {
		assert 0 == word.getAffixes().size();
		Affix affix = new Affix();
		affix.setType(AffixType.prefix);
		word.insertAffixAt(affix, 0);
		assert 1 == word.getAffixes().size();
		assert AffixType.prefix == word.getAffixes().get(0).getType();

		Affix affix2 = new Affix();
		affix2.setType(AffixType.suffix);
		word.insertAffixAt(affix2, 1);
		assert 2 == word.getAffixes().size();
		assert AffixType.suffix == word.getAffixes().get(1).getType();
	}

	@Test
	public final void deleteFeatureTest() {
		Feature feature = new Feature();
		feature.setLabel("gender");
		feature.setMatch("alpha");
		word.getFeatures().add(feature);
		Feature feature2 = new Feature();
		feature2.setLabel("number");
		feature2.setMatch("singular");
		word.getFeatures().add(feature2);
		assert 2 == word.getFeatures().size();
		word.deleteFeature(feature);
		assert 1 == word.getFeatures().size();
		assert feature2 == word.getFeatures().get(0);
		// trying to delete it again does not crash
		word.deleteFeature(feature);
		assert 1 == word.getFeatures().size();
		assert feature2 == word.getFeatures().get(0);
	}

	@Test
	public final void swapPositionOfAffixesTest() {
		createTwoAffixes();
		word.swapPositionOfAffixes(-1, 0); // is a no-op
		assert 2 == word.getAffixes().size();
		assert AffixType.prefix == word.getAffixes().get(0).getType();
		assert AffixType.suffix == word.getAffixes().get(1).getType();
		word.swapPositionOfAffixes(2, 0); // is a no-op
		assert 2 == word.getAffixes().size();
		assert AffixType.prefix == word.getAffixes().get(0).getType();
		assert AffixType.suffix == word.getAffixes().get(1).getType();
		word.swapPositionOfAffixes(0, -1); // is a no-op
		assert 2 == word.getAffixes().size();
		assert AffixType.prefix == word.getAffixes().get(0).getType();
		assert AffixType.suffix == word.getAffixes().get(1).getType();
		word.swapPositionOfAffixes(0, 2); // is a no-op
		assert 2 == word.getAffixes().size();
		assert AffixType.prefix == word.getAffixes().get(0).getType();
		assert AffixType.suffix == word.getAffixes().get(1).getType();

		word.swapPositionOfAffixes(0, 1);
		assert 2 == word.getAffixes().size();
		assert AffixType.suffix == word.getAffixes().get(0).getType();
		assert AffixType.prefix == word.getAffixes().get(1).getType();

		word.swapPositionOfAffixes(1, 0);
		assert 2 == word.getAffixes().size();
		assert AffixType.prefix == word.getAffixes().get(0).getType();
		assert AffixType.suffix == word.getAffixes().get(1).getType();
	}
}