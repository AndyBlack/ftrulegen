/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlTransient;

public class FLExFeatureValue
{
	@XmlAttribute(name="abbr")
	private String Abbreviation = "";
	public final String getAbbreviation()
	{
		return Abbreviation;
	}
	public final void setAbbreviation(String value)
	{
		Abbreviation = value;
	}

	@XmlTransient
	private FLExFeature Feature;
	public final FLExFeature getFeature()
	{
		return Feature;
	}
	public final void setFeature(FLExFeature value)
	{
		Feature = value;
	}

	public FLExFeatureValue()
	{
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (getFeature() != null)
		{
			sb.append(getFeature().getName());
			sb.append(" : ");
		}
		sb.append(getAbbreviation());
		return sb.toString();
	}
}