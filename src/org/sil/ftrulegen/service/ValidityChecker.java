/**
 * Copyright (c) 2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import java.util.ResourceBundle;

import org.sil.ftrulegen.model.Affix;
import org.sil.ftrulegen.model.FLExTransRule;
import org.sil.ftrulegen.model.HeadValue;
import org.sil.ftrulegen.model.Word;

/**
 * @author Andy Black
 *
 */
public class ValidityChecker {
	private static final ValidityChecker instance = new ValidityChecker();

	public static ValidityChecker getInstance() {
		return instance;
	}

	protected ResourceBundle bundle;
	protected FLExTransRule rule;

	public ValidityChecker() {
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public FLExTransRule getRule() {
		return rule;
	}

	public void setRule(FLExTransRule rule) {
		this.rule = rule;
	}

	public String getMessage(String key) {
		return bundle.getString(key);
	}
	
	public boolean checkSourceWordsHaveCategories() {
		for (Word word : rule.getSource().getPhrase().getWords()) {
			if (word.getCategory().length() == 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkTargetHasFeature() {
		for (Word word : rule.getTarget().getPhrase().getWords()) {
			if (word.getFeatures() != null && word.getFeatures().size() > 0) {
				return true;
			}
			for (Affix affix : word.getAffixes()) {
				if (affix.getFeatures() != null && affix.getFeatures().size() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkTargetWordMarkedAsHead() {
		for (Word word : rule.getTarget().getPhrase().getWords()) {
			if (word.getHead() == HeadValue.yes) {
				return true;
			}
		}
		return false;
	}
}
