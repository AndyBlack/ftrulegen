/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

public class SourceFLExData // extends FLExDataBase
{
	private String name = "";
	@XmlAttribute(name = "name")
	public final String getName()
	{
		return name;
	}
	public final void setName(String value)
	{
		name = value;
	}

	private List<FLExCategory> categories = new ArrayList<FLExCategory>();
	private List<FLExFeature> features = new ArrayList<FLExFeature>();

	public SourceFLExData()
	{
	}

	public SourceFLExData(String name, List<FLExCategory> categories, List<FLExFeature> features) {
		super();
		this.name = name;
		this.categories = categories;
		this.features = features;
	}

	public SourceFLExData(List<FLExCategory> categories) {
		super();
		this.categories = categories;
	}

	public SourceFLExData(String name, List<FLExCategory> categories) {
		super();
		this.name = name;
		this.categories = categories;
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
	public void setFeatures(List<FLExFeature> features) {
		this.features = features;
	}

	public void clear()
	{
		categories.clear();
		features.clear();
	}

	public void load(SourceFLExData sourceFlexData) {
		this.categories = sourceFlexData.getCategories();
	}
	
	public void setFeatureInFeatureValues()
	{
		for (FLExFeature feat : features)
		{
			feat.setFeatureInFeatureValues();
		}
	}

	
	@Override
	public int hashCode() {
		String sCombo = name + categories.stream().hashCode() + features.stream().hashCode();
		return sCombo.hashCode();
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
		SourceFLExData flexData = (SourceFLExData) obj;
		if (!getName().equals(flexData.getName())) {
			result = false;
		} else if (!getCategories().equals(flexData.getCategories()))
		{
			result = false;
		} else if (!getFeatures().equals(flexData.getFeatures()))
		{
			return false;
		}
		return result;
	}


//	@Override
//	public void createClassesFromXmlNode(Node node) {
//		super.createCommonClassesFromXmlNode(node);
//	}
}