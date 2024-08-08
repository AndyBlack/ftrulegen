/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.flexmodel.FLExData;
import org.sil.ftrulegen.service.ServiceTestBase;
import org.sil.ftrulegen.service.XMLFLExDataBackEndProvider;

/**
 * @author Andy Black
 *
 */
public class DisjointFeatureSetTests extends ServiceTestBase {

	DisjointFeatureSet featureSet = new DisjointFeatureSet();
	final String sBantuSg = "Bantu1 singular";
	final String sBantuPl = "Bantu2 plural";
	final String sBantu3 = "Bantu3 trial";
	final String sBantu4 = "Bantu4 quadral";
	final String sBantu5 = "Bantu5 dual";
	final String sBantu6 = "Bantu6 paucal";
	final String sg = "sg";
	final String pl = "sg";
	final String dual = "dual";
	final String paucal = "paucal";
	final String trial = "trial";
	final String quadral = "quadral";
	DisjointFeatureValuePairing pairing1;
	DisjointFeatureValuePairing pairing2;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		featureSet.setName("BantuNounClass");
		ObservableList<DisjointFeatureValuePairing> pairings = FXCollections.observableArrayList();
		pairing1 = createNewPairing(sBantuSg, "sg");
		pairing2 = createNewPairing(sBantuPl, "sg");
		pairings.add(pairing1);
		pairings.add(pairing2);
		featureSet.setDisjointFeatureValuePairings(pairings);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void hasFLExFeatureInListTest() {
		XMLFLExDataBackEndProvider providerFLExData;
		FLExData flexData = new FLExData();
		providerFLExData = new XMLFLExDataBackEndProvider(flexData, new Locale("en"));
		setRuleGenExpected(Paths.get(getTestDataDir(), "DisjointFeatureSetFLExData.xml").toString());
		providerFLExData.loadFLExDataFromFile(getRuleGenExpected());
		flexData = providerFLExData.getFLExData();
		boolean result = featureSet.hasFLExFeatureInList(flexData.getSourceData().getFeatures());
		assertEquals(false,result);
		result = featureSet.hasFLExFeatureInList(flexData.getTargetData().getFeatures());
		assertEquals(true,result);
	}

	@Test
	public void removePairingsFromTest() {
		DisjointFeatureValuePairing pairing3 = createNewPairing(sBantu3, trial);
		DisjointFeatureValuePairing pairing4 = createNewPairing(sBantu4, quadral);
		DisjointFeatureValuePairing pairing5 = createNewPairing(sBantu5, dual);
		DisjointFeatureValuePairing pairing6 = createNewPairing(sBantu6, paucal);

		assertEquals(2, featureSet.getDisjointFeatureValuePairings().size());
		featureSet.removePairingsFrom(0);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);
		featureSet.removePairingsFrom(1);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);
		featureSet.removePairingsFrom(2);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);
		featureSet.removePairingsFrom(3);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);
		featureSet.removePairingsFrom(4);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);
		featureSet.removePairingsFrom(5);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);
		featureSet.removePairingsFrom(6);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);
		featureSet.removePairingsFrom(7);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);

		featureSet.getDisjointFeatureValuePairings().add(pairing3);
		featureSet.removePairingsFrom(3);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);

		featureSet.getDisjointFeatureValuePairings().add(pairing3);
		featureSet.getDisjointFeatureValuePairings().add(pairing4);
		featureSet.removePairingsFrom(3);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);

		featureSet.getDisjointFeatureValuePairings().add(pairing3);
		featureSet.getDisjointFeatureValuePairings().add(pairing4);
		featureSet.getDisjointFeatureValuePairings().add(pairing5);
		featureSet.removePairingsFrom(3);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);

		featureSet.getDisjointFeatureValuePairings().add(pairing3);
		featureSet.getDisjointFeatureValuePairings().add(pairing4);
		featureSet.getDisjointFeatureValuePairings().add(pairing5);
		featureSet.getDisjointFeatureValuePairings().add(pairing6);
		featureSet.removePairingsFrom(3);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);

		featureSet.getDisjointFeatureValuePairings().add(pairing3);
		featureSet.getDisjointFeatureValuePairings().add(pairing4);
		featureSet.getDisjointFeatureValuePairings().add(pairing5);
		featureSet.getDisjointFeatureValuePairings().add(pairing6);
		featureSet.removePairingsFrom(3);
		checkPairing(0, 2, sBantuSg, sg);
		checkPairing(1, 2, sBantuPl, pl);

		featureSet.getDisjointFeatureValuePairings().add(pairing3);
		featureSet.getDisjointFeatureValuePairings().add(pairing4);
		featureSet.getDisjointFeatureValuePairings().add(pairing5);
		featureSet.getDisjointFeatureValuePairings().add(pairing6);
		featureSet.removePairingsFrom(5);
		checkPairing(0, 4, sBantuSg, sg);
		checkPairing(1, 4, sBantuPl, pl);
		checkPairing(2, 4, sBantu3, trial);
		checkPairing(3, 4, sBantu4, quadral);

		featureSet.getDisjointFeatureValuePairings().add(pairing5);
		featureSet.getDisjointFeatureValuePairings().add(pairing6);
		featureSet.removePairingsFrom(6);
		checkPairing(0, 5, sBantuSg, sg);
		checkPairing(1, 5, sBantuPl, pl);
		checkPairing(2, 5, sBantu3, trial);
		checkPairing(3, 5, sBantu4, quadral);
		checkPairing(4, 5, sBantu5, dual);
	}

	private DisjointFeatureValuePairing createNewPairing(String flexFeature, String coFeatureValue) {
		DisjointFeatureValuePairing pairing = new DisjointFeatureValuePairing();
		pairing.setFlexFeatureName(flexFeature);
		pairing.setCoFeatureValue(coFeatureValue);
		return  pairing;
	}

	private void checkPairing(int pairingIndex, int size, String flexFeature, String coFeatureValue) {
		DisjointFeatureValuePairing pairing = featureSet.getDisjointFeatureValuePairings().get(pairingIndex);
		assertEquals(size, featureSet.getDisjointFeatureValuePairings().size());
		assertEquals(flexFeature, pairing.getFlexFeatureName());
		assertEquals(coFeatureValue, pairing.getCoFeatureValue());
	}
}
