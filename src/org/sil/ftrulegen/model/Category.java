/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import jakarta.xml.bind.annotation.XmlTransient;

public class Category extends RuleConstituent
{
	@XmlTransient
	private String Name = "";
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}

	public Category(String name)
	{
		setName(name);
	}

	public final RuleConstituent findConstituent(int identifier)
	{
		RuleConstituent constituent = null;
		if (getIdentifier() == identifier)
		{
			return this;
		}
		return constituent;
	}

	public final String produceHtml()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		sb.append(produceSpan("tf-nc category", "c"));
		sb.append(bundle.getString("model.cat"));
		sb.append(":");
		sb.append(getName());
		sb.append("</span></li>\n");
		return sb.toString();
	}

	public final Category duplicate()
	{
		Category newCat = new Category(getName());
		return newCat;
	}
}