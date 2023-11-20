/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import jakarta.xml.bind.annotation.XmlAttribute;

public class FLExFeatureValue
{
	private String abbreviation = "";
	@XmlAttribute(name = "abbr")
	public final String getAbbreviation()
	{
		return abbreviation;
	}
	public final void setAbbreviation(String value)
	{
		abbreviation = value;
	}

	private FLExFeature feature;
	public final FLExFeature getFeature()
	{
		return feature;
	}
	public final void setFeature(FLExFeature value)
	{
		feature = value;
	}

	public FLExFeatureValue()
	{
	}

	public FLExFeatureValue(String abbreviation)
	{
		this.abbreviation = abbreviation;
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
	
	@Override
	public int hashCode() {
		String sCombo = abbreviation;
		return sCombo.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		boolean result = true;
		FLExFeatureValue val = (FLExFeatureValue) obj;
		if (!getAbbreviation().equals(val.getAbbreviation())) {
			result = false;
		}
		return result;
	}
}