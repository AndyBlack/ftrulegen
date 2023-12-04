/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import jakarta.xml.bind.annotation.XmlAttribute;

public class FLExCategory {
	private String abbreviation = "";

	@XmlAttribute(name = "abbr")
	public final String getAbbreviation() {
		return abbreviation;
	}

	public final void setAbbreviation(String value) {
		abbreviation = value;
	}

	public FLExCategory() {
	}

	@Override
	public String toString() {
		return getAbbreviation();
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
		FLExCategory cat = (FLExCategory) obj;
		if (!getAbbreviation().equals(cat.getAbbreviation())) {
			result = false;
		}
		return result;
	}
}