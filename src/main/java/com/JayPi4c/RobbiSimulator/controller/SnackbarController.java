package com.JayPi4c.RobbiSimulator.controller;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.i18n;

@Slf4j
public class SnackbarController {

    private JFXSnackbar snackbar;

    public SnackbarController(Pane root) {
        snackbar = new JFXSnackbar(root);
    }

    public void showMessage(String key, Object... args) {
        logger.debug("Showing snackbar-message: {}; {}", key, i18n(key, args));
        snackbar.fireEvent(new SnackbarEvent(new JFXSnackbarLayout(i18n(key, args))));
    }

}
