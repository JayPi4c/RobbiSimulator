package com.JayPi4c.RobbiSimulator.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.NotificationPane;

import java.util.concurrent.*;

import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.i18n;

@Slf4j
public class NotificationController {

    private final NotificationPane notificationPane;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = Executors.defaultThreadFactory().newThread(r);
        thread.setDaemon(true);
        return thread;
    });
    private Future<?> schedulerFuture;

    public NotificationController(Node node) {
        notificationPane = new NotificationPane(node);
        notificationPane.setShowFromTop(false);
        notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
    }

    public Parent getScene() {
        return notificationPane;
    }

    /**
     * Shows a message in a notification pane. The message will be shown for the given timeout in milliseconds.
     * If a message is already shown, it will be replaced by the new message and the timeout will be reset.
     *
     * @param timeout in milliseconds
     * @param key     the key of the message
     * @param args    the arguments for the message
     */
    public void showMessage(int timeout, String key, Object... args) {
        if (schedulerFuture != null && !schedulerFuture.isDone()) {
            schedulerFuture.cancel(true);
        }
        logger.info("Showing snackbar-message: {}; {}", key, i18n(key, args));
        notificationPane.show(i18n(key, args));
        schedulerFuture = scheduler.schedule(notificationPane::hide, timeout, TimeUnit.MILLISECONDS);
    }

}
