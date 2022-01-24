package com.JayPi4c.RobbiSimulator.controller;

import java.util.Locale;

import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Tooltip;

/**
 * Controller to handle the change of language.
 * 
 * @author Jonas Pohl
 *
 */
public class LanguageController {
	private MainStage stage;

	/**
	 * Constructor to create a new LanguageController. Sets the actions to the
	 * languageSelection and binds text to all graphical elements.
	 * 
	 * @param mainStage the stage, this controller is for
	 */
	public LanguageController(MainStage mainStage) {
		this.stage = mainStage;

		mainStage.getGermanLanguageMenuItem().setOnAction(e -> {
			I18nUtils.setLocale(Locale.GERMANY);
			updateTitle();
		});
		mainStage.getEnglishLanguageMenuItem().setOnAction(e -> {
			I18nUtils.setLocale(Locale.UK);
			updateTitle();
		});

		// text bindings
		mainStage.getNewEditorMenuItem().textProperty().bind(createBinding("Menu.editor.new"));
		mainStage.getSaveEditorMenuItem().textProperty().bind(createBinding("Menu.editor.save"));
		mainStage.getOpenEditorMenuItem().textProperty().bind(createBinding("Menu.editor.open"));
		mainStage.getCompileEditorMenuItem().textProperty().bind(createBinding("Menu.editor.compile"));
		mainStage.getPrintEditorMenuItem().textProperty().bind(createBinding("Menu.editor.print"));
		mainStage.getQuitEditorMenuItem().textProperty().bind(createBinding("Menu.editor.quit"));
		mainStage.getEditorMenu().textProperty().bind(createBinding("Menu.editor"));

		// territory Menu
		mainStage.getSaveXMLTerritoryMenuItem().textProperty().bind(createBinding("Menu.territory.save.xml"));
		mainStage.getSaveJAXBTerritoryMenuItem().textProperty().bind(createBinding("Menu.territory.save.jaxb"));
		mainStage.getSaveSerialTerritoryMenuItem().textProperty().bind(createBinding("Menu.territory.save.serialize"));
		mainStage.getSaveTerritoryMenu().textProperty().bind(createBinding("Menu.territory.save"));
		mainStage.getLoadXMLTerritoryMenuItem().textProperty().bind(createBinding("Menu.territory.load.xml"));
		mainStage.getLoadJAXBTerritoryMenuItem().textProperty().bind(createBinding("Menu.territory.load.jaxb"));
		mainStage.getLoadSerialTerritoryMenuItem().textProperty()
				.bind(createBinding("Menu.territory.load.deserialize"));
		mainStage.getLoadTerritoryMenu().textProperty().bind(createBinding("Menu.territory.load"));
		mainStage.getSaveAsPNGMenuItem().textProperty().bind(createBinding("Menu.territory.saveAsPic.png"));
		mainStage.getSaveAsGifMenuItem().textProperty().bind(createBinding("Menu.territory.saveAsPic.gif"));
		mainStage.getSaveAsPicMenu().textProperty().bind(createBinding("Menu.territory.saveAsPic"));
		mainStage.getPrintTerritoryMenuItem().textProperty().bind(createBinding("Menu.territory.print"));

		mainStage.getChangeSizeTerritoryMenuItem().textProperty().bind(createBinding("Menu.territory.size"));
		mainStage.getPlaceRobbiTerritoryRadioMenuItem().textProperty()
				.bind(createBinding("Menu.territory.place.robbi"));
		mainStage.getPlaceHollowTerritoryRadioMenuItem().textProperty()
				.bind(createBinding("Menu.territory.place.hollow"));
		mainStage.getPlacePileOfScrapTerritoryRadioMenuItem().textProperty()
				.bind(createBinding("Menu.territory.place.pileOfScrap"));
		mainStage.getPlaceStockpileTerritoryRadioMenuItem().textProperty()
				.bind(createBinding("Menu.territory.place.stockpile"));
		mainStage.getPlaceAccuTerritoryRadioMenuItem().textProperty().bind(createBinding("Menu.territory.place.accu"));
		mainStage.getPlaceScrewTerritoryRadioMenuItem().textProperty()
				.bind(createBinding("Menu.territory.place.screw"));
		mainStage.getPlaceNutTerritoryRadioMenuItem().textProperty().bind(createBinding("Menu.territory.place.nut"));
		mainStage.getDeleteFieldRadioMenuItem().textProperty().bind(createBinding("Menu.territory.delete"));
		mainStage.getTerritoryMenu().textProperty().bind(createBinding("Menu.territory"));
		// robbi Menu
		mainStage.getItemPresentMenuItem().textProperty().bind(createBinding("Menu.robbi.itemPresent"));
		mainStage.getIsStockpileMenuItem().textProperty().bind(createBinding("Menu.robbi.isStockpile"));
		mainStage.getHollowAheadMenuItem().textProperty().bind(createBinding("Menu.robbi.hollowAhead"));
		mainStage.getPileOfScrapAheadMenuItem().textProperty().bind(createBinding("Menu.robbi.pileOfScrapAhead"));
		mainStage.getIsBagFullMenuItem().textProperty().bind(createBinding("Menu.robbi.isBagFull"));
		mainStage.getPushPileOfScrapMenuItem().textProperty().bind(createBinding("Menu.robbi.pushPileOfScrap"));
		mainStage.getMoveMenuItem().textProperty().bind(createBinding("Menu.robbi.move"));
		mainStage.getTurnLeftMenuItem().textProperty().bind(createBinding("Menu.robbi.turnLeft"));
		mainStage.getPutMenuItem().textProperty().bind(createBinding("Menu.robbi.put"));
		mainStage.getTakeMenuItem().textProperty().bind(createBinding("Menu.robbi.take"));
		mainStage.getRobbiMenu().textProperty().bind(createBinding("Menu.robbi"));
		// simulation Menu
		mainStage.getStartMenuItem().textProperty().bind(createBinding("Menu.simulation.start"));
		mainStage.getPauseMenuItem().textProperty().bind(createBinding("Menu.simulation.pause"));
		mainStage.getStopMenuItem().textProperty().bind(createBinding("Menu.simulation.stop"));
		mainStage.getSimulationMenu().textProperty().bind(createBinding("Menu.simulation"));
		// window Menu
		mainStage.getLanguageMenu().textProperty().bind(createBinding("Menu.window.language"));
		mainStage.getEnglishLanguageMenuItem().textProperty().bind(createBinding("Menu.window.language.english"));
		mainStage.getGermanLanguageMenuItem().textProperty().bind(createBinding("Menu.window.language.german"));
		mainStage.getChangeCursorMenuItem().textProperty().bind(createBinding("Menu.window.changeCursor"));
		mainStage.getWindowMenu().textProperty().bind(createBinding("Menu.window"));
		mainStage.getDarkModeMenuItem().textProperty().bind(createBinding("Menu.window.darkmode"));
		mainStage.getLibraryMenuItem().textProperty().bind(createBinding("Menu.window.libraries"));
		mainStage.getInfoMenuItem().textProperty().bind(createBinding("Menu.window.info"));

		// Tool bar
		mainStage.getNewButtonToolbar().setTooltip(createTooltip("Toolbar.control.new"));
		mainStage.getLoadButtonToolbar().setTooltip(createTooltip("Toolbar.control.load"));
		mainStage.getSaveButtonToolbar().setTooltip(createTooltip("Toolbar.control.save"));
		mainStage.getCompileButtonToolbar().setTooltip(createTooltip("Toolbar.control.compile"));

		mainStage.getChangeSizeButtonToolbar().setTooltip(createTooltip("Toolbar.territory.size"));
		mainStage.getPlaceRobbiToggleButtonToolbar().setTooltip(createTooltip("Toolbar.territory.placeRobbi"));
		mainStage.getPlaceHollowToggleButtonToolbar().setTooltip(createTooltip("Toolbar.territory.placeHollow"));
		mainStage.getPlacePileOfScrapToggleButtonToolbar()
				.setTooltip(createTooltip("Toolbar.territory.placePileOfScrap"));
		mainStage.getPlaceStockpileToggleButtonToolbar().setTooltip(createTooltip("Toolbar.territory.placeStockpile"));
		mainStage.getPlaceAccuToggleButtonToolbar().setTooltip(createTooltip("Toolbar.territory.placeAccu"));
		mainStage.getPlaceScrewToggleButtonToolbar().setTooltip(createTooltip("Toolbar.territory.placeScrew"));
		mainStage.getPlaceNutToggleButtonToolbar().setTooltip(createTooltip("Toolbar.territory.placeNut"));
		mainStage.getDeleteFieldToggleButtonToolbar().setTooltip(createTooltip("Toolbar.territory.delete"));

		mainStage.getRobbiTurnLeftButtonToolbar().setTooltip(createTooltip("Toolbar.robbi.turnLeft"));
		mainStage.getRobbiMoveButtonToolbar().setTooltip(createTooltip("Toolbar.robbi.move"));
		mainStage.getRobbiPutButtonToolbar().setTooltip(createTooltip("Toolbar.robbi.put"));
		mainStage.getRobbiTakeButtonToolbar().setTooltip(createTooltip("Toolbar.robbi.take"));

		mainStage.getResetButtonToolbar().setTooltip(createTooltip("Toolbar.action.reset"));
		mainStage.getStartToggleButtonToolbar().setTooltip(createTooltip("Toolbar.action.start"));
		mainStage.getPauseToggleButtonToolbar().setTooltip(createTooltip("Toolbar.action.pause"));
		mainStage.getStopToggleButtonToolbar().setTooltip(createTooltip("Toolbar.action.stop"));
		mainStage.getSpeedSliderToolbar().setTooltip(createTooltip("Toolbar.action.speed"));

		mainStage.getMessageLabel().textProperty().bind(createBinding("Messages.label.greeting"));

		mainStage.getExamplesMenu().textProperty().bind(createBinding("Menu.examples"));
		mainStage.getLoadExampleMenuItem().textProperty().bind(createBinding("Menu.examples.load"));
		mainStage.getSaveExampleMenuItem().textProperty().bind(createBinding("Menu.examples.save"));

		if (PropertiesLoader.isTutor()) {
			mainStage.getLoadRequestMenuItem().textProperty().bind(createBinding("Menu.tutor.loadRequest"));
			mainStage.getSaveAnswerMenuItem().textProperty().bind(createBinding("Menu.tutor.saveAnswer"));
		} else {
			mainStage.getSendRequestMenuItem().textProperty().bind(createBinding("Menu.tutor.sendRequest"));
			mainStage.getReceiveAnswerMenuItem().textProperty().bind(createBinding("Menu.tutor.receiveAnswer"));
		}
		mainStage.getTutorMenu().textProperty().bind(createBinding("Menu.tutor"));
	}

	/**
	 * Helper to crate a new String Binding for the provided key.
	 * 
	 * @param key the key to be mapped on the resources
	 * @return a binding for the provided key
	 */
	private StringBinding createBinding(String key) {
		return Bindings.createStringBinding(() -> I18nUtils.i18n(key), I18nUtils.localeProperty());
	}

	/**
	 * Helper to create a new Tooltip with a text-binding to the provided key.
	 * 
	 * @param key the key to map the text to
	 * @return a Tooltip with text-binding
	 */
	private Tooltip createTooltip(String key) {
		Tooltip tt = new Tooltip();
		tt.textProperty().bind(createBinding(key));
		return tt;
	}

	/**
	 * needed since the star (*) would mess with the binding property
	 */
	private void updateTitle() {
		stage.setTitle(I18nUtils.i18n("Main.title") + ": " + stage.getProgram().getName()
				+ (stage.getProgram().isEdited() ? "*" : ""));
	}

}
