/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.view;

import java.util.List;

import org.sil.ftrulegen.Main;
import org.sil.ftrulegen.flexmodel.FLExCategory;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * 
 */
public class FLExCategoryChooserController {
	
	@FXML
	ListView<FLExCategory> lvCategories;
	
	Stage dialogStage;
	Main main;
	FLExCategory catChosen = null;
	boolean okClicked = false;

	public boolean isOkClicked() {
		return okClicked;
	}

	public void setDialogStage(Stage value) {
		dialogStage = value;
	}
	
	public void setMain(Main value) {
		main = value;
	}

	public void setCategories(List<FLExCategory> categories) {
		lvCategories.getItems().addAll(categories);
	}
	
	public FLExCategory getCategoryChosen() {
		return catChosen;
	}
	
	public void handleOK() {
		catChosen = lvCategories.getSelectionModel().getSelectedItem();
		okClicked = true;
		handleCancel();
	}
	
	public void handleCancel() {
		// set any preferences
		dialogStage.close();
	}
}
