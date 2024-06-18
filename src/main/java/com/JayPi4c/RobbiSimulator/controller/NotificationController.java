package com.JayPi4c.RobbiSimulator.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.NotificationPane;

import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.i18n;

@Slf4j
public class NotificationController {

    private final NotificationPane notificationPane;
    private final Timeline hideTimeline = new Timeline();

    public NotificationController(Node node) {
        notificationPane = new NotificationPane(node);
        notificationPane.setShowFromTop(false);
        notificationPane.getStyleClass().add(NotificationPane.STYLE_CLASS_DARK);
        hideTimeline.setCycleCount(1);
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
        hideTimeline.stop();
        notificationPane.show(i18n(key, args));
        KeyFrame kf = new KeyFrame(Duration.millis(timeout), e -> {
            if (notificationPane.isShowing())
                notificationPane.hide();
        });
        hideTimeline.getKeyFrames().setAll(kf);
        hideTimeline.play();
    }

}
