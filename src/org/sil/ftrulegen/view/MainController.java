/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.view;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.ResourceBundle;

import org.sil.ftrulegen.*;
import org.sil.ftrulegen.flexmodel.FLExData;
import org.sil.ftrulegen.model.FLExTransRule;
import org.sil.ftrulegen.model.FLExTransRuleGenerator;
import org.sil.ftrulegen.service.WebPageProducer;
import org.sil.ftrulegen.service.XMLFLExDataBackEndProvider;
import org.sil.ftrulegen.service.XmlBackEndProvider;
import org.sil.utility.HandleExceptionMessage;
import org.sil.utility.view.ObservableResourceFactory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * 
 */
public class MainController implements Initializable {

	@FXML
	private WebView browser;
	@FXML
	private WebEngine webEngine;
	@FXML
	private Label lblRules;
	@FXML
	private Label lblRightClickToEdit;
	@FXML
	TextField tfRuleName;
	@FXML
	ListView<FLExTransRule> lvRules;

	FLExTransRule currentRule = null;
	WebPageProducer producer = null;
	String webPageFileUri = "file:///C:/ProgramData/SIL/FLExTransRuleGenerator/FLExTransRule.html";
	String webPageFile = "C:\\ProgramData\\SIL\\FLExTransRuleGenerator\\FLExTransRule.html";

	// following lines from
	// https://stackoverflow.com/questions/32464974/javafx-change-application-language-on-the-run
	private static final ObservableResourceFactory RESOURCE_FACTORY = ObservableResourceFactory
			.getInstance();
	static {
		RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(Constants.RESOURCE_LOCATION,
				new Locale("en")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lvRules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FLExTransRule>() {
			@Override
			public void changed(ObservableValue<? extends FLExTransRule> observable, FLExTransRule oldValue, FLExTransRule newValue) {
				tfRuleName.setText(newValue.getName());
				currentRule = newValue;
				produceAndShowWebPage(newValue, resources);
			}

			protected void produceAndShowWebPage(FLExTransRule newValue, ResourceBundle resources) {
				producer = WebPageProducer.getInstance();
				String html =producer.produceWebPage(newValue, resources);
				try {
					Files.write(Paths.get(webPageFile), html.getBytes(Charset.forName("UTF-8")));
					webEngine.load(webPageFileUri);
				} catch (IOException e) {
					e.printStackTrace();
					HandleExceptionMessage.show(resources.getString("file.error"), resources.getString("file.error.load.header"),
							resources.getString("file.error.load.content")
							+ webPageFile, true);
				}
			}
		});

		tfRuleName.textProperty().addListener((observable, oldValue, newValue) -> {
			if (currentRule != null) {
				currentRule.setName(tfRuleName.getText());
			}
		});


//		Alert alert = new Alert(AlertType.INFORMATION, "content", ButtonType.OK);
//		alert.show();
		webEngine = browser.getEngine();
		webEngine.load(webPageFileUri);
		lblRightClickToEdit.setLayoutX(lblRules.getLayoutX() + 40);

		FLExTransRuleGenerator generator = new FLExTransRuleGenerator();
		XmlBackEndProvider provider = new XmlBackEndProvider(generator, new Locale ("en"));
		provider.loadDataFromFile("C:\\Users\\Andy Black\\Documents\\FieldWorks\\FLExTrans\\RuleGenerator\\AndyPlay\\ExampleRules.xml");
		generator = provider.getRuleGenerator();
		lvRules.getItems().addAll(generator.getFLExTransRules());

		FLExData flexData = new FLExData();
		XMLFLExDataBackEndProvider flexProvider = new XMLFLExDataBackEndProvider(flexData, new Locale("en"));
		flexProvider.loadFLExDataFromFile("C:\\Users\\Andy Black\\Documents\\FieldWorks\\FLExTrans\\RuleGenerator\\AndyPlay\\FLExDataSpanFrench.xml");
		flexData = flexProvider.getFLExData();

		if (currentRule == null && lvRules.getItems().size() > 0)
		{
//			TODO: use application preference to remember and set last used index
			lvRules.getSelectionModel().select(0);
		}
	}

}
