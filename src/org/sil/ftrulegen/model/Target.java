/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.Locale;

import jakarta.xml.bind.annotation.XmlElement;

public class Target extends ConstituentWithPhrase {
	public Target() {
		getPhrase().setType(PhraseType.target);
	}

	public final Target duplicate() {
		Target newTarget = new Target();
		newTarget.setPhrase(getPhrase().duplicate());
		return newTarget;
	}
}