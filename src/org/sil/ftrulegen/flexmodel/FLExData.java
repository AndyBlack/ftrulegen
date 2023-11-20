/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.flexmodel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "FLExData")
public class FLExData extends FLExDataItem
{
	private SourceFLExData sourceData = new SourceFLExData();
	@XmlElement(name = "SourceData")
	public final SourceFLExData getSourceData()
	{
		return sourceData;
	}
	public final void setSourceData(SourceFLExData value)
	{
		sourceData = value;
	}
	
	private TargetFLExData targetData = new TargetFLExData();
	@XmlElement(name = "TargetData")
	public final TargetFLExData getTargetData()
	{
		return targetData;
	}
	public final void setTargetData(TargetFLExData value)
	{
		targetData = value;
	}

	public FLExData()
	{
	}

	public void clear()
	{
		sourceData.clear();
		targetData.clear();
	}
	
	public void setFeatureInFeatureValues()
	{
		sourceData.setFeatureInFeatureValues();
		targetData.setFeatureInFeatureValues();
	}
	
	@Override
	public void createClassesFromXmlNode(Node node) {
//		NodeList list = node.getChildNodes();
//		for (int i =0; i < list.getLength(); i++)
//		{
//			Node subNode = list.item(i);
//			String nodeName = subNode.getLocalName();
//			if (nodeName != null)
//			{
//				switch (nodeName) {
//				case "SourceData":
//					sourceData = new SourceFLExData();
//					sourceData.createClassesFromXmlNode(subNode);
//					break;
//				case "TargetData":
//					targetData = new TargetFLExData();
//					targetData.createClassesFromXmlNode(subNode);
//					break;					
//				}
//			}
//		}
	}
}