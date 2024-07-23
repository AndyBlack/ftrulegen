/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FLExTransRuleGenerator")
public class FLExTransRuleGenerator {
	Locale locale;
	private List<FLExTransRule> flexTransRules = new ArrayList<FLExTransRule>();
	private ObservableList<DisjointFeatureSet> disjointFeatures = FXCollections.observableArrayList();

	public void setLocale(Locale value) {
		locale = value;
		for (FLExTransRule rule : flexTransRules) {
			rule.setLocale(value);
		}
	}

	@XmlElementWrapper(name = "FLExTransRules")
	@XmlElement(name = "FLExTransRule")
	public List<FLExTransRule> getFLExTransRules() {
		return flexTransRules;
	}

	public void setFLExTransRules(List<FLExTransRule> value) {
		flexTransRules = value;
	}

	@XmlElementWrapper(name = "DisjointFeatureSets")
	@XmlElement(name = "DisjointFeatureSet")
	public ObservableList<DisjointFeatureSet> getDisjointFeatures() {
		return disjointFeatures;
	}

	public void setDisjointFeatures(ObservableList<DisjointFeatureSet> value) {
		disjointFeatures = value;
	}

	public FLExTransRuleGenerator() {
	}
}