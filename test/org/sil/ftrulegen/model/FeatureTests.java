/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Andy Black
 *
 */
public class FeatureTests {
	Word word;
	Affix affix;
	Feature fCase;
	Feature fGender;
	Feature fNumber;
	int maxRankings = 5;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		word = new Word();
		affix = new Affix();
		fCase = new Feature();
		fCase.setLabel("case");
		fCase.setValue("acc");
		fGender = new Feature();
		fGender.setLabel("gender");
		fGender.setValue("m");
		fNumber = new Feature();
		fNumber.setLabel("number");
		fNumber.setValue("sg");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void assignOtherRankingsinAffixTest() {
		initAffixFeatures();
		fCase.setRanking(2);
		fCase.assignRankingsToSisterFeaturesWithoutARanking(maxRankings);
		assertEquals(2, fCase.getRanking());
		assertEquals(1, fGender.getRanking());
		assertEquals(3, fNumber.getRanking());
	}

	@Test
	public void assignOtherRankingsinWordTest() {
		initWordFeatures();
		fCase.setRanking(2);
		fCase.assignRankingsToSisterFeaturesWithoutARanking(maxRankings);
		assertEquals(2, fCase.getRanking());
		assertEquals(1, fGender.getRanking());
		assertEquals(3, fNumber.getRanking());

		fCase.setRanking(0);
		fNumber.setRanking(0);
		fGender.setRanking(1);
		fCase.assignRankingsToSisterFeaturesWithoutARanking(maxRankings);
		assertEquals(2, fCase.getRanking());
		assertEquals(1, fGender.getRanking());
		assertEquals(3, fNumber.getRanking());
	}

	protected void initWordFeatures() {
		assertEquals(0, fCase.getRanking());
		assertEquals(0, fGender.getRanking());
		assertEquals(0, fNumber.getRanking());
		word.getFeatures().add(fCase);
		word.getFeatures().add(fGender);
		word.getFeatures().add(fNumber);
		fCase.setParent(word);
		fGender.setParent(word);
		fNumber.setParent(word);
	}

	@Test
	public void removeAllRankingsInAffixTest() {
		initAffixFeatures();
		fCase.setRanking(1);
		fGender.setRanking(2);
		fNumber.setRanking(3);
		fCase.setRanking(0);
		fCase.removeRankingsFromSisterFeatures();
		assertEquals(0, fCase.getRanking());
		assertEquals(0, fGender.getRanking());
		assertEquals(0, fNumber.getRanking());
	}

	protected void initAffixFeatures() {
		assertEquals(0, fCase.getRanking());
		assertEquals(0, fGender.getRanking());
		assertEquals(0, fNumber.getRanking());
		affix.getFeatures().add(fCase);
		affix.getFeatures().add(fGender);
		affix.getFeatures().add(fNumber);
		fCase.setParent(affix);
		fGender.setParent(affix);
		fNumber.setParent(affix);
	}

	@Test
	public void removeAllRankingsInWordTest() {
		initWordFeatures();
		assertEquals(3, word.getFeatures().size());
		fCase.setRanking(1);
		fGender.setRanking(2);
		fNumber.setRanking(3);
		fCase.setRanking(0);
		fCase.removeRankingsFromSisterFeatures();
		assertEquals(0, fCase.getRanking());
		assertEquals(0, fGender.getRanking());
		assertEquals(0, fNumber.getRanking());
	}

	@Test
	public void swapRankingOfSisterFeatureWithRankingAffixTest() {
		initAffixFeatures();
		fCase.setRanking(1);
		fGender.setRanking(2);
		fNumber.setRanking(3);
		fCase.setRanking(3);
		fCase.swapRankingOfSisterFeatureWithRanking(3, 1);
		assertEquals(3, fCase.getRanking());
		assertEquals(2, fGender.getRanking());
		assertEquals(1, fNumber.getRanking());

		fCase.setRanking(2);
		fCase.swapRankingOfSisterFeatureWithRanking(2, 3);
		assertEquals(2, fCase.getRanking());
		assertEquals(3, fGender.getRanking());
		assertEquals(1, fNumber.getRanking());
	}

	@Test
	public void swapRankingOfSisterFeatureWithRankingWordTest() {
		initWordFeatures();
		fCase.setRanking(1);
		fGender.setRanking(2);
		fNumber.setRanking(3);
		fCase.setRanking(2);
		fCase.swapRankingOfSisterFeatureWithRanking(2, 1);
		assertEquals(2, fCase.getRanking());
		assertEquals(1, fGender.getRanking());
		assertEquals(3, fNumber.getRanking());

		fCase.setRanking(3);
		fCase.swapRankingOfSisterFeatureWithRanking(3, 2);
		assertEquals(3, fCase.getRanking());
		assertEquals(1, fGender.getRanking());
		assertEquals(2, fNumber.getRanking());
	}
}
