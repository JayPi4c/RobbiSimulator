package com.JayPi4c.RobbiSimulator.controller;

import com.JayPi4c.RobbiSimulator.model.Dimension;
import com.JayPi4c.RobbiSimulator.model.Territory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.Optional;

import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.i18n;

/**
 * This EventHandler combines all the code needed to change the territory size
 *
 * @author Jonas Pohl
 */
public class ChangeTerritorySizeHandler implements EventHandler<ActionEvent> {

    private final Territory territory;

    private Dialog<Dimension> dialog;
    private final Window parent;

    // language keys
    private static final String CHANGESIZE_DIALOG_TITLE = "ChangeSize.dialog.title";
    private static final String CHANGESIZE_DIALOG_HEADER = "ChangeSize.dialog.header";
    private static final String CHANGESIZE_DIALOG_COLS = "ChangeSize.dialog.cols";
    private static final String CHANGESIZE_DIALOG_ROWS = "ChangeSize.dialog.rows";

    /**
     * Creates a new ChangeTerritorySizeHandler and sets up a new Dialog, which can
     * be invoked in the handle method
     *
     * @param territory The territory, which size should be changed
     * @param parent    The parent window in order to set the dialog relative to the
     *                  current window
     */
    public ChangeTerritorySizeHandler(Window parent, Territory territory) {
        this.territory = territory;
        this.parent = parent;
    }

    /**
     * Creates a new Dialog to handle the size change. The Dialog will be newly
     * created every time in order to have the correct language at every moment.
     */
    private void createDialog() {
        dialog = new Dialog<>();
        dialog.setTitle(i18n(CHANGESIZE_DIALOG_TITLE));
        dialog.setHeaderText(i18n(CHANGESIZE_DIALOG_HEADER));
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField rowField = new TextField();
        rowField.textProperty().addListener((observeable, oldVal, newVal) -> dialog.getDialogPane()
                .lookupButton(ButtonType.OK).setDisable(newVal.isEmpty() || !isValid(newVal)));
        TextField colField = new TextField();
        colField.textProperty().addListener((observable, oldVal, newVal) -> dialog.getDialogPane()
                .lookupButton(ButtonType.OK).setDisable(newVal.isEmpty() || !isValid(newVal)));

        GridPane grid = new GridPane();
        grid.addRow(0, new Label(i18n(CHANGESIZE_DIALOG_COLS)), colField);
        grid.addRow(1, new Label(i18n(CHANGESIZE_DIALOG_ROWS)), rowField);
        grid.setHgap(20);
        dialogPane.setContent(grid);
        rowField.setText(Integer.toString(territory.getNumRows()));
        colField.setText(Integer.toString(territory.getNumCols()));
        Platform.runLater(colField::requestFocus);
        dialog.setResultConverter(button -> button == ButtonType.OK
                ? new Dimension(Integer.parseInt(colField.getText()), Integer.parseInt(rowField.getText()))
                : null);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parent);
    }

    /**
     * Checks if the given String is between 1 and 100.
     *
     * @param s the String which represents the user-input
     * @return true if the s is > 0 and <= 100, false otherwise
     */
    private boolean isValid(String s) {
        try {
            int val = Integer.parseInt(s);
            return val > 0 && val <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void handle(ActionEvent event) {
        createDialog();
        Optional<Dimension> optionalDimension = dialog.showAndWait();
        optionalDimension.ifPresent(result -> territory.changeSize(result.cols(), result.rows()));
    }

}
