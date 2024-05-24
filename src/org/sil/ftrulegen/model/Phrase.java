/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.model;


import java.util.*;

import org.sil.ftrulegen.flexmodel.FLExCategory;
import org.sil.ftrulegen.flexmodel.FLExFeature;
import org.sil.ftrulegen.flexmodel.FLExFeatureValue;
import org.sil.ftrulegen.flexmodel.ValidFeature;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlTransient;

public class Phrase extends RuleConstituent {

	private List<Word> words = new ArrayList<Word>();
	private List<FLExFeature> featuresInUse = new ArrayList<FLExFeature>();

	@XmlElementWrapper(name = "Words")
	@XmlElement(name = "Word")
	public final List<Word> getWords() {
		return words;
	}

	public final void setWords(List<Word> value) {
		words = value;
	}

	public List<FLExFeature> getFeaturesInUse() {
		featuresInUse.clear();
		for (Word w : words) {
			for (Feature feat : w.getFeatures()) {
				if (featureAlreadyInUse(feat) | isNewlyInsertedFeature(feat)) {
					continue;
				}
				List<FLExFeatureValue> values = new ArrayList<FLExFeatureValue>();
				FLExFeatureValue fv = new FLExFeatureValue(feat.getMatch());
				values.add(fv);
				FLExFeature existingFLExFeature = new FLExFeature(feat.getLabel(), values);
				existingFLExFeature.setFeatureInFeatureValues();
				featuresInUse.add(existingFLExFeature);
			}
		}
		return featuresInUse;
	}

	private boolean featureAlreadyInUse(Feature feat) {
		for (FLExFeature ff : featuresInUse) {
			if (ff.getName().equals(feat.getLabel())
					&& ff.getValues().get(0).getAbbreviation().equals(feat.getMatch())) {
				return true;
			}
		}
		return false;
	}

	public List<FLExFeature> getFeaturesInUseForCategory(List<FLExCategory> categories, Category cat) {
		featuresInUse.clear();
		for (Word w : words) {
			for (Feature feat : w.getFeatures()) {
				if (featureAlreadyInUse(feat) | isNewlyInsertedFeature(feat)) {
					continue;
				}
				Optional<FLExCategory> flexCatOp = categories.stream()
						.filter(c -> c.getAbbreviation().equals(cat.getName())).findFirst();
				List<ValidFeature> validFeatures = new ArrayList<ValidFeature>();
				if (flexCatOp.isPresent()) {
					FLExCategory flexCat = flexCatOp.get();
					validFeatures = flexCat.getValidFeatures();
				}
				for (ValidFeature vf : validFeatures) {
					Optional<FLExFeature> ffOp = featuresInUse.stream().filter(f -> f.getName().equals(vf.getName())).findFirst();
					if (!ffOp.isPresent()) {
						// only include valid features for the category
						continue;
					}
				}
				List<FLExFeatureValue> values = new ArrayList<FLExFeatureValue>();
				FLExFeatureValue fv = new FLExFeatureValue(feat.getMatch());
				values.add(fv);
				FLExFeature existingFLExFeature = new FLExFeature(feat.getLabel(), values);
				existingFLExFeature.setFeatureInFeatureValues();
				featuresInUse.add(existingFLExFeature);
			}
		}
		return featuresInUse;
	}

	private boolean isNewlyInsertedFeature(Feature feat) {
		if (feat.getLabel().equals("") && feat.getMatch().equals("")) {
			return true;
		}
		return false;
	}

	private PhraseType phraseType = PhraseType.source;

	@XmlTransient
	public final PhraseType getType() {
		return phraseType;
	}

	public final void setType(PhraseType value) {
		phraseType = value;
	}

	@Override
	public void setLocale(Locale value) {
		super.setLocale(value);
		for (Word word : words) {
			word.setLocale(value);
		}
	}

	public Phrase() {
	}

	public final void deleteWordAt(int index) {
		if (index < 0 || index >= getWords().size()) {
			return;
		}
		getWords().remove(index);
	}

	public final void insertNewWordAt(int index) {
		if (index < 0 || index > getWords().size()) {
			return;
		}
		Word newWord = new Word();
		newWord.setId(getIdOfNewlyAddedWord());
		getWords().add(index, newWord);
	}

	public final void insertWordAt(Word word, int index) {
		if (index < 0 || index > getWords().size()) {
			return;
		}
		getWords().add(index, word);
	}

	public String getIdOfNewlyAddedWord() {
		return Integer.toString(getWords().size() + 1);
	}

	public final void swapPositionOfWords(int index, int otherIndex) {
		if (index < 0 | index >= getWords().size()) {
			return;
		}
		if (otherIndex < 0 | otherIndex >= getWords().size()) {
			return;
		}
		Word word = getWords().get(index);
		Word otherWord = getWords().get(otherIndex);
		getWords().set(index, otherWord);
		getWords().set(otherIndex, word);
	}

	public final void changeIdOfWord(int index, String oldId, String newId) {
		if (index < 0 | index >= getWords().size()) {
			return;
		}
		Optional<Word> optional = getWords().stream().filter(w -> w.getId().equals(newId)).findFirst();
		if (optional.isPresent()) {
			Word otherWord = optional.get();
			int indexOther = getWords().indexOf(otherWord);
			getWords().get(indexOther).setId(oldId);
		}
		getWords().get(index).setId(newId);
	}

	public final void markWordAsHead(Word word) {
		int index = getWords().indexOf(word);
		if (index < 0 || index >= getWords().size()) {
			return;
		}
		for (Word w : getWords()) {
			if (w == word) {
				w.setHead(HeadValue.yes);
			} else {
				w.setHead(HeadValue.no);
			}
		}
	}

	public final String produceHtml(ResourceBundle bundle) {
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		sb.append(produceSpan("tf-nc", "p"));
		sb.append(bundle.getString("model.phrase"));
		sb.append("<span class=\"language\">");
		if (getType() == PhraseType.source) {
			sb.append(bundle.getString("model.src"));
		} else {
			sb.append(bundle.getString("model.tgt"));
		}
		sb.append("</span></span>\n");
		sb.append("<ul>");
		for (Word word : getWords()) {
			sb.append(word.produceHtml(bundle));
		}
		sb.append("</ul>");
		sb.append("</li>");
		return sb.toString();
	}

	public final RuleConstituent findConstituent(int identifier) {
		RuleConstituent constituent = null;
		if (getIdentifier() == identifier) {
			return this;
		}
		for (Word word : getWords()) {
			constituent = word.findConstituent(identifier);
			if (constituent != null) {
				return constituent;
			}
		}
		return constituent;
	}

	public final Phrase duplicate() {
		Phrase newPhrase = new Phrase();
		for (Word word : getWords()) {
			newPhrase.getWords().add(word.duplicate());
		}
		return newPhrase;
	}

	public Category getCategoryOfWordWithId(String wordId) {
		Category cat = null;
		RuleConstituent parent = getParent();
		if (parent instanceof FLExTransRule) {
			Phrase phrase = ((FLExTransRule) parent).getSource().getPhrase();
			Optional<Word> optWord = phrase.getWords().stream()
					.filter(w -> w.getId().equals(wordId)).findFirst();
			if (optWord.isPresent()) {
				Word word = optWord.get();
				return word.getCategoryConstituent();
			}
		}
		return cat;
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