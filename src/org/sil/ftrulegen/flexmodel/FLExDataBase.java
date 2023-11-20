/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class FLExDataBase extends FLExDataItem
{
	private String Name = "";
	public final String getName()
	{
		return Name;
	}
	public final void setName(String value)
	{
		Name = value;
	}
	private FLExCategories categories = new FLExCategories();
	public final FLExCategories getCategories()
	{
		return categories;
	}
	public final void setCategories(FLExCategories value)
	{
		categories = value;
	}
	private FLExFeatures features = new FLExFeatures();
	public final FLExFeatures getFeatures()
	{
		return features;
	}
	public final void setFeatures(FLExFeatures value)
	{
		features = value;
	}
	
	public FLExDataBase()
	{
	}

	protected void createCommonClassesFromXmlNode(Node node) {
		NodeList list = node.getChildNodes();
		for (int i =0; i < list.getLength(); i++)
		{
			Node subNode = list.item(i);
			String nodeName = subNode.getLocalName();
			if (nodeName != null)
			{
				switch (nodeName) {
				case "name":
					setName(subNode.getNodeValue());
					break;
				case "Categories":
					categories = new FLExCategories();
					categories.createClassesFromXmlNode(subNode);
					break;					
				case "Features":
					features = new FLExFeatures();
					features.createClassesFromXmlNode(subNode);
					break;					
				}
			}
		}
	}

}