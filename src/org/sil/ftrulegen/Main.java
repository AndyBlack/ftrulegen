package org.sil.ftrulegen;
	
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class Main extends Application {
	@FXML
	private BorderPane mainPane;
	@FXML
	private WebView browser;
	@FXML
	private WebEngine webEngine;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
			
			primaryStage.setTitle("Rule Generator for FLExTrans");
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/fxml/Main.fxml"));
//			ResourceBundle bundle = ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, locale);
//			loader.setResources(bundle);
			mainPane = (BorderPane) loader.load();
			Scene scene = new Scene(mainPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
//			browser = new WebView();
//			webEngine = browser.getEngine();
////			webEngine.load("file:///C:/ProgramData/SIL/FLExTransRuleGenerator/FLExTransRule.html");
//			webEngine.loadContent("<html><body><div>Hi there!</div</body></html>");

			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
