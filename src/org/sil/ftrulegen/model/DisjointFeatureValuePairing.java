/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * @author Andy Black
 *
 */
public class DisjointFeatureValuePairing {

	private String flexFeatureName = "";

	@XmlAttribute(name = "flexFeatureName")
	public final String getFlexFeatureName() {
		return flexFeatureName;
	}

	public final void setFlexFeatureNAme(String value) {
		flexFeatureName = value;
	}

	private String coFeatureValue = "";

	@XmlAttribute(name = "coFeatureValue")
	public final String getCoFeatureValue() {
		return coFeatureValue;
	}

	public final void setCoFeatureValue(String value) {
		coFeatureValue = value;
	}

}
