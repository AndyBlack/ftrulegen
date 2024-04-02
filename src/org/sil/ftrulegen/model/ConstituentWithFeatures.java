/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

public abstract class ConstituentWithFeatures extends RuleConstituent {

	private List<Feature> features = new ArrayList<Feature>();

	@XmlElementWrapper(name = "Features")
	@XmlElement(name = "Feature")
	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> value) {
		features = value;
	}

	@Override
	public void setLocale(Locale value) {
		super.setLocale(value);
		for (Feature feature : features) {
			feature.setLocale(value);
		}
	}

	public ConstituentWithFeatures() {
	}

	public final void deleteFeature(Feature feature) {
		getFeatures().remove(feature);
	}

	public final Feature insertNewFeature(String label, String match) {
		Feature feature = new Feature();
		feature.setLabel(label);
		feature.setMatch(match);
		getFeatures().add(feature);
		return feature;
	}

	protected final RuleConstituent findConstituentInFeatures(int identifier) {
		RuleConstituent constituent = null;
		for (Feature feature : getFeatures()) {
			constituent = feature.findConstituent(identifier);
			if (constituent != null) {
				return constituent;
			}
		}
		return constituent;
	}

	protected final void produceHtmlForFeatures(ResourceBundle bundle, StringBuilder sb, boolean isHead) {
		if (getFeatures().size() > 0) {
			sb.append("<li>");
			sb.append("<table class=\"tf-nc\">\n");
			for (Feature feature : getFeatures()) {
				sb.append ("<tr>\n");
				sb.append ("<td align=\"left\">");
				sb.append(feature.produceHtml(bundle, isHead));
				sb.append("</td>\n");
				sb.append("</tr>\n");
			}
			sb.append("</table>\n");
			sb.append("</li>\n");
		}
	}

	protected final ArrayList<Feature> duplicateFeatures() {
		ArrayList<Feature> newFeatures = new ArrayList<Feature>();
		for (Feature feature : getFeatures()) {
			newFeatures.add(feature.duplicate());
		}
		return newFeatures;
	}
}