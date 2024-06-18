package com.JayPi4c.RobbiSimulator.controller.tutor;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Controller to handle actions of a tutor.
 *
 * @author Jonas Pohl
 */
@Slf4j
public class TutorController {

    /**
     * Code to bind a tutor instance to the RMI instance.
     */
    public static final String TUTOR_CODE = "Tutor";
    private static final int NO_ID = -1;
    // language keys
    private static final String MENU_TUTOR_LOADREQUEST_SUCCESS = "Menu.tutor.loadRequest.success";
    private static final String MENU_TUTOR_LOADREQUEST_WARNING = "Menu.tutor.loadRequest.warning";
    private static final String MENU_TUTOR_SAVEANSWER_INFORMATION = "Menu.tutor.saveAnswer.information";
    private static Tutor tutor;
    private static Registry registry;
    private MainStage stage;
    private int currentID = NO_ID;

    /**
     * Constructor to create a new TutorController.
     *
     * @param mainStage the stage, the controller is for
     */
    public TutorController(MainStage mainStage) {
        this.stage = mainStage;
        mainStage.getMenubar().getLoadRequestMenuItem().setOnAction(e -> loadRequest());
        mainStage.getMenubar().getSaveAnswerMenuItem().setOnAction(e -> saveAnswer());
        mainStage.getMenubar().getLoadRequestMenuItem().setDisable(false);
        mainStage.getMenubar().getSaveAnswerMenuItem().setDisable(true);
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

    /**
     * Helper to handle loadRequest actions
     */
    private void loadRequest() {
        tutor.getNewRequest().ifPresentOrElse(request -> {
            logger.debug("Loading request with id {}.", request.id());
            stage.getProgram().setEditorContent(request.code());
            stage.getProgram().save();
            ProgramController.compile(stage.getProgram(), false, stage);
            stage.getTerritory().fromXML(new ByteArrayInputStream(request.territory().getBytes()));
            currentID = request.id();
            stage.getMenubar().getLoadRequestMenuItem().setDisable(true);
            stage.getMenubar().getSaveAnswerMenuItem().setDisable(false);
            stage.getNotificationController().showMessage(3000, MENU_TUTOR_LOADREQUEST_SUCCESS, request.id());
        }, () -> {
            logger.debug("no request available");
            stage.getNotificationController().showMessage(3000, MENU_TUTOR_LOADREQUEST_WARNING);
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
        stage.getMenubar().getLoadRequestMenuItem().setDisable(false);
        stage.getMenubar().getSaveAnswerMenuItem().setDisable(true);
        stage.getNotificationController().showMessage(3000, MENU_TUTOR_SAVEANSWER_INFORMATION);
    }

}
