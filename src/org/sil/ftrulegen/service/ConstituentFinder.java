/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import org.sil.ftrulegen.model.*;

public class ConstituentFinder {
	private static final ConstituentFinder instance = new ConstituentFinder();

	public static ConstituentFinder getInstance() {
		return instance;
	}

	public final RuleConstituent findConstituent(FLExTransRule rule, int identifier) {
		RuleConstituent constituent = null;
		if (identifier < rule.getTarget().getPhrase().getIdentifier()) {
			constituent = rule.getSource().getPhrase().findConstituent(identifier);
			if (constituent != null) {
				return constituent;
			}
		} else {
			constituent = rule.getTarget().getPhrase().findConstituent(identifier);
		}
		return constituent;
	}
}