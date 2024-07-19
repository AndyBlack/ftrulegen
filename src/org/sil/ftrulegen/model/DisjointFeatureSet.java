/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * @author Andy Black
 *
 */
public class DisjointFeatureSet {

	private StringProperty name;
	private StringProperty splitOn;
	private DisjointFeatureSetKind setKind = DisjointFeatureSetKind.target;
	private final SimpleListProperty<String> valuesList;
	private final StringProperty valuesRepresentation;
	ObservableList<String> valuesAsListOfStrings = FXCollections.observableArrayList();

	public DisjointFeatureSet() {
		this.name = new SimpleStringProperty("");
		this.splitOn = new SimpleStringProperty("");
		this.valuesList = new SimpleListProperty<String>();
		this.valuesRepresentation = new SimpleStringProperty("");
	}

	@XmlAttribute(name = "DisjointName")
	public final String getName() {
		return name.get();
	}

	public final void setName(String value) {
		name.set(value);
	}

	public StringProperty nameProperty() {
		return name;
	}

	@XmlAttribute(name = "SplitOn")
	public final String getSplitOn() {
		return splitOn.get();
	}

	public final void setSplitOn(String value) {
		splitOn.set(value);;
	}

	public StringProperty splitOnProperty() {
		return splitOn;
	}

	@XmlAttribute(name = "kind")
	public final DisjointFeatureSetKind getKind() {
		return setKind;
	}

	public final void setKind(DisjointFeatureSetKind value) {
		setKind = value;
	}

	public SimpleListProperty<String> valuesListProperty() {
		return valuesList;
	}

	public String getValuesRepresentation() {
		return valuesRepresentation.get();
	}

	public StringProperty valuesRepresentationProperty() {
		return valuesRepresentation;
	}

	public void setValuesRepresentation(String sncRepresentation) {
		this.valuesRepresentation.set(sncRepresentation);
	}

	public ObservableList<String> getValuesAsListOfStrings() {
		return valuesAsListOfStrings;
	}

	public void setValuesAsListOfStrings(ObservableList<String> value) {
		this.valuesAsListOfStrings = value;
	}

	public SimpleListProperty<String> valuesAsListOfStringsProperty() {
		return valuesList;
	}

}
