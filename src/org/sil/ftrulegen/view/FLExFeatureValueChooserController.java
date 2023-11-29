/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.view;

import java.util.List;

import org.sil.ftrulegen.Main;
import org.sil.ftrulegen.flexmodel.FLExFeature;
import org.sil.ftrulegen.flexmodel.FLExFeatureValue;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * 
 */
public class FLExFeatureValueChooserController {
	
	@FXML
	ListView<FLExFeatureValue> lvFeatureValues;
	
	Stage dialogStage;
	Main main;
	FLExFeatureValue featureValueChosen = null;
	boolean okClicked = false;
    int maxVariables = 4;
	String[] variables = { "α", "β", "γ", "δ", "ε", "ζ", "η", "θ", "ι", "κ", "μ", "ν" };
    
	public int getMaxVariables() {
		return maxVariables;
	}

	public void setMaxVariables(int maxVariables) {
		this.maxVariables = maxVariables;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void setDialogStage(Stage value) {
		dialogStage = value;
	}
	
	public void setMain(Main value) {
		main = value;
	}

	public void setFeatures(List<FLExFeature> features) {
		lvFeatureValues.getItems().clear();
		for (FLExFeature feature : features) {
			lvFeatureValues.getItems().addAll(feature.getValues());
			for (int i =0; i < maxVariables; i++) {
				FLExFeatureValue variableValue = new FLExFeatureValue(variables[i]);
				variableValue.setFeature(feature);
				lvFeatureValues.getItems().add(variableValue);
			}
		}
	}
	
	public FLExFeatureValue getFeatureValueChosen() {
		return featureValueChosen;
	}
	
	public void handleOK() {
		featureValueChosen = lvFeatureValues.getSelectionModel().getSelectedItem();
		okClicked = true;
		handleCancel();
	}
	
	public void handleCancel() {
		// set any preferences
		dialogStage.close();
	}
}
