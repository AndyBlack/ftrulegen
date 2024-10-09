/**
 * Copyright (c) 2023-2024 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.sil.ftrulegen.ApplicationPreferences;
import org.sil.ftrulegen.flexmodel.FLExFeature;
import org.sil.ftrulegen.flexmodel.FLExFeatureValue;
import org.sil.ftrulegen.model.Feature;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 */
public class FLExFeatureValueChooserController implements Initializable {

	@FXML
	ListView<FLExFeatureValue> lvFeatureValues;
	@FXML
	Label lblUnmarked;

	Stage dialogStage;
	ApplicationPreferences prefs;
	FLExFeatureValue featureValueChosen = null;
	boolean okClicked = false;
	int index = 0;

	public boolean isOkClicked() {
		return okClicked;
	}

	public void setDialogStage(Stage value) {
		dialogStage = value;
		prefs = ApplicationPreferences.getInstance();
		dialogStage = prefs.getLastWindowParameters(ApplicationPreferences.LAST_FEATURE_CHOOSER, dialogStage, 240.0, 290.0);
	}

	public void setXandYCoordinates(double x, double y) {
		dialogStage.setX(x);
		dialogStage.setY(y);
	}

	public void setFeatures(List<FLExFeature> features) {
		lvFeatureValues.getItems().clear();
		for (FLExFeature feature : features) {
			lvFeatureValues.getItems().addAll(feature.getValues());
		}
	}

	public void ShowUnmarkedlabel(boolean show) {
		lblUnmarked.setVisible(show);
		if (!show) {
			AnchorPane.setTopAnchor(lvFeatureValues, 0.0);
		}
	}

	public FLExFeatureValue getFeatureValueChosen() {
		return featureValueChosen;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		lvFeatureValues.setOnKeyPressed(keyEvent -> {
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
		lvFeatureValues.setOnMouseClicked(mouseEvent -> {
			if (mouseEvent.getClickCount() >= 2) {
				handleOK();
			}
		});
	}

	public void selectFLExFeatureValue(Feature feat) {
		for (int i = 0; i < lvFeatureValues.getItems().size(); i++) {
			FLExFeatureValue ffv = lvFeatureValues.getItems().get(i);
			if (ffv.getFeature().getName().equals(feat.getLabel())
					&& ffv.getAbbreviation().equals(feat.getMatchOrValue())) {
				featureValueChosen = ffv;
				index = i;
				break;
			}
		}
		if (featureValueChosen != null) {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					lvFeatureValues.requestFocus();
					lvFeatureValues.getSelectionModel().select(index);
					lvFeatureValues.scrollTo(index);
				}
			});
		}
	}

	public void handleOK() {
		featureValueChosen = lvFeatureValues.getSelectionModel().getSelectedItem();
		okClicked = true;
		handleCancel();
	}

	public void handleCancel() {
		// set any preferences
		prefs.setLastWindowParameters(ApplicationPreferences.LAST_FEATURE_CHOOSER, dialogStage);
		dialogStage.close();
	}
}
