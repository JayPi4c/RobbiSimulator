package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.JayPi4c.RobbiSimulator.view.MainStage;

public class TutorController {
	private static ITutor tutor;
	private static Registry registry;

	public TutorController(MainStage mainStage) {
		mainStage.getLoadRequestMenuItem().setOnAction(e -> loadRequest());
		mainStage.getSaveAnswerMenuItem().setOnAction(e -> saveAnswer());
	}

	private void loadRequest() {

	}

	private void saveAnswer() {

	}

	public static void initialize() {
		try {
			tutor = new Tutor();

			LocateRegistry.createRegistry(2345); // Load from properties
			registry = LocateRegistry.getRegistry(2345);

			registry.bind("Tutor", tutor);
			System.out.println("done");
		} catch (RemoteException | AlreadyBoundException re) {
			re.printStackTrace();
		}
	}

	public static void shutdown() {
		try {
			registry.unbind("Tutor");
			UnicastRemoteObject.unexportObject(tutor, true);
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
