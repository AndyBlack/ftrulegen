/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.*;


public class FLExFeatureValues
{
	private List<FLExFeatureValue> Values = new ArrayList<FLExFeatureValue> ();
	public final List<FLExFeatureValue> getValues()
	{
		return Values;
	}
	public final void setValues(List<FLExFeatureValue> value)
	{
		Values = value;
	}

	public FLExFeatureValues()
	{
	}
}