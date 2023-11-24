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

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;

/**
 * 
 */
public class MainController implements Initializable {

	@FXML
	Stage stage;
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
	@FXML
	ContextMenu ruleEditContextMenu = new ContextMenu();
	@FXML
	MenuItem cmRuleInsertBefore;
	@FXML
	MenuItem cmRuleInsertAfter;
	@FXML
	MenuItem cmRuleMoveUp;
	@FXML
	MenuItem cmRuleMoveDown;
	@FXML
	MenuItem cmRuleDelete;
	@FXML
	MenuItem cmRuleDuplicate;

	int selectedRuleIndex = -1;
	WebPageProducer producer = null;
	String webPageFileUri = "file:///C:/ProgramData/SIL/FLExTransRuleGenerator/FLExTransRule.html";
	String webPageFile = "C:\\ProgramData\\SIL\\FLExTransRuleGenerator\\FLExTransRule.html";
	boolean changesMade = false;
	ResourceBundle bundle;
	FLExTransRuleGenerator generator;
	XmlBackEndProvider provider;
	String ruleGenFile = "C:\\Users\\Andy Black\\Documents\\FieldWorks\\FLExTrans\\RuleGenerator\\AndyPlay\\ExampleRules.xml";

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

		bundle = resources;
		lvRules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FLExTransRule>() {
			@Override
			public void changed(ObservableValue<? extends FLExTransRule> observable, FLExTransRule oldValue, FLExTransRule newValue) {
				tfRuleName.setText(newValue.getName());
				selectedRuleIndex = lvRules.getItems().indexOf(newValue);
				produceAndShowWebPage(newValue, resources);
				enableDisableRuleContextMenuItems();
			}

			protected void produceAndShowWebPage(FLExTransRule newValue, ResourceBundle resources) {
				producer = WebPageProducer.getInstance();
				String html = producer.produceWebPage(newValue, resources);
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
			lvRules.getSelectionModel().getSelectedItem().setName(newValue);
			lvRules.refresh();
		});

		createContextMenuItems(resources);

//		Alert alert = new Alert(AlertType.INFORMATION, "content", ButtonType.OK);
//		alert.show();
		webEngine = browser.getEngine();
		webEngine.load(webPageFileUri);
		lblRightClickToEdit.setLayoutX(lblRules.getLayoutX() + 40);

		generator = new FLExTransRuleGenerator();
		provider = new XmlBackEndProvider(generator, new Locale ("en"));
		provider.loadDataFromFile(ruleGenFile);
		generator = provider.getRuleGenerator();
		lvRules.getItems().addAll(generator.getFLExTransRules());

		FLExData flexData = new FLExData();
		XMLFLExDataBackEndProvider flexProvider = new XMLFLExDataBackEndProvider(flexData, new Locale("en"));
		flexProvider.loadFLExDataFromFile("C:\\Users\\Andy Black\\Documents\\FieldWorks\\FLExTrans\\RuleGenerator\\AndyPlay\\FLExDataSpanFrench.xml");
		flexData = flexProvider.getFLExData();

		if (lvRules.getItems().size() > 0)
		{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
//					TODO: use application preference to remember and set last used index
					selectedRuleIndex = 0;
					lvRules.scrollTo(selectedRuleIndex);
					lvRules.getSelectionModel().select(selectedRuleIndex);
				}
			});
		}
	}

	void createContextMenuItems(ResourceBundle bundle)
	{
		cmRuleDelete = new MenuItem(bundle.getString("view.cmDelete"));
		cmRuleDelete.setOnAction((event) -> {
			handleRuleDelete();
		});
		cmRuleDuplicate = new MenuItem(bundle.getString("view.cmDuplicate"));
		cmRuleDuplicate.setOnAction((event) -> {
			handleRuleDuplicate();
		});
		cmRuleInsertAfter = new MenuItem(bundle.getString("view.cmInsertAfter"));
		cmRuleInsertAfter.setOnAction((event) -> {
			handleRuleInsertAfter();
		});
		cmRuleInsertBefore = new MenuItem(bundle.getString("view.cmInsertBefore"));
		cmRuleInsertBefore.setOnAction((event) -> {
			handleRuleInsertBefore();
		});
		cmRuleMoveDown = new MenuItem(bundle.getString("view.cmMoveDown"));
		cmRuleMoveDown.setOnAction((event) -> {
			handleRuleMoveDown();
		});
		cmRuleMoveUp = new MenuItem(bundle.getString("view.cmMoveUp"));
		cmRuleMoveUp.setOnAction((event) -> {
			handleRuleMoveUp();
		});
		ruleEditContextMenu.getItems().addAll(cmRuleDuplicate, cmRuleInsertBefore, cmRuleInsertAfter,
				new SeparatorMenuItem(), cmRuleMoveUp, cmRuleMoveDown, new SeparatorMenuItem(), cmRuleDelete);
		lvRules.setContextMenu(ruleEditContextMenu);

	}

	void enableDisableRuleContextMenuItems()
	{
		if (selectedRuleIndex == 0) {
			cmRuleMoveUp.setDisable(true);
		} else {
			cmRuleMoveUp.setDisable(false);
		}
		if (selectedRuleIndex == lvRules.getItems().size() - 1) {
			cmRuleMoveDown.setDisable(true);
		} else {
			cmRuleMoveDown.setDisable(false);
		}
		if (selectedRuleIndex == 0 && lvRules.getItems().size() == 1) {
			cmRuleDelete.setDisable(true);
		} else {
			cmRuleDelete.setDisable(false);
		}
	}

	public void handleRuleDelete()
	{
		generator.getFLExTransRules().remove(selectedRuleIndex);
		lvRules.getItems().remove(selectedRuleIndex);
		lvRules.requestFocus();
//			lvRules.getSelectionModel().select(iListViewindex);
//			lvRules.getFocusModel().focus(iListViewindex);
//			lvRules.scrollTo(iListViewindex);
		lvRules.refresh();
	}

	public void handleRuleDuplicate()
	{
		System.out.println("rule duplicate clicked");

	}
	public void handleRuleInsertAfter()
	{
		System.out.println("rule insert after clicked");

	}
	public void handleRuleInsertBefore()
	{
		System.out.println("rule insert before clicked");

	}
	public void handleRuleMoveDown()
	{
		System.out.println("rule move down clicked");

	}
	public void handleRuleMoveUp()
	{
		System.out.println(" rule move up clicked");

	}

	public void handleSave() {
		provider.saveDataToFile(ruleGenFile);
	}

	void markAsChanged(boolean changed)
	{
		changesMade = changed;
		showChangeStatusOnForm();
	}

	void showChangeStatusOnForm()
	{
		String title = bundle.getString("main.Title");
		if (changesMade)
			title += "*";
		stage.setTitle(title);
	}

	public void setStage(Stage stage)
	{
		this.stage = stage;
	}

}
