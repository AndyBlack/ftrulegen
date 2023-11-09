/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "FLExTransRuleGenerator")
public class FLExTransRuleGenerator
{
	Locale locale;
	public final void setLocale(Locale value)
	{
		locale = value;
		for (FLExTransRule rule : FLExTransRules)
		{
			rule.setLocale(value);
		}
	}
	private List<FLExTransRule> FLExTransRules = new ArrayList<FLExTransRule> ();
	public final List<FLExTransRule> getFLExTransRules()
	{
		return FLExTransRules;
	}
	public final void setFLExTransRules(List<FLExTransRule> value)
	{
		FLExTransRules = value;
	}

	public FLExTransRuleGenerator()
	{
	}
}