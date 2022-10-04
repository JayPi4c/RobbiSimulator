package com.JayPi4c.RobbiSimulator.controller;

import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.createBinding;
import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.createTooltip;
import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.i18n;

import java.util.Locale;

import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import com.JayPi4c.RobbiSimulator.view.MenuBar;
import com.JayPi4c.RobbiSimulator.view.Toolbar;

/**
 * Controller to handle the change of language.
 * 
 * @author Jonas Pohl
 *
 */
public class LanguageController {

	private MainStage stage;

	// language keys
	private static final String LANGUAGE_CHANGED = "language.changed";

	private static final String MENU_EDITOR_NEW = "Menu.editor.new";
	private static final String MENU_EDITOR_SAVE = "Menu.editor.save";
	private static final String MENU_EDITOR_OPEN = "Menu.editor.open";
	private static final String MENU_EDITOR_FORMAT = "Menu.editor.format";
	private static final String MENU_EDITOR_COMPILE = "Menu.editor.compile";
	private static final String MENU_EDITOR_PRINT = "Menu.editor.print";
	private static final String MENU_EDITOR_QUIT = "Menu.editor.quit";
	private static final String MENU_EDITOR = "Menu.editor";

	private static final String MENU_TERRITORY_SAVE_XML = "Menu.territory.save.xml";
	private static final String MENU_TERRITORY_SAVE_JAXB = "Menu.territory.save.jaxb";
	private static final String MENU_TERRITORY_SAVE_SERIALIZE = "Menu.territory.save.serialize";
	private static final String MENU_TERRITORY_SAVE = "Menu.territory.save";
	private static final String MENU_TERRITORY_LOAD_XML = "Menu.territory.load.xml";
	private static final String MENU_TERRITORY_LOAD_JAXB = "Menu.territory.load.jaxb";
	private static final String MENU_TERRITORY_LOAD_DESERIALIZE = "Menu.territory.load.deserialize";
	private static final String MENU_TERRITORY_LOAD = "Menu.territory.load";
	private static final String MENU_TERRITORY_SAVEASPIC_PNG = "Menu.territory.saveAsPic.png";
	private static final String MENU_TERRITORY_SAVEASPIC_GIF = "Menu.territory.saveAsPic.gif";
	private static final String MENU_TERRITORY_SAVEASPIC = "Menu.territory.saveAsPic";
	private static final String MENU_TERRITORY_PRINT = "Menu.territory.print";
	private static final String MENU_TERRITORY_SIZE = "Menu.territory.size";
	private static final String MENU_TERRITORY_PLACE_ROBBI = "Menu.territory.place.robbi";
	private static final String MENU_TERRITORY_PLACE_HOLLOW = "Menu.territory.place.hollow";
	private static final String MENU_TERRITORY_PLACE_PILEOFSCRAP = "Menu.territory.place.pileOfScrap";
	private static final String MENU_TERRITORY_PLACE_STOCKPILE = "Menu.territory.place.stockpile";
	private static final String MENU_TERRITORY_PLACE_ACCU = "Menu.territory.place.accu";
	private static final String MENU_TERRITORY_PLACE_SCREW = "Menu.territory.place.screw";
	private static final String MENU_TERRITORY_PLACE_NUT = "Menu.territory.place.nut";
	private static final String MENU_TERRITORY_DELETE = "Menu.territory.delete";
	private static final String MENU_TERRITORY = "Menu.territory";

	private static final String MENU_ROBBI_ITEMPRESENT = "Menu.robbi.itemPresent";
	private static final String MENU_ROBBI_ISSTOCKPILE = "Menu.robbi.isStockpile";
	private static final String MENU_ROBBI_HOLLOWAHEAD = "Menu.robbi.hollowAhead";
	private static final String MENU_ROBBI_PILEOFSCRAPAHEAD = "Menu.robbi.pileOfScrapAhead";
	private static final String MENU_ROBBI_ISBAGFULL = "Menu.robbi.isBagFull";
	private static final String MENU_ROBBI_PUSHPILEOFSCRAP = "Menu.robbi.pushPileOfScrap";
	private static final String MENU_ROBBI_MOVE = "Menu.robbi.move";
	private static final String MENU_ROBBI_TURNLEFT = "Menu.robbi.turnLeft";
	private static final String MENU_ROBBI_PUT = "Menu.robbi.put";
	private static final String MENU_ROBBI_TAKE = "Menu.robbi.take";
	private static final String MENU_ROBBI = "Menu.robbi";
	private static final String MENU_SIMULATION_RESET = "Menu.simulation.reset";
	private static final String MENU_SIMULATION_START = "Menu.simulation.start";
	private static final String MENU_SIMULATION_PAUSE = "Menu.simulation.pause";
	private static final String MENU_SIMULATION_STOP = "Menu.simulation.stop";
	private static final String MENU_SIMULATION = "Menu.simulation";
	private static final String MENU_WINDOW_LANGUAGE = "Menu.window.language";
	private static final String MENU_WINDOW_LANGUAGE_ENGLISH = "Menu.window.language.english";
	private static final String MENU_WINDOW_LANGUAGE_GERMAN = "Menu.window.language.german";
	private static final String MENU_WINDOW_CHANGECURSOR = "Menu.window.changeCursor";
	private static final String MENU_WINDOW = "Menu.window";
	private static final String MENU_WINDOW_DARKMODE = "Menu.window.darkmode";
	private static final String MENU_WINDOW_ENABLESOUNDS = "Menu.window.enableSounds";
	private static final String MENU_WINDOW_LIBRARIES = "Menu.window.libraries";
	private static final String MENU_WINDOW_INFO = "Menu.window.info";

	private static final String TOOLBAR_CONTROL_NEW = "Toolbar.control.new";
	private static final String TOOLBAR_CONTROL_LOAD = "Toolbar.control.load";
	private static final String TOOLBAR_CONTROL_SAVE = "Toolbar.control.save";
	private static final String TOOLBAR_CONTROL_COMPILE = "Toolbar.control.compile";

	private static final String TOOLBAR_TERRITORY_SIZE = "Toolbar.territory.size";
	private static final String TOOLBAR_TERRITORY_PLACEROBBI = "Toolbar.territory.placeRobbi";
	private static final String TOOLBAR_TERRITORY_PLACEHOLLOW = "Toolbar.territory.placeHollow";
	private static final String TOOLBAR_TERRITORY_PLACEPILEOFSCRAP = "Toolbar.territory.placePileOfScrap";
	private static final String TOOLBAR_TERRITORY_PLACESTOCKPILE = "Toolbar.territory.placeStockpile";
	private static final String TOOLBAR_TERRITORY_PLACEACCU = "Toolbar.territory.placeAccu";
	private static final String TOOLBAR_TERRITORY_PLACESCREW = "Toolbar.territory.placeScrew";
	private static final String TOOLBAR_TERRITORY_PLACENUT = "Toolbar.territory.placeNut";
	private static final String TOOLBAR_TERRITORY_DELETE = "Toolbar.territory.delete";

	private static final String TOOLBAR_ROBBI_TURNLEFT = "Toolbar.robbi.turnLeft";
	private static final String TOOLBAR_ROBBI_MOVE = "Toolbar.robbi.move";
	private static final String TOOLBAR_ROBBI_PUT = "Toolbar.robbi.put";
	private static final String TOOLBAR_ROBBI_TAKE = "Toolbar.robbi.take";

	private static final String TOOLBAR_ACTION_RESET = "Toolbar.action.reset";
	private static final String TOOLBAR_ACTION_START = "Toolbar.action.start";
	private static final String TOOLBAR_ACTION_PAUSE = "Toolbar.action.pause";
	private static final String TOOLBAR_ACTION_STOP = "Toolbar.action.stop";
	private static final String TOOLBAR_ACTION_SPEED = "Toolbar.action.speed";

	private static final String MENU_EXAMPLES = "Menu.examples";
	private static final String MENU_EXAMPLES_LOAD = "Menu.examples.load";
	private static final String MENU_EXAMPLES_SAVE = "Menu.examples.save";
	private static final String MENU_TUTOR_LOADREQUEST = "Menu.tutor.loadRequest";
	private static final String MENU_TUTOR_SAVEANSWER = "Menu.tutor.saveAnswer";
	private static final String MENU_TUTOR_SENDREQUEST = "Menu.tutor.sendRequest";
	private static final String MENU_TUTOR_RECEIVEANSWER = "Menu.tutor.receiveAnswer";
	private static final String MENU_TUTOR = "Menu.tutor";

	private static final String MAIN_TITLE = "Main.title";

	/**
	 * Constructor to create a new LanguageController. Sets the actions to the
	 * languageSelection and binds text to all graphical elements.
	 * 
	 * @param mainStage the stage, this controller is for
	 */
	public LanguageController(MainStage mainStage) {
		this.stage = mainStage;

		MenuBar menubar = mainStage.getMenubar();

		menubar.getGermanLanguageMenuItem().setOnAction(e -> {
			I18nUtils.setLocale(Locale.GERMANY);
			updateTitle();
			mainStage.getSnackbarController().showMessage(LANGUAGE_CHANGED);
		});
		menubar.getEnglishLanguageMenuItem().setOnAction(e -> {
			I18nUtils.setLocale(Locale.UK);
			updateTitle();
			mainStage.getSnackbarController().showMessage(LANGUAGE_CHANGED);
		});

		// text bindings
		menubar.getNewEditorMenuItem().textProperty().bind(createBinding(MENU_EDITOR_NEW));
		menubar.getSaveEditorMenuItem().textProperty().bind(createBinding(MENU_EDITOR_SAVE));
		menubar.getOpenEditorMenuItem().textProperty().bind(createBinding(MENU_EDITOR_OPEN));
		menubar.getFormatSourceCodeMenuItem().textProperty().bind(createBinding(MENU_EDITOR_FORMAT));
		menubar.getCompileEditorMenuItem().textProperty().bind(createBinding(MENU_EDITOR_COMPILE));
		menubar.getPrintEditorMenuItem().textProperty().bind(createBinding(MENU_EDITOR_PRINT));
		menubar.getQuitEditorMenuItem().textProperty().bind(createBinding(MENU_EDITOR_QUIT));
		menubar.getEditorMenu().textProperty().bind(createBinding(MENU_EDITOR));

		// territory Menu
		menubar.getSaveXMLTerritoryMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_SAVE_XML));
		menubar.getSaveJAXBTerritoryMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_SAVE_JAXB));
		menubar.getSaveSerialTerritoryMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_SAVE_SERIALIZE));
		menubar.getSaveTerritoryMenu().textProperty().bind(createBinding(MENU_TERRITORY_SAVE));
		menubar.getLoadXMLTerritoryMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_LOAD_XML));
		menubar.getLoadJAXBTerritoryMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_LOAD_JAXB));
		menubar.getLoadSerialTerritoryMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_LOAD_DESERIALIZE));
		menubar.getLoadTerritoryMenu().textProperty().bind(createBinding(MENU_TERRITORY_LOAD));
		menubar.getSaveAsPNGMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_SAVEASPIC_PNG));
		menubar.getSaveAsGifMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_SAVEASPIC_GIF));
		menubar.getSaveAsPicMenu().textProperty().bind(createBinding(MENU_TERRITORY_SAVEASPIC));
		menubar.getPrintTerritoryMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_PRINT));

		menubar.getChangeSizeTerritoryMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_SIZE));
		menubar.getPlaceRobbiTerritoryRadioMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_PLACE_ROBBI));
		menubar.getPlaceHollowTerritoryRadioMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_PLACE_HOLLOW));
		menubar.getPlacePileOfScrapTerritoryRadioMenuItem().textProperty()
				.bind(createBinding(MENU_TERRITORY_PLACE_PILEOFSCRAP));
		menubar.getPlaceStockpileTerritoryRadioMenuItem().textProperty()
				.bind(createBinding(MENU_TERRITORY_PLACE_STOCKPILE));
		menubar.getPlaceAccuTerritoryRadioMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_PLACE_ACCU));
		menubar.getPlaceScrewTerritoryRadioMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_PLACE_SCREW));
		menubar.getPlaceNutTerritoryRadioMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_PLACE_NUT));
		menubar.getDeleteFieldRadioMenuItem().textProperty().bind(createBinding(MENU_TERRITORY_DELETE));
		menubar.getTerritoryMenu().textProperty().bind(createBinding(MENU_TERRITORY));

		// robbi Menu
		menubar.getItemPresentMenuItem().textProperty().bind(createBinding(MENU_ROBBI_ITEMPRESENT));
		menubar.getIsStockpileMenuItem().textProperty().bind(createBinding(MENU_ROBBI_ISSTOCKPILE));
		menubar.getHollowAheadMenuItem().textProperty().bind(createBinding(MENU_ROBBI_HOLLOWAHEAD));
		menubar.getPileOfScrapAheadMenuItem().textProperty().bind(createBinding(MENU_ROBBI_PILEOFSCRAPAHEAD));
		menubar.getIsBagFullMenuItem().textProperty().bind(createBinding(MENU_ROBBI_ISBAGFULL));
		menubar.getPushPileOfScrapMenuItem().textProperty().bind(createBinding(MENU_ROBBI_PUSHPILEOFSCRAP));
		menubar.getMoveMenuItem().textProperty().bind(createBinding(MENU_ROBBI_MOVE));
		menubar.getTurnLeftMenuItem().textProperty().bind(createBinding(MENU_ROBBI_TURNLEFT));
		menubar.getPutMenuItem().textProperty().bind(createBinding(MENU_ROBBI_PUT));
		menubar.getTakeMenuItem().textProperty().bind(createBinding(MENU_ROBBI_TAKE));
		menubar.getRobbiMenu().textProperty().bind(createBinding(MENU_ROBBI));

		// simulation Menu
		menubar.getResetMenuItem().textProperty().bind(createBinding(MENU_SIMULATION_RESET));
		menubar.getStartMenuItem().textProperty().bind(createBinding(MENU_SIMULATION_START));
		menubar.getPauseMenuItem().textProperty().bind(createBinding(MENU_SIMULATION_PAUSE));
		menubar.getStopMenuItem().textProperty().bind(createBinding(MENU_SIMULATION_STOP));
		menubar.getSimulationMenu().textProperty().bind(createBinding(MENU_SIMULATION));

		// window Menu
		menubar.getLanguageMenu().textProperty().bind(createBinding(MENU_WINDOW_LANGUAGE));
		menubar.getEnglishLanguageMenuItem().textProperty().bind(createBinding(MENU_WINDOW_LANGUAGE_ENGLISH));
		menubar.getGermanLanguageMenuItem().textProperty().bind(createBinding(MENU_WINDOW_LANGUAGE_GERMAN));
		menubar.getChangeCursorMenuItem().textProperty().bind(createBinding(MENU_WINDOW_CHANGECURSOR));
		menubar.getWindowMenu().textProperty().bind(createBinding(MENU_WINDOW));
		menubar.getDarkModeMenuItem().textProperty().bind(createBinding(MENU_WINDOW_DARKMODE));
		menubar.getEnableSoundsMenuItem().textProperty().bind(createBinding(MENU_WINDOW_ENABLESOUNDS));
		menubar.getLibraryMenuItem().textProperty().bind(createBinding(MENU_WINDOW_LIBRARIES));
		menubar.getInfoMenuItem().textProperty().bind(createBinding(MENU_WINDOW_INFO));

		Toolbar toolbar = mainStage.getToolbar();
		// Tool bar

		toolbar.getNewButtonToolbar().setTooltip(createTooltip(TOOLBAR_CONTROL_NEW));
		toolbar.getLoadButtonToolbar().setTooltip(createTooltip(TOOLBAR_CONTROL_LOAD));
		toolbar.getSaveButtonToolbar().setTooltip(createTooltip(TOOLBAR_CONTROL_SAVE));
		toolbar.getCompileButtonToolbar().setTooltip(createTooltip(TOOLBAR_CONTROL_COMPILE));

		toolbar.getChangeSizeButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_SIZE));
		toolbar.getPlaceRobbiToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_PLACEROBBI));
		toolbar.getPlaceHollowToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_PLACEHOLLOW));
		toolbar.getPlacePileOfScrapToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_PLACEPILEOFSCRAP));
		toolbar.getPlaceStockpileToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_PLACESTOCKPILE));
		toolbar.getPlaceAccuToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_PLACEACCU));
		toolbar.getPlaceScrewToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_PLACESCREW));
		toolbar.getPlaceNutToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_PLACENUT));
		toolbar.getDeleteFieldToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_TERRITORY_DELETE));

		toolbar.getRobbiTurnLeftButtonToolbar().setTooltip(createTooltip(TOOLBAR_ROBBI_TURNLEFT));
		toolbar.getRobbiMoveButtonToolbar().setTooltip(createTooltip(TOOLBAR_ROBBI_MOVE));
		toolbar.getRobbiPutButtonToolbar().setTooltip(createTooltip(TOOLBAR_ROBBI_PUT));
		toolbar.getRobbiTakeButtonToolbar().setTooltip(createTooltip(TOOLBAR_ROBBI_TAKE));

		toolbar.getResetButtonToolbar().setTooltip(createTooltip(TOOLBAR_ACTION_RESET));
		toolbar.getStartToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_ACTION_START));
		toolbar.getPauseToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_ACTION_PAUSE));
		toolbar.getStopToggleButtonToolbar().setTooltip(createTooltip(TOOLBAR_ACTION_STOP));
		toolbar.getSpeedSliderToolbar().setTooltip(createTooltip(TOOLBAR_ACTION_SPEED));

		menubar.getExamplesMenu().textProperty().bind(createBinding(MENU_EXAMPLES));
		menubar.getLoadExampleMenuItem().textProperty().bind(createBinding(MENU_EXAMPLES_LOAD));
		menubar.getSaveExampleMenuItem().textProperty().bind(createBinding(MENU_EXAMPLES_SAVE));

		if (PropertiesLoader.isTutor()) {
			menubar.getLoadRequestMenuItem().textProperty().bind(createBinding(MENU_TUTOR_LOADREQUEST));
			menubar.getSaveAnswerMenuItem().textProperty().bind(createBinding(MENU_TUTOR_SAVEANSWER));
		} else {
			menubar.getSendRequestMenuItem().textProperty().bind(createBinding(MENU_TUTOR_SENDREQUEST));
			menubar.getReceiveAnswerMenuItem().textProperty().bind(createBinding(MENU_TUTOR_RECEIVEANSWER));
		}
		menubar.getTutorMenu().textProperty().bind(createBinding(MENU_TUTOR));
	}

	/**
	 * needed since the star (*) would mess with the binding property
	 * 
	 * TODO use formatted String
	 */
	public void updateTitle() {
		stage.setTitle(
				i18n(MAIN_TITLE) + ": " + stage.getProgram().getName() + (stage.getProgram().isEdited() ? "*" : ""));
	}

}
