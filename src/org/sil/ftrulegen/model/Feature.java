/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.ResourceBundle;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Feature extends RuleConstituent {
	private static final String ksFeatureClass = "feature";
	private String featureMatch = "";

	@XmlAttribute(name = "match")
	public final String getMatch() {
		return featureMatch;
	}

	public final void setMatch(String value) {
		featureMatch = value;
	}

	private String featureLabel = "";

	@XmlAttribute(name = "label")
	public final String getLabel() {
		return featureLabel;
	}

	public final void setLabel(String value) {
		featureLabel = value;
	}

	private String featureValue = "";

	@XmlAttribute(name = "value")
	public final String getValue() {
		return featureValue;
	}

	public final void setValue(String value) {
		featureValue = value;
	}

	private String featureDefault = "";

	@XmlAttribute(name = "unmarked_default")
	public final String getUnmarked() {
		return featureDefault;
	}

	public final void setUnmarked(String Default) {
		featureDefault = Default;
	}

	public Feature() {
	}

	public Phrase getPhrase() {
		Phrase phrase = null;
		Word word = null;
		RuleConstituent constituent = getParent();
		if (constituent instanceof Word) {
			word = (Word) constituent;
			phrase = (Phrase) word.getParent();
		} else if (constituent instanceof Affix) {
			Affix affix = (Affix) constituent;
			if (affix != null) {
				word = (Word) affix.getParent();
				if (word != null) {
					phrase = (Phrase) word.getParent();
				}
			}
		}
		return phrase;
	}

	public final RuleConstituent findConstituent(int identifier) {
		RuleConstituent constituent = null;
		if (getIdentifier() == identifier) {
			return this;
		}
		return constituent;
	}

	public String getMatchOrValue() {
		if (getMatch().length() > 0) {
			return getMatch();
		} else {
			return getValue();
		}
	}

	public final String produceHtml(ResourceBundle bundle, boolean isHead) {
		StringBuilder sb = new StringBuilder();
		String sClass = isHead ? ksFeatureClass + " headfeature" : ksFeatureClass;
		sb.append(produceSpan(sClass, "f"));
		sb.append((getLabel().length() > 0) ? getLabel() : bundle.getString("model.FeatureX"));
		sb.append(":");
		sb.append(getMatchOrValue());
		if (getUnmarked().length() > 0) {
			formatUnmarked(bundle, sb);
		}
		sb.append("</span>");
		return sb.toString();
	}

	protected void formatUnmarked(ResourceBundle bundle, StringBuilder sb) {
		sb.append("\n<span class=\"unmarked ");
		sb.append(ksFeatureClass);
		sb.append("\">");
		sb.append(bundle.getString("model.unmarked"));
		sb.append(":");
		sb.append(getUnmarked());
		sb.append("</span>");
	}

	public final Feature duplicate() {
		Feature newFeature = new Feature();
		newFeature.setLabel(getLabel());
		newFeature.setMatch(getMatch());
		newFeature.setLabel(getValue());
		newFeature.setUnmarked(getUnmarked());
		return newFeature;
	}
}
