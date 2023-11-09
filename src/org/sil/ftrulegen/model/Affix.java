/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.Locale;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Affix extends ConstituentWithFeatures
{
	@XmlAttribute(name="type")
	private AffixType Type = AffixType.suffix;
	public final AffixType getType()
	{
		return Type;
	}
	public final void setType(AffixType value)
	{
		Type = value;
	}

	public Affix()
	{
	}

	public final RuleConstituent findConstituent(int identifier)
	{
		RuleConstituent constituent = null;
		if (getIdentifier() == identifier)
		{
			return this;
		}
		constituent = findConstituentInFeatures(identifier);
		return constituent;
	}

	public final String produceHtml()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		sb.append(produceSpan("tf-nc affix", "a"));
		sb.append((getType() == AffixType.prefix) ? bundle.getString("model.prefix") : bundle.getString("model.suffix"));
		sb.append("</span>");
		if (getFeatures().size() > 0)
		{
			sb.append("<ul>");
			produceHtmlForFeatures(sb);
			sb.append("</ul>");
		}
		sb.append("</li>\n");
		return sb.toString();
	}

	public final Affix duplicate()
	{
		Affix newAffix = new Affix();
		newAffix.setType(getType());
		newAffix.setFeatures(duplicateFeatures());
		return newAffix;
	}
}