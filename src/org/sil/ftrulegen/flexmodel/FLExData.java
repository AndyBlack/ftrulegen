/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

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
}