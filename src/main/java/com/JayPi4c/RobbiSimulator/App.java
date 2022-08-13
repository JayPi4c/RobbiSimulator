package com.JayPi4c.RobbiSimulator;

import java.util.ResourceBundle;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.controller.tutor.TutorController;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.HibernateUtils;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Hauptklasse des Robbi Simulators.<br>
 * Javaversion: 17 <br>
 * JavaFX: 17<br>
 * 
 * @author Jonas Pohl
 *
 */
@Slf4j
public class App extends Application {

	/**
	 * Application entry point
	 * 
	 * @param args the arguments from the command line
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() {
		logger.info("Initialize application");

		logger.debug("Initializing properties...");
		if (PropertiesLoader.initialize()) {
			logger.debug("Loaded properties successfully");
		} else
			logger.debug("Failed to load properties.");

		I18nUtils.setBundle(ResourceBundle.getBundle("lang.messages", PropertiesLoader.getLocale()));

		logger.debug("Loading Program Controller");
		if (!ProgramController.initialize()) {
			logger.error("Failed to load Program Controller");
			AlertHelper.showAlertAndWait(AlertType.ERROR, I18nUtils.i18n("Init.error.message"), null, null,
					I18nUtils.i18n("Init.error.title"), I18nUtils.i18n("Init.error.header"));
			Platform.exit();
		}
		logger.debug("loading Program Controller successfully");

		if (PropertiesLoader.isTutor()) {
			logger.debug("Starting Tutor RMI server");
			if (TutorController.initialize())
				logger.debug("RMI server started");
			else
				logger.debug("Failed to initialize RMI server.");
		}
	}

	@Override
	public void start(Stage primaryStage) {
		logger.info("Starting application");
		logger.debug("Creating scene");
		ProgramController.createAndShow(ProgramController.DEFAULT_ROBBI_FILE_NAME);
		logger.debug("Scene creation done");
	}

	@Override
	public void stop() {

		logger.debug("Shutting down database connection");
		HibernateUtils.shutdown();

		if (PropertiesLoader.isTutor()) {
			logger.debug("Stopping Tutor-Server.");
			if (TutorController.shutdown())
				logger.debug("Tutor RMI server stopped successfully.");
			else
				logger.debug("Failed to shutdown Tutor RMI server");
		}

		logger.debug("saving properties...");
		if (PropertiesLoader.finish())
			logger.debug("Properties saved");
		else
			logger.debug("Failed to save properties");

		logger.info("Quitting application");
	}

}