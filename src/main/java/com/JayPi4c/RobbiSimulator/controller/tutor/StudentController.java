package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.io.ByteArrayInputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.utils.ILanguageChangeListener;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;
import com.JayPi4c.RobbiSimulator.view.MainStage;

public class StudentController implements ILanguageChangeListener {
	private static Logger logger = LogManager.getLogger(StudentController.class);

	private int requestID;

	private MainStage stage;

	public StudentController(MainStage stage) {
		this.stage = stage;
		Messages.registerListener(this);
		stage.getSendRequestMenuItem().setOnAction(e -> sendRequest());
		stage.getReceiveAnswerMenuItem().setOnAction(e -> receiveAnswer());
		stage.getSendRequestMenuItem().setDisable(false);
		stage.getReceiveAnswerMenuItem().setDisable(true);
	}

	private void receiveAnswer() {
		try {
			Registry registry = LocateRegistry.getRegistry(PropertiesLoader.getTutorhost(),
					PropertiesLoader.getTutorport());
			ITutor tutor = (ITutor) registry.lookup(TutorController.TUTOR_CODE);
			Answer answer = tutor.getAnswer(requestID);
			if (answer == null) {
				logger.debug("Answer is not ready yet!");
				return;
			}
			stage.getProgram().setEditorContent(answer.code());
			stage.getTerritory().fromXML(new ByteArrayInputStream(answer.territory().getBytes()));
			stage.getSendRequestMenuItem().setDisable(false);
			stage.getReceiveAnswerMenuItem().setDisable(true);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			logger.debug("Failed to fetch answer from tutor.");
		}
	}

	private void sendRequest() {
		try {
			Registry registry = LocateRegistry.getRegistry(PropertiesLoader.getTutorhost(),
					PropertiesLoader.getTutorport());
			ITutor tutor = (ITutor) registry.lookup(TutorController.TUTOR_CODE);
			requestID = tutor.sendRequest(stage.getProgram().getEditorContent(),
					stage.getTerritory().toXML().toString());
			logger.debug("The request has ID {}.", requestID);
			stage.getSendRequestMenuItem().setDisable(true);
			stage.getReceiveAnswerMenuItem().setDisable(false);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			logger.debug("Failed to send request to tutor.");
		}
	}

	@Override
	public void onLanguageChanged() {
		// TODO
	}

}
