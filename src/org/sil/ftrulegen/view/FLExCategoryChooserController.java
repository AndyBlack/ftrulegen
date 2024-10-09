/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.sil.ftrulegen.ApplicationPreferences;
import org.sil.ftrulegen.flexmodel.FLExCategory;
import org.sil.ftrulegen.model.Category;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * 
 */
public class FLExCategoryChooserController implements Initializable {

	@FXML
	ListView<FLExCategory> lvCategories;

	Stage dialogStage;
	ApplicationPreferences prefs;
	FLExCategory catChosen = null;
	boolean okClicked = false;

	public boolean isOkClicked() {
		return okClicked;
	}

	public void setDialogStage(Stage value) {
		dialogStage = value;
		prefs = ApplicationPreferences.getInstance();
		dialogStage = prefs.getLastWindowParameters(ApplicationPreferences.LAST_CATEGORY_CHOOSER, dialogStage, 300.0, 240.0);
	}

	public void setXandYCoordinates(double x, double y) {
		dialogStage.setX(x);
		dialogStage.setY(y);
	}

	public void setCategories(List<FLExCategory> categories) {
		lvCategories.getItems().addAll(categories);
	}

	public FLExCategory getCategoryChosen() {
		return catChosen;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lvCategories.setOnKeyPressed(keyEvent -> {
			switch (keyEvent.getCode()) {
			case ENTER:
				handleOK();
				break;
			case ESCAPE:
				handleCancel();
				break;
			default:
				break;
			}
		});
		lvCategories.setOnMouseClicked(mouseEvent -> {
			if (mouseEvent.getClickCount() >= 2) {
				handleOK();
			}
		});
	}

	public void selectFLExCategory(Category cat) {
		for (FLExCategory fcat : lvCategories.getItems()) {
			if (fcat.getAbbreviation().equals(cat.getName())) {
				catChosen = fcat;
				break;
			}
		}
		if (catChosen != null) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					lvCategories.requestFocus();
					int index = lvCategories.getItems().indexOf(catChosen);
					lvCategories.getSelectionModel().select(index);
					lvCategories.scrollTo(index);
				}
			});
		}
	}

	public void handleOK() {
		catChosen = lvCategories.getSelectionModel().getSelectedItem();
		okClicked = true;
		handleCancel();
	}

	public void handleCancel() {
		// set any preferences
		prefs.setLastWindowParameters(ApplicationPreferences.LAST_CATEGORY_CHOOSER, dialogStage);
		dialogStage.close();
	}
}
