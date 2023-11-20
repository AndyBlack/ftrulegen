/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

module RuleGen {
	exports org.sil.ftrulegen;
	exports org.sil.ftrulegen.flexmodel;
	exports org.sil.ftrulegen.model;
	exports org.sil.ftrulegen.service;
	exports org.sil.ftrulegen.view;

//	opens org.sil.ftrulegen to javafx.graphics, javafx.fxml;
	opens org.sil.ftrulegen.view to javafx.fxml;
	opens org.sil.ftrulegen.view.fxml to javafx.fxml;

	// Java
	requires java.desktop;
	requires java.prefs;

	// JavaFX
	requires transitive javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.swing;
	requires javafx.web;
	

	// JAXB
	requires jakarta.xml.bind;
	requires jakarta.activation;
	opens org.sil.ftrulegen.model;
	opens org.sil.ftrulegen.flexmodel;

	// JUnit
	requires junit;
	
	// Other modules/libraries
	requires transitive libjavadev;
//	requires java.xml;
	
	requires javafx.base;
	requires javafx.media;
	requires java.base;
}
