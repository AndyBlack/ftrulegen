/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

public class FLExCategory {
	private String abbreviation = "";
	private List<ValidFeature> validFeatures = new ArrayList<ValidFeature>();

	@XmlAttribute(name = "abbr")
	public final String getAbbreviation() {
		return abbreviation;
	}

	public final void setAbbreviation(String value) {
		abbreviation = value;
	}

	@XmlElementWrapper(name = "ValidFeatures")
	@XmlElement(name = "ValidFeature")
	public final List<ValidFeature> getValidFeatures() {
		return validFeatures;
	}

	public void setValidFeatures(List<ValidFeature> validFeatures) {
		this.validFeatures = validFeatures;
	}


	public FLExCategory() {
	}

	@Override
	public String toString() {
		return getAbbreviation();
	}

	@Override
	public int hashCode() {
		String sCombo = abbreviation + validFeatures.stream().hashCode();
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
		} else if (!getValidFeatures().equals(cat.getValidFeatures())) {
			result = false;
		}
		return result;
	}
}