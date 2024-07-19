/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import jakarta.xml.bind.annotation.XmlElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Andy Black
 *
 */
public class DisjointFeatures {

	private ObservableList<DisjointFeatureSet> disjointFeatureSets =  FXCollections.observableArrayList();

	@XmlElement(name = "DisjointFeatureSets")
	public ObservableList<DisjointFeatureSet> getDisjointFeatureSets() {
		return disjointFeatureSets;
	}

	public void setDisjointGenderFeatures(ObservableList<DisjointFeatureSet> value) {
		disjointFeatureSets = value;
	}

}
