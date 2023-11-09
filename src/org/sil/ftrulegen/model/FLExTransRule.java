/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.Locale;

import jakarta.xml.bind.annotation.XmlAttribute;

public class FLExTransRule extends RuleConstituent
{
	private Source Source = new Source();
	public final Source getSource()
	{
		return Source;
	}
	
	public final void setSource(Source value)
	{
		Source = value;
	}

	private Target Target = new Target();
	public final Target getTarget()
	{
		return Target;
	}
	public final void setTarget(Target value)
	{
		Target = value;
	}

	@XmlAttribute(name="name")
	private String Name = "";
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}

	@Override
	public void setLocale(Locale value)
	{
		super.setLocale(value);
		Source.setLocale(value);
	}
	
	public FLExTransRule()
	{
	}

	public final FLExTransRule duplicate()
	{
		FLExTransRule newRule = new FLExTransRule();
		newRule.setName(getName());
		Source newSource = getSource().duplicate();
		newRule.setSource(newSource);
		Target newTarget = getTarget().duplicate();
		newRule.setTarget(newTarget);
		return newRule;
	}

	@Override
	public String toString()
	{
		String result = getName();
		if (getName().length() == 0)
		{
			result = bundle.getString("model.namemissing");
		}
		return result;
	}
}