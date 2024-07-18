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
public class DisjointFeatureSet {

	private String name = "";

	@XmlAttribute(name = "DisjointName")
	public final String getName() {
		return name;
	}

	public final void setName(String value) {
		name = value;
	}

	private String splitOn = "";

	@XmlAttribute(name = "SplitOn")
	public final String getSplitOn() {
		return splitOn;
	}

	public final void setSplitOn(String value) {
		splitOn = value;
	}

	private DisjointFeatureSetKind setKind = DisjointFeatureSetKind.target;

	@XmlAttribute(name = "kind")
	public final DisjointFeatureSetKind getKind() {
		return setKind;
	}

	public final void setKind(DisjointFeatureSetKind value) {
		setKind = value;
	}

}
