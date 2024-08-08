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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.sil.ftrulegen.ApplicationPreferences;
import org.sil.ftrulegen.Main;
import org.sil.ftrulegen.flexmodel.FLExData;
import org.sil.ftrulegen.flexmodel.FLExFeature;
import org.sil.ftrulegen.flexmodel.FLExFeatureValue;
import org.sil.ftrulegen.model.DisjointFeatureSet;
import org.sil.ftrulegen.model.DisjointFeatureValuePairing;
import org.sil.ftrulegen.model.PhraseType;
import org.sil.utility.StringUtilities;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

/**
 * @author Andy Black
 *
 */

public class DisjointFeaturesEditorController implements Initializable {

	@FXML
	private SplitPane splitPane;
	@FXML
	private TableView<DisjointFeatureSet> disjointFeaturesTable;
	@FXML
	private TableColumn<DisjointFeatureSet, String> nameColumn;
	@FXML
	private TableColumn<DisjointFeatureSet, String> coFeatureColumn;
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
	private Slider pairingsSlider;
	@FXML
	private Button closeButton;
	@FXML
	private ComboBox<String> flexFeature1ComboBox;
	@FXML
	private ComboBox<String> coFeatureValue1ComboBox;
	@FXML
	private ComboBox<String> flexFeature2ComboBox;
	@FXML
	private ComboBox<String> coFeatureValue2ComboBox;
	@FXML
	private ComboBox<String> flexFeature3ComboBox;
	@FXML
	private ComboBox<String> coFeatureValue3ComboBox;
	@FXML
	private ComboBox<String> flexFeature4ComboBox;
	@FXML
	private ComboBox<String> coFeatureValue4ComboBox;
	@FXML
	private ComboBox<String> flexFeature5ComboBox;
	@FXML
	private ComboBox<String> coFeatureValue5ComboBox;
	@FXML
	private ComboBox<String> flexFeature6ComboBox;
	@FXML
	private ComboBox<String> coFeatureValue6ComboBox;



	ObservableList<DisjointFeatureSet> disjointFeatureSets;
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
	int visiblePairings = 2;
	final String kEmpty = "??";

	public DisjointFeaturesEditorController() {

	}

	protected void initializeTableColumnWidthsAndSplitDividerPosition() {
		Double d = prefs.getDoubleValue(sDisjointEditor + splitPane.getId(), .4);
		splitPane.getDividers().get(0).setPosition(d);
		initializeTableColumnWidths(prefs);
	}

	public void setTableView(TableView<DisjointFeatureSet> tableView) {
		this.disjointFeaturesTable = tableView;
	}

	protected void initializeTableColumnWidths(ApplicationPreferences prefs) {
		for (TableColumn<DisjointFeatureSet, ?> column : disjointFeaturesTable.getColumns()) {
			Double d = prefs.getDoubleValue(sDisjointEditor + column.getId(), column.getPrefWidth());
			column.setPrefWidth(d);
		}
	}

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

	/**
	 * @return the mainApp
	 */
	public Main getMainApp() {
		return mainApp;
	}

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
		languageColumn.setCellValueFactory(cellData -> {
			PhraseType ptLanguage = cellData.getValue().getLanguage();
			String sLanguage = (ptLanguage == PhraseType.source) ? bundle.getString("disjoint.source") : bundle
					.getString("disjoint.target");
			cellData.getValue().LanguageProperty().set(sLanguage);
			return cellData.getValue().LanguageProperty();
		});
		coFeatureColumn
				.setCellValueFactory(cellData -> cellData.getValue().CoFeatureNameProperty());

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

		flexFeature1ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(0).getFlexFeatureName();
					flexFeature1ComboBox.getSelectionModel().select(sValue);
				} else {
					currentFeatureSet.getDisjointFeatureValuePairings().get(0).setFlexFeatureName(newValue);
				}
			}
		});
		flexFeature2ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(1).getFlexFeatureName();
					flexFeature2ComboBox.getSelectionModel().select(sValue);
				} else {
					currentFeatureSet.getDisjointFeatureValuePairings().get(1).setFlexFeatureName(newValue);
				}
			}
		});
		flexFeature3ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = kEmpty;
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 3) {
						sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(2).getFlexFeatureName();
					} else if ((int)pairingsSlider.getValue() >= 3) {
						createNewPairing(sValue);
					}
					flexFeature3ComboBox.getSelectionModel().select(sValue);
				} else {
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 3) {
						currentFeatureSet.getDisjointFeatureValuePairings().get(2).setFlexFeatureName(newValue);
					} else if ((int)pairingsSlider.getValue() >= 3) {
						createNewPairing(newValue);
					}
					flexFeature3ComboBox.getSelectionModel().select(newValue);
				}
			}
		});
		flexFeature4ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = kEmpty;
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 4) {
						sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(3).getFlexFeatureName();
					} else if ((int)pairingsSlider.getValue() >= 4) {
						createNewPairing(sValue);
					}
					flexFeature4ComboBox.getSelectionModel().select(sValue);
				} else {
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 4) {
						currentFeatureSet.getDisjointFeatureValuePairings().get(3).setFlexFeatureName(newValue);
					} else if ((int)pairingsSlider.getValue() >= 4) {
						createNewPairing(newValue);
					}
					flexFeature4ComboBox.getSelectionModel().select(newValue);
				}
			}
		});
		flexFeature5ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = kEmpty;
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 5) {
						sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(4).getFlexFeatureName();
					} else if ((int)pairingsSlider.getValue() >= 5) {
						createNewPairing(sValue);
					}
					flexFeature5ComboBox.getSelectionModel().select(sValue);
				} else {
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 5) {
						currentFeatureSet.getDisjointFeatureValuePairings().get(4).setFlexFeatureName(newValue);
					} else if ((int)pairingsSlider.getValue() >= 5) {
						createNewPairing(newValue);
					}
					flexFeature5ComboBox.getSelectionModel().select(newValue);
				}
			}
		});
		flexFeature6ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = kEmpty;
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 6) {
						sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(5).getFlexFeatureName();
					} else if ((int)pairingsSlider.getValue() >= 6) {
						createNewPairing(sValue);
					}
					flexFeature6ComboBox.getSelectionModel().select(sValue);
				} else {
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 6) {
						currentFeatureSet.getDisjointFeatureValuePairings().get(5).setFlexFeatureName(newValue);
					} else if ((int)pairingsSlider.getValue() >= 6) {
						createNewPairing(newValue);
					}
					flexFeature6ComboBox.getSelectionModel().select(newValue);
				}
			}
		});

		coFeatureValue1ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(0).getCoFeatureValue();
					flexFeature1ComboBox.getSelectionModel().select(sValue);
				} else {
					currentFeatureSet.getDisjointFeatureValuePairings().get(0).setCoFeatureValue(newValue);
				}
			}
		});
		coFeatureValue2ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(1).getCoFeatureValue();
					flexFeature1ComboBox.getSelectionModel().select(sValue);
				} else {
					currentFeatureSet.getDisjointFeatureValuePairings().get(1).setCoFeatureValue(newValue);
				}
			}
		});
		coFeatureValue3ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = kEmpty;
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 3) {
						sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(2).getCoFeatureValue();
					} else if ((int)pairingsSlider.getValue() >= 3) {
						createNewPairing(sValue);
					}
					coFeatureValue3ComboBox.getSelectionModel().select(sValue);
				} else {
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 3) {
						currentFeatureSet.getDisjointFeatureValuePairings().get(2).setCoFeatureValue(newValue);
					} else if ((int)pairingsSlider.getValue() >= 3) {
						createNewPairing(newValue);
					}
					coFeatureValue3ComboBox.getSelectionModel().select(newValue);
				}
			}
		});
		coFeatureValue4ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = kEmpty;
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 4) {
						sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(3).getCoFeatureValue();
					} else if ((int)pairingsSlider.getValue() >= 4) {
						createNewPairing(sValue);
					}
					coFeatureValue4ComboBox.getSelectionModel().select(sValue);
				} else {
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 4) {
						currentFeatureSet.getDisjointFeatureValuePairings().get(3).setCoFeatureValue(newValue);
					} else if ((int)pairingsSlider.getValue() >= 4) {
						createNewPairing(newValue);
					}
					coFeatureValue4ComboBox.getSelectionModel().select(newValue);
				}
			}
		});
		coFeatureValue5ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = kEmpty;
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 5) {
						sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(4).getCoFeatureValue();
					} else if ((int)pairingsSlider.getValue() >= 5) {
						createNewPairing(sValue);
					}
					coFeatureValue5ComboBox.getSelectionModel().select(sValue);
				} else {
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 5) {
						currentFeatureSet.getDisjointFeatureValuePairings().get(4).setCoFeatureValue(newValue);
					} else if ((int)pairingsSlider.getValue() >= 5) {
						createNewPairing(newValue);
					}
					coFeatureValue5ComboBox.getSelectionModel().select(newValue);
				}
			}
		});
		coFeatureValue6ComboBox.getSelectionModel().selectedItemProperty()
		.addListener((options, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				if (newValue == null) {
					// somehow this can be null; fix it
					String sValue = kEmpty;
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 6) {
						sValue = currentFeatureSet.getDisjointFeatureValuePairings().get(5).getCoFeatureValue();
					} else if ((int)pairingsSlider.getValue() >= 6) {
						createNewPairing(sValue);
					}
					coFeatureValue6ComboBox.getSelectionModel().select(sValue);
				} else {
					if (currentFeatureSet.getDisjointFeatureValuePairings().size() >= 6) {
						currentFeatureSet.getDisjointFeatureValuePairings().get(5).setCoFeatureValue(newValue);
					} else if ((int)pairingsSlider.getValue() >= 6) {
						createNewPairing(newValue);
					}
					coFeatureValue6ComboBox.getSelectionModel().select(newValue);
				}
			}
		});

		makeColumnHeaderWrappable(nameColumn);
		makeColumnHeaderWrappable(languageColumn);
		makeColumnHeaderWrappable(coFeatureColumn);

		pairingsSlider.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
					Number newValue) {
				int result = (int) pairingsSlider.getValue();
				showVisiblePairingsComboBoxes(result);
				if ((double)oldValue > (double)newValue) {
					currentFeatureSet.removePairingsFrom(result + 1);
				}
			}
		});
		// Clear details.
		showFeatureSetDetails(null);

		// Listen for selection changes and show the details when changed.
		disjointFeaturesTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> {
							showFeatureSetDetails(newValue);
						});

		// Handle TextField text changes.
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (currentFeatureSet != null) {
				currentFeatureSet.setName(nameField.getText());
			}
		});

		// Use of Enter move focus to next item.
		nameField.setOnAction((event) -> {
			coFeatureNameComboBox.requestFocus();
		});

		nameField.requestFocus();

	}

	protected void createNewPairing(String newValue) {
		DisjointFeatureValuePairing pairing = new DisjointFeatureValuePairing();
		pairing.setFlexFeatureName(newValue);
		currentFeatureSet.getDisjointFeatureValuePairings().add(pairing);
	}

	protected void showVisiblePairingsComboBoxes(int visiblePairings) {
		if (visiblePairings < 3) {
			flexFeature3ComboBox.setVisible(false);
			coFeatureValue3ComboBox.setVisible(false);
		} else {
			flexFeature3ComboBox.setVisible(true);
			coFeatureValue3ComboBox.setVisible(true);
		}
		if (visiblePairings < 4) {
			flexFeature4ComboBox.setVisible(false);
			coFeatureValue4ComboBox.setVisible(false);
		} else {
			flexFeature4ComboBox.setVisible(true);
			coFeatureValue4ComboBox.setVisible(true);
		}
		if (visiblePairings < 5) {
			flexFeature5ComboBox.setVisible(false);
			coFeatureValue5ComboBox.setVisible(false);
		} else {
			flexFeature5ComboBox.setVisible(true);
			coFeatureValue5ComboBox.setVisible(true);
		}
		if (visiblePairings < 6) {
			flexFeature6ComboBox.setVisible(false);
			coFeatureValue6ComboBox.setVisible(false);
		} else {
			flexFeature6ComboBox.setVisible(true);
			coFeatureValue6ComboBox.setVisible(true);
		}
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
					int iCurrentIndex = disjointFeaturesTable.getItems().indexOf(featureSet);
					prefs.setLastDisjointFeatureSetItemUsed(iCurrentIndex);
					coFeatureNameComboBox.setItems(flexFeatureNames);
					coFeatureNameComboBox.getSelectionModel().select(featureSet.getCoFeatureName());

					int numPairings = featureSet.getDisjointFeatureValuePairings().size();
					pairingsSlider.setValue(numPairings);

					flexFeature1ComboBox.setItems(flexFeatureMinusCoFeatureNames);
					flexFeature1ComboBox.getSelectionModel().select(featureSet.getDisjointFeatureValuePairings().get(0).getFlexFeatureName());
					flexFeature2ComboBox.setItems(flexFeatureMinusCoFeatureNames);
					flexFeature2ComboBox.getSelectionModel().select(featureSet.getDisjointFeatureValuePairings().get(1).getFlexFeatureName());
					flexFeature3ComboBox.setItems(flexFeatureMinusCoFeatureNames);
					String s3Feature = kEmpty;
					if (numPairings >= 3) {
						s3Feature = featureSet.getDisjointFeatureValuePairings().get(2).getFlexFeatureName();
					}
					flexFeature3ComboBox.getSelectionModel().select(s3Feature);
					flexFeature4ComboBox.setItems(flexFeatureMinusCoFeatureNames);
					String s4Feature = kEmpty;
					if (numPairings >= 4) {
						s4Feature = featureSet.getDisjointFeatureValuePairings().get(3).getFlexFeatureName();
					}
					flexFeature4ComboBox.getSelectionModel().select(s4Feature);
					flexFeature5ComboBox.setItems(flexFeatureMinusCoFeatureNames);
					String s5Feature = kEmpty;
					if (numPairings >= 5) {
						s5Feature = featureSet.getDisjointFeatureValuePairings().get(4).getFlexFeatureName();
					}
					flexFeature5ComboBox.getSelectionModel().select(s5Feature);
					flexFeature6ComboBox.setItems(flexFeatureMinusCoFeatureNames);
					String s6Feature = kEmpty;
					if (numPairings >= 6) {
						s6Feature = featureSet.getDisjointFeatureValuePairings().get(5).getFlexFeatureName();
					}
					flexFeature6ComboBox.getSelectionModel().select(s6Feature);

					coFeatureValue1ComboBox.setItems(flexFeatureValues);
					coFeatureValue1ComboBox.getSelectionModel().select(featureSet.getDisjointFeatureValuePairings().get(0).getCoFeatureValue());
					coFeatureValue2ComboBox.setItems(flexFeatureValues);
					coFeatureValue2ComboBox.getSelectionModel().select(featureSet.getDisjointFeatureValuePairings().get(1).getCoFeatureValue());
					coFeatureValue3ComboBox.setItems(flexFeatureValues);
					String s3Value = kEmpty;
					if (numPairings >= 3) {
						s3Value = featureSet.getDisjointFeatureValuePairings().get(2).getCoFeatureValue();
					}
					coFeatureValue3ComboBox.getSelectionModel().select(s3Value);
					coFeatureValue4ComboBox.setItems(flexFeatureValues);
					String s4Value = kEmpty;
					if (numPairings >= 4) {
						s4Value = featureSet.getDisjointFeatureValuePairings().get(3).getCoFeatureValue();
					}
					coFeatureValue4ComboBox.getSelectionModel().select(s4Value);
					coFeatureValue5ComboBox.setItems(flexFeatureValues);
					String s5Value = kEmpty;
					if (numPairings >= 5) {
						s5Value = featureSet.getDisjointFeatureValuePairings().get(4).getCoFeatureValue();
					}
					coFeatureValue5ComboBox.getSelectionModel().select(s5Value);
					coFeatureValue6ComboBox.setItems(flexFeatureValues);
					String s6Value = kEmpty;
					if (numPairings >= 6) {
						s6Value = featureSet.getDisjointFeatureValuePairings().get(5).getCoFeatureValue();
					}
					coFeatureValue6ComboBox.getSelectionModel().select(s6Value);
				} else {
					// FeatureSet is null, remove all the text.
					nameField.setText("");
					coFeatureNameComboBox.getSelectionModel().select(kEmpty);
				}
				enableDisableButtons();
			}
		});
	}

	private void createFLExFeatureNames(DisjointFeatureSet featureSet) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				flexFeatureNames.clear();
				flexFeatureMinusCoFeatureNames.clear();
				flexFeatures.clear();
				if (featureSet != null) {
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
					createCoFeatureValues(featureSet.getCoFeatureName());
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
				if (iLastIndex > -1) {
					currentFeatureSet = disjointFeatureSets.get(iLastIndex);
				} else {
					currentFeatureSet = new DisjointFeatureSet();
				}
				currentFeatureSet.setBundle(bundle);
				// following is used to ensure the language column starts out with the correct value
				currentFeatureSet.setLanguage(currentFeatureSet.getLanguage());
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

	// code taken from
	// http://bekwam.blogspot.com/2014/10/cut-copy-and-paste-from-javafx-menubar.html
	TextField[] createTextFields() {
		return new TextField[] { nameField };
	}

}
