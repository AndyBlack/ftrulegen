/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http: //www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import java.io.File;
import java.util.Locale;

import org.sil.ftrulegen.flexmodel.*;
import org.sil.utility.HandleExceptionMessage;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

public class XMLFLExDataBackEndProvider extends BackEndProvider {
	private FLExData flexData;

	public XMLFLExDataBackEndProvider(FLExData flexData, Locale locale) {
		this.flexData = flexData;
		setResourceStrings(locale);
	}

	public FLExData getFLExData() {
		return flexData;
	}

	public void setFLExData(FLExData value) {
		flexData = value;
	}

	public void loadFLExDataFromFile(String fileName) {
		try {
			File file = new File(fileName);
			JAXBContext context = JAXBContext.newInstance(FLExData.class);
			Unmarshaller um = context.createUnmarshaller();
			// Reading XML from the file and unmarshalling.
			flexData = (FLExData) um.unmarshal(file);
			flexData.setFeatureInFeatureValues();
		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
			HandleExceptionMessage.show(sFileError, sFileErrorLoadHeader, sFileErrorLoadContent + fileName, true);
		}
	}
}