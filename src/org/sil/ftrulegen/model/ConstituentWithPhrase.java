/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import jakarta.xml.bind.annotation.XmlElement;

/**
 * 
 */
public class ConstituentWithPhrase  extends RuleConstituent 
{

	protected Phrase phrase = new Phrase();

	@XmlElement(name = "Phrase")
	public Phrase getPhrase() {
		return phrase;
	}

	public void setPhrase(Phrase value) {
		phrase = value;
	}

}
