package com.JayPi4c.RobbiSimulator;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * 
 * Hauptklasse des Robbi Simulators.<br>
 * Javaversion: 17 <br>
 * JavaFX: 17<br>
 * 
 * @author Jonas Pohl
 *
 */
public class App extends Application {

	Logger logger = Logger.getLogger(App.class.getName());

	@Override
	public void start(Stage primaryStage) {
		logger.log(Level.INFO, "starting application");
		logger.log(Level.INFO, "creating scene");
		ProgramController.createAndShow(ProgramController.DEFAULT_ROBBI_FILE_NAME);
		logger.log(Level.INFO, "Scene creation done");
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() {
		// logger.setLevel(Level.WARNING);
		logger.log(Level.INFO, "initialize application");

		logger.log(Level.INFO, "loading Program Controller");
		if (!ProgramController.initialize()) {
			logger.log(Level.SEVERE, "Failed to load Program Controller");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Messages.getString("Init.error.title"));
			alert.setHeaderText(Messages.getString("Init.error.header"));
			alert.setContentText(Messages.getString("Init.error.message"));
			alert.showAndWait();
			Platform.exit();
		}
		logger.log(Level.INFO, "loading Program Controller successfully");

		logger.log(Level.INFO, "Loading images");
		MainStage.loadImages();
		TerritoryPanel.loadImages();
		logger.log(Level.INFO, "Finished loading images");

	}

	@Override
	public void stop() {
		// do some final serialization
		logger.log(Level.INFO, "Quitting application");
	}

}