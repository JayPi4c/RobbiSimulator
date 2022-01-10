package com.JayPi4c.RobbiSimulator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	private static final Logger logger = LogManager.getLogger(App.class);

	@Override
	public void start(Stage primaryStage) {
		logger.info("Starting application");
		logger.debug("Creating scene");
		ProgramController.createAndShow(ProgramController.DEFAULT_ROBBI_FILE_NAME);
		logger.debug("Scene creation done");
	}

	/**
	 * Application entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() {
		logger.info("Initialize application");

		logger.debug("Loading Program Controller");
		if (!ProgramController.initialize()) {
			logger.error("Failed to load Program Controller");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Messages.getString("Init.error.title"));
			alert.setHeaderText(Messages.getString("Init.error.header"));
			alert.setContentText(Messages.getString("Init.error.message"));
			alert.showAndWait();
			Platform.exit();
		}
		logger.debug("loading Program Controller successfully");

		logger.debug("Loading images");
		MainStage.loadImages();
		TerritoryPanel.loadImages();
		logger.debug("Finished loading images");

	}

	@Override
	public void stop() {
		// do some final serialization
		logger.info("Quitting application");
	}

}