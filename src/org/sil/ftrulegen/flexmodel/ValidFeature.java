/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlTransient;

public class ValidFeature {
	private String name = "";

	@XmlAttribute(name = "name")
	public final String getName() {
		return name;
	}

	public final void setName(String value) {
		name = value;
	}

	private String sValidFeatureType = "stem";

	@XmlAttribute(name = "type")
	public final String getSType() {
		return sValidFeatureType;
	}

	public final void setSType(String value) {
		sValidFeatureType = value;
		switch (sValidFeatureType) {
		case "prefix":
			validFeatureType = ValidFeatureType.prefix;
			break;
		case "prefix|stem":
			validFeatureType = ValidFeatureType.prefixstem;
			break;
		case "prefix|stem|suffix":
			validFeatureType = ValidFeatureType.prefixstemsuffix;
			break;
		case "prefix|suffix":
			validFeatureType = ValidFeatureType.prefixsuffix;
			break;
		case "stem":
			validFeatureType = ValidFeatureType.stem;
			break;
		case "stem|suffix":
			validFeatureType = ValidFeatureType.stemsuffix;
			break;
		case "suffix":
			validFeatureType = ValidFeatureType.suffix;
			break;
		}
	}

	private ValidFeatureType validFeatureType = ValidFeatureType.stem;

	@XmlTransient
	public final ValidFeatureType getType() {
		return validFeatureType;
	}

	public final void setType(ValidFeatureType value) {
		validFeatureType = value;
	}

	public ValidFeature() {
	}

	public ValidFeature(String name, ValidFeatureType type) {
		super();
		this.name = name;
		this.validFeatureType = type;
	}

	@Override
	public String toString() {
		return getName() + getType();
	}

	@Override
	public int hashCode() {
		String sCombo = name + getType().getValue();
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
		ValidFeature feat = (ValidFeature) obj;
		if (!getName().equals(feat.getName())) {
			result = false;
		} else if (!getType().equals(feat.getType())) {
			result = false;
		}
		return result;
	}
}