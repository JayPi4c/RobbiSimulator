package com.JayPi4c.RobbiSimulator.controller;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;

import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnackbarController {

    private JFXSnackbar snackbar;

    public SnackbarController(Pane root) {
        snackbar = new JFXSnackbar(root);
    }

    public void showMessage(String message) {
        logger.debug("Showing snackbar-message: {}", message);
        snackbar.fireEvent(new SnackbarEvent(new JFXSnackbarLayout(message)));
    }

}
