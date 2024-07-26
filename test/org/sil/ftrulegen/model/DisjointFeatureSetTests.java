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
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		featureSet.setName("BantuNounClass");
		ObservableList<DisjointFeatureValuePairing> pairings = FXCollections.observableArrayList();
		DisjointFeatureValuePairing pair1 = new DisjointFeatureValuePairing();
		pair1.setFlexFeatureName(sBantuSg);
		pairings.add(pair1);
		DisjointFeatureValuePairing pair2 = new DisjointFeatureValuePairing();
		pair2.setFlexFeatureName(sBantuPl);
		pairings.add(pair2);
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

}
