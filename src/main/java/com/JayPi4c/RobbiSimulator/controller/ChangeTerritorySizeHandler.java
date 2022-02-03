package com.JayPi4c.RobbiSimulator.controller;

import java.util.Optional;

import com.JayPi4c.RobbiSimulator.model.Dimension;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 * This EventHandler combines all the code needed to change the territory size
 * 
 * @author Jonas Pohl
 *
 */
public class ChangeTerritorySizeHandler implements EventHandler<ActionEvent> {

	private Territory territory;

	private Dialog<Dimension> dialog;
	private TextField rowField;
	private TextField colField;

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

		dialog = new Dialog<>();
		dialog.setTitle(I18nUtils.i18n("ChangeSize.dialog.title"));
		dialog.setHeaderText(I18nUtils.i18n("ChangeSize.dialog.header"));
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		rowField = new TextField();
		rowField.textProperty().addListener((observeable, oldVal, newVal) -> dialog.getDialogPane()
				.lookupButton(ButtonType.OK).setDisable(newVal.isEmpty() || !isValid(newVal)));
		colField = new TextField();
		colField.textProperty().addListener((observable, oldVal, newVal) -> dialog.getDialogPane()
				.lookupButton(ButtonType.OK).setDisable(newVal.isEmpty() || !isValid(newVal)));

		GridPane grid = new GridPane();
		grid.addRow(0, new Label(I18nUtils.i18n("ChangeSize.dialog.cols")), colField);
		grid.addRow(1, new Label(I18nUtils.i18n("ChangeSize.dialog.rows")), rowField);
		grid.setHgap(20);
		dialogPane.setContent(grid);
		Platform.runLater(rowField::requestFocus);
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
		rowField.setText(Integer.toString(territory.getNumRows()));
		colField.setText(Integer.toString(territory.getNumCols()));
		Optional<Dimension> optionalDimension = dialog.showAndWait();
		optionalDimension.ifPresent(result -> territory.changeSize(result.cols(), result.rows()));
	}

}
