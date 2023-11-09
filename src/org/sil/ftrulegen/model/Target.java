/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.Locale;

public class Target
{
	private Phrase Phrase = new Phrase();
	public final Phrase getPhrase()
	{
		return Phrase;
	}
	public final void setPhrase(Phrase value)
	{
		Phrase = value;
	}

	public void setLocale(Locale value)
	{
		Phrase.setLocale(value);
	}

	public Target()
	{
		getPhrase().setType(PhraseType.target);
	}

	public final Target duplicate()
	{
		Target newTarget = new Target();
		newTarget.setPhrase(getPhrase().duplicate());
		return newTarget;
	}
}