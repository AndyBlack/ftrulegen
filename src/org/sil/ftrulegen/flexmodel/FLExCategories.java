/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FLExCategories extends FLExDataItem
{
	private List<FLExCategory> categories = new ArrayList<FLExCategory> ();
	public final List<FLExCategory> getCategories()
	{
		return categories;
	}
	public final void setCategories(List<FLExCategory> value)
	{
		categories = value;
	}

	public FLExCategories()
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
				case "FLEXCategory":
					FLExCategory cat = new FLExCategory();
					cat.setAbbreviation(subNode.getNodeValue());
					categories.add(cat);
					break;
				}
			}
		}
	}
}