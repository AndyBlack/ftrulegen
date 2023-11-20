/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.service;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 */
public abstract class BackEndProvider {

	protected String sFileError;
	protected String sFileErrorLoadHeader;
	protected String sFileErrorLoadContent;
	protected String sFileErrorSaveHeader;
	protected String sFileErrorSaveContent;

	protected void setResourceStrings(Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(org.sil.ftrulegen.Constants.RESOURCE_LOCATION, locale);
		sFileError = bundle.getString("file.error");
		sFileErrorLoadHeader = bundle.getString("file.error.load.header");
		sFileErrorLoadContent = bundle.getString("file.error.load.content");
		sFileErrorSaveHeader = bundle.getString("file.error.save.header");
		sFileErrorSaveContent = bundle.getString("file.error.save.content");
	}

}
