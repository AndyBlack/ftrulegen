/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlTransient;

public class Phrase extends RuleConstituent
{

	private List<Word> words = new ArrayList<Word>();
	@XmlElementWrapper(name="Words")
	@XmlElement(name="Word")
	public final List<Word> getWords()
	{
		return words;
	}
	public final void setWords(List<Word> value)
	{
		words = value;
	}

	private PhraseType phraseType = PhraseType.source;
	@XmlTransient
	public final PhraseType getType()
	{
		return phraseType;
	}
	public final void setType(PhraseType value)
	{
		phraseType = value;
	}

	@Override
	public void setLocale(Locale value)
	{
		super.setLocale(value);
		for (Word word : words)
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

	public final String produceHtml(ResourceBundle bundle)
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
			sb.append(word.produceHtml(bundle));
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

	@Override
	public int hashCode() {
//		String sCombo = phraseType.hashCode() + words.stream().hashCode();
		return phraseType.hashCode() + words.stream().hashCode();
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
		Phrase phrase = (Phrase) obj;
		if (!phraseType.equals(phrase.getType()))
			result = false;
		else if (!getWords().equals(phrase.getWords()))
			result = false;
		return result;
	}
}