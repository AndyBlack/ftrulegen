/**
 * Copyright (c) 2023 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.ftrulegen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.sil.ftrulegen.view.MainController;
import org.sil.utility.view.ControllerUtilities;
import org.sil.utility.view.ObservableResourceFactory;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	private Locale locale;
	static String[] arguments;
	private Stage primaryStage;
	MainController controller;
	int maxVariables = 4;
	Image flexTransImage;
	private ApplicationPreferences applicationPreferences;

	@FXML
	private BorderPane mainPane;

	// following lines from
	// https://stackoverflow.com/questions/32464974/javafx-change-application-language-on-the-run
	private static final ObservableResourceFactory RESOURCE_FACTORY = ObservableResourceFactory.getInstance();
	static {
		RESOURCE_FACTORY.setResources(ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, new Locale("en")));
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			applicationPreferences = ApplicationPreferences.getInstance();
			applicationPreferences.setObject(this);
			locale = new Locale(applicationPreferences.getLastLocaleLanguage());

			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/fxml/Main.fxml"));
			ResourceBundle bundle = ResourceBundle.getBundle(Constants.RESOURCE_LOCATION, locale);
			loader.setResources(bundle);
			if (!checkArguments(bundle)) {
				System.exit(1);
			}
			Path workingFilesPath = checkExistanceOfCSSFiles();
			mainPane = (BorderPane) loader.load();
			Scene scene = new Scene(mainPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle(loader.getResources().getString("main.Title"));
			primaryStage.getIcons().add(getNewMainIconImage());
			this.primaryStage = primaryStage;
			restoreWindowSettings();
			controller = loader.getController();
			if (controller != null) {
				double dividerPosition = applicationPreferences.getLastSplitPaneDividerPosition();
				controller.getSplitPane().setDividerPosition(0, dividerPosition);
				int lastSelectedRule = applicationPreferences.getLastSelectedRule();
				controller.setSelectedRuleIndex(lastSelectedRule);
				controller.setStage(primaryStage);
				controller.setRuleGenFile(arguments[0]);
				controller.setFLexDataFile(arguments[1]);
				controller.loadDataFiles();
				controller.setMaxVariables(maxVariables);
				controller.setFLExTransImage(flexTransImage);
				controller.setWebPageFile(workingFilesPath.toString() + File.separatorChar + Constants.HTML_FILE_NAME);
				controller.setWebPageFileUri(Paths.get(workingFilesPath.toString()).toUri().toString() + "/" + Constants.HTML_FILE_NAME);
			}
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
	}

	public static void main(String[] args) {
		arguments = args;
		launch(args);
	}

	@Override
	public void stop() throws IOException {
		applicationPreferences.setLastWindowParameters(ApplicationPreferences.LAST_WINDOW,
				primaryStage);
		double[] dividers = controller.getSplitPane().getDividerPositions();
		applicationPreferences.setLastSplitPaneDividerPosition(dividers[0]);
		applicationPreferences.setLastLocaleLanguage(locale.getLanguage());
		applicationPreferences.setLastSelectedRule(controller.getSelectedRuleIndex());
		if (controller.isDirty()) {
			controller.askAboutSaving();
		}
	}

	private Boolean checkArguments(ResourceBundle bundle) {
		if (arguments.length < 2 || arguments.length > 3) {
			writeHelp(bundle);
			return false;
		}

		if (!Files.exists(Paths.get(arguments[0]))) {
			System.out.println(bundle.getString("main.RuleFileNotFound"));
			return false;
		}

		if (!Files.exists(Paths.get(arguments[1]))) {
			System.out.println(bundle.getString("main.FLExDataSourceTargetFileNotFound"));
			return false;
		}

		if (arguments.length >= 3) {
			try {
				maxVariables = Integer.parseInt(arguments[2]);
			} catch (NumberFormatException e) {
				Object[] args = { arguments[2] };
				MessageFormat msgFormatter = new MessageFormat("");
				msgFormatter.setLocale(new Locale("en"));
				msgFormatter.applyPattern(RESOURCE_FACTORY.getStringBinding("main.MaxVariablesNotAnInteger").get());
				System.out.println(msgFormatter.format(args));
				return false;
			}
		}

		return true;
	}

	private Path checkExistanceOfCSSFiles() throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");
		Path workingFilesPath = Paths.get(tempDir, Constants.CSS_FILES_LOCATION);
		if (!Files.exists(workingFilesPath)) {
			String appDir = System.getProperty("user.dir");
			String cssFiles = appDir + File.separatorChar + "CSSFiles";
			Main.copyDirectory(cssFiles, workingFilesPath.toString());
		}
		return workingFilesPath;
	}

	private void restoreWindowSettings() {
		primaryStage = applicationPreferences.getLastWindowParameters(
				ApplicationPreferences.LAST_WINDOW, primaryStage, 660.0, 1000.);
	}

//	@Override
	public Image getNewMainIconImage() {
		flexTransImage = ControllerUtilities.getIconImageFromURL(Constants.APPLICATION_ICON_RESOURCE,
				Constants.RESOURCE_SOURCE_LOCATION);
		return flexTransImage;
	}

	private static void writeHelp(ResourceBundle bundle) {
		System.out.println(bundle.getString("main.HelpTitle"));
		System.out.println();
		System.out.println(bundle.getString("main.CommandLineTemplate"));
		System.out.println();
		System.out.println(bundle.getString("main.RuleFile"));
		System.out.println(bundle.getString("main.FLExDataSourceTargetFile"));
		System.out.println(bundle.getString("main.OptionalMaxVariables"));
	}

//	Following is from <https://www.baeldung.com/java-copy-directory>
	public static void copyDirectory(String sourceDirectoryLocation,
			String destinationDirectoryLocation) throws IOException {
		Files.walk(Paths.get(sourceDirectoryLocation)).forEach(
				source -> {
					Path destination = Paths.get(destinationDirectoryLocation, source.toString()
							.substring(sourceDirectoryLocation.length()));
					try {
						Files.copy(source, destination);
					} catch (IOException e) {
						e.printStackTrace();
						System.out.print(e.getMessage());
					}
				});
	}
}
