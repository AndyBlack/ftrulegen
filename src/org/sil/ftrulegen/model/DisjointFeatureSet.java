/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.ResourceBundle;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * @author Andy Black
 *
 */
public class DisjointFeatureSet {

	private StringProperty name;
	private StringProperty coFeatureName;
	private StringProperty language;
	private PhraseType ptLanguage = PhraseType.target;
	private final SimpleListProperty<String> valuesList;
	private final StringProperty valuesRepresentation;
	ObservableList<String> valuesAsListOfStrings = FXCollections.observableArrayList();
	ObservableList<DisjointFeatureValuePairing> pairings = FXCollections.observableArrayList();
	ResourceBundle bundle;

	public DisjointFeatureSet() {
		this.name = new SimpleStringProperty("");
		this.language = new SimpleStringProperty("target");
		this.coFeatureName = new SimpleStringProperty("");
		this.valuesList = new SimpleListProperty<String>();
		this.valuesRepresentation = new SimpleStringProperty("");
	}

	@XmlAttribute(name = "disjointName")
	public final String getName() {
		return name.get();
	}

	public final void setName(String value) {
		name.set(value);
	}

	public StringProperty nameProperty() {
		return name;
	}

	@XmlAttribute(name = "coFeatureName")
	public final String getCoFeatureName() {
		return coFeatureName.get();
	}

	public final void setCoFeatureName(String value) {
		coFeatureName.set(value);;
	}

	public StringProperty CoFeatureNameProperty() {
		return coFeatureName;
	}

	@XmlAttribute(name = "language")
	public final PhraseType getLanguage() {
		return ptLanguage;
	}

	public final void setLanguage(PhraseType value) {
		ptLanguage = value;
		String sLanguage = "target";
		if (bundle != null) {
			sLanguage = (value == PhraseType.source) ? bundle.getString("disjoint.source") : bundle
					.getString("disjoint.target");
		}
		language.set(sLanguage);
	}

	public StringProperty LanguageProperty() {
		return language;
	}
	public SimpleListProperty<String> valuesListProperty() {
		return valuesList;
	}

	@XmlTransient
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
		valuesAsListOfStrings.clear();
		StringBuilder sb = new StringBuilder();
		for (DisjointFeatureValuePairing pairing : pairings) {
			sb.append(pairing.getFlexFeatureName());
			sb.append(" / ");
			sb.append(pairing.getCoFeatureValue());
			if (pairings.indexOf(pairing) < pairings.size()-1) {
				sb.append("\n");
			}
		}
		valuesAsListOfStrings.add(sb.toString());
		return valuesAsListOfStrings;
	}
	@XmlElementWrapper(name = "DisjointFeatureValuePairings")
	@XmlElement(name = "DisjointFeatureValuePairing")
	public ObservableList<DisjointFeatureValuePairing> getDisjointFeatureValuePairings() {
		return pairings;
	}

	public void setDisjointFeatureValuePairings(ObservableList<DisjointFeatureValuePairing> value) {
		this.pairings = value;
	}

	public SimpleListProperty<String> valuesAsListOfStringsProperty() {
		return valuesList;
	}

	@XmlTransient
	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

}
