// Copyright (c) 2024 SIL International
// This software is licensed under the LGPL, version 2.1 or later
// (http://www.gnu.org/licenses/lgpl-2.1.html)
/**
 *
 */
package org.sil.ftrulegen.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.sil.ftrulegen.ApplicationPreferences;
import org.sil.ftrulegen.Constants;
import org.sil.ftrulegen.Main;
import org.sil.ftrulegen.flexmodel.FLExData;
import org.sil.ftrulegen.flexmodel.FLExFeature;
import org.sil.ftrulegen.flexmodel.FLExFeatureValue;
import org.sil.ftrulegen.model.DisjointFeatureSet;
import org.sil.ftrulegen.model.DisjointFeatureValuePairing;
import org.sil.ftrulegen.model.FLExTransRuleGenerator;
import org.sil.ftrulegen.model.PhraseType;
import org.sil.utility.StringUtilities;
import org.sil.utility.view.ControllerUtilities;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * @author Andy Black
 *
 */

public class DisjointFeaturesEditorController implements Initializable {

//	protected final class AnalysisWrappingTableCell extends TableCell<DisjointFeatureSet, String> {
//		private Text text;
//
//		@Override
//		protected void updateItem(String item, boolean empty) {
//			super.updateItem(item, empty);
//			processAnalysisTableCell(this, text, item, empty);
//		}
//	}

//	protected final class GenericWrappingTableCell extends TableCell<DisjointFeatureSet, String> {
//		private Text text;
//
//		@Override
//		protected void updateItem(String item, boolean empty) {
//			super.updateItem(item, empty);
//			processTableCell(this, text, item, empty);
//		}
//	}

	@FXML
	private SplitPane splitPane;
	@FXML
	private TableView<DisjointFeatureSet> disjointFeaturesTable;
	@FXML
	private TableColumn<DisjointFeatureSet, String> nameColumn;
	@FXML
	private TableColumn<DisjointFeatureSet, String> coFeatureColumn;
	@FXML
	private TableColumn<DisjointFeatureSet, String> pairingsColumn;
	@FXML
	private TableColumn<DisjointFeatureSet, String> languageColumn;

	@FXML
	private TextField nameField;
	@FXML
	private ComboBox<String> coFeatureNameComboBox;
	@FXML
	protected ToggleGroup group;
	@FXML
	private RadioButton sourceRadioButton;
	@FXML
	private RadioButton targetRadioButton;
	@FXML
	private Button addSetButton;
	@FXML
	private Button deleteSetButton;
	@FXML
	private Button addPairingButton;
	@FXML
	private Button deletePairingButton;
	@FXML
	private Button closeButton;

	@FXML
	private TableView<DisjointFeatureValuePairing> disjointFeatureValuePairingTable;
	@FXML
	private TableColumn<DisjointFeatureValuePairing, StringProperty> flexFeatureNameColumn;
	@FXML
	private TableColumn<DisjointFeatureValuePairing, StringProperty> coFeatureValueColumn;

	ObservableList<DisjointFeatureSet> disjointFeatureSets;
//	TableView<DisjointFeatureSet> tableView;
	protected String sDisjointEditor = ApplicationPreferences.DISJOINT_FEATURE_EDITOR;
	protected  ApplicationPreferences prefs;

	Stage dialogStage;

	private DisjointFeatureSet currentFeatureSet;
	protected Main mainApp;
	protected ResourceBundle bundle;
	protected Locale locale;
	protected Clipboard systemClipboard = Clipboard.getSystemClipboard();
	protected FLExData flexData;
	List<FLExFeature> flexFeatures = new ArrayList<FLExFeature>();
	List<FLExFeature> flexSourceFeatures = new ArrayList<FLExFeature>();
	List<FLExFeature> flexTargetFeatures = new ArrayList<FLExFeature>();
	ObservableList<String> flexFeatureNames = FXCollections.observableArrayList();
	ObservableList<String> flexFeatureMinusCoFeatureNames = FXCollections.observableArrayList();
	ObservableList<String> flexFeatureValues = FXCollections.observableArrayList();
	HashMap<DisjointFeatureSet, ObservableList<DisjointFeatureValuePairing>> setPairingsMapping = new HashMap<>();
	final int minimumPairings = 2;

	public DisjointFeaturesEditorController() {

	}

	// =====================================
	// from SplitPaneWithTableViewController
	protected void initializeTableColumnWidthsAndSplitDividerPosition() {
		Double d = prefs.getDoubleValue(sDisjointEditor + splitPane.getId(), .4);
		splitPane.getDividers().get(0).setPosition(d);
		initializeTableColumnWidths(prefs);
	}
	// =====================================

	// =====================================
	// From TableViewController
	public void setTableView(TableView<DisjointFeatureSet> tableView) {
		this.disjointFeaturesTable = tableView;
	}

	protected void initializeTableColumnWidths(ApplicationPreferences prefs) {
		for (TableColumn<DisjointFeatureSet, ?> column : disjointFeaturesTable.getColumns()) {
			Double d = prefs.getDoubleValue(sDisjointEditor + column.getId(), column.getPrefWidth());
			column.setPrefWidth(d);
		}
	}
	// =====================================

	// =====================================
	// from SylParserBaseController

	protected void makeColumnHeaderWrappable(@SuppressWarnings("rawtypes") TableColumn col) {
		Label label = new Label(col.getText());
		label.setStyle("-fx-padding: 8px;");
		label.setWrapText(true);
		label.setAlignment(Pos.CENTER);
		label.setTextAlignment(TextAlignment.CENTER);
	
		StackPane stack = new StackPane();
		stack.getChildren().add(label);
		stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
		label.prefWidthProperty().bind(stack.prefWidthProperty());
		col.setGraphic(stack);
	}

	protected int adjustIndexValue(int value, int max) {
		if (value >= max) {
			value = max-1;
		} else if (value < 0) {
			value = 0;
		}
		return value;
	}

	protected void handleInsertNewItem(ObservableList<DisjointFeatureSet> list,
			TableView<DisjointFeatureSet> tableView) {
		int i = list.size() - 1;
		tableView.requestFocus();
		tableView.getSelectionModel().select(i);
		tableView.getFocusModel().focus(i);
		tableView.scrollTo(i);
	}

	protected void handleRemoveItem(ObservableList<DisjointFeatureSet> list,
			DisjointFeatureSet obj, TableView<DisjointFeatureSet> tableView) {
		int i = list.indexOf(obj);
		obj = null;
		if (i >= 0) {
			list.remove(i);
			int max = tableView.getItems().size();
			i = adjustIndexValue(i, max);
			// select the last one used
			tableView.requestFocus();
			tableView.getSelectionModel().select(i);
			tableView.getFocusModel().focus(i);
			tableView.scrollTo(i);
		}
		tableView.refresh();
	}

	protected void handlePreviousItem(ObservableList<DisjointFeatureSet> list,
			DisjointFeatureSet obj, TableView<DisjointFeatureSet> tableView) {
		int i = list.indexOf(obj) - 1;
		if (i >= 0) {
			tableView.requestFocus();
			tableView.getSelectionModel().select(i);
			tableView.getFocusModel().focus(i);
			tableView.scrollTo(i);
		}
		tableView.refresh();
	}
	protected void handleNextItem(ObservableList<DisjointFeatureSet> list,
			DisjointFeatureSet obj, TableView<DisjointFeatureSet> tableView) {
		int i = list.indexOf(obj) + 1;
		if (i < tableView.getItems().size()) {
			tableView.requestFocus();
			tableView.getSelectionModel().select(i);
			tableView.getFocusModel().focus(i);
			tableView.scrollTo(i);
		}
		tableView.refresh();
	}

//	protected void processAnalysisTableCell(TableCell<DisjointFeatureSet, String> cell, Text text, String item, boolean empty) {
//		Color textColor = languageProject.getAnalysisLanguage().getColor();
//		Font fontToUse = languageProject.getAnalysisLanguage().getFont();
//		cell.setNodeOrientation(languageProject.getAnalysisLanguage().getOrientation());
//		processCell(cell, item, empty, textColor, fontToUse);
//	}

//	protected void processCell(TableCell<DisjointFeatureSet, String> cell, String item,
//			boolean empty, Color textColor, Font fontToUse) {
//		Text text;
//		if (item == null || empty) {
//			cell.setText(null);
//			cell.setStyle("");
//		} else {
//			cell.setStyle("");
//			text = new Text(item.toString());
//			// Get it to wrap.
//			text.wrappingWidthProperty().bind(cell.getTableColumn().widthProperty());
//			DisjointFeatureSet obj = (DisjointFeatureSet) cell.getTableRow().getItem();
//			if (fontToUse != null) {
//				text.setFont(fontToUse);
//			}
//			cell.setGraphic(text);
//		}
//	}
//
//	protected void processTableCell(TableCell<DisjointFeatureSet, String> cell, Text text, String item, boolean empty) {
//		processCell(cell, item, empty, null, null);
//	}

	protected double guessPrefHeight(Object g, double columnWidth) {
		double maxSize = 12.0; 
				//Math.max(vernacular.getFontSize(),
				//analysis.getFontSize());
		return calculatePrefHeight(g, columnWidth, maxSize);
	}

	protected double calculatePrefHeight(Object g, double columnWidth, double maxSize) {
		TextFlow tf = (TextFlow) g;
		double area = tf.computeAreaInScreen();
		if (area > 0.0) {
			double heightGuess = area / columnWidth;
			if (heightGuess <= maxSize) {
				return tf.getPrefHeight();
			} else {
				return heightGuess;
			}
		} else {
			int iSize = tf.getChildrenUnmodifiable().size();
			double dItemsGuess = 1.25 * iSize;
			double d = (dItemsGuess * maxSize) / columnWidth;
			return d * maxSize;
		}
	}

	// =====================================

	// =====================================
	// From ApproachEditorController


	/**
	 * @return the mainApp
	 */
	public Main getMainApp() {
		return mainApp;
	}

//	public void setRootLayout(RootLayoutController controller) {
//		rootController = controller;
//	}

	/**
	 * @param mainApp
	 *            the mainApp to set
	 */
	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	private String getSelectedText(TextField[] textFields) {
		for (TextField tf : textFields) {
			String text = tf.getSelectedText();
			if (!StringUtilities.isNullOrEmpty(text)) {
				return text;
			}
		}
		return null;
	}

	protected TextField findFocusedTextField(TextField[] textFields) {
		if (textFields != null && textFields.length > 0) {
			for (TextField tf : textFields) {
				if (tf.isFocused()) {
					return tf;
				}
			}
		}
		return null;
	}

	protected TextField getFocusedTextField() {
		TextField[] textFields = createTextFields();
		return findFocusedTextField(textFields);
	}

	void handleCopy() {
		TextField[] textFields = createTextFields();
		String text = getSelectedText(textFields);
		ClipboardContent content = new ClipboardContent();
		content.putString(text);
		systemClipboard.setContent(content);
	}

	void handleCut() {
		TextField focusedTF = getFocusedTextField();
		processCut(focusedTF);
	}

	protected void processCut(TextField focusedTF) {
		if (focusedTF != null) {
			String text;
			IndexRange range;

			text = focusedTF.getSelectedText();
			range = focusedTF.getSelection();
			ClipboardContent content = new ClipboardContent();
			content.putString(text);
			systemClipboard.setContent(content);
			String origText = focusedTF.getText();
			String firstPart = origText.substring(0, range.getStart());
			String lastPart = origText.substring(range.getEnd(), origText.length());
			focusedTF.setText(firstPart + lastPart);

			focusedTF.positionCaret(range.getStart());
		}
	}

	void handlePaste() {

		String clipboardText = systemClipboard.getString();
		if (clipboardText == null || clipboardText.length() == 0) {
			return;
		}

		TextField focusedTF = getFocusedTextField();
		if (focusedTF == null) {
			return;
		}
		IndexRange range = focusedTF.getSelection();

		String origText = focusedTF.getText();
		processPaste(clipboardText, focusedTF, range, origText);
	}

	protected void processPaste(String clipboardText, TextField focusedTF, IndexRange range,
			String origText) {
		if (origText != null) {
			int endPos = 0;
			String updatedText = "";
			String firstPart = origText.substring(0, range.getStart());
			String lastPart = origText.substring(range.getEnd(), origText.length());

			updatedText = firstPart + clipboardText + lastPart;

			if (range.getStart() == range.getEnd()) {
				endPos = range.getEnd() + clipboardText.length();
			} else {
				endPos = range.getStart() + clipboardText.length();
			}

			focusedTF.setText(updatedText);
			focusedTF.positionCaret(endPos);
		}
	}

	boolean anythingSelected() {
		TextField[] textFields = createTextFields();
		if (textFields != null && textFields.length > 0) {
			for (TextField tf : textFields) {
				String sSelected = tf.getSelectedText();
				if (!StringUtilities.isNullOrEmpty(sSelected)) {
					return true;
				}
			}
		}
		return false;
	}

	@FXML
	public void keyboardReleased(KeyEvent evt) {
		KeyCode code = evt.getCode();
		if (evt.isShiftDown()) {
			if (code == KeyCode.LEFT || code == KeyCode.KP_LEFT || code == KeyCode.RIGHT
					|| code == KeyCode.KP_RIGHT || code == KeyCode.HOME || code == KeyCode.END) {
				TextField tf = (TextField) evt.getSource();
				String selectedText = tf.getSelectedText();
				IndexRange selectedRange = tf.getSelection();
//				toolBarDelegate.updateToolBarForClipboard(tf, selectedText, selectedRange);
			}
		} else { // attempt at trying to fix odd case where using toolbar with
					// keyboard may paste in unexpected location
			if (code == KeyCode.LEFT || code == KeyCode.KP_LEFT || code == KeyCode.RIGHT
					|| code == KeyCode.KP_RIGHT || code == KeyCode.HOME || code == KeyCode.END) {
				TextField tf = (TextField) evt.getSource();
				String selectedText = tf.getSelectedText();
				IndexRange selectedRange = tf.getSelection();
//				toolBarDelegate.updateToolBarForClipboard(tf, selectedText, selectedRange);
			}
		}
	}

	// =====================================

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sourceRadioButton.setText(resources.getString("disjoint.source"));
		targetRadioButton.setText(resources.getString("disjoint.target"));

		for (TableColumn<DisjointFeatureSet, ?> column: disjointFeaturesTable.getColumns()) {
			  column.widthProperty().addListener(new ChangeListener<Number>() {
			    @Override
			      public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) {
			        prefs.setPreferencesKey(sDisjointEditor + column.getId(), newWidth.doubleValue());
			    }
			  });
			}

		splitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue,
					Number newValue) {
				prefs.setPreferencesKey(sDisjointEditor + splitPane.getId(), newValue.doubleValue());
			}
		});

		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		languageColumn.setCellValueFactory(cellData -> cellData.getValue().LanguageProperty());
		pairingsColumn.setCellValueFactory(cellData -> cellData.getValue()
				.valuesRepresentationProperty());
		coFeatureColumn
				.setCellValueFactory(cellData -> cellData.getValue().CoFeatureNameProperty());

		// Custom rendering of the table cell.
//		nameColumn.setCellFactory(column -> {
//			return new AnalysisWrappingTableCell();
//		});

		pairingsColumn.setCellFactory(column -> {
			return new TableCell<DisjointFeatureSet, String>() {
				// We override computePrefHeight because by default, the graphic's height
				// gets set to the height of all items in the TextFlow as if none of them
				// wrapped.  So for now, we're doing this hack.
				@Override
				protected double computePrefHeight(double width) {
					Object g = getGraphic();
					if (g instanceof TextFlow) {
						return guessPrefHeight(g, column.widthProperty().get());
					}
					return super.computePrefHeight(-1);
				}

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					DisjointFeatureSet nc = ((DisjointFeatureSet) getTableRow().getItem());
					if (item == null || empty || nc == null) {
						setGraphic(null);
						setText(null);
						setStyle("");
					} else {
						setGraphic(null);
						TextFlow tf = new TextFlow();
						tf = buildTextFlow(nc.getValuesAsListOfStrings());
						setGraphic(tf);
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
					}
				}
			};
		});

//		featureColumn.setCellFactory(column -> {
//			return new AnalysisWrappingTableCell();
//		});

		coFeatureNameComboBox.getSelectionModel().selectedItemProperty()
				.addListener((options, oldValue, newValue) -> {
					if (currentFeatureSet != null) {
						if (newValue == null) {
							// somehow this can be null; fix it
							String sValue = currentFeatureSet.getCoFeatureName();
							coFeatureNameComboBox.getSelectionModel().select(sValue);
							createCoFeatureValues(sValue);
						} else {
							currentFeatureSet.setCoFeatureName(newValue);
							createCoFeatureValues(newValue);
						}
					}
				});
		coFeatureNameComboBox.setPromptText(resources.getString("disjoint.cofeaturename"));

		makeColumnHeaderWrappable(nameColumn);
		makeColumnHeaderWrappable(languageColumn);
		makeColumnHeaderWrappable(pairingsColumn);
		makeColumnHeaderWrappable(coFeatureColumn);

		flexFeatureNameColumn.setCellValueFactory(cellData -> {
			final StringProperty value = cellData.getValue().FLExFeatureNameProperty();
			return Bindings.createObjectBinding(() -> value);
		});
		flexFeatureNameColumn.setCellFactory(col -> {
			TableCell<DisjointFeatureValuePairing, StringProperty> c = new TableCell<>();
			final ComboBox<String> comboBox = new ComboBox<>(flexFeatureMinusCoFeatureNames);
			c.itemProperty().addListener((observable, oldValue, newValue) -> {
				if (oldValue != null) {
					comboBox.valueProperty().unbindBidirectional(oldValue);
				}
				if (newValue != null) {
					comboBox.valueProperty().bindBidirectional(newValue);
				}
			});
			c.graphicProperty().bind(
					Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
			return c;
		});

		coFeatureValueColumn.setCellValueFactory(cellData -> {
			final StringProperty value = cellData.getValue().CoFeatureValueProperty();
			return Bindings.createObjectBinding(() -> value);
		});
		coFeatureValueColumn.setCellFactory(col -> {
			TableCell<DisjointFeatureValuePairing, StringProperty> c = new TableCell<>();
			final ComboBox<String> comboBox = new ComboBox<>(flexFeatureValues);
			c.itemProperty().addListener((observable, oldValue, newValue) -> {
				if (oldValue != null) {
					comboBox.valueProperty().unbindBidirectional(oldValue);
				}
				if (newValue != null) {
					comboBox.valueProperty().bindBidirectional(newValue);
				}
			});
			c.graphicProperty().bind(
					Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
			return c;
		});

		// Clear details.
		showFeatureSetDetails(null);

		// Listen for selection changes and show the details when changed.
		disjointFeaturesTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> {
							if (featureSetPairingsAreNull(newValue)) {
								newValue = retoreFeatureSetPairings(newValue);
							}
							showFeatureSetDetails(newValue);
						});

		// Handle TextField text changes.
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				currentFeatureSet.setName(nameField.getText());
			}
		});

		// Listen for selection changes and show the details when changed.
		disjointFeatureValuePairingTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> {
							showPairingsDetails(newValue);
						});

		// Use of Enter move focus to next item.
		nameField.setOnAction((event) -> {
			coFeatureNameComboBox.requestFocus();
		});

		nameField.requestFocus();

	}

	protected boolean featureSetPairingsAreNull(DisjointFeatureSet featureSet) {
		if (featureSet != null) {
			for (DisjointFeatureValuePairing pairing : featureSet.getDisjointFeatureValuePairings()) {
				if (pairing == null || pairing.getFlexFeatureName() == null || pairing.getCoFeatureValue() == null) {
					return true;
				}
			}
		}
		return false;
	}

	protected DisjointFeatureSet retoreFeatureSetPairings(DisjointFeatureSet featureSet) {
		if (setPairingsMapping.containsKey(featureSet)) {
			featureSet.setDisjointFeatureValuePairings(setPairingsMapping.get(featureSet));
		}
		return featureSet;
	}

	protected void createCoFeatureValues(String featureName) {
		flexFeatureValues.clear();
		for (FLExFeature ff : flexFeatures) {
			if (ff.getName().equals(currentFeatureSet.getCoFeatureName())) {
				for (FLExFeatureValue val : ff.getValues()) {
					flexFeatureValues.add(val.getAbbreviation());
				}
				break;
			}
		}
	}

	public void setViewItemUsed(int value) {
		int max = disjointFeaturesTable.getItems().size();
		value = adjustIndexValue(value, max);
		disjointFeaturesTable.getSelectionModel().clearAndSelect(value);
	}

	/**
	 * Fills all text fields to show details about the CV natural class. If the
	 * specified segment is null, all text fields are cleared.
	 *
	 * @param featureSet
	 *            the segment or null
	 */
	private void showFeatureSetDetails(DisjointFeatureSet featureSet) {
		rememberFeatureSetPairings(currentFeatureSet);
		currentFeatureSet = featureSet;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				createFLExFeatureNames(featureSet);
				if (featureSet != null) {
					// Fill the text fields with info from the featureSet
					// object.
					nameField.setText(featureSet.getName());
					if (featureSet.getLanguage() == PhraseType.target) {
						targetRadioButton.setSelected(true);
					} else {
						sourceRadioButton.setSelected(true);
					}
					// For some reason, the pairings values can get nulled out;
					// We work around this by creating a map containing the good values
					if (!featureSetPairingsAreNull(featureSet)) {
						rememberFeatureSetPairings(featureSet);
					} else {
						retoreFeatureSetPairings(featureSet);
					}
					disjointFeatureValuePairingTable.setItems(featureSet
							.getDisjointFeatureValuePairings());
					int iCurrentIndex = disjointFeaturesTable.getItems().indexOf(featureSet);
					prefs.setLastDisjointFeatureSetItemUsed(iCurrentIndex);
					coFeatureNameComboBox.setItems(flexFeatureNames);
					coFeatureNameComboBox.getSelectionModel().select(featureSet.getCoFeatureName());
				} else {
					// FeatureSet is null, remove all the text.
					nameField.setText("");
					coFeatureNameComboBox.getSelectionModel().select("??");
				}
				enableDisableButtons();
			}
		});
	}

	protected void rememberFeatureSetPairings(DisjointFeatureSet featureSet) {
		if (featureSet != null) {
			ObservableList<DisjointFeatureValuePairing> rememberList = FXCollections.observableArrayList();
			for (DisjointFeatureValuePairing pairing : featureSet.getDisjointFeatureValuePairings()) {
				DisjointFeatureValuePairing newPairing = new DisjointFeatureValuePairing();
				newPairing.setFlexFeatureName(pairing.getFlexFeatureName());
				newPairing.setCoFeatureValue(pairing.getCoFeatureValue());
				rememberList.add(newPairing);
			}
			setPairingsMapping.put(featureSet, rememberList);
		}
	}

	private void showPairingsDetails(DisjointFeatureValuePairing pairing) {
		// this is a no-op, at least for now
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				System.out.println("\nshowPairingsDetails pairing=" + pairing);
//				if (pairing != null) {
//					System.out.println("\tFLEx feature=" + pairing.getFlexFeatureName());
//					System.out.println("\tco feature value=" + pairing.getCoFeatureValue());
//				} else {
//					// pairing is null, remove all the text.
//					System.out.println("\tpairing is null");
//				}
//			}
//		});
	}

	private void createFLExFeatureNames(DisjointFeatureSet featureSet) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				flexFeatureNames.clear();
				flexFeatureMinusCoFeatureNames.clear();
				flexFeatures.clear();
				if (featureSet != null) {
					// Fill the text fields with info from the featureSet
					// object.
					if (featureSet.getLanguage() == PhraseType.target) {
						flexFeatures.addAll(flexTargetFeatures);
					} else {
						flexFeatures.addAll(flexSourceFeatures);
					}
					for (FLExFeature ff : flexFeatures) {
						flexFeatureNames.add(ff.getName());
						if (!ff.getName().equals(featureSet.getCoFeatureName())) {
							flexFeatureMinusCoFeatureNames.add(ff.getName());
						}
					}
				}

			}
		});
	}

	protected void enableDisableButtons() {
		if (disjointFeaturesTable.getItems().size() > 0) {
			deleteSetButton.setDisable(false);
		} else {
			deleteSetButton.setDisable(true);
		}
		if (disjointFeatureValuePairingTable.getItems().size() > minimumPairings) {
			deletePairingButton.setDisable(false);
		} else {
			deletePairingButton.setDisable(true);
		}
	}

	protected void fillSncTextFlow(StringBuilder sb,
			ObservableList<String> segmentsOrNaturalClasses) {
		int i = 1;
		int iCount = segmentsOrNaturalClasses.size();
		for (String snc : segmentsOrNaturalClasses) {
			Text t;
			String s;
			t = new Text(snc);
			Text tBar = new Text(" | ");
			tBar.setStyle("-fx-stroke: lightgrey;");
//			valuesTextFlow.getChildren().addAll(t, tBar);
			if (i++ < iCount) {
				sb.append(", ");
			}
		}
	}

	protected TextFlow buildTextFlow(ObservableList<String> valueList) {
		TextFlow tf = new TextFlow();
		for (String value : valueList) {
			Text t;
			String s;
			t = new Text(value);
			Text tBar = new Text(" | ");
			tBar.setStyle("-fx-stroke: lightgrey;");
			tf.getChildren().addAll(t, tBar);
		}
		return tf;
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * @param approachType = which approach invoked this
	 * @param cvApproachController = CV data
	 */
	public void setData(ObservableList<DisjointFeatureSet> disjointFeatureSets, FLExData flexData) {
		this.flexData = flexData;
		this.disjointFeatureSets = disjointFeatureSets;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (disjointFeatureSets.size() == 0) {
					createNewDisjointFeatureSet(disjointFeatureSets);
				}
				// Add observable list data to the table
				disjointFeaturesTable.setItems(disjointFeatureSets);
				int max = disjointFeaturesTable.getItems().size();
				// retrieve selection
				int iLastIndex = 0;
				if (prefs != null) {
					iLastIndex = prefs.getLastDisjointFeatureSetItemUsed();
					iLastIndex = adjustIndexValue(iLastIndex, max);
					if (max > 0) {
						selectFeatureSetInTableByIndex(iLastIndex);
					}
				} else {
					selectFeatureSetInTableByIndex(iLastIndex);
				}
				currentFeatureSet = disjointFeatureSets.get(iLastIndex);
				currentFeatureSet.setBundle(bundle);
				// following is used to ensure the language column starts out with the correct value
				currentFeatureSet.setLanguage(currentFeatureSet.getLanguage());
				disjointFeatureValuePairingTable.setItems(currentFeatureSet.getDisjointFeatureValuePairings());
				flexSourceFeatures = flexData.getSourceData().getFeaturesWithoutVariables();
				flexTargetFeatures = flexData.getTargetData().getFeaturesWithoutVariables();
			}
		});
	}

	protected void selectFeatureSetInTableByIndex(int iLastIndex) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// select the last one used
				disjointFeaturesTable.requestFocus();
				disjointFeaturesTable.getSelectionModel().select(iLastIndex);
				disjointFeaturesTable.getFocusModel().focus(iLastIndex);
				disjointFeaturesTable.scrollTo(iLastIndex);
			}
		});
	}

	protected void createNewDisjointFeatureSet(
			ObservableList<DisjointFeatureSet> disjointFeatureSets) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				currentFeatureSet = new DisjointFeatureSet();
				disjointFeatureSets.add(currentFeatureSet);
				DisjointFeatureValuePairing one = new DisjointFeatureValuePairing();
				DisjointFeatureValuePairing two = new DisjointFeatureValuePairing();
				currentFeatureSet.getDisjointFeatureValuePairings().addAll(one, two);
				disjointFeatureValuePairingTable.setItems(currentFeatureSet
						.getDisjointFeatureValuePairings());
				selectFeatureSetInTableByIndex(disjointFeatureSets.size() -1);
			}
		});
	}

	public void setDialogStage(Stage value) {
		dialogStage = value;
		prefs = ApplicationPreferences.getInstance();
		dialogStage = prefs.getLastWindowParameters(sDisjointEditor, dialogStage, 450.0, 900.0);
	}


	void handleInsertNewItem() {
		DisjointFeatureSet newDisjointFeatureSet = new DisjointFeatureSet();
		disjointFeatureSets.add(newDisjointFeatureSet);
		handleInsertNewItem(disjointFeatureSets, disjointFeaturesTable);
	}

	void handleRemoveItem() {
		handleRemoveItem(disjointFeatureSets, currentFeatureSet, disjointFeaturesTable);
	}

	void handlePreviousItem() {
		handlePreviousItem(disjointFeatureSets, currentFeatureSet, disjointFeaturesTable);
	}

	void handleNextItem() {
		handleNextItem(disjointFeatureSets, currentFeatureSet, disjointFeaturesTable);
	}

	@FXML
	void handleSourceRadioButton() {
		sourceRadioButton.setSelected(true);
		currentFeatureSet.setLanguage(PhraseType.source);
		createFLExFeatureNames(currentFeatureSet);
		coFeatureNameComboBox.setPromptText(bundle.getString("disjoint.cofeaturename"));
	}
	
	@FXML
	void handleTargetRadioButton() {
		targetRadioButton.setSelected(true); // needed by test for some
		currentFeatureSet.setLanguage(PhraseType.target);
		createFLExFeatureNames(currentFeatureSet);
		coFeatureNameComboBox.setPromptText(bundle.getString("disjoint.cofeaturename"));
	}
	
	public void stop() throws IOException {
		System.out.println("stop");
		handleClose();
	}

	@FXML
	public void handleAddPairing() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				DisjointFeatureValuePairing newPairing = new DisjointFeatureValuePairing();
				currentFeatureSet.getDisjointFeatureValuePairings().add(newPairing);
				disjointFeatureValuePairingTable.setItems(currentFeatureSet
						.getDisjointFeatureValuePairings());
				String sCoFeature = currentFeatureSet.getCoFeatureName();
				if (sCoFeature.length() > 0) {
					createCoFeatureValues(sCoFeature);
				}
				enableDisableButtons();
			}
		});
	}

	@FXML
	public void handleDeletePairing() {
		if (currentFeatureSet != null) {
			if (currentFeatureSet.getDisjointFeatureValuePairings().size() > minimumPairings) {
				int index = disjointFeatureValuePairingTable.getSelectionModel().getSelectedIndex();
				if (index > -1) {
					if (currentFeatureSet != null) {
						setPairingsMapping.remove(currentFeatureSet);
					}
					currentFeatureSet.getDisjointFeatureValuePairings().remove(index);
				}
			}
		}
		enableDisableButtons();
	}

	@FXML
	public void handleAddSet() {
		createNewDisjointFeatureSet(disjointFeatureSets);
	}

	@FXML
	public void handleDeleteSet() {
		int index = disjointFeaturesTable.getSelectionModel().getSelectedIndex();
		if (index > -1) {
			if (currentFeatureSet != null) {
				setPairingsMapping.remove(currentFeatureSet);
			}
			disjointFeatureSets.remove(index);
			if (index > 0)
				selectFeatureSetInTableByIndex(index - 1);
		}
		showFeatureSetDetails(currentFeatureSet);
	}

	@FXML
	public void handleClose() {
		// set any preferences
		prefs.setLastWindowParameters(sDisjointEditor, dialogStage);
		dialogStage.close();
	}

	/**
	 * Opens a dialog to show the chooser.
	 */
	public void showValuesChooser() {
//		try {
			Stage dialogStage = new Stage();
			String resource = "fxml/DisjointFeatureValuesChooser.fxml";
			String sTitle = bundle.getString("main.Title");
//			FXMLLoader loader = ControllerUtilities.getLoader(mainApp, locale, dialogStage,
//					sTitle, DisjointFeaturesEditorController.class.getResource(resource),
//					Constants.RESOURCE_LOCATION);
//			DisjointFeatureValuesChooserController controller = loader.getController();
//			controller.setDialogStage(dialogStage);
//			controller.setMainApp(mainApp);
//			controller.setFeatureSet(currentFeatureSet);
//			controller.setData(disjointFeatures);
//			controller.initializeTableColumnWidths(mainApp.getApplicationPreferences());
//
//			dialogStage.showAndWait();

//		} catch (IOException e) {
//			e.printStackTrace();
//			Main.reportException(e, bundle);
//		}
	}

	// code taken from
	// http://bekwam.blogspot.com/2014/10/cut-copy-and-paste-from-javafx-menubar.html
	TextField[] createTextFields() {
		return new TextField[] { nameField };
	}

}
