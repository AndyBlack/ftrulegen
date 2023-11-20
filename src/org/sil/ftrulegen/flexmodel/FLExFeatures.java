/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 */
public class FLExFeatures extends FLExDataItem {

	private List<FLExFeature> features = new ArrayList<FLExFeature> ();
	public final List<FLExFeature> getFeatures()
	{
		return features;
	}
	public final void setFeatures(List<FLExFeature> value)
	{
		features = value;
	}

	public FLExFeatures()
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
				case "FLEXFeature":
					FLExFeature feat = new FLExFeature();
					feat.setName(subNode.getNodeValue());
					feat.createClassesFromXmlNode(subNode);
					features.add(feat);
					break;
				}
			}
		}
		
	}

}
