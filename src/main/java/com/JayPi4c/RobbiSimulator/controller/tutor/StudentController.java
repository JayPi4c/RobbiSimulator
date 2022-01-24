package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.JayPi4c.RobbiSimulator.view.MainStage;

public class StudentController {

	int requestID;

	private static final int NO_ID = -1;

	private MainStage stage;

	public StudentController(MainStage stage) {
		this.stage = stage;
		requestID = NO_ID;
		stage.getSendRequestMenuItem().setOnAction(e -> sendRequest());
		stage.getReceiveAnswerMenuItem().setOnAction(e -> receiveAnswer());
	}

	private void receiveAnswer() {
		requestID = NO_ID;
	}

	private void sendRequest() {

		if (requestActive())
			return;

		try {
			Registry registry = LocateRegistry.getRegistry(2345); // TODO change to properties
			ITutor tutor = (ITutor) registry.lookup("Tutor");
			requestID = tutor.sendRequest(stage.getProgram().getEditorContent(),
					stage.getTerritory().toXML().toString());
			System.out.println("ID is " + requestID);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	private boolean requestActive() {
		return requestID != NO_ID;
	}
}
