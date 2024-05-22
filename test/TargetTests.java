/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

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
import org.sil.ftrulegen.service.ServiceTestBase;
import org.sil.ftrulegen.service.XMLFLExDataBackEndProvider;

/**
 * @author Andy Black
 *
 */
public class TargetTests extends ServiceTestBase {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testgetFeaturesForCategoryTest() {
		XMLFLExDataBackEndProvider providerFLExData;
		FLExData flexData = new FLExData();
		providerFLExData = new XMLFLExDataBackEndProvider(flexData, new Locale("en"));
		setRuleGenExpected(Paths.get(getTestDataDir(), "FLExDataFrenchSpan.xml").toString());
		providerFLExData.loadFLExDataFromFile(getRuleGenExpected());
		flexData = providerFLExData.getFLExData();
		assertNotNull(flexData);
		Category cat = new Category("n");
		List<FLExFeature> flexFeatures = flexData.getTargetData().getFeaturesForCategory(cat);
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
	}

}
