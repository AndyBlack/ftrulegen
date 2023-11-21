/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Feature extends RuleConstituent
{
	private String featureMatch = "";
	@XmlAttribute(name="match")
	public final String getMatch()
	{
		return featureMatch;
	}
	public final void setMatch(String value)
	{
		featureMatch = value;
	}

	private String featureLabel = "";
	@XmlAttribute(name="label")
	public final String getLabel()
	{
		return featureLabel;
	}
	public final void setLabel(String value)
	{
		featureLabel = value;
	}

	public Feature()
	{
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
		if (getLabel().length() > 0 || getMatch().length() > 0)
		{
			sb.append("<li>");
			sb.append(produceSpan("tf-nc feature", "f"));
			sb.append((getLabel().length() > 0) ? getLabel() : bundle.getString("model.FeatureX"));
			sb.append(":");
			sb.append((getMatch().length() > 0) ? getMatch() : bundle.getString("model.MatchX"));
			sb.append("</span></li>\n");
		}
		return sb.toString();
	}

	public final Feature duplicate()
	{
		Feature newFeature = new Feature();
		newFeature.setMatch(getMatch());
		newFeature.setLabel(getLabel());
		return newFeature;
	}
}