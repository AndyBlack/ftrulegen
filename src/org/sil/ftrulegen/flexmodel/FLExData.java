/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FLExData")
public class FLExData
{
	private SourceFLExData SourceData = new SourceFLExData();
	public final SourceFLExData getSourceData()
	{
		return SourceData;
	}
	public final void setSourceData(SourceFLExData value)
	{
		SourceData = value;
	}
	private TargetFLExData TargetData = new TargetFLExData();
	public final TargetFLExData getTargetData()
	{
		return TargetData;
	}
	public final void setTargetData(TargetFLExData value)
	{
		TargetData = value;
	}

	public FLExData()
	{
	}

	public final void setFeatureInFeatureValues()
	{
		getSourceData().setFeatureInFeatureValues();
		getTargetData().setFeatureInFeatureValues();
	}
}