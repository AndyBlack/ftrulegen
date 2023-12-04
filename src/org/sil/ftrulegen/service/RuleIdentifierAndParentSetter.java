/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import java.util.*;

import org.sil.ftrulegen.model.*;

public class RuleIdentifierAndParentSetter {
	private static final RuleIdentifierAndParentSetter instance = new RuleIdentifierAndParentSetter();

	private static int CurrentIdentifer = 0;

	public static int getCurrentIdentifer() {
		return CurrentIdentifer;
	}

	public static void setCurrentIdentifer(int value) {
		CurrentIdentifer = value;
	}

	public static RuleIdentifierAndParentSetter getInstance() {
		return instance;
	}

	public final void setIdentifiersAndParents(FLExTransRule rule) {
		setCurrentIdentifer(0);
		RuleIdentifierAndParentSetter idSetter = RuleIdentifierAndParentSetter.instance;
		setPhraseIdentifiers(rule.getSource().getPhrase());
		setPhraseIdentifiers(rule.getTarget().getPhrase());
		rule.getSource().getPhrase().setParent(rule);
		rule.getTarget().getPhrase().setParent(rule);
	}

	private static void setPhraseIdentifiers(Phrase phrase) {
		setCurrentIdentifer(getCurrentIdentifer() + 1);
		phrase.setIdentifier(getCurrentIdentifer());
		for (Word word : phrase.getWords()) {
			setCurrentIdentifer(getCurrentIdentifer() + 1);
			word.setIdentifier(getCurrentIdentifer());
			word.setParent(phrase);
			setCurrentIdentifer(getCurrentIdentifer() + 1);
			word.getCategoryConstituent().setIdentifier(getCurrentIdentifer());
			word.getCategoryConstituent().setParent(word);
			setFeatureIdentifiers(word.getFeatures(), word);
			for (Affix affix : word.getAffixes()) {
				setCurrentIdentifer(getCurrentIdentifer() + 1);
				affix.setIdentifier(getCurrentIdentifer());
				affix.setParent(word);
				setFeatureIdentifiers(affix.getFeatures(), affix);
			}
		}
	}

	private static void setFeatureIdentifiers(List<Feature> features, RuleConstituent parent) {
		for (Feature feature : features) {
			setCurrentIdentifer(getCurrentIdentifer() + 1);
			feature.setIdentifier(getCurrentIdentifer());
			feature.setParent(parent);
		}
	}
}