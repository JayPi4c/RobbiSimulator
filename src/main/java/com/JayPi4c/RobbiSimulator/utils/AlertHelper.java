package com.JayPi4c.RobbiSimulator.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Window;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Static class to create Alerts with the necessary information.
 *
 * @author Jonas Pohl
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertHelper {

    /**
     * Creates and shows an alert with the given information.
     *
     * @param type    the AlertType of the alert
     * @param message the contentText for the alert
     * @param owner   the alerts owner to place it relative to the parent
     */
    public static void showAlertAndWait(AlertType type, String message, Window owner) {
        Alert alert = createAlert(type, message, owner);
        alert.showAndWait();
    }

    /**
     * Creates and shows an alert with the given information.
     *
     * @param type     the AlertType of the alert
     * @param message  the contentText for the alert
     * @param owner    the alerts owner to place it relative to the parent
     * @param modality the modality of the alert
     */
    public static void showAlertAndWait(AlertType type, String message, Window owner, Modality modality) {
        Alert alert = createAlert(type, message, owner, modality);
        alert.showAndWait();
    }

    /**
     * Creates and shows an alert with the given information.
     *
     * @param type     the AlertType of the alert
     * @param message  the contentText for the alert
     * @param owner    the alerts owner to place it relative to the parent
     * @param modality the modality of the alert
     * @param title    the title for the alert
     * @param header   the header for the alert
     */
    public static void showAlertAndWait(AlertType type, String message, Window owner, Modality modality, String title,
                                        String header) {
        Alert alert = createAlert(type, message, owner, modality, title, header);
        alert.showAndWait();
    }

    /**
     * Creates an alert with the given information.
     *
     * @param type     the AlertType of the alert
     * @param message  the contentText for the alert
     * @param owner    the alerts owner to place it relative to the parent
     * @param modality the modality of the alert
     * @param title    the title for the alert
     * @param header   the header for the alert
     * @return the alert with the given information
     */
    public static Alert createAlert(AlertType type, String message, Window owner, Modality modality, String title,
                                    String header) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        if (title != null)
            alert.setTitle(title);
        if (header != null)
            alert.setHeaderText(header);
        alert.initModality(modality);
        alert.initOwner(owner);
        return alert;
    }

    /**
     * Creates an alert with the given information.
     *
     * @param type     the AlertType of the alert
     * @param message  the contentText for the alert
     * @param owner    the alerts owner to place it relative to the parent
     * @param modality the modality of the alert
     * @return the alert with the given information
     */
    public static Alert createAlert(AlertType type, String message, Window owner, Modality modality) {
        return createAlert(type, message, owner, modality, null, null);
    }

    /**
     * Creates an alert with the given information.
     *
     * @param type    the AlertType of the alert
     * @param message the contentText for the alert
     * @param owner   the alerts owner to place it relative to the parent
     * @return the alert with the given information
     */
    public static Alert createAlert(AlertType type, String message, Window owner) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.initOwner(owner);
        return alert;
    }

}
