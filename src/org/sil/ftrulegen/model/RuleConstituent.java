/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.Locale;
import java.util.ResourceBundle;

import org.sil.ftrulegen.Constants;

import jakarta.xml.bind.annotation.XmlTransient;

public class RuleConstituent {
	@XmlTransient
	protected ResourceBundle bundle;

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	protected Locale locale;

	@XmlTransient
	public void setLocale(Locale value) {
		locale = value;
		bundle = ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, locale);
	}

	private int identifier;

	@XmlTransient
	public final int getIdentifier() {
		return identifier;
	}

	public final void setIdentifier(int value) {
		identifier = value;
	}

	private RuleConstituent parent;

	@XmlTransient
	public final RuleConstituent getParent() {
		return parent;
	}

	public final void setParent(RuleConstituent value) {
		parent = value;
	}

	public RuleConstituent() {
	}

	protected final String produceSpan(String sClass, String sType) {
		StringBuilder sb = new StringBuilder();
		sb.append("<span class=\"");
		sb.append(sClass);
		sb.append("\" id=\"");
		sb.append(sType);
		sb.append(".");
		sb.append(getIdentifier());
		sb.append("\" onclick=");
		sb.append(produceToApp(sType));
		sb.append(" oncontextmenu=");
		sb.append(produceToApp(sType));
		sb.append("\">");
		return sb.toString();
	}

	protected final String produceToApp(String sType) {
		StringBuilder sb = new StringBuilder();
		sb.append("\"toApp('");
		sb.append(sType);
		sb.append(".");
		sb.append(getIdentifier());
		sb.append("',event)\"");
		return sb.toString();
	}
}