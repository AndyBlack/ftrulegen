/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * @author Andy Black
 *
 */
public class DisjointFeatureValuePairing {

	private StringProperty flexFeatureName;
	private StringProperty coFeatureValue;

	public DisjointFeatureValuePairing() {
		this.flexFeatureName = new SimpleStringProperty("");
		this.coFeatureValue = new SimpleStringProperty("");
	}

	@XmlAttribute(name = "flex_feature_name")
	public final String getFlexFeatureName() {
		return flexFeatureName.get();
	}

	public final void setFlexFeatureName(String value) {
		flexFeatureName.set(value);
	}

	public StringProperty FLExFeatureNameProperty() {
		return flexFeatureName;
	}

	@XmlAttribute(name = "co_feature_value")
	public final String getCoFeatureValue() {
		return coFeatureValue.get();
	}

	public final void setCoFeatureValue(String value) {
		coFeatureValue.set(value);
	}

	public StringProperty CoFeatureValueProperty() {
		return coFeatureValue;
	}

}
