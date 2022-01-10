package com.JayPi4c.RobbiSimulator.controller;

import java.util.logging.Logger;

import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.model.BagIsEmptyException;
import com.JayPi4c.RobbiSimulator.model.BagIsFullException;
import com.JayPi4c.RobbiSimulator.model.HollowAheadException;
import com.JayPi4c.RobbiSimulator.model.NoItemException;
import com.JayPi4c.RobbiSimulator.model.NoPileOfScrapAheadException;
import com.JayPi4c.RobbiSimulator.model.RobbiException;
import com.JayPi4c.RobbiSimulator.model.TileBlockedException;
import com.JayPi4c.RobbiSimulator.model.TileIsFullException;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;

public class MainStageController {
	Logger logger = Logger.getLogger(MainStageController.class.toString());

	private ButtonState buttonState;

	private MainStage mainStage;

	private boolean changeCursor = false;

	private RadioMenuItem selectedRadioMenuItem = null;

	public MainStageController(MainStage mainStage, ButtonState buttonState) {
		this.mainStage = mainStage;
		this.buttonState = buttonState;

		// editor actions
		mainStage.getNewEditorMenuItem().setOnAction(e -> ProgramController.createAndShow());
		mainStage.getNewButtonToolbar().onActionProperty().bind(mainStage.getNewEditorMenuItem().onActionProperty());

		mainStage.getOpenEditorMenuItem().setOnAction(e -> ProgramController.openProgram());
		mainStage.getLoadButtonToolbar().onActionProperty().bind(mainStage.getOpenEditorMenuItem().onActionProperty());

		mainStage.getSaveEditorMenuItem().setOnAction(e -> {
			mainStage.getProgram().save(mainStage.getTextArea().getText());
			mainStage.setTitle(Messages.getString("Main.title") + ": " + mainStage.getProgram().getName());
		});
		mainStage.getSaveButtonToolbar().onActionProperty().bind(mainStage.getSaveEditorMenuItem().onActionProperty());

		mainStage.getCompileEditorMenuItem().setOnAction(e -> {
			Program program = mainStage.getProgram();
			program.save(mainStage.getTextArea().getText());
			mainStage.setTitle(Messages.getString("Main.title") + ": " + program.getName());
			ProgramController.compile(program);
		});
		mainStage.getCompileButtonToolbar().onActionProperty()
				.bind(mainStage.getCompileEditorMenuItem().onActionProperty());

		mainStage.getQuitEditorMenuItem().setOnAction(e -> {
			Program program = mainStage.getProgram();
			logger.info("exiting " + program.getName());
			program.save(mainStage.getTextArea().getText());
			ProgramController.close(program.getName());
			mainStage.close();
		});

		mainStage.getChangeSizeTerritoryMenuItem()
				.setOnAction(new ChangeTerritorySizeHandler(mainStage.getTerritory()));
		mainStage.getChangeSizeButtonToolbar().onActionProperty()
				.bind(mainStage.getChangeSizeTerritoryMenuItem().onActionProperty());

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

		// set robbi menuitem actions
		mainStage.getItemPresentMenuItem().setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText(Messages.getString("Execution.information.itemPresent")
					+ mainStage.getTerritory().getRobbi().gegenstandDa());
			alert.showAndWait();
		});
		mainStage.getIsStockpileMenuItem().setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText(Messages.getString("Execution.information.stockpile")
					+ mainStage.getTerritory().getRobbi().istLagerplatz());
			alert.showAndWait();
		});
		mainStage.getHollowAheadMenuItem().setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText(Messages.getString("Execution.information.hollow")
					+ mainStage.getTerritory().getRobbi().vornKuhle());
			alert.showAndWait();
		});
		mainStage.getPileOfScrapAheadMenuItem().setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText(Messages.getString("Execution.information.pileOfScrap")
					+ mainStage.getTerritory().getRobbi().vornSchrotthaufen());
			alert.showAndWait();
		});
		mainStage.getIsBagFullMenuItem().setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText(Messages.getString("Execution.information.bag")
					+ mainStage.getTerritory().getRobbi().istTascheVoll());
			alert.showAndWait();
		});
		mainStage.getPushPileOfScrapMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().schiebeSchrotthaufen();
			} catch (NoPileOfScrapAheadException | TileBlockedException ex) {
				showWarning(ex);
			}
		});
		mainStage.getMoveMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().vor();
			} catch (HollowAheadException ex) {
				showWarning(ex);
			}
		});
		mainStage.getTurnLeftMenuItem().setOnAction(e -> {
			mainStage.getTerritory().getRobbi().linksUm();
		});
		mainStage.getPutMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().legeAb();
			} catch (BagIsEmptyException | TileIsFullException ex) {
				showWarning(ex);
			}
		});
		mainStage.getTakeMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().nehmeAuf();
			} catch (NoItemException | BagIsFullException ex) {
				showWarning(ex);
			}
		});

		// content Panel
		mainStage.getTextArea().textProperty().addListener((observalble, oldVal, newVal) -> {
			Program program = mainStage.getProgram();
			boolean before = program.isEdited();
			program.setEdited(!newVal.equals(program.getEditorContent()));
			if (before != program.isEdited())
				mainStage.setTitle(
						Messages.getString("Main.title") + ": " + program.getName() + (program.isEdited() ? "*" : ""));
		});
	}

	private void showWarning(RobbiException ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(ex.getMessage());
		alert.showAndWait();
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
