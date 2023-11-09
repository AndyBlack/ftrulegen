/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;

public abstract class ConstituentWithFeatures extends RuleConstituent
{
	private List<Feature> Features = new ArrayList<Feature> ();
	public final List<Feature> getFeatures()
	{
		return Features;
	}
	public final void setFeatures(List<Feature> value)
	{
		Features = value;
	}

	@Override
	public void setLocale(Locale value)
	{
		super.setLocale(value);
		for (Feature feature : Features)
		{
			feature.setLocale(value);
		}
	}

	public ConstituentWithFeatures()
	{
	}

	public final void deleteFeature(Feature feature)
	{
		getFeatures().remove(feature);
	}

	public final Feature insertNewFeature(String label, String match)
	{
		Feature feature = new Feature();
		feature.setLabel(label);
		feature.setMatch(match);
		getFeatures().add(feature);
		return feature;
	}

	protected final RuleConstituent findConstituentInFeatures(int identifier)
	{
		RuleConstituent constituent = null;
		for (Feature feature : getFeatures())
		{
			constituent = feature.findConstituent(identifier);
			if (constituent != null)
			{
				return constituent;
			}
		}
		return constituent;
	}

	protected final void produceHtmlForFeatures(StringBuilder sb)
	{
		for (Feature feature : getFeatures())
		{
			sb.append(feature.produceHtml());
		}
	}

	protected final ArrayList<Feature> duplicateFeatures()
	{
		ArrayList<Feature> newFeatures = new ArrayList<Feature>();
		for (Feature feature : getFeatures())
		{
			newFeatures.add(feature.duplicate());
		}
		return newFeatures;
	}
}