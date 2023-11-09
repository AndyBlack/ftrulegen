/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.*;

public class FLExCategories
{
	private List<FLExCategory> Categories = new ArrayList<FLExCategory> ();
	public final List<FLExCategory> getCategories()
	{
		return Categories;
	}
	public final void setCategories(List<FLExCategory> value)
	{
		Categories = value;
	}

	public FLExCategories()
	{
	}
}