/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.Locale;
import java.util.ResourceBundle;

import org.sil.ftrulegen.Constants;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class FLExTransRule extends RuleConstituent {
	private PermutationsValue createPermutations = PermutationsValue.no;

	@XmlAttribute(name = "create_permutations")
	public final PermutationsValue getPermutations() {
		return createPermutations;
	}

	public final void setPermutations(PermutationsValue value) {
		createPermutations = value;
	}

	private Source source = new Source();

	@XmlElement(name = "Source")
	public final Source getSource() {
		return source;
	}

	public final void setSource(Source value) {
		source = value;
	}

	private Target target = new Target();

	@XmlElement(name = "Target")
	public final Target getTarget() {
		return target;
	}

	public final void setTarget(Target value) {
		target = value;
	}

	private String ruleName = "";

	@XmlAttribute(name = "name")
	public final String getName() {
		return ruleName;
	}

	public final void setName(String value) {
		ruleName = value;
	}

	@Override
	public void setLocale(Locale value) {
		super.setLocale(value);
		source.setLocale(value);
	}

	public FLExTransRule() {
	}

	public final FLExTransRule duplicate() {
		FLExTransRule newRule = new FLExTransRule();
		newRule.setName(getName());
		Source newSource = getSource().duplicate();
		newRule.setSource(newSource);
		Target newTarget = getTarget().duplicate();
		newRule.setTarget(newTarget);
		// need to be sure to set this phrase as target
		newTarget.getPhrase().setType(PhraseType.target);
		newRule.setPermutations(getPermutations());
		return newRule;
	}

	@Override
	public String toString() {
		String result = getName();
		if (getName().length() == 0) {
			if (bundle == null) {
				bundle = ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, new Locale("en"));
			}
			result = bundle.getString("model.namemissing");
		}
		return result;
	}

	@Override
	public int hashCode() {
		String sCombo = ruleName + source.hashCode() + target.hashCode() + createPermutations.hashCode();
		return sCombo.hashCode();
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
		FLExTransRule rule = (FLExTransRule) obj;
		if (!ruleName.equals(rule.getName()))
			result = false;
		else if (!getSource().equals(rule.getSource())) {
			result = false;
		} else if (!getTarget().equals(rule.getTarget())) {
			result = false;
		} else if (!getPermutations().equals(rule.getPermutations())) {
			result = false;
		}
		return result;
	}

}