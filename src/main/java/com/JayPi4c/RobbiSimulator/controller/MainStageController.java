package com.JayPi4c.RobbiSimulator.controller;

import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;

public class MainStageController {

	private ButtonState buttonState;

	private MainStage mainStage;

	private boolean changeCursor = false;

	private RadioMenuItem selectedRadioMenuItem = null;

	public MainStageController(MainStage mainStage, ButtonState buttonState) {
		this.mainStage = mainStage;
		this.buttonState = buttonState;
		// set radio button actions
		mainStage.getPlaceRobbiTerritoryRadioMenuItem()
				.setOnAction(getRadioHandler(MainStage.menuRobbiImage, ButtonState.ROBBI));
		mainStage.getPlaceHollowTerritoryRadioMenuItem()
				.setOnAction(getRadioHandler(MainStage.menuHollowImage, ButtonState.HOLLOW));
		mainStage.getPlacePileOfScrapTerritoryRadioMenuItem()
				.setOnAction(getRadioHandler(MainStage.menuPileOfScrapImage, ButtonState.PILE_OF_SCRAP));
		mainStage.getPlaceStockpileTerritoryRadioMenuItem()
				.setOnAction(getRadioHandler(MainStage.menuStockpileImage, ButtonState.STOCKPILE));
		mainStage.getPlaceAccuTerritoryRadioMenuItem()
				.setOnAction(getRadioHandler(MainStage.menuAccuImage, ButtonState.ACCU));
		mainStage.getPlaceScrewTerritoryRadioMenuItem()
				.setOnAction(getRadioHandler(MainStage.menuScrewImage, ButtonState.SCREW));
		mainStage.getPlaceNutTerritoryRadioMenuItem()
				.setOnAction(getRadioHandler(MainStage.menuNutImage, ButtonState.NUT));
		mainStage.getDeleteFieldRadioMenuItem()
				.setOnAction(getRadioHandler(MainStage.menuDeleteImage, ButtonState.CLEAR));

		// set toolbar button actions
		mainStage.getPlaceRobbiToggleButtonToolbar()
				.setOnAction(getButtonHandler(MainStage.menuRobbiImage, ButtonState.ROBBI));
		mainStage.getPlaceHollowToggleButtonToolbar()
				.setOnAction(getButtonHandler(MainStage.menuHollowImage, ButtonState.HOLLOW));
		mainStage.getPlacePileOfScrapToggleButtonToolbar()
				.setOnAction(getButtonHandler(MainStage.menuPileOfScrapImage, ButtonState.PILE_OF_SCRAP));
		mainStage.getPlaceStockpileToggleButtonToolbar()
				.setOnAction(getButtonHandler(MainStage.menuStockpileImage, ButtonState.STOCKPILE));
		mainStage.getPlaceAccuToggleButtonToolbar()
				.setOnAction(getButtonHandler(MainStage.menuAccuImage, ButtonState.ACCU));
		mainStage.getPlaceScrewToggleButtonToolbar()
				.setOnAction(getButtonHandler(MainStage.menuScrewImage, ButtonState.SCREW));
		mainStage.getPlaceNutToggleButtonToolbar()
				.setOnAction(getButtonHandler(MainStage.menuNutImage, ButtonState.NUT));
		mainStage.getDeleteFieldToggleButtonToolbar()
				.setOnAction(getButtonHandler(MainStage.menuDeleteImage, ButtonState.CLEAR));
	}

	public void setChangeCursor(boolean flag) {
		this.changeCursor = flag;
	}

	private EventHandler<ActionEvent> getButtonHandler(Image img, int index) {
		return e -> {
			if (((ToggleButton) (e.getSource())).isSelected()) {
				if (changeCursor && img != null)
					mainStage.getMainStageScene().setCursor(new ImageCursor(img));
				buttonState.setSelected(index);
			} else {
				if (changeCursor)
					mainStage.getMainStageScene().setCursor(Cursor.DEFAULT);
				buttonState.deselect();
			}
		};
	}

	private EventHandler<ActionEvent> getRadioHandler(Image img, int index) {
		return e -> {
			RadioMenuItem item = ((RadioMenuItem) (e.getSource()));
			if (item.equals(selectedRadioMenuItem)) {
				if (changeCursor)
					mainStage.getMainStageScene().setCursor(Cursor.DEFAULT);
				selectedRadioMenuItem.setSelected(false);
				selectedRadioMenuItem = null;
				buttonState.deselect();
			} else {
				if (changeCursor && img != null)
					mainStage.getMainStageScene().setCursor(new ImageCursor(img));
				selectedRadioMenuItem = item;
				buttonState.setSelected(index);
			}

		};
	}
}
