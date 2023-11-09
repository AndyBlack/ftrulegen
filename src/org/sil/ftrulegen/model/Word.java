/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlTransient;

public class Word extends ConstituentWithFeatures
{
	private List<Affix> Affixes = new ArrayList<Affix> ();
	public final List<Affix> getAffixes()
	{
		return Affixes;
	}
	public final void setAffixes(ArrayList<Affix> value)
	{
		Affixes = value;
	}

	@XmlAttribute(name="id")
	private String Id = "";
	public final String getId()
	{
		return Id;
	}
	public final void setId(String value)
	{
		Id = value;
	}

	@XmlAttribute(name="category")
	private String Category = "";
	public final String getCategory()
	{
		return Category;
	}
	public final void setCategory(String value)
	{
		Category = value;
	}

	@XmlAttribute(name="head")
	private HeadValue Head = HeadValue.no;
	public final HeadValue getHead()
	{
		return Head;
	}
	public final void setHead(HeadValue value)
	{
		Head = value;
	}

	@XmlTransient
	private Category CategoryConstituent;
	public final Category getCategoryConstituent()
	{
		return CategoryConstituent;
	}
	public final void setCategoryConstituent(Category value)
	{
		CategoryConstituent = value;
	}

	@Override
	public void setLocale(Locale value)
	{
		super.setLocale(value);
		for (Affix affix : Affixes)
		{
			affix.setLocale(value);
		}
		CategoryConstituent.setLocale(value);
	}

	public Word()
	{
		setCategoryConstituent(new Category(getCategory()));
	}

	public final void deleteCategory()
	{
		setCategory("");
		setCategoryConstituent(new Category(""));
	}

	public final void insertCategory(String cat)
	{
		setCategory(cat);
		if (getCategoryConstituent() != null)
		{
			getCategoryConstituent().setName(cat);
		}
		else
		{
			setCategoryConstituent(new Category(cat));
		}
	}

	public final void deleteAffixAt(int index)
	{
		if (index < 0 || index >= getAffixes().size())
		{
			return;
		}
		getAffixes().remove(index);
	}

	public final void insertAffixAt(Affix affix, int index)
	{
		if (index < 0 || (index > getAffixes().size() && getAffixes().size() > 0))
		{
			return;
		}
		getAffixes().add(index, affix);
	}

	public final void insertNewAffixAt(AffixType type, int index)
	{
		if (index < 0 || (index > getAffixes().size() && getAffixes().size() > 0))
		{
			return;
		}
		Affix newAffix = new Affix();
		newAffix.setType(type);
		getAffixes().add(index, newAffix);
	}

	public final void swapPositionOfAffixes(int index, int otherIndex)
	{
		if (index < 0 | index >= getAffixes().size())
		{
			return;
		}
		if (otherIndex < 0 | otherIndex >= getAffixes().size())
		{
			return;
		}
		Affix affix = getAffixes().get(index);
		Affix otherAffix = getAffixes().get(otherIndex);
		getAffixes().set(index, otherAffix);
		getAffixes().set(otherIndex, affix);
	}

	public final RuleConstituent findConstituent(int identifier)
	{
		RuleConstituent constituent = null;
		if (getIdentifier() == identifier)
		{
			return this;
		}
		constituent = getCategoryConstituent().findConstituent(identifier);
		if (constituent != null)
		{
			return constituent;
		}
		constituent = findConstituentInFeatures(identifier);
		if (constituent != null)
		{
			return constituent;
		}
		for (Affix affix : getAffixes())
		{
			constituent = affix.findConstituent(identifier);
			if (constituent != null)
			{
				return constituent;
			}
		}
		return constituent;
	}

	public final String produceHtml()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		sb.append(produceSpan("tf-nc", "w"));
		sb.append(bundle.getString("model.word"));
		if (getHead() == HeadValue.yes)
		{
			sb.append("(");
			sb.append("<span style=\"font-style:italic; font-size:smaller\">");
			sb.append(bundle.getString("model.head"));
			sb.append("</span>");
			sb.append(")");
		}
		if (getId().length() > 0)
		{
			sb.append("<span class=\"index\">");
			sb.append(getId());
			sb.append("</span></span>\n");
		}
		if (getCategory().length() > 0 || getFeatures().size() > 0 || getAffixes().size() > 0)
		{
			sb.append("<ul>\n");
			if (getCategory().length() > 0)
			{
				sb.append(getCategoryConstituent().produceHtml());
			}
			produceHtmlForFeatures(sb);
			for (Affix affix : getAffixes())
			{
				sb.append(affix.produceHtml());
			}
			sb.append("</ul>\n");
		}
		sb.append("</li>");
		return sb.toString();
	}

	public final Word duplicate()
	{
		Word newWord = new Word();
		newWord.setId(getId());
		newWord.setCategory(getCategory());
		newWord.setCategoryConstituent(getCategoryConstituent().duplicate());
		newWord.setHead(getHead());
		for (Affix affix : getAffixes())
		{
			newWord.getAffixes().add(affix.duplicate());
		}
		newWord.setFeatures(duplicateFeatures());
		return newWord;
	}
}