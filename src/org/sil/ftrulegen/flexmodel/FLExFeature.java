/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

public class FLExFeature {
	private String name = "";

	@XmlAttribute(name = "name")
	public final String getName() {
		return name;
	}

	public final void setName(String value) {
		name = value;
	}

	private List<FLExFeatureValue> values = new ArrayList<FLExFeatureValue>();

	@XmlElementWrapper(name = "Values")
	@XmlElement(name = "FLExFeatureValue")
	public final List<FLExFeatureValue> getValues() {
		return values;
	}

	public void setValues(List<FLExFeatureValue> values) {
		this.values = values;
	}

	public FLExFeature() {
	}

	public FLExFeature(String name, List<FLExFeatureValue> values) {
		super();
		this.name = name;
		this.values = values;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		String sCombo = name + values.stream().hashCode();
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
		FLExFeature feat = (FLExFeature) obj;
		if (!getName().equals(feat.getName())) {
			result = false;
		} else if (!getValues().equals(feat.getValues())) {
			result = false;
		}
		return result;
	}

	public void setFeatureInFeatureValues() {
		for (FLExFeatureValue val : values) {
			val.setFeature(this);
		}
	}
}