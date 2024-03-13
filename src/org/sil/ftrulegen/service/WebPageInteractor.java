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
	String sItemClickedOn = "";
	int xCoord = 0;
	int yCoord = 0;

	public WebPageInteractor(MainController controller) {
		this.controller = controller;
	}

	public String getItemClickedOn() {
		return sItemClickedOn;
	}

	public void setItemClickedOn(String value) {
		sItemClickedOn = value;
		controller.processItemClickedOn(sItemClickedOn);
	}

	public int getXCoord() {
		return xCoord;
	}

	public void setXCoord(int value) {
		xCoord = value;
	}

	public int getYCoord() {
		return yCoord;
	}

	public void setYCoord(int value) {
		yCoord = value;
	}
}
