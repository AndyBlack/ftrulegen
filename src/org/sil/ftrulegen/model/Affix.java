/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Affix extends ConstituentWithFeatures
{
	private AffixType affixType = AffixType.suffix;
	@XmlAttribute(name="type")
	public final AffixType getType()
	{
		return affixType;
	}
	public final void setType(AffixType value)
	{
		affixType = value;
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

	@Override
	public int hashCode() {
//		String sCombo = wordCategory.hashCode() + wordId.hashCode();
		return affixType.hashCode() + getFeatures().stream().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		boolean result = true;
		Affix word = (Affix) obj;
		if (!affixType.equals(word.getType()))
			result = false;
		else if (!getFeatures().equals(word.getFeatures()))
			result = false;
		return result;
	}

}