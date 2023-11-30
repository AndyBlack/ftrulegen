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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import netscape.javascript.JSObject;

import org.sil.ftrulegen.*;
import org.sil.ftrulegen.flexmodel.FLExCategory;
import org.sil.ftrulegen.flexmodel.FLExData;
import org.sil.ftrulegen.flexmodel.FLExFeature;
import org.sil.ftrulegen.flexmodel.FLExFeatureValue;
import org.sil.ftrulegen.model.Affix;
import org.sil.ftrulegen.model.AffixType;
import org.sil.ftrulegen.model.Category;
import org.sil.ftrulegen.model.FLExTransRule;
import org.sil.ftrulegen.model.FLExTransRuleGenerator;
import org.sil.ftrulegen.model.Feature;
import org.sil.ftrulegen.model.Phrase;
import org.sil.ftrulegen.model.RuleConstituent;
import org.sil.ftrulegen.model.Word;
import org.sil.ftrulegen.service.ConstituentFinder;
import org.sil.ftrulegen.service.WebPageInteractor;
import org.sil.ftrulegen.service.WebPageProducer;
import org.sil.ftrulegen.service.XMLFLExDataBackEndProvider;
import org.sil.ftrulegen.service.XmlBackEndProvider;
import org.sil.utility.HandleExceptionMessage;
import org.sil.utility.view.ControllerUtilities;
import org.sil.utility.view.ObservableResourceFactory;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

	ContextMenu affixEditContextMenu = new ContextMenu();
	MenuItem cmAffixDuplicate;
	MenuItem cmAffixToggleAffixType;
	MenuItem cmAffixInsertPrefixBefore;
	MenuItem cmAffixInsertPrefixAfter;
	MenuItem cmAffixInsertSuffixBefore;
	MenuItem cmAffixInsertSuffixAfter;
	MenuItem cmAffixMoveLeft;
	MenuItem cmAffixMoveRight;
	MenuItem cmAffixDelete;
	MenuItem cmAffixInsertFeature;

	ContextMenu categoryEditContextMenu = new ContextMenu();
	MenuItem cmCategoryEdit;
	MenuItem cmCategoryDelete;

	ContextMenu featureEditContextMenu = new ContextMenu();
	MenuItem cmFeatureEdit;
	MenuItem cmFeatureDelete;

	ContextMenu ruleEditContextMenu = new ContextMenu();
	MenuItem cmRuleInsertBefore;
	MenuItem cmRuleInsertAfter;
	MenuItem cmRuleMoveUp;
	MenuItem cmRuleMoveDown;
	MenuItem cmRuleDelete;
	MenuItem cmRuleDuplicate;

	ContextMenu wordEditContextMenu = new ContextMenu();
	MenuItem cmWordDuplicate;
	MenuItem cmWordInsertBefore;
	MenuItem cmWordInsertAfter;
	MenuItem cmWordMoveLeft;
	MenuItem cmWordMoveRight;
	MenuItem cmWordDelete;
	MenuItem cmWordInsertPrefix;
	MenuItem cmWordInsertSuffix;
	MenuItem cmWordInsertCategory;
	MenuItem cmWordInsertFeature;
	MenuItem cmWordMarkAsHead;
	MenuItem cmWordRemoveHeadMarking;

	int selectedRuleIndex = -1;
	WebPageProducer producer = null;
	String webPageFileUri = "file:///C:/ProgramData/SIL/FLExTransRuleGenerator/FLExTransRule.html";
	String webPageFile = "C:\\ProgramData\\SIL\\FLExTransRuleGenerator\\FLExTransRule.html";
	boolean changesMade = false;
	ResourceBundle bundle;
	FLExTransRuleGenerator generator;
	XmlBackEndProvider provider;
	WebPageInteractor webPageInteractor;
	ConstituentFinder finder;
	String ruleGenFile = "C:\\Users\\Andy Black\\Documents\\FieldWorks\\FLExTrans\\RuleGenerator\\AndyPlay\\ExampleRules.xml";
	String flexDataFile = "C:\\Users\\Andy Black\\Documents\\FieldWorks\\FLExTrans\\RuleGenerator\\AndyPlay\\FLExDataSpanFrench.xml";
	FLExData flexData;

	Affix affix;
	Category category;
	Feature feature;
	Phrase phrase;
	Word word;

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
				produceAndShowWebPage(newValue);
				enableDisableRuleContextMenuItems();
			}


		});

		tfRuleName.textProperty().addListener((observable, oldValue, newValue) -> {
			lvRules.getSelectionModel().getSelectedItem().setName(newValue);
			lvRules.refresh();
		});

		webEngine = browser.getEngine();
//		webEngine.setOnAlert(event -> showAlert(event.getData()));
//		webPageInteractor = new WebPageInteractor(language, webEngine, this);
		webPageInteractor = new WebPageInteractor(this);
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							// TODO: is there a way to associate the Java code
							// with the
							// javascript code *before* the page is loaded?
							// We are working around it by sleeping for 10ms in
							// the
							// javascript onload() function.
							JSObject win = (JSObject) webEngine.executeScript("window");
							win.setMember("ftRuleGenApp", webPageInteractor);
//							webEngine.executeScript("Initialize('" + getCurrentLocaleCode() + "')");
//							updatePageLabels();
//							// Timeline timeline = new Timeline(new
//							// KeyFrame(Duration.millis(500), event -> {
//							// handleRefresh();
//							// }));
//							// timeline.play();
						}
					});
				}
			}
		});

		createContextMenuItems();

//		Alert alert = new Alert(AlertType.INFORMATION, "content", ButtonType.OK);
//		alert.show();
		webEngine.load(webPageFileUri);
		lblRightClickToEdit.setLayoutX(lblRules.getLayoutX() + 40);

		generator = new FLExTransRuleGenerator();
		provider = new XmlBackEndProvider(generator, bundle.getLocale());
		provider.loadDataFromFile(ruleGenFile);
		generator = provider.getRuleGenerator();
		finder = ConstituentFinder.getInstance();
		lvRules.getItems().addAll(generator.getFLExTransRules());

		flexData = new FLExData();
		XMLFLExDataBackEndProvider flexProvider = new XMLFLExDataBackEndProvider(flexData, bundle.getLocale());
		flexProvider.loadFLExDataFromFile(flexDataFile);
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
	protected void produceAndShowWebPage(FLExTransRule newValue) {
		producer = WebPageProducer.getInstance();
		String html = producer.produceWebPage(newValue, bundle);
		try {
			Files.write(Paths.get(webPageFile), html.getBytes(Charset.forName("UTF-8")));
			webEngine.load(webPageFileUri);
		} catch (IOException e) {
			e.printStackTrace();
			HandleExceptionMessage.show(bundle.getString("file.error"), bundle.getString("file.error.load.header"),
					bundle.getString("file.error.load.content")
					+ webPageFile, true);
		}
	}

	void createContextMenuItems()
	{
		createAffixContextMenuItems();
		createCategoryContextMenuItems();
		createFeatureContextMenuItems();
		createRuleContextMenuItems();
		createWordContextMenuItems();
	}

	protected void createRuleContextMenuItems() {
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

	void createAffixContextMenuItems() {
		cmAffixDuplicate = new MenuItem(bundle.getString("view.cmDuplicate"));
		cmAffixDuplicate.setOnAction((event) -> {
			handleAffixDuplicate();
		});
		cmAffixToggleAffixType = new MenuItem(bundle.getString("view.cmToggleAffixType"));
		cmAffixToggleAffixType.setOnAction((event) -> {
			handleAffixToggleAffixType();
		});
		cmAffixInsertPrefixBefore = new MenuItem(bundle.getString("view.cmInsertPrefixBefore"));
		cmAffixInsertPrefixBefore.setOnAction((event) -> {
			handleAffixInsertPrefixBefore();
		});
		cmAffixInsertPrefixAfter = new MenuItem(bundle.getString("view.cmInsertPrefixAfter"));
		cmAffixInsertPrefixAfter.setOnAction((event) -> {
			handleAffixInsertPrefixAfter();
		});
		cmAffixInsertSuffixBefore = new MenuItem(bundle.getString("view.cmInsertSuffixBefore"));
		cmAffixInsertSuffixBefore.setOnAction((event) -> {
			handleAffixInsertSuffixBefore();
		});
		cmAffixInsertSuffixAfter = new MenuItem(bundle.getString("view.cmInsertSuffixAfter"));
		cmAffixInsertSuffixAfter.setOnAction((event) -> {
			handleAffixInsertSuffixAfter();
		});
		cmAffixMoveLeft = new MenuItem(bundle.getString("view.cmMoveLeft"));
		cmAffixMoveLeft.setOnAction((event) -> {
			handleAffixMoveLeft();
		});
		cmAffixMoveRight = new MenuItem(bundle.getString("view.cmMoveRight"));
		cmAffixMoveRight.setOnAction((event) -> {
			handleAffixMoveRight();
		});
		cmAffixDelete = new MenuItem(bundle.getString("view.cmDelete"));
		cmAffixDelete.setOnAction((event) -> {
			handleAffixDelete();
		});
		cmAffixInsertFeature = new MenuItem(bundle.getString("view.cmInsertFeature"));
		cmAffixInsertFeature.setOnAction((event) -> {
			handleAffixInsertFeature();
		});
		affixEditContextMenu.getItems().addAll(cmAffixDuplicate, new SeparatorMenuItem(), cmAffixToggleAffixType,
				new SeparatorMenuItem(), cmAffixInsertFeature, new SeparatorMenuItem(), cmAffixInsertPrefixBefore,
				cmAffixInsertPrefixAfter, cmAffixInsertSuffixBefore, cmAffixInsertSuffixAfter, new SeparatorMenuItem(),
				cmAffixMoveLeft, cmAffixMoveRight, new SeparatorMenuItem(), cmAffixDelete);
	}

	void createCategoryContextMenuItems() {
		cmCategoryEdit = new MenuItem(bundle.getString("view.cmEdit"));
		cmCategoryEdit.setOnAction((event) -> {
			handleCategoryEdit();
		});
		cmCategoryDelete = new MenuItem(bundle.getString("view.cmDelete"));
		cmCategoryDelete.setOnAction((event) -> {
			handleCategoryDelete();
		});
		categoryEditContextMenu.getItems().addAll(cmCategoryEdit, new SeparatorMenuItem(), cmCategoryDelete);
	}

	void createFeatureContextMenuItems() {
		cmFeatureEdit = new MenuItem(bundle.getString("view.cmEdit"));
		cmFeatureEdit.setOnAction((event) -> {
			handleFeatureEdit();
		});
		cmFeatureDelete = new MenuItem(bundle.getString("view.cmDelete"));
		cmFeatureDelete.setOnAction((event) -> {
			handleFeatureDelete();
		});
		featureEditContextMenu.getItems().addAll(cmFeatureEdit, new SeparatorMenuItem(), cmFeatureDelete);
	}

	void createWordContextMenuItems() {
		cmWordDuplicate = new MenuItem(bundle.getString("view.cmDuplicate"));
		cmWordDuplicate.setOnAction((event) -> {
			handleWordDuplicate();
		});
		cmWordInsertBefore = new MenuItem(bundle.getString("view.cmInsertBefore"));
		cmWordInsertBefore.setOnAction((event) -> {
			handleWordInsertBefore();
		});
		cmWordInsertAfter = new MenuItem(bundle.getString("view.cmInsertAfter"));
		cmWordInsertAfter.setOnAction((event) -> {
			handleWordInsertAfter();
		});
		cmWordMoveLeft = new MenuItem(bundle.getString("view.cmMoveLeft"));
		cmWordMoveLeft.setOnAction((event) -> {
			handleWordMoveLeft();
		});
		cmWordMoveRight = new MenuItem(bundle.getString("view.cmMoveRight"));
		cmWordMoveRight.setOnAction((event) -> {
			handleWordMoveRight();
		});
		cmWordDelete = new MenuItem(bundle.getString("view.cmDelete"));
		cmWordDelete.setOnAction((event) -> {
			handleWordDelete();
		});
		cmWordInsertPrefix = new MenuItem(bundle.getString("view.cmInsertPrefix"));
		cmWordInsertPrefix.setOnAction((event) -> {
			handleWordInsertPrefix();
		});
		cmWordInsertSuffix = new MenuItem(bundle.getString("view.cmInsertSuffix"));
		cmWordInsertSuffix.setOnAction((event) -> {
			handleWordInsertSuffix();
		});
		cmWordInsertCategory = new MenuItem(bundle.getString("view.cmInsertCategory"));
		cmWordInsertCategory.setOnAction((event) -> {
			handleWordInsertCategory();
		});
		cmWordInsertFeature = new MenuItem(bundle.getString("view.cmInsertFeature"));
		cmWordInsertFeature.setOnAction((event) -> {
			handleWordInsertFeature();
		});
		cmWordMarkAsHead = new MenuItem(bundle.getString("view.cmMarkAsHead"));
		cmWordMarkAsHead.setOnAction((event) -> {
			handleWordMarkAsHead();
		});
		cmWordRemoveHeadMarking = new MenuItem(bundle.getString("view.cmRemoveHeadMarking"));
		cmWordRemoveHeadMarking.setOnAction((event) -> {
			handleWordRemoveHeadMarking();
		});
		wordEditContextMenu.getItems().addAll(cmWordDuplicate, new SeparatorMenuItem(), cmWordMarkAsHead,
				cmWordRemoveHeadMarking, new SeparatorMenuItem(), cmWordInsertBefore, cmWordInsertAfter,
				new SeparatorMenuItem(), cmWordInsertPrefix, cmWordInsertSuffix, cmWordInsertCategory,
				cmWordInsertFeature, new SeparatorMenuItem(), cmWordMoveLeft, cmWordMoveRight, new SeparatorMenuItem(),
				cmWordDelete);
	}

	void enableDisableAffixContextMenuItems()
	{
		Word word = (Word)affix.getParent();
		int index = word.getAffixes().indexOf(affix);
		if (index == 0) {
			cmAffixMoveLeft.setDisable(true);
		} else {
			cmAffixMoveLeft.setDisable(false);
		}
		if (index == word.getAffixes().size() - 1) {
			cmAffixMoveRight.setDisable(true);
		} else {
			cmAffixMoveRight.setDisable(false);
		}
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

	void enableDisableWordContextMenuItems()
	{
		phrase = (Phrase) word.getParent();
		int index = phrase.getWords().indexOf(word);
		if (index == 0) {
			cmWordMoveLeft.setDisable(true);
		} else {
			cmWordMoveRight.setDisable(false);
		}
		if (index == phrase.getWords().size() - 1) {
			cmWordMoveRight.setDisable(true);
		} else {
			cmWordMoveRight.setDisable(false);
		}
		if (index == 0 && phrase.getWords().size() == 1) {
			cmWordDelete.setDisable(true);
		} else {
			cmWordDelete.setDisable(false);
		}
	}

	public void handleAffixDelete() {
		word = (Word)affix.getParent();
		if (word != null) {
			int index = word.getAffixes().indexOf(affix);
			word.deleteAffixAt(index);;
			reportChangesMade();
		}
	}

	public void handleAffixDuplicate() {
		word = (Word)affix.getParent();
		if (word != null) {
			int index = word.getAffixes().indexOf(affix);
			Affix newAffix = affix.duplicate();
			word.insertAffixAt(newAffix, index);
			reportChangesMade();
		}
	}

	public void handleAffixInsertFeature() {
		affix.insertNewFeature("", "");
		feature = affix.getFeatures().get(affix.getFeatures().size()-1);
		feature.setParent(affix);
		processInsertFeature();
		reportChangesMade();
	}

	public void handleAffixInsertPrefixAfter() {
		word = (Word)affix.getParent();
		int index = word.getAffixes().indexOf(affix);
		index = Math.min(word.getAffixes().size(), index + 1);
		insertNewAffix(index, AffixType.prefix);
	}

	public void handleAffixInsertPrefixBefore() {
		word = (Word)affix.getParent();
		int index = word.getAffixes().indexOf(affix);
		insertNewAffix(index, AffixType.prefix);
	}

	public void handleAffixInsertSuffixAfter() {
		word = (Word)affix.getParent();
		int index = word.getAffixes().indexOf(affix);
		index = Math.min(word.getAffixes().size(), index + 1);
		insertNewAffix(index, AffixType.suffix);
	}

	public void handleAffixInsertSuffixBefore() {
		word = (Word)affix.getParent();
		int index = word.getAffixes().indexOf(affix);
		insertNewAffix(index, AffixType.suffix);
	}

	protected void insertNewAffix(int index, AffixType type) {
		Affix newAffix = new Affix();
		word = (Word)affix.getParent();
		word.insertAffixAt(newAffix, index);
		newAffix.setType(type);
		reportChangesMade();
	}

	public void handleAffixMoveLeft() {
		word = (Word)affix.getParent();
		int index = word.getAffixes().indexOf(affix);
		moveAffix(index, index - 1);
	}

	public void handleAffixMoveRight() {
		word = (Word)affix.getParent();
		int index = word.getAffixes().indexOf(affix);
		moveAffix(index, index + 1);
	}

	protected void moveAffix(int index1, int index2) {
		Word word = (Word)affix.getParent();
		Collections.swap(word.getAffixes(), index1, index2);
		reportChangesMade();
	}

	public void handleAffixToggleAffixType() {
		if (affix.getType() == AffixType.prefix) {
			affix.setType(AffixType.suffix);
		} else {
			affix.setType(AffixType.prefix);
		}
		reportChangesMade();
	}

	public void handleCategoryDelete() {
		word = (Word)category.getParent();
		if (word != null) {
			word.deleteCategory();
			reportChangesMade();
		}
	}

	public void handleCategoryEdit() {
		phrase = category.getPhrase();
		if (phrase != null) {
			FLExTransRule rule = (FLExTransRule)phrase.getParent();
			if (rule != null) {
				if (phrase == rule.getSource().getPhrase()) {
					launchCategoryChooser(flexData.getSourceData().getCategories(), bundle);
				} else {
					launchCategoryChooser(flexData.getTargetData().getCategories(), bundle);
				}
			}
		}
	}

	void launchCategoryChooser(List<FLExCategory> categories, ResourceBundle bundle) {
		try {
			Stage dialogStage = new Stage();
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/fxml/FLExCategoryChooser.fxml"));
			loader.setResources(bundle);
			AnchorPane pane = loader.load();
			FLExCategoryChooserController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCategories(categories);
			controller.selectFLExCategory(category);
			Scene scene = new Scene(pane);
			dialogStage.setScene(scene);
			dialogStage.setTitle(loader.getResources().getString("chooser.CategoryTitle"));
			dialogStage.getIcons().add(ControllerUtilities.getIconImageFromURL("file:resources/FLExTransWindowIcon.png",
					Constants.RESOURCE_SOURCE_LOCATION));
			dialogStage.showAndWait();
			FLExCategory cat = controller.getCategoryChosen();
			if (controller.isOkClicked() && cat != null) {
				category.setName(cat.getAbbreviation());
				reportChangesMade();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void handleFeatureDelete() {
		RuleConstituent constituent = feature.getParent();
		if (constituent instanceof Word) {
			word = (Word) constituent;
			word.deleteFeature(feature);
			reportChangesMade();
		} else if (constituent instanceof Affix) {
			affix = (Affix) constituent;
			affix.deleteFeature(feature);
			reportChangesMade();
		}
	}

	public void handleFeatureEdit() {
		processInsertFeature();
	}

	protected void processInsertFeature() {
		phrase = feature.getPhrase();
		if (phrase != null) {
			FLExTransRule rule = (FLExTransRule)phrase.getParent();
			if (rule != null) {
				if (phrase == rule.getSource().getPhrase()) {
					launchFLExFeatureValueChooser(flexData.getSourceData().getFeatures(), bundle);
				} else {
					launchFLExFeatureValueChooser(flexData.getTargetData().getFeatures(), bundle);
				}
			}
		}
	}

	void launchFLExFeatureValueChooser(List<FLExFeature> features, ResourceBundle bundle) {
		try {
			Stage dialogStage = new Stage();
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/fxml/FLExFeatureValueChooser.fxml"));
			loader.setResources(bundle);
			AnchorPane pane = loader.load();
			FLExFeatureValueChooserController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setFeatures(features);
			controller.selectFLExFeatureValue(feature);
			Scene scene = new Scene(pane);
			dialogStage.setScene(scene);
			dialogStage.setTitle(loader.getResources().getString("chooser.FeatureValueTitle"));
			dialogStage.getIcons().add(ControllerUtilities.getIconImageFromURL("file:resources/FLExTransWindowIcon.png",
					Constants.RESOURCE_SOURCE_LOCATION));
			dialogStage.showAndWait();
			FLExFeatureValue featValue = controller.getFeatureValueChosen();
			if (controller.isOkClicked() && featValue != null) {
				feature.setLabel(featValue.getFeature().getName());
				feature.setMatch(featValue.getAbbreviation());
				reportChangesMade();
			}
		} catch(Exception e) {
			e.printStackTrace();
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
		markAsChanged(true);
	}

	public void handleRuleDuplicate()
	{
		FLExTransRule selectedRule = lvRules.getSelectionModel().getSelectedItem();
		FLExTransRule newRule = selectedRule.duplicate();
		generator.getFLExTransRules().add(selectedRuleIndex, newRule);
		lvRules.getItems().add(selectedRuleIndex, newRule);
		markAsChanged(true);
	}
	public void handleRuleInsertAfter()
	{
		insertNewRule(Math.min(lvRules.getItems().size(), selectedRuleIndex + 1));
	}
	public void handleRuleInsertBefore()
	{
		insertNewRule(selectedRuleIndex);
	}

	protected void insertNewRule(int index) {
		FLExTransRule newRule = new FLExTransRule();
		newRule.getSource().getPhrase().insertNewWordAt(0);
		newRule.getTarget().getPhrase().insertNewWordAt(0);
		newRule.setBundle(bundle);
		generator.getFLExTransRules().add(index, newRule);
		lvRules.getItems().add(index, newRule);
		lvRules.getSelectionModel().clearAndSelect(index);
		markAsChanged(true);
	}

	public void handleRuleMoveDown()
	{
		moveRule(selectedRuleIndex, selectedRuleIndex + 1);
	}

	public void handleRuleMoveUp()
	{
		moveRule(selectedRuleIndex, selectedRuleIndex - 1);
	}

	protected void moveRule(int index1, int index2) {
		Collections.swap(generator.getFLExTransRules(), index1, index2);
		Collections.swap(lvRules.getItems(), index1, index2);
		lvRules.getSelectionModel().clearAndSelect(index2);
		markAsChanged(true);
	}
	public void handleWordDelete() {
	}
	public void handleWordDuplicate() {
	}
	public void handleWordInsertAfter() {
	}
	public void handleWordInsertBefore() {
	}
	public void handleWordInsertCategory() {
	}
	public void handleWordInsertFeature() {
	}
	public void handleWordInsertPrefix() {
	}
	public void handleWordInsertSuffix() {
	}
	public void handleWordMarkAsHead() {
	}
	public void handleWordMoveLeft() {
	}
	public void handleWordMoveRight() {
	}
	public void handleWordRemoveHeadMarking() {
	}

	public void handleSave() {
		provider.saveDataToFile(ruleGenFile);
	}

	void markAsChanged(boolean changed)
	{
		changesMade = changed;
		showChangeStatusOnForm();
	}

	public void processItemClickedOn(String sItem) {
		String sCode = sItem.substring(0, 1);
		int identifier = Integer.parseInt(sItem.substring(2));
		RuleConstituent constituent = finder.findConstituent(lvRules.getSelectionModel().getSelectedItem(), identifier);
		int xCoord = webPageInteractor.getXCoord();
		int yCoord = webPageInteractor.getYCoord();
		switch (sCode) {
		case "a":
			affix = (Affix)constituent;
			enableDisableAffixContextMenuItems();
			affixEditContextMenu.show(stage, xCoord, yCoord);
			break;
		case "c":
			category = (Category)constituent;
			categoryEditContextMenu.show(stage, xCoord, yCoord);
			break;
		case "f":
			feature = (Feature)constituent;
			featureEditContextMenu.show(stage, xCoord, yCoord);
			break;
		case "p":
			phrase = (Phrase)constituent;
			// nothing else to do
			break;
		case "w":
			word = (Word)constituent;
			enableDisableWordContextMenuItems();
			wordEditContextMenu.show(stage, xCoord, yCoord);
			break;
		}
	}

	void reportChangesMade() {
		produceAndShowWebPage(lvRules.getSelectionModel().getSelectedItem());
		markAsChanged(true);
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
