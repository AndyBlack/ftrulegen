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
public class DisjointFeatureValue {

	private String featureValue = "";

	@XmlAttribute(name = "value")
	public final String getValue() {
		return featureValue;
	}

	public final void setValue(String value) {
		featureValue = value;
	}

}
