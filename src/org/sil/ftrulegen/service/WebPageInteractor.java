/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import org.sil.ftrulegen.view.MainController;

/**
 * 
 */
public class WebPageInteractor {

	MainController controller;
	
	public WebPageInteractor(MainController controller)
	{
		this.controller = controller;		
	}
	String sItemClickedOn = "";
	public String getItemClickedOn() {
		return sItemClickedOn;
	}
	
	public void setItemClickedOn(String value) {
		sItemClickedOn = value;
		System.out.println("item clicked on:" + sItemClickedOn);
		controller.processItemClickedOn(sItemClickedOn);
	}
}
