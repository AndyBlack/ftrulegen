/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

public class TargetFLExData extends FLExDataBase {
	public TargetFLExData() {
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
		TargetFLExData flexData = (TargetFLExData) obj;
		if (!getName().equals(flexData.getName())) {
			result = false;
		} else if (!getCategories().equals(flexData.getCategories())) {
			result = false;
		} else if (!getFeatures().equals(flexData.getFeatures())) {
			return false;
		}
		return result;
	}
}