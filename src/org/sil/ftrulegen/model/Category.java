/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.ResourceBundle;

import jakarta.xml.bind.annotation.XmlTransient;

public class Category extends RuleConstituent {
	private String catName = "";

	@XmlTransient
	public final String getName() {
		return catName;
	}

	public final void setName(String value) {
		catName = value;
	}

	public Category() {
	}

	public Category(String name) {
		setName(name);
	}

	public Phrase getPhrase() {
		Phrase phrase = null;
		Word word = (Word) getParent();
		if (word != null) {
			phrase = (Phrase) word.getParent();
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

	public final String produceHtml(ResourceBundle bundle) {
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		sb.append(produceSpan("tf-nc category", "c"));
		sb.append(bundle.getString("model.cat"));
		sb.append(":");
		sb.append(getName());
		sb.append("</span></li>\n");
		return sb.toString();
	}

	public final Category duplicate() {
		Category newCat = new Category(getName());
		return newCat;
	}
}