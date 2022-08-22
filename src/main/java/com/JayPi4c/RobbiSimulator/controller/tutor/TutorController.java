package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.io.ByteArrayInputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller to handle actions of a tutor.
 * 
 * @author Jonas Pohl
 *
 */
@Slf4j
public class TutorController {

	private static Tutor tutor;
	private static Registry registry;

	private MainStage stage;

	private static final int NO_ID = -1;
	private int currentID = NO_ID;

	/**
	 * Code to bind a tutor instance to the RMI instance.
	 */
	public static final String TUTOR_CODE = "Tutor";

	/**
	 * Constructor to create a new TutorController.
	 * 
	 * @param mainStage the stage, the controller is for
	 */
	public TutorController(MainStage mainStage) {
		this.stage = mainStage;
		mainStage.getLoadRequestMenuItem().setOnAction(e -> loadRequest());
		mainStage.getSaveAnswerMenuItem().setOnAction(e -> saveAnswer());
		mainStage.getLoadRequestMenuItem().setDisable(false);
		mainStage.getSaveAnswerMenuItem().setDisable(true);
	}

	/**
	 * Helper to handle loadRequest actions
	 * 
	 */
	private void loadRequest() {
		tutor.getNewRequest().ifPresentOrElse(request -> {
			logger.debug("Loading request with id {}.", request.id());
			stage.getProgram().setEditorContent(request.code());
			stage.getProgram().save();
			ProgramController.compile(stage.getProgram(), false, stage);
			stage.getTerritory().fromXML(new ByteArrayInputStream(request.territory().getBytes()));
			currentID = request.id();
			stage.getLoadRequestMenuItem().setDisable(true);
			stage.getSaveAnswerMenuItem().setDisable(false);
			stage.getSnackbarController().showMessage("Menu.tutor.loadRequest.success", request.id());
		}, () -> {
			logger.debug("no request available");
			stage.getSnackbarController().showMessage("Menu.tutor.loadRequest.warning");
		});
	}

	/**
	 * Helper to handle save Answer actions
	 */
	private void saveAnswer() {
		logger.debug("Saving answer for id {}.", currentID);
		stage.getProgram().save(stage.getTextArea().getEditor().getDocument().getText());
		tutor.setAnswer(currentID,
				new Answer(stage.getProgram().getEditorContent(), stage.getTerritory().toXML().toString()));
		currentID = NO_ID;
		stage.getLoadRequestMenuItem().setDisable(false);
		stage.getSaveAnswerMenuItem().setDisable(true);
		stage.getSnackbarController().showMessage("Menu.tutor.saveAnswer.information");
	}

	/**
	 * Creates a new RMI registry and gets ready to accept connections.
	 * 
	 * @return true if the initialization finished successfully, false otherwise
	 */
	public static boolean initialize() {
		try {
			tutor = new Tutor();

			registry = LocateRegistry.createRegistry(PropertiesLoader.getTutorport());

			registry.bind(TUTOR_CODE, tutor);
		} catch (RemoteException | AlreadyBoundException re) {
			return false;
		}
		return true;
	}

	/**
	 * Unbinds all previously binded instances.
	 * 
	 * @return true if the unbinding was successful, false otherwise
	 */
	public static boolean shutdown() {
		try {
			registry.unbind(TUTOR_CODE);
			UnicastRemoteObject.unexportObject(tutor, true);
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (RemoteException | NotBoundException e) {
			return false;
		}
		return true;
	}

}
