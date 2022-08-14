package com.JayPi4c.RobbiSimulator.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.derby.tools.sysinfo;
import org.apache.logging.log4j.Logger;
import org.hibernate.Version;

import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.model.BagIsEmptyException;
import com.JayPi4c.RobbiSimulator.model.BagIsFullException;
import com.JayPi4c.RobbiSimulator.model.HollowAheadException;
import com.JayPi4c.RobbiSimulator.model.NoItemException;
import com.JayPi4c.RobbiSimulator.model.NoPileOfScrapAheadException;
import com.JayPi4c.RobbiSimulator.model.TileBlockedException;
import com.JayPi4c.RobbiSimulator.model.TileIsFullException;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.Observable;
import com.JayPi4c.RobbiSimulator.utils.Observer;
import com.JayPi4c.RobbiSimulator.utils.SceneManager;
import com.JayPi4c.RobbiSimulator.utils.SoundManager;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;
import com.jfoenix.utils.JFXUtilities;

import jakarta.xml.bind.JAXBContext;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller contains all the settings for the mainStage
 * 
 * @author Jonas Pohl
 *
 */
@Slf4j
public class MainStageController implements Observer {

	private ButtonState buttonState;

	private MainStage mainStage;

	@Setter
	private boolean changeCursor = false;
	@Getter
	private boolean soundsEnabled = false;

	private RadioMenuItem selectedRadioMenuItem = null;

	/**
	 * Creates a new MainStageController and sets all the actions for the buttons in
	 * the mainStage
	 * 
	 * @param mainStage   the mainStage for this controller
	 * @param buttonState the buttonState for this controller
	 */
	public MainStageController(MainStage mainStage, ButtonState buttonState) {
		this.mainStage = mainStage;
		this.buttonState = buttonState;
		this.mainStage.getProgram().addObserver(this);

		mainStage.setTitle(I18nUtils.i18n("Main.title") + ": " + mainStage.getProgram().getName());

		mainStage.setOnCloseRequest(e -> {
			mainStage.getProgram().save(mainStage.getTextArea().getText());
			ProgramController.close(mainStage.getProgram().getName());
		});

		// editor (menuBar)
		mainStage.getNewEditorMenuItem().setOnAction(e -> ProgramController.createAndShow(mainStage));
		mainStage.getOpenEditorMenuItem().setOnAction(e -> ProgramController.openProgram(mainStage));
		mainStage.getSaveEditorMenuItem().setOnAction(e -> {
			mainStage.getProgram().save(mainStage.getTextArea().getText());
			mainStage.setTitle(getTitle(mainStage.getProgram()));
		});

		mainStage.getCompileEditorMenuItem().setOnAction(e -> {
			mainStage.getSimulationController().stopSimulation();
			Program program = mainStage.getProgram();
			program.save(mainStage.getTextArea().getText());
			mainStage.setTitle(getTitle(program));
			ProgramController.compile(program, mainStage);
		});
		// TODO print editor content
		mainStage.getPrintEditorMenuItem().setOnAction(
				e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION, "Not yet implemented", mainStage));
		mainStage.getQuitEditorMenuItem().setOnAction(e -> {
			Program program = mainStage.getProgram();
			logger.info("exiting {}", program.getName());
			program.save(mainStage.getTextArea().getText());
			ProgramController.close(program.getName());
			mainStage.close();
		});
		// Territory (menuBar)
		// save -> TerritorySaveController
		mainStage.getSaveAsPNGMenuItem().setOnAction(e -> {
			String extension = ".png";
			File file = getFile(I18nUtils.i18n("Menu.territory.saveAsPic.png.description"), extension);
			if (file == null)
				return;

			if (!saveAsImage(file, extension)) {
				AlertHelper.showAlertAndWait(AlertType.ERROR, I18nUtils.i18n("Menu.territory.saveAsPic.error"),
						mainStage);
			}
		});
		mainStage.getSaveAsGifMenuItem().setOnAction(e -> {
			String extension = ".gif";
			File file = getFile(I18nUtils.i18n("Menu.territory.saveAsPic.gif.description"), extension);
			if (file == null)
				return;

			if (!saveAsImage(file, extension)) {
				AlertHelper.showAlertAndWait(AlertType.ERROR, I18nUtils.i18n("Menu.territory.saveAsPic.error"),
						mainStage);
			}
		});
		mainStage.getPrintTerritoryMenuItem().setOnAction(e -> printTerritory());
		mainStage.getChangeSizeTerritoryMenuItem()
				.setOnAction(new ChangeTerritorySizeHandler(mainStage, mainStage.getTerritory()));

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
		// Robbi (menuBar)
		mainStage.getMoveMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().vor();
			} catch (HollowAheadException ex) {
				AlertHelper.showAlertAndWait(AlertType.WARNING, ex.getMessage(), mainStage);
			}
		});
		mainStage.getTurnLeftMenuItem().setOnAction(e -> mainStage.getTerritory().getRobbi().linksUm());
		mainStage.getPutMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().legeAb();
			} catch (BagIsEmptyException | TileIsFullException ex) {
				AlertHelper.showAlertAndWait(AlertType.WARNING, ex.getMessage(), mainStage);
			}
		});
		mainStage.getTakeMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().nehmeAuf();
			} catch (NoItemException | BagIsFullException ex) {
				AlertHelper.showAlertAndWait(AlertType.WARNING, ex.getMessage(), mainStage);
			}
		});
		mainStage.getPushPileOfScrapMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().schiebeSchrotthaufen();
			} catch (NoPileOfScrapAheadException | TileBlockedException ex) {
				AlertHelper.showAlertAndWait(AlertType.WARNING, ex.getMessage(), mainStage);
			}
		});
		mainStage.getItemPresentMenuItem()
				.setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
						I18nUtils.i18n("Execution.information.itemPresent")
								+ mainStage.getTerritory().getRobbi().gegenstandDa(),
						mainStage));
		mainStage.getIsStockpileMenuItem().setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
				I18nUtils.i18n("Execution.information.stockpile") + mainStage.getTerritory().getRobbi().istLagerplatz(),
				mainStage));
		mainStage.getHollowAheadMenuItem().setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
				I18nUtils.i18n("Execution.information.hollow") + mainStage.getTerritory().getRobbi().vornKuhle(),
				mainStage));
		mainStage.getPileOfScrapAheadMenuItem()
				.setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
						I18nUtils.i18n("Execution.information.pileOfScrap")
								+ mainStage.getTerritory().getRobbi().vornSchrotthaufen(),
						mainStage));

		mainStage.getIsBagFullMenuItem().setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
				I18nUtils.i18n("Execution.information.bag") + mainStage.getTerritory().getRobbi().istTascheVoll(),
				mainStage));
		// simualtion (menuBar) -> SimualtionController
		// examples (menuBar) -> ExamplesController
		// tutor (menuBar) -> TutorController / StudentController
		// window (menuBar)
		// language -> LangaugeController
		mainStage.getChangeCursorMenuItem().setOnAction(e -> {
			setChangeCursor(mainStage.getChangeCursorMenuItem().isSelected());
			if (!mainStage.getChangeCursorMenuItem().isSelected())
				mainStage.getScene().setCursor(Cursor.DEFAULT);
		});
		mainStage.getDarkModeMenuItem().selectedProperty().bindBidirectional(SceneManager.darkmodeProperty());
		SceneManager.darkmodeProperty().addListener((obs, oldVal, newVal) -> {
			if (Boolean.TRUE.equals(newVal)) {
				mainStage.getScene().getStylesheets().add(SceneManager.getDarkmodeCss());
			} else
				mainStage.getScene().getStylesheets().remove(SceneManager.getDarkmodeCss());
		});
		if (SceneManager.getDarkmode())
			mainStage.getScene().getStylesheets().add(SceneManager.getDarkmodeCss());
		mainStage.getEnableSoundsMenuItem().selectedProperty().bindBidirectional(SoundManager.soundProperty());
		mainStage.getInfoMenuItem()
				.setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
						I18nUtils.i18n("Menu.window.info.content"), mainStage, Modality.WINDOW_MODAL,
						I18nUtils.i18n("Menu.window.info.title"), I18nUtils.i18n("Menu.window.info.header")));
		mainStage.getLibraryMenuItem().setOnAction(e -> {
			String javaFxVersion = System.getProperty("javafx.version");
			String javaVersion = System.getProperty("java.version");
			String derbyVersion = sysinfo.getVersionString();
			String jaxbVersion = JAXBContext.class.getPackage().getImplementationVersion();
			String hibernateVersion = Version.getVersionString();
			String lombokVersion = Generated.class.getPackage().getImplementationVersion();
			String log4jVersion = Logger.class.getPackage().getImplementationVersion();
			String jfoenixVersion = JFXUtilities.class.getPackage().getImplementationVersion();
			AlertHelper.showAlertAndWait(AlertType.INFORMATION,
					I18nUtils.i18n("Menu.window.libraries.content", javaVersion, javaFxVersion, jfoenixVersion,
							derbyVersion, jaxbVersion, hibernateVersion, log4jVersion, lombokVersion),
					mainStage, Modality.WINDOW_MODAL, I18nUtils.i18n("Menu.window.libraries.title"),
					I18nUtils.i18n("Menu.window.libraries.header"));
		});

		// Editor (toolbar)
		mainStage.getNewButtonToolbar().onActionProperty().bind(mainStage.getNewEditorMenuItem().onActionProperty());
		mainStage.getLoadButtonToolbar().onActionProperty().bind(mainStage.getOpenEditorMenuItem().onActionProperty());
		mainStage.getSaveButtonToolbar().onActionProperty().bind(mainStage.getSaveEditorMenuItem().onActionProperty());
		mainStage.getCompileButtonToolbar().onActionProperty()
				.bind(mainStage.getCompileEditorMenuItem().onActionProperty());
		// Territory (toolbar)
		mainStage.getChangeSizeButtonToolbar().onActionProperty()
				.bind(mainStage.getChangeSizeTerritoryMenuItem().onActionProperty());
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
		// Robbi (Toolbar)
		mainStage.getRobbiMoveButtonToolbar().onActionProperty().bind(mainStage.getMoveMenuItem().onActionProperty());
		mainStage.getRobbiTurnLeftButtonToolbar().onActionProperty()
				.bind(mainStage.getTurnLeftMenuItem().onActionProperty());
		mainStage.getRobbiPutButtonToolbar().onActionProperty().bind(mainStage.getPutMenuItem().onActionProperty());
		mainStage.getRobbiTakeButtonToolbar().onActionProperty().bind(mainStage.getTakeMenuItem().onActionProperty());
		// Simulation (Toolbar) -> SimulationController

		// editor Panel
		mainStage.getTextArea().textProperty().addListener((observalble, oldVal, newVal) -> {
			Program program = mainStage.getProgram();
			boolean before = program.isEdited();
			program.setEdited(!newVal.equals(program.getEditorContent()));
			if (before != program.isEdited())
				mainStage.setTitle(getTitle(program));
		});

	}

	/**
	 * Saves the current territoryPanel in the given file with the given extension.
	 * If the file does not have the correct extension, the given extension will be
	 * appended to the files name.
	 * 
	 * @param file      The file the image should be written into
	 * @param extension The image extension
	 * @return false, if the creation failed, true otherwise
	 */
	private boolean saveAsImage(File file, String extension) {

		if (!file.getName().endsWith(extension)) {
			file = new File(file.getAbsolutePath() + extension);
		}

		TerritoryPanel tPanel = mainStage.getTerritoryPanel();
		WritableImage snapshot = tPanel.snapshot(new SnapshotParameters(), null);

		BufferedImage bufferedImage = new BufferedImage((int) tPanel.getWidth(), (int) tPanel.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		BufferedImage image = SwingFXUtils.fromFXImage(snapshot, bufferedImage);
		try {
			logger.info("Saving territory to {}", file);
			if (!ImageIO.write(image, extension.substring(1), file))
				logger.debug("Failed to find appropiate ImageWriter");

		} catch (IOException e) {
			logger.info("Failed to save territory as image");
			return false;
		}
		return true;
	}

	/**
	 * Opens a FileChooser in the default programs folder and asks the user to
	 * select or save a file to save an image.
	 * 
	 * @param description the description for the extension
	 * @param extension   the file-extension
	 * @return the selected file, null otherwise
	 */
	private File getFile(String description, String extension) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, "*" + extension));
		return chooser.showSaveDialog(mainStage);
	}

	/**
	 * Gets the title for the application by the corresponding program
	 * 
	 * @param program The program for the current instance
	 * @return the name of the application include a * at the end, if the program
	 *         has unsaved changes
	 */
	private String getTitle(Program program) {
		StringBuilder builder = new StringBuilder();
		builder.append(I18nUtils.i18n("Main.title")).append(": ").append(program.getName())
				.append((program.isEdited() ? "*" : ""));
		return builder.toString();
	}

	/**
	 * Sends the territory to a printer in order to print it.
	 */
	private void printTerritory() {

		PrinterJob printerJob = PrinterJob.createPrinterJob();

		if (printerJob != null) {
			boolean flag = printerJob.showPrintDialog(mainStage);
			if (!flag)
				return;
			TerritoryPanel node = mainStage.getTerritoryPanel();

			// scale the node
			// https://www.tabnine.com/code/java/methods/javafx.print.PageLayout/getPrintableWidth
			PageLayout pageLayout = printerJob.getJobSettings().getPageLayout();
			double scaleX = 1.0;
			if (pageLayout.getPrintableWidth() < node.getBoundsInParent().getWidth()) {
				scaleX = pageLayout.getPrintableWidth() / node.getBoundsInParent().getWidth();
			}
			double scaleY = 1.0;
			if (pageLayout.getPrintableHeight() < node.getBoundsInParent().getHeight()) {
				scaleY = pageLayout.getPrintableHeight() / node.getBoundsInParent().getHeight();
			}
			double scaleXY = Double.min(scaleX, scaleY);
			Scale scale = new Scale(scaleXY, scaleXY);
			node.getTransforms().add(scale);
			// Print the node
			flag = printerJob.printPage(node);
			node.getTransforms().remove(scale);
			if (flag) {
				// End the printer job
				if (!printerJob.endJob()) {
					logger.debug("Could not end printerJob");
				}
			} else {
				logger.info("Printing failed or cancled");
			}
		} else {
			logger.info("Failed to create printerJob");
			AlertHelper.showAlertAndWait(AlertType.ERROR, I18nUtils.i18n("Menu.territory.print.error"), mainStage);
		}
	}

	/**
	 * Creates an EventHandler for the Toolbar buttons. This EventHandlers are only
	 * usable for the buttons which allow to interact with the territory, e.g.
	 * placing something into the territory or removing items from it.
	 * 
	 * @param img   The image, the cursor should change to, if the button is pressed
	 * @param index the buttonIndex, the buttonState should change to if the button
	 *              is pressed
	 * @return the eventHandler for the Toolbar button
	 */
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

	/**
	 * Creates an EventHandler for the MenuItem buttons. This EventHandlers are only
	 * usable for the items which allow to interact with the territory, e.g. placing
	 * something into the territory or removing items from it.
	 * 
	 * @param img   The image, the cursor should change to, if the item is selected
	 * @param index the buttonIndex, the buttonState should change to if the item is
	 *              selected
	 * @return the eventHandler for the MenuItem
	 */
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

	@Override
	public void update(Observable observable) {
		if (observable instanceof Program program) {
			mainStage.getTextArea().setText(program.getEditorContent());
		}
	}

}
