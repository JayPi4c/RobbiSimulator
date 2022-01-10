package com.JayPi4c.RobbiSimulator.controller;

import java.util.Optional;

import com.JayPi4c.RobbiSimulator.model.Dimension;
import com.JayPi4c.RobbiSimulator.model.Territory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ChangeTerritorySizeHandler implements EventHandler<ActionEvent> {

	Dialog<Dimension> dialog;
	Territory territory;

	public ChangeTerritorySizeHandler(Territory territory) {
		// öffne view
		this.territory = territory;

		dialog = new Dialog<>();
		dialog.setTitle("Change Size");
		dialog.setHeaderText("please choose new Dimension");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		TextField rowField = new TextField(Integer.toString(territory.getNumRows()));
		rowField.textProperty().addListener((observeable, oldVal, newVal) -> dialog.getDialogPane()
				.lookupButton(ButtonType.OK).setDisable(newVal.isEmpty() || !isValid(newVal)));
		TextField colField = new TextField(Integer.toString(territory.getNumCols()));
		colField.textProperty().addListener((observable, oldVal, newVal) -> dialog.getDialogPane()
				.lookupButton(ButtonType.OK).setDisable(newVal.isEmpty() || !isValid(newVal)));

		GridPane grid = new GridPane();
		grid.addRow(0, new Label("cols:"), colField);
		grid.addRow(1, new Label("rows"), rowField);
		grid.setHgap(20);
		dialogPane.setContent(grid);
		Platform.runLater(rowField::requestFocus);
		dialog.setResultConverter(button -> {
			if (button == ButtonType.OK) {

				return new Dimension(Integer.parseInt(colField.getText()), Integer.parseInt(rowField.getText()));
			}
			return null;
		});

	}

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
		Optional<Dimension> optionalDimension = dialog.showAndWait();
		optionalDimension.ifPresent(result -> territory.changeSize(result.cols(), result.rows()));
	}

}