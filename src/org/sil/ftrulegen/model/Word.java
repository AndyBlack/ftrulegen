/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;

import java.util.*;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlTransient;

public class Word extends ConstituentWithFeatures {

	private List<Affix> affixes = new ArrayList<Affix>();

	@XmlElementWrapper(name = "Affixes")
	@XmlElement(name = "Affix")
	public final List<Affix> getAffixes() {
		return affixes;
	}

	public final void setAffixes(List<Affix> value) {
		affixes = value;
	}

	private String wordId = "";

	@XmlAttribute(name = "id")
	public final String getId() {
		return wordId;
	}

	public final void setId(String value) {
		wordId = value;
	}

	private String wordCategory = "";

	@XmlAttribute(name = "category")
	public final String getCategory() {
		return wordCategory;
	}

	public final void setCategory(String value) {
		wordCategory = value;
	}

	private HeadValue wordHead = HeadValue.no;

	@XmlAttribute(name = "head")
	public final HeadValue getHead() {
		return wordHead;
	}

	public final void setHead(HeadValue value) {
		wordHead = value;
	}

	private Category CategoryConstituent;

	@XmlTransient
	public final Category getCategoryConstituent() {
		return CategoryConstituent;
	}

	public final void setCategoryConstituent(Category value) {
		CategoryConstituent = value;
	}

	@Override
	public void setLocale(Locale value) {
		super.setLocale(value);
		for (Affix affix : affixes) {
			affix.setLocale(value);
		}
		CategoryConstituent.setLocale(value);
	}

	public Word() {
		setCategoryConstituent(new Category(getCategory()));
	}

	public final void deleteCategory() {
		setCategory("");
		setCategoryConstituent(new Category(""));
	}

	public final void insertCategory(String cat) {
		setCategory(cat);
		if (getCategoryConstituent() != null) {
			getCategoryConstituent().setName(cat);
		} else {
			setCategoryConstituent(new Category(cat));
		}
	}

	public final void deleteAffixAt(int index) {
		if (index < 0 || index >= getAffixes().size()) {
			return;
		}
		getAffixes().remove(index);
	}

	public final void insertAffixAt(Affix affix, int index) {
		if (index < 0 || (index > getAffixes().size() && getAffixes().size() > 0)) {
			return;
		}
		getAffixes().add(index, affix);
	}

	public final void insertNewAffixAt(AffixType type, int index) {
		if (index < 0 || (index > getAffixes().size() && getAffixes().size() > 0)) {
			return;
		}
		Affix newAffix = new Affix();
		newAffix.setType(type);
		getAffixes().add(index, newAffix);
	}

	public final void swapPositionOfAffixes(int index, int otherIndex) {
		if (index < 0 | index >= getAffixes().size()) {
			return;
		}
		if (otherIndex < 0 | otherIndex >= getAffixes().size()) {
			return;
		}
		Affix affix = getAffixes().get(index);
		Affix otherAffix = getAffixes().get(otherIndex);
		getAffixes().set(index, otherAffix);
		getAffixes().set(otherIndex, affix);
	}

	public final RuleConstituent findConstituent(int identifier) {
		RuleConstituent constituent = null;
		if (getIdentifier() == identifier) {
			return this;
		}
		constituent = getCategoryConstituent().findConstituent(identifier);
		if (constituent != null) {
			return constituent;
		}
		constituent = findConstituentInFeatures(identifier);
		if (constituent != null) {
			return constituent;
		}
		for (Affix affix : getAffixes()) {
			constituent = affix.findConstituent(identifier);
			if (constituent != null) {
				return constituent;
			}
		}
		return constituent;
	}

	public Category getCategoryOfWordOrCorrespondingSourceWord() {
		Category result = CategoryConstituent;
		if (getParent() != null) {
			Phrase phrase = (Phrase) getParent();
			if (phrase != null) {
				Category cat = phrase.getCategoryOfWordWithId(wordId);
				if (cat != null && cat.getName().length() > 0) {
					result = cat;
				}
			}
		}
		return result;
	}

	public List<Feature> getAllFeaturesInWord() {
		List<Feature> features = new ArrayList<Feature>();
		features.addAll(getFeatures());
		for (Affix affix: getAffixes()) {
			features.addAll(affix.getFeatures());
		}
		return features;
	}

	public boolean hasMoreThanOneFeature() {
		int count = getFeatures().size();
		for (Affix affix : getAffixes()) {
			count += affix.getFeatures().size();
		}
		if (count > 1) {
			return true;
		}
		return false;
	}

	public boolean rankingIsAvailable(int ranking) {
		for (Feature feat : getFeatures()) {
			if (feat.getRanking() == ranking) {
				return false;
			}
		}
		for (Affix affix : getAffixes()) {
			for (Feature feat : affix.getFeatures()) {
				if (feat.getRanking() == ranking) {
					return false;
				}
			}
		}
		return true;
	}

	public final String produceHtml(ResourceBundle bundle) {
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		sb.append("<table class=\"tf-nc\">\n");
		sb.append ("<tr>\n");
		sb.append ("<td align=\"center\">");
		String sClass = wordHead == HeadValue.yes ? "headword" : "";
		sb.append(produceSpan(sClass, "w"));
		sb.append(bundle.getString("model.word"));
		if (getHead() == HeadValue.yes) {
			sb.append("(");
			sb.append("<span style=\"font-style:italic; font-size:smaller\">");
			sb.append(bundle.getString("model.head"));
			sb.append("</span>");
			sb.append(")");
		}
		if (getId().length() > 0) {
			sb.append("<span class=\"index\">");
			sb.append(getId());
			sb.append("</span>\n");
		}
		sb.append("</span>\n");
		sb.append("</td>\n");
		sb.append("</tr>\n");
		if (getCategory().length() > 0) {
			produceHtmlOfCategory(getCategoryConstituent(), bundle, sb, true);
		} else {
			Category cat = getCategoryOfWordOrCorrespondingSourceWord();
			if (cat != null && cat.getName().length() > 0) {
				produceHtmlOfCategory(cat, bundle, sb, false);
			}
		}
		sb.append("</table>\n");
		if (getFeatures().size() > 0 || getAffixes().size() > 0) {
			sb.append("<ul>\n");
			produceHtmlForFeatures(bundle, sb, wordHead == HeadValue.yes);
			for (Affix affix : getAffixes()) {
				sb.append(affix.produceHtml(bundle));
			}
			sb.append("</ul>\n");
		}
		sb.append("</li>");
		return sb.toString();
	}

	protected void produceHtmlOfCategory(Category cat, ResourceBundle bundle, StringBuilder sb, boolean isSource) {
		sb.append("<tr>\n");
		sb.append("<td align=\"center\">");
		if (isSource) {
			sb.append(cat.produceHtml(bundle));
		} else {
			sb.append(cat.produceHtmlTarget(bundle, getIdentifier()));
		}
		sb.append("</td>\n");
		sb.append("</tr>\n");
	}

	public final Word duplicate(boolean assignNewId) {
		Word newWord = new Word();
		String sId = getId();
		if (assignNewId) {
			RuleConstituent parent = getParent();
			if (parent instanceof Phrase) {
				Phrase phrase = (Phrase)parent;
				sId = phrase.getIdOfNewlyAddedWord();
			}
		}
		newWord.setId(sId);
		newWord.setCategory(getCategory());
		newWord.setCategoryConstituent(getCategoryConstituent().duplicate());
		newWord.setHead(getHead());
		for (Affix affix : getAffixes()) {
			newWord.getAffixes().add(affix.duplicate());
		}
		newWord.setFeatures(duplicateFeatures());
		return newWord;
	}

	@Override
	public int hashCode() {
//		String sCombo = wordCategory.hashCode() + wordId.hashCode();
		return wordCategory.hashCode() + affixes.stream().hashCode() + wordHead.hashCode() + wordId.hashCode();
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
		Word word = (Word) obj;
		if (!wordCategory.equals(word.getCategory()))
			result = false;
		else if (!wordHead.equals(word.getHead()))
			result = false;
		else if (!wordId.equals(word.getId()))
			result = false;
		else if (!getAffixes().equals(word.getAffixes()))
			result = false;
		return result;
	}
}