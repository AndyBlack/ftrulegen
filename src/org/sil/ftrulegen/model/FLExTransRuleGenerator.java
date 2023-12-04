/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FLExTransRuleGenerator")
public class FLExTransRuleGenerator {
	Locale locale;

	public void setLocale(Locale value) {
		locale = value;
		for (FLExTransRule rule : flexTransRules) {
			rule.setLocale(value);
		}
	}

	private List<FLExTransRule> flexTransRules = new ArrayList<FLExTransRule>();

	@XmlElementWrapper(name = "FLExTransRules")
	@XmlElement(name = "FLExTransRule")
	public List<FLExTransRule> getFLExTransRules() {
		return flexTransRules;
	}

	public void setFLExTransRules(List<FLExTransRule> value) {
		flexTransRules = value;
	}

	public FLExTransRuleGenerator() {
	}
}