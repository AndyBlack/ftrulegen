/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.Locale;

public class Source
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
	
	public Source()
	{
		getPhrase().setType(PhraseType.source);
	}

	public final Source duplicate()
	{
		Source newSource = new Source();
		newSource.setPhrase(getPhrase().duplicate());
		return newSource;
	}
}