/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.sil.ftrulegen.model.Category;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

public abstract class FLExDataBase {
	private String name = "";
	protected List<FLExCategory> categories = new ArrayList<FLExCategory>();
	protected List<FLExFeature> features = new ArrayList<FLExFeature>();
	protected int maxVariables = 4;
	protected String[] variables = { "α", "β", "γ", "δ", "ε", "ζ", "η", "θ", "ι", "κ", "μ", "ν" };

	public int getMaxVariables() {
		return maxVariables;
	}

	public void setMaxVariables(int maxVariables) {
		this.maxVariables = maxVariables;
	}

	public void addVariableValuesToFeatures() {
		for (FLExFeature feature : features) {
			for (int i = 0; i < maxVariables && i < variables.length; i++) {
				FLExFeatureValue variableValue = new FLExFeatureValue(variables[i]);
				variableValue.setFeature(feature);
				feature.getValues().add(variableValue);
			}
		}
	}

	@XmlAttribute(name = "name")
	public final String getName() {
		return name;
	}

	public final void setName(String value) {
		name = value;
	}

	/**
	 * @return the categories
	 */
	@XmlElementWrapper(name = "Categories")
	@XmlElement(name = "FLExCategory")
	public List<FLExCategory> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<FLExCategory> categories) {
		this.categories = categories;
	}

	/**
	 * @return the features
	 */
	@XmlElementWrapper(name = "Features")
	@XmlElement(name = "FLExFeature")
	public List<FLExFeature> getFeatures() {
		return features;
	}

	/**
	 * @param features the features to set
	 */
	public void setFeatures(List<FLExFeature> features, TargetFLExData targetFLExData) {
		this.features = features;
	}

	public void clear() {
		categories.clear();
		features.clear();
	}

	public void setFeatureInFeatureValues() {
		for (FLExFeature feat : features) {
			feat.setFeatureInFeatureValues();
		}
	}

	public FLExDataBase() {
	}

	public List<FLExFeature> getFeaturesForCategory(Category cat) {
		List<FLExFeature> featuresForCategory = new ArrayList<FLExFeature>();
		Optional<FLExCategory> catOp = categories.stream().filter(c -> c.getAbbreviation().equals(cat.getName())).findFirst();
		if (catOp.isPresent()) {
			FLExCategory flexCat = catOp.get();
			for (ValidFeature feat : flexCat.getValidFeatures()) {
				Optional<FLExFeature> featOp = features.stream().filter(f -> f.getName().equals(feat.getName())).findFirst();
				if (featOp.isPresent()) {
					FLExFeature flexFeat = featOp.get();
					featuresForCategory.add(flexFeat);
				}
			}
		}
		return featuresForCategory;
	}

	@Override
	public int hashCode() {
		String sCombo = getName() + categories.stream().hashCode() + features.stream().hashCode();
		return sCombo.hashCode();
	}

}