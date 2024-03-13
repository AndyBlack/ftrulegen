/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.flexmodel.*;
import org.sil.ftrulegen.model.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class XMLFLExDataBackEndProviderTests extends ServiceTestBase {
	private XMLFLExDataBackEndProvider providerFLExData;
	private FLExData flexData;

	@Before
	@Override
	public void setup() {
		super.setup();
		flexData = new FLExData();
		providerFLExData = new XMLFLExDataBackEndProvider(flexData, new Locale("en"));
	}

	@Test
	public final void loadFLExDataTest() {
		setRuleGenExpected(Paths.get(getTestDataDir(), "FLExDataSpanFrench.xml").toString());
		providerFLExData.loadFLExDataFromFile(getRuleGenExpected());
		FLExData flexData = providerFLExData.getFLExData();
		assertNotNull(flexData);
		SourceFLExData source = flexData.getSourceData();
		assertNotNull(source);
		assertEquals("Spanish-FLExTrans-Exp4", source.getName());
		assertNotNull(source.getCategories());
		List<FLExCategory> catList = source.getCategories();
		assertEquals(14, catList.size());
		assert 14 == catList.size();
		checkCategory(catList, "adj", 0);
		checkCategory(catList, "adv", 1);
		checkCategory(catList, "conj", 2);
		checkCategory(catList, "coordconj", 3);
		checkCategory(catList, "cop", 4);
		checkCategory(catList, "def", 5);
		checkCategory(catList, "det", 6);
		checkCategory(catList, "indf", 7);
		checkCategory(catList, "n", 8);
		checkCategory(catList, "nprop", 9);
		checkCategory(catList, "prep", 10);
		checkCategory(catList, "prepart", 11);
		checkCategory(catList, "pro", 12);
		checkCategory(catList, "v", 13);
		checkFeatures(source.getFeatures());

		TargetFLExData target = flexData.getTargetData();
		assertNotNull(target);
		assertEquals("French-FLExTrans-Exp4", target.getName());
		assertNotNull(target.getCategories());
		catList = target.getCategories();
		assert 15 == catList.size();
		checkCategory(catList, "adj", 0);
		checkCategory(catList, "adv", 1);
		checkCategory(catList, "conj", 2);
		checkCategory(catList, "coordconj", 3);
		checkCategory(catList, "cop", 4);
		checkCategory(catList, "def", 5);
		checkCategory(catList, "det", 6);
		checkCategory(catList, "existmrkr", 7);
		checkCategory(catList, "indf", 8);
		checkCategory(catList, "n", 9);
		checkCategory(catList, "nprop", 10);
		checkCategory(catList, "prep", 11);
		checkCategory(catList, "prepart", 12);
		checkCategory(catList, "pro", 13);
		checkCategory(catList, "v", 14);

		checkFeatures(target.getFeatures());
	}

	private void checkFeatures(List<FLExFeature> features) {
		List<FLExFeature> featList = features;
		assert 5 == featList.size();
		List<FLExFeatureValue> values = checkFeatureName(featList, "absolute tense", 0, 4);
		FLExFeature feat = featList.get(0);
		checkFeatureValue(values, "fut", 0, feat);
		checkFeatureValue(values, "pret", 1, feat);
		checkFeatureValue(values, "prs", 2, feat);
		checkFeatureValue(values, "pst", 3, feat);
		values = checkFeatureName(featList, "case", 1, 3);
		feat = featList.get(1);
		checkFeatureValue(values, "acc", 0, feat);
		checkFeatureValue(values, "dat", 1, feat);
		checkFeatureValue(values, "nom", 2, feat);
		values = checkFeatureName(featList, "gender", 2, 3);
		feat = featList.get(2);
		checkFeatureValue(values, "f", 0, feat);
		checkFeatureValue(values, "m", 1, feat);
		checkFeatureValue(values, "?", 2, feat);
		values = checkFeatureName(featList, "number", 3, 2);
		feat = featList.get(3);
		checkFeatureValue(values, "pl", 0, feat);
		checkFeatureValue(values, "sg", 1, feat);
		values = checkFeatureName(featList, "person", 4, 3);
		feat = featList.get(4);
		checkFeatureValue(values, "1", 0, feat);
		checkFeatureValue(values, "2", 1, feat);
		checkFeatureValue(values, "3", 2, feat);
	}

	private void checkFeatureValue(List<FLExFeatureValue> values, String sAbbr, int index, FLExFeature feat) {
		FLExFeatureValue value = values.get(index);
		assertEquals(sAbbr, value.getAbbreviation());
		assertEquals(feat, value.getFeature());
	}

	private List<FLExFeatureValue> checkFeatureName(List<FLExFeature> features, String sName, int index, int iCount) {
		FLExFeature feat = features.get(index);
		assertEquals(sName, feat.getName());
		List<FLExFeatureValue> valList = feat.getValues();
		assert iCount == valList.size();
		return valList;
	}

	private void checkCategory(List<FLExCategory> cats, String sAbbr, int index) {
		FLExCategory cat = cats.get(index);
		assertEquals(sAbbr, cat.getAbbreviation());
	}

	protected final void checkWordAttributes(Word word, String id, String category, HeadValue head) {
		assertNotNull(word);
		assert id == word.getId();
		assert category == word.getCategory();
		assert head == word.getHead();
	}

	protected final void checkFeatureAttributes(Feature feature, String label, String match) {
		assertNotNull(feature);
		assert label == feature.getLabel();
		assert match == feature.getMatch();
	}
}