/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.*;

import jakarta.xml.bind.annotation.XmlAttribute;

public class FLExFeature
{
	@XmlAttribute(name="name")
	private String Name = "";
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}

	private List<FLExFeatureValue> Values = new ArrayList<FLExFeatureValue> ();
	public final List<FLExFeatureValue> getValues()
	{
		return Values;
	}
	public final void setValues(ArrayList<FLExFeatureValue> value)
	{
		Values = value;
	}

	public FLExFeature()
	{
	}

	@Override
	public String toString()
	{
		return getName();
	}
}