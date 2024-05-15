/**
 * Copyright (c) 2023-2024 SIL International
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
		sb.append(produceSpan("category", "c"));
		produceHtmlCatValue(bundle, sb);
		return sb.toString();
	}

	public final String produceHtmlTarget(ResourceBundle bundle, int wordId) {
		StringBuilder sb = new StringBuilder();
		sb.append(produceSpanForTarget("categorytgt", "w", wordId));
		produceHtmlCatValue(bundle, sb);
		return sb.toString();
	}

	protected String produceSpanForTarget(String sClass, String sType, int wordId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<span class=\"");
		sb.append(sClass);
		sb.append("\" id=\"");
		sb.append(sType);
		sb.append(".");
		sb.append(wordId);
		sb.append("\" onclick=");
		sb.append("\"toApp('");
		sb.append(sType);
		sb.append(".");
		sb.append(wordId);
		sb.append("',event)\"");
		sb.append(">");
		return sb.toString();
	}

	protected void produceHtmlCatValue(ResourceBundle bundle, StringBuilder sb) {
		sb.append(bundle.getString("model.cat"));
		sb.append(":");
		sb.append(getName());
		sb.append("</span>\n");
	}

	public final Category duplicate() {
		Category newCat = new Category(getName());
		return newCat;
	}
}