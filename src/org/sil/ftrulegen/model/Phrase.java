/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;

import jakarta.xml.bind.annotation.XmlTransient;

public class Phrase extends RuleConstituent
{
	private ArrayList<Word> Words = new ArrayList<Word> ();
	public final ArrayList<Word> getWords()
	{
		return Words;
	}
	public final void setWords(ArrayList<Word> value)
	{
		Words = value;
	}

	@XmlTransient
	private PhraseType Type = PhraseType.source;
	public final PhraseType getType()
	{
		return Type;
	}
	public final void setType(PhraseType value)
	{
		Type = value;
	}

	@Override
	public void setLocale(Locale value)
	{
		super.setLocale(value);
		for (Word word : Words)
		{
			word.setLocale(value);
		}
	}

	public Phrase()
	{
	}

	public final void deleteWordAt(int index)
	{
		if (index < 0 || index >= getWords().size())
		{
			return;
		}
		getWords().remove(index);
	}

	public final void insertNewWordAt(int index)
	{
		if (index < 0 || index > getWords().size())
		{
			return;
		}
		Word newWord = new Word();
		newWord.setId(Integer.toString(getWords().size() + 1));
		getWords().add(index, newWord);
	}

	public final void insertWordAt(Word word, int index)
	{
		if (index < 0 || index >= getWords().size())
		{
			return;
		}
		getWords().add(index, word);
	}

	public final void swapPositionOfWords(int index, int otherIndex)
	{
		if (index < 0 | index >= getWords().size())
		{
			return;
		}
		if (otherIndex < 0 | otherIndex >= getWords().size())
		{
			return;
		}
		Word word = getWords().get(index);
		Word otherWord = getWords().get(otherIndex);
		getWords().set(index, otherWord);
		getWords().set(otherIndex, word);
	}

	public final void markWordAsHead(Word word)
	{
		int index = getWords().indexOf(word);
		if (index < 0 || index >= getWords().size())
		{
			return;
		}
		for (Word w : getWords())
		{
			if (w == word)
			{
				w.setHead(HeadValue.yes);
			}
			else
			{
				w.setHead(HeadValue.no);
			}
		}
	}

	public final String produceHtml()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		sb.append(produceSpan("tf-nc", "p"));
		sb.append(bundle.getString("model.phrase"));
		sb.append("<span class=\"language\">");
		if (getType() == PhraseType.source)
		{
			sb.append(bundle.getString("model.src"));
		}
		else
		{
			sb.append(bundle.getString("model.tgt"));
		}
		sb.append("</span></span>\n");
		sb.append("<ul>");
		for (Word word : getWords())
		{
			sb.append(word.produceHtml());
		}
		sb.append("</ul>");
		sb.append("</li>");
		return sb.toString();
	}

	public final RuleConstituent findConstituent(int identifier)
	{
		RuleConstituent constituent = null;
		if (getIdentifier() == identifier)
		{
			return this;
		}
		for (Word word : getWords())
		{
			constituent = word.findConstituent(identifier);
			if (constituent != null)
			{
				return constituent;
			}
		}
		return constituent;
	}

	public final Phrase duplicate()
	{
		Phrase newPhrase = new Phrase();
		for (Word word : getWords())
		{
			newPhrase.getWords().add(word.duplicate());
		}
		return newPhrase;
	}
}