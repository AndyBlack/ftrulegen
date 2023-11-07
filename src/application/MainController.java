/**
 * Copyright (c) 2021 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.ButtonType;

/**
 * 
 */
public class MainController implements Initializable {

	@FXML
	private WebView browser;
	@FXML
	private WebEngine webEngine;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Alert alert = new Alert(AlertType.INFORMATION, "content", ButtonType.OK);
//		alert.show();
		webEngine = browser.getEngine();
		webEngine.load("file:///C:/ProgramData/SIL/FLExTransRuleGenerator/FLExTransRule.html");
//		webEngine.loadContent("<html><body><div>Hi there!</div</body></html>");

	}

}
