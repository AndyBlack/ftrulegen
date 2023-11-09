/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.*;

import jakarta.xml.bind.annotation.XmlAttribute;


public abstract class FLExDataBase
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

	private List<FLExCategory> Categories = new ArrayList<FLExCategory> ();
	public final List<FLExCategory> getCategories()
	{
		return Categories;
	}
	public final void setCategories(ArrayList<FLExCategory> value)
	{
		Categories = value;
	}
	private List<FLExFeature> Features = new ArrayList<FLExFeature> ();
	public final List<FLExFeature> getFeatures()
	{
		return Features;
	}
	public final void setFeatures(List<FLExFeature> value)
	{
		Features = value;
	}

	public FLExDataBase()
	{
	}

	public final void setFeatureInFeatureValues()
	{
		for (FLExFeature feat : getFeatures())
		{
			for (FLExFeatureValue value : feat.getValues())
			{
				value.setFeature(feat);
			}
		}
	}
}