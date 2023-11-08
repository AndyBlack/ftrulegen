/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

module RuleGen {
	exports org.sil.ftrulegen.view;

	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires libjavadev;
	
	opens org.sil.ftrulegen to javafx.graphics, javafx.fxml;
	opens org.sil.ftrulegen.view to javafx.fxml;
}
