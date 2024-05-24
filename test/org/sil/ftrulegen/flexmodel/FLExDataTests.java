/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sil.ftrulegen.flexmodel.FLExData;
import org.sil.ftrulegen.flexmodel.FLExFeature;
import org.sil.ftrulegen.flexmodel.FLExFeatureValue;
import org.sil.ftrulegen.model.Category;
import org.sil.ftrulegen.model.PhraseType;
import org.sil.ftrulegen.service.ServiceTestBase;
import org.sil.ftrulegen.service.XMLFLExDataBackEndProvider;

/**
 * @author Andy Black
 *
 */
public class FLExDataTests extends ServiceTestBase {

	FLExData flexData;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		XMLFLExDataBackEndProvider providerFLExData;
		flexData = new FLExData();
		providerFLExData = new XMLFLExDataBackEndProvider(flexData, new Locale("en"));
		setRuleGenExpected(Paths.get(getTestDataDir(), "FLExDataFrenchSpan.xml").toString());
		providerFLExData.loadFLExDataFromFile(getRuleGenExpected());
		flexData = providerFLExData.getFLExData();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getFLExCategoriesForPhraseTest() {
		assertNotNull(flexData);
		List<FLExCategory> flexCategories = flexData.getFLExCategoriesForPhrase(PhraseType.source);
		assertNotNull(flexCategories);
		assertEquals(15, flexCategories.size());
		
		FLExCategory flexCat = flexCategories.get(0);
		assertEquals("adj", flexCat.getAbbreviation());
		assertEquals(2, flexCat.getValidFeatures().size());
		ValidFeature validFeature = flexCat.getValidFeatures().get(0);
		assertEquals("gender", validFeature.getName());
		assertEquals(ValidFeatureType.prefix, validFeature.getType());
		validFeature = flexCat.getValidFeatures().get(1);
		assertEquals("number", validFeature.getName());
		assertEquals(ValidFeatureType.suffix, validFeature.getType());

		flexCat = flexCategories.get(1);
		assertEquals("adv", flexCat.getAbbreviation());
		assertEquals(0, flexCat.getValidFeatures().size());

		flexCat = flexCategories.get(9);
		assertEquals("n", flexCat.getAbbreviation());
		assertEquals(3, flexCat.getValidFeatures().size());
		validFeature = flexCat.getValidFeatures().get(0);
		assertEquals("case", validFeature.getName());
		assertEquals(ValidFeatureType.prefixstemsuffix, validFeature.getType());
		validFeature = flexCat.getValidFeatures().get(1);
		assertEquals("gender", validFeature.getName());
		assertEquals(ValidFeatureType.prefixstemsuffix, validFeature.getType());
		validFeature = flexCat.getValidFeatures().get(2);
		assertEquals("number", validFeature.getName());
		assertEquals(ValidFeatureType.prefixsuffix, validFeature.getType());
	}

	@Test
	public void getFeaturesInPhraseForCategoryTest() {
		assertNotNull(flexData);
		Category cat = new Category("n");
		List<FLExFeature> flexFeatures = flexData.getFeaturesInPhraseForCategory(PhraseType.target, cat);
		assertNotNull(flexFeatures);
		assertEquals(2, flexFeatures.size());

		FLExFeature flexFeature = flexFeatures.get(0);
		assertEquals("gender", flexFeature.getName());
		assertEquals(3, flexFeature.getValues().size());
		FLExFeatureValue value = flexFeature.getValues().get(0);
		assertEquals("?", value.getAbbreviation());
		value = flexFeature.getValues().get(1);
		assertEquals("f", value.getAbbreviation());
		value = flexFeature.getValues().get(2);
		assertEquals("m", value.getAbbreviation());

		flexFeature = flexFeatures.get(1);
		assertEquals("number", flexFeature.getName());
		assertEquals(2, flexFeature.getValues().size());
		value = flexFeature.getValues().get(0);
		assertEquals("pl", value.getAbbreviation());
		value = flexFeature.getValues().get(1);
		assertEquals("sg", value.getAbbreviation());

		flexFeatures = flexData.getFeaturesInPhraseForCategory(PhraseType.source, cat);
		assertNotNull(flexFeatures);
		assertEquals(3, flexFeatures.size());

		flexFeature = flexFeatures.get(0);
		assertEquals("case", flexFeature.getName());
		assertEquals(3, flexFeature.getValues().size());
		value = flexFeature.getValues().get(0);
		assertEquals("acc", value.getAbbreviation());
		value = flexFeature.getValues().get(1);
		assertEquals("dat", value.getAbbreviation());
		value = flexFeature.getValues().get(2);
		assertEquals("nom", value.getAbbreviation());

		flexFeature = flexFeatures.get(1);
		assertEquals("gender", flexFeature.getName());
		assertEquals(3, flexFeature.getValues().size());
		value = flexFeature.getValues().get(0);
		assertEquals("?", value.getAbbreviation());
		value = flexFeature.getValues().get(1);
		assertEquals("f", value.getAbbreviation());

		value = flexFeature.getValues().get(2);
		assertEquals("m", value.getAbbreviation());
		flexFeature = flexFeatures.get(2);
		assertEquals("number", flexFeature.getName());
		assertEquals(2, flexFeature.getValues().size());
		value = flexFeature.getValues().get(0);
		assertEquals("pl", value.getAbbreviation());
		value = flexFeature.getValues().get(1);
		assertEquals("sg", value.getAbbreviation());
	}

}
