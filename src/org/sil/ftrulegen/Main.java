/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen;
	
import java.util.Locale;
import java.util.ResourceBundle;

import org.sil.ftrulegen.view.MainController;
import org.sil.utility.view.ControllerUtilities;
import org.sil.ftrulegen.Constants;
//import org.sil.utility.view.ControllerUtilities;
//import org.sil.utility.view.ObservableResourceFactory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class Main extends Application {
	private Locale locale;
	private static final String kApplicationIconResource = "file:resources/FLExTransWindowIcon.png";

	@FXML
	private BorderPane mainPane;
	@FXML
	private WebView browser;
	@FXML
	private WebEngine webEngine;

	@Override
	public void start(Stage primaryStage) {
		try {
//			locale = new Locale(applicationPreferences.getLastLocaleLanguage());
			locale = new Locale("en");
			BorderPane root = new BorderPane();
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/fxml/Main.fxml"));
			ResourceBundle bundle = ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, locale);
			loader.setResources(bundle);
			mainPane = (BorderPane) loader.load();
			Scene scene = new Scene(mainPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle(loader.getResources().getString("main.Title"));
			primaryStage.getIcons().add(getNewMainIconImage());

			MainController controller = loader.getController();
			if (controller != null)
			{
				controller.setStage(primaryStage);
			}
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
//	@Override
	public Image getNewMainIconImage() {
		Image img = ControllerUtilities.getIconImageFromURL(kApplicationIconResource,
				Constants.RESOURCE_SOURCE_LOCATION);
		return img;
	}

}
