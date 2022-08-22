package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.io.ByteArrayInputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.scene.control.Alert.AlertType;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle actions of a student.
 * 
 * @author Jonas Pohl
 *
 */
@Slf4j
public class StudentController {

	private int requestID;

	private MainStage stage;

	/**
	 * Constructor to create a new StudentController
	 * 
	 * @param stage the stage, the controller is for
	 */
	public StudentController(MainStage stage) {
		this.stage = stage;
		stage.getSendRequestMenuItem().setOnAction(e -> sendRequest());
		stage.getReceiveAnswerMenuItem().setOnAction(e -> receiveAnswer());
		stage.getSendRequestMenuItem().setDisable(false);
		stage.getReceiveAnswerMenuItem().setDisable(true);
	}

	/**
	 * Helper to handle receiveAnswer actions
	 */
	private void receiveAnswer() {
		try {
			Registry registry = LocateRegistry.getRegistry(PropertiesLoader.getTutorhost(),
					PropertiesLoader.getTutorport());
			ITutor tutor = (ITutor) registry.lookup(TutorController.TUTOR_CODE);
			Answer answer = tutor.getAnswer(requestID);
			if (answer == null) {
				logger.debug("Answer is not ready yet!");
				stage.getSnackbarController().showMessage("Menu.tutor.receiveAnswer.information");
				return;
			}
			stage.getProgram().setEditorContent(answer.code());
			stage.getProgram().save();
			ProgramController.compile(stage.getProgram(), false, stage);
			stage.getTerritory().fromXML(new ByteArrayInputStream(answer.territory().getBytes()));
			stage.getSendRequestMenuItem().setDisable(false);
			stage.getReceiveAnswerMenuItem().setDisable(true);
		} catch (RemoteException | NotBoundException e) {
			logger.debug("Failed to fetch answer from tutor.");
			AlertHelper.showAlertAndWait(AlertType.ERROR, I18nUtils.i18n("Menu.tutor.receiveAnswer.error"), stage);
		}
	}

	/**
	 * Helper to handle sendRequest actions
	 */
	private void sendRequest() {
		try {
			Registry registry = LocateRegistry.getRegistry(PropertiesLoader.getTutorhost(),
					PropertiesLoader.getTutorport());
			ITutor tutor = (ITutor) registry.lookup(TutorController.TUTOR_CODE);
			Program program = stage.getProgram();
			program.setEdited(true);
			program.save(stage.getTextArea().getText());
			stage.getLanguageController().updateTitle();
			requestID = tutor.sendRequest(program.getEditorContent(), stage.getTerritory().toXML().toString());
			logger.debug("The request has ID {}.", requestID);
			stage.getSendRequestMenuItem().setDisable(true);
			stage.getReceiveAnswerMenuItem().setDisable(false);

			stage.getSnackbarController().showMessage("Menu.tutor.sendRequest.information");

		} catch (RemoteException | NotBoundException e) {
			logger.debug("Failed to send request to tutor.");
			AlertHelper.showAlertAndWait(AlertType.ERROR, I18nUtils.i18n("Menu.tutor.sendRequest.error"), stage);
		}
	}

}
