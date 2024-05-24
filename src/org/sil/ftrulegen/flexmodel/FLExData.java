/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.ArrayList;
import java.util.List;

import org.sil.ftrulegen.model.Category;
import org.sil.ftrulegen.model.PhraseType;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FLExData")
public class FLExData {
	private SourceFLExData sourceData = new SourceFLExData();

	@XmlElement(name = "SourceData")
	public final SourceFLExData getSourceData() {
		return sourceData;
	}

	public final void setSourceData(SourceFLExData value) {
		sourceData = value;
	}

	private TargetFLExData targetData = new TargetFLExData();

	@XmlElement(name = "TargetData")
	public final TargetFLExData getTargetData() {
		return targetData;
	}

	public final void setTargetData(TargetFLExData value) {
		targetData = value;
	}

	public FLExData() {
	}

	public void clear() {
		sourceData.clear();
		targetData.clear();
	}

	public void setFeatureInFeatureValues() {
		sourceData.setFeatureInFeatureValues();
		targetData.setFeatureInFeatureValues();
	}

	public List<FLExCategory> getFLExCategoriesForPhrase(PhraseType phraseType) {
		List<FLExCategory> flexCategories = new ArrayList<FLExCategory>();
		if (phraseType == PhraseType.source) {
			flexCategories = getSourceData().getCategories();
		} else {
			flexCategories = getTargetData().getCategories();
		}
		return flexCategories;
	}

	public List<FLExFeature> getFeaturesInPhraseForCategory(PhraseType phraseType, Category cat) {
		List<FLExFeature> flexFeatures = new ArrayList<FLExFeature>();
		if (phraseType == PhraseType.source) {
			flexFeatures = getSourceData().getFeaturesForCategory(cat);
		} else {
			flexFeatures = getTargetData().getFeaturesForCategory(cat);
		}
		return flexFeatures;
	}
}
