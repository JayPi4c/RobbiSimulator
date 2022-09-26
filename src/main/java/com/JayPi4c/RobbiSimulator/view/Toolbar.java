package com.JayPi4c.RobbiSimulator.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleNode;

import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * The toolbar allows faster access to the most common actions.
 * 
 * @author Jonas Pohl
 *
 */
@Getter
@Slf4j
public class Toolbar extends ToolBar {

	private MenuBar menubar;

	// Tool bar
	private Button newButtonToolbar;
	private Button loadButtonToolbar;

	private Button saveButtonToolbar;
	private Button compileButtonToolbar;

	private Button changeSizeButtonToolbar;
	private ToggleButton placeRobbiToggleButtonToolbar;
	private ToggleButton placeHollowToggleButtonToolbar;
	private ToggleButton placePileOfScrapToggleButtonToolbar;
	private ToggleButton placeStockpileToggleButtonToolbar;
	private ToggleButton placeAccuToggleButtonToolbar;
	private ToggleButton placeScrewToggleButtonToolbar;
	private ToggleButton placeNutToggleButtonToolbar;
	private ToggleButton deleteFieldToggleButtonToolbar;

	private Button robbiTurnLeftButtonToolbar;
	private Button robbiMoveButtonToolbar;
	private Button robbiPutButtonToolbar;
	private Button robbiTakeButtonToolbar;

	private Button resetButtonToolbar;
	private ToggleButton startToggleButtonToolbar;
	private ToggleButton pauseToggleButtonToolbar;
	private ToggleButton stopToggleButtonToolbar;

	private Slider speedSliderToolbar;

	/**
	 * Constant for the minimum value for the speed slider.
	 */
	public static final int MIN_SPEED_VALUE = 1;
	/**
	 * Constant for the maximum value for the speed slider.
	 */
	public static final int MAX_SPEED_VALUE = 100;

	/**
	 * Creates a new Toolbar
	 * 
	 * @param menubar the menubar to link some actions from the menubar to the
	 *                toolbar buttons
	 */
	public Toolbar(MenuBar menubar) {
		this.menubar = menubar;
		createToolbar();
	}

	/**
	 * Creates a toolbar for direct-access to the most important features.
	 */
	private void createToolbar() {
		logger.debug("Create toolbar");
		newButtonToolbar = new JFXButton(null, new ImageView(MainStage.newImage));
		((JFXButton) newButtonToolbar).setButtonType(ButtonType.RAISED);

		loadButtonToolbar = new JFXButton(null, new ImageView(MainStage.openImage));

		saveButtonToolbar = new JFXButton(null, new ImageView(MainStage.saveImage));

		compileButtonToolbar = new JFXButton(null, new ImageView(MainStage.compileImage));

		changeSizeButtonToolbar = new JFXButton(null, new ImageView(MainStage.terrainImage));

		var placeGroupToolbar = new ToggleGroup();

		placeRobbiToggleButtonToolbar = new JFXToggleNode();
		placeRobbiToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuRobbiImage));
		placeRobbiToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeRobbiToggleButtonToolbar.selectedProperty()
				.bindBidirectional(menubar.getPlaceRobbiTerritoryRadioMenuItem().selectedProperty());

		placeHollowToggleButtonToolbar = new JFXToggleNode();
		placeHollowToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuHollowImage));
		placeHollowToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeHollowToggleButtonToolbar.selectedProperty()
				.bindBidirectional(menubar.getPlaceHollowTerritoryRadioMenuItem().selectedProperty());

		placePileOfScrapToggleButtonToolbar = new JFXToggleNode();
		placePileOfScrapToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuPileOfScrapImage));
		placePileOfScrapToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placePileOfScrapToggleButtonToolbar.selectedProperty()
				.bindBidirectional(menubar.getPlacePileOfScrapTerritoryRadioMenuItem().selectedProperty());

		placeStockpileToggleButtonToolbar = new JFXToggleNode();
		placeStockpileToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuStockpileImage));
		placeStockpileToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeStockpileToggleButtonToolbar.selectedProperty()
				.bindBidirectional(menubar.getPlaceStockpileTerritoryRadioMenuItem().selectedProperty());

		placeAccuToggleButtonToolbar = new JFXToggleNode();
		placeAccuToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuAccuImage));
		placeAccuToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeAccuToggleButtonToolbar.selectedProperty()
				.bindBidirectional(menubar.getPlaceAccuTerritoryRadioMenuItem().selectedProperty());

		placeScrewToggleButtonToolbar = new JFXToggleNode();
		placeScrewToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuScrewImage));
		placeScrewToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeScrewToggleButtonToolbar.selectedProperty()
				.bindBidirectional(menubar.getPlaceScrewTerritoryRadioMenuItem().selectedProperty());

		placeNutToggleButtonToolbar = new JFXToggleNode();
		placeNutToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuNutImage));
		placeNutToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeNutToggleButtonToolbar.selectedProperty()
				.bindBidirectional(menubar.getPlaceNutTerritoryRadioMenuItem().selectedProperty());

		deleteFieldToggleButtonToolbar = new JFXToggleNode();
		deleteFieldToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuDeleteImage));
		deleteFieldToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		deleteFieldToggleButtonToolbar.selectedProperty()
				.bindBidirectional(menubar.getDeleteFieldRadioMenuItem().selectedProperty());

		robbiMoveButtonToolbar = new JFXButton(null, new ImageView(MainStage.robbiMove));

		robbiTurnLeftButtonToolbar = new JFXButton(null, new ImageView(MainStage.robbiTurnLeft));

		robbiPutButtonToolbar = new JFXButton(null, new ImageView(MainStage.robbiPut));

		robbiTakeButtonToolbar = new JFXButton(null, new ImageView(MainStage.robbiTake));

		resetButtonToolbar = new JFXButton(null, new ImageView(MainStage.resetImage));

		var simulationGroupToolbar = new ToggleGroup();
		startToggleButtonToolbar = new JFXToggleNode();
		startToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuStartImage));
		startToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

		pauseToggleButtonToolbar = new JFXToggleNode();
		pauseToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuPauseImage));
		pauseToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

		stopToggleButtonToolbar = new JFXToggleNode();
		stopToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuStopImage));
		stopToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

		speedSliderToolbar = new JFXSlider(MIN_SPEED_VALUE, MAX_SPEED_VALUE, (MIN_SPEED_VALUE + MAX_SPEED_VALUE) / 2d);

		getItems().addAll(newButtonToolbar, loadButtonToolbar, new Separator(), saveButtonToolbar, compileButtonToolbar,
				new Separator(), changeSizeButtonToolbar, placeRobbiToggleButtonToolbar, placeHollowToggleButtonToolbar,
				placePileOfScrapToggleButtonToolbar, placeStockpileToggleButtonToolbar, placeAccuToggleButtonToolbar,
				placeScrewToggleButtonToolbar, placeNutToggleButtonToolbar, deleteFieldToggleButtonToolbar,
				new Separator(), robbiMoveButtonToolbar, robbiTurnLeftButtonToolbar, robbiPutButtonToolbar,
				robbiTakeButtonToolbar, new Separator(), resetButtonToolbar, startToggleButtonToolbar,
				pauseToggleButtonToolbar, stopToggleButtonToolbar, new Separator(), speedSliderToolbar);
	}
}
