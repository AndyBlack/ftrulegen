/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import jakarta.xml.bind.annotation.XmlAttribute;

public class FLExCategory
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

	public FLExCategory()
	{
	}

	@Override
	public String toString()
	{
		return getAbbreviation();
	}
}