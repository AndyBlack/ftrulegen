/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FLExFeatureValues extends FLExDataItem
{
	private List<FLExFeatureValue> values = new ArrayList<FLExFeatureValue> ();
	public final List<FLExFeatureValue> getValues()
	{
		return values;
	}
	public final void setValues(List<FLExFeatureValue> value)
	{
		values = value;
	}

	private FLExFeature parentFeature;
	public final FLExFeature getParentFeature()
	{
		return parentFeature;
	}
	public void setParentFeature(FLExFeature feature)
	{
		parentFeature = feature;
	}
	
	public FLExFeatureValues()
	{
	}
	
	@Override
	public void createClassesFromXmlNode(Node node) {
		NodeList list = node.getChildNodes();
		for (int i =0; i < list.getLength(); i++)
		{
			Node subNode = list.item(i);
			String nodeName = subNode.getLocalName();
			if (nodeName != null)
			{
				switch (nodeName) {
				case "FLExFeatureValue":
					FLExFeatureValue val = new FLExFeatureValue();
					val.setAbbreviation(subNode.getNodeValue());
					val.setFeature(parentFeature);
					values.add(val);
					break;
				}
			}
		}
	}
}