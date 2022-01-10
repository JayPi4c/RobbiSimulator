package com.JayPi4c.RobbiSimulator.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import com.JayPi4c.RobbiSimulator.utils.ILanguageChangeListener;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;

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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * This controller contains all the settings for the mainStage
 * 
 * @author Jonas Pohl
 *
 */
public class MainStageController implements ILanguageChangeListener {
	private static final Logger logger = LogManager.getLogger(MainStageController.class);

	private ButtonState buttonState;

	private MainStage mainStage;

	private boolean changeCursor = false;

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

		Messages.registerListener(this);

		mainStage.setTitle(Messages.getString("Main.title") + ": " + mainStage.getProgram().getName());
		mainStage.setOnCloseRequest(e -> {
			mainStage.getProgram().save(mainStage.getTextArea().getText());
			ProgramController.close(mainStage.getProgram().getName());
		});

		// editor actions
		mainStage.getNewEditorMenuItem().setOnAction(e -> ProgramController.createAndShow(mainStage));
		mainStage.getNewButtonToolbar().onActionProperty().bind(mainStage.getNewEditorMenuItem().onActionProperty());

		mainStage.getOpenEditorMenuItem().setOnAction(e -> ProgramController.openProgram(mainStage));
		mainStage.getLoadButtonToolbar().onActionProperty().bind(mainStage.getOpenEditorMenuItem().onActionProperty());

		mainStage.getSaveEditorMenuItem().setOnAction(e -> {
			mainStage.getProgram().save(mainStage.getTextArea().getText());
			mainStage.setTitle(getTitle(mainStage.getProgram()));
		});
		mainStage.getSaveButtonToolbar().onActionProperty().bind(mainStage.getSaveEditorMenuItem().onActionProperty());

		mainStage.getCompileEditorMenuItem().setOnAction(e -> {
			mainStage.getSimulationController().stopSimulation();
			Program program = mainStage.getProgram();
			program.save(mainStage.getTextArea().getText());
			mainStage.setTitle(getTitle(program));
			ProgramController.compile(program, mainStage);
		});
		mainStage.getCompileButtonToolbar().onActionProperty()
				.bind(mainStage.getCompileEditorMenuItem().onActionProperty());

		mainStage.getQuitEditorMenuItem().setOnAction(e -> {
			Program program = mainStage.getProgram();
			logger.info("exiting {}", program.getName());
			program.save(mainStage.getTextArea().getText());
			ProgramController.close(program.getName());
			mainStage.close();
		});

		mainStage.getChangeSizeTerritoryMenuItem()
				.setOnAction(new ChangeTerritorySizeHandler(mainStage, mainStage.getTerritory()));
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
		mainStage.getItemPresentMenuItem()
				.setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
						Messages.getString("Execution.information.itemPresent")
								+ mainStage.getTerritory().getRobbi().gegenstandDa(),
						mainStage));

		mainStage.getIsStockpileMenuItem()
				.setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
						Messages.getString("Execution.information.stockpile")
								+ mainStage.getTerritory().getRobbi().istLagerplatz(),
						mainStage));

		mainStage.getHollowAheadMenuItem().setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
				Messages.getString("Execution.information.hollow") + mainStage.getTerritory().getRobbi().vornKuhle(),
				mainStage));

		mainStage.getPileOfScrapAheadMenuItem()
				.setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
						Messages.getString("Execution.information.pileOfScrap")
								+ mainStage.getTerritory().getRobbi().vornSchrotthaufen(),
						mainStage));

		mainStage.getIsBagFullMenuItem().setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION,
				Messages.getString("Execution.information.bag") + mainStage.getTerritory().getRobbi().istTascheVoll(),
				mainStage));

		mainStage.getPushPileOfScrapMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().schiebeSchrotthaufen();
			} catch (NoPileOfScrapAheadException | TileBlockedException ex) {
				AlertHelper.showAlertAndWait(AlertType.WARNING, ex.getMessage(), mainStage);
			}
		});
		mainStage.getMoveMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().vor();
			} catch (HollowAheadException ex) {
				AlertHelper.showAlertAndWait(AlertType.WARNING, ex.getMessage(), mainStage);
			}
		});
		mainStage.getRobbiMoveButtonToolbar().onActionProperty().bind(mainStage.getMoveMenuItem().onActionProperty());

		mainStage.getTurnLeftMenuItem().setOnAction(e -> {
			mainStage.getTerritory().getRobbi().linksUm();
		});
		mainStage.getRobbiTurnLeftButtonToolbar().onActionProperty()
				.bind(mainStage.getTurnLeftMenuItem().onActionProperty());

		mainStage.getPutMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().legeAb();
			} catch (BagIsEmptyException | TileIsFullException ex) {
				AlertHelper.showAlertAndWait(AlertType.WARNING, ex.getMessage(), mainStage);
			}
		});
		mainStage.getRobbiPutButtonToolbar().onActionProperty().bind(mainStage.getPutMenuItem().onActionProperty());

		mainStage.getTakeMenuItem().setOnAction(e -> {
			try {
				mainStage.getTerritory().getRobbi().nehmeAuf();
			} catch (NoItemException | BagIsFullException ex) {
				AlertHelper.showAlertAndWait(AlertType.WARNING, ex.getMessage(), mainStage);
			}
		});
		mainStage.getRobbiTakeButtonToolbar().onActionProperty().bind(mainStage.getTakeMenuItem().onActionProperty());

		mainStage.getSaveAsPNGMenuItem().setOnAction(e -> {
			String extension = "png";
			File file = getFile(Messages.getString("Menu.territory.saveAsPic.png.description"), extension);
			if (file == null)
				return;

			if (!saveAsImage(file, extension)) {
				AlertHelper.showAlertAndWait(AlertType.ERROR, Messages.getString("Menu.territory.saveAsPic.error"),
						mainStage);
			}
		});

		mainStage.getSaveAsGifMenuItem().setOnAction(e -> {
			String extension = "gif";
			File file = getFile(Messages.getString("Menu.territory.saveAsPic.gif.description"), extension);
			if (file == null)
				return;

			if (!saveAsImage(file, extension)) {
				AlertHelper.showAlertAndWait(AlertType.ERROR, Messages.getString("Menu.territory.saveAsPic.error"),
						mainStage);
			}
		});

		mainStage.getPrintTerritoryMenuItem().setOnAction(e -> {

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
				AlertHelper.showAlertAndWait(AlertType.ERROR, Messages.getString("Menu.territory.print.error"),
						mainStage);
			}
		});

		// content Panel
		mainStage.getTextArea().textProperty().addListener((observalble, oldVal, newVal) -> {
			Program program = mainStage.getProgram();
			boolean before = program.isEdited();
			program.setEdited(!newVal.equals(program.getEditorContent()));
			if (before != program.isEdited())
				mainStage.setTitle(getTitle(program));
		});
		// TODO set slider labels
		// https://stackoverflow.com/questions/18447963/javafx-slider-text-as-tick-label

		// TODO print editor content
		mainStage.getPrintEditorMenuItem().setOnAction(e -> {
			AlertHelper.showAlertAndWait(AlertType.INFORMATION, "Not yet implemented", mainStage);
		});

	}

	/**
	 * Saves the current territoryPanel in the given file with the given extension.
	 * 
	 * @param file      The file the image should be written into
	 * @param extension The image extension
	 * @return false, if the creation failed, true otherwise
	 */
	private boolean saveAsImage(File file, String extension) {
		// TODO check if file has correct extension
		TerritoryPanel tPanel = mainStage.getTerritoryPanel();
		WritableImage snapshot = tPanel.snapshot(new SnapshotParameters(), null);

		BufferedImage bufferedImage = new BufferedImage((int) tPanel.getWidth(), (int) tPanel.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		BufferedImage image = SwingFXUtils.fromFXImage(snapshot, bufferedImage);
		try {
			logger.info("Saving territory to {}", file);
			// Graphics2D gd = (Graphics2D) image.getGraphics();
			// gd.translate(tPanel.getWidth(), tPanel.getHeight());
			if (!ImageIO.write(image, extension, file))
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
		chooser.setSelectedExtensionFilter(new ExtensionFilter(description, extension));
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
		builder.append(Messages.getString("Main.title")).append(": ").append(program.getName())
				.append((program.isEdited() ? "*" : ""));
		return builder.toString();
	}

	/**
	 * updates the changeCursor variable to set if the cursor should change to the
	 * selected state
	 * 
	 * @param flag true if the cursor should change, false otherwise
	 */
	public void setChangeCursor(boolean flag) {
		this.changeCursor = flag;
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

	/**
	 * When the language has changed, update all buttons to the new language.
	 * 
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void onLanguageChanged() {
		// Add multilanguage support
		mainStage.setTitle(Messages.getString("Main.title") + ": " + mainStage.getProgram()
				+ (mainStage.getProgram().isEdited() ? "*" : ""));

		mainStage.getNewEditorMenuItem().setText(Messages.getString("Menu.editor.new"));
		mainStage.getSaveEditorMenuItem().setText(Messages.getString("Menu.editor.save"));
		mainStage.getOpenEditorMenuItem().setText(Messages.getString("Menu.editor.open"));
		mainStage.getCompileEditorMenuItem().setText(Messages.getString("Menu.editor.compile"));
		mainStage.getPrintEditorMenuItem().setText(Messages.getString("Menu.editor.print"));
		mainStage.getQuitEditorMenuItem().setText(Messages.getString("Menu.editor.quit"));
		mainStage.getEditorMenu().setText(Messages.getString("Menu.editor"));

		// territory Menu
		mainStage.getSaveXMLTerritoryMenuItem().setText(Messages.getString("Menu.territory.save.xml"));
		mainStage.getSaveJAXBTerritoryMenuItem().setText(Messages.getString("Menu.territory.save.jaxb"));
		mainStage.getSaveSerialTerritoryMenuItem().setText(Messages.getString("Menu.territory.save.serialize"));
		mainStage.getSaveTerritoryMenu().setText(Messages.getString("Menu.territory.save"));
		mainStage.getLoadXMLTerritoryMenuItem().setText(Messages.getString("Menu.territory.load.xml"));
		mainStage.getLoadJAXBTerritoryMenuItem().setText(Messages.getString("Menu.territory.load.jaxb"));
		mainStage.getLoadSerialTerritoryMenuItem().setText(Messages.getString("Menu.territory.load.deserialize"));
		mainStage.getLoadTerritoryMenu().setText(Messages.getString("Menu.territory.load"));
		mainStage.getSaveAsPNGMenuItem().setText(Messages.getString("Menu.territory.saveAsPic.png"));
		mainStage.getSaveAsGifMenuItem().setText(Messages.getString("Menu.territory.saveAsPic.gif"));
		mainStage.getSaveAsPicMenu().setText(Messages.getString("Menu.territory.saveAsPic"));
		mainStage.getPrintTerritoryMenuItem().setText(Messages.getString("Menu.territory.print"));

		mainStage.getChangeSizeTerritoryMenuItem().setText(Messages.getString("Menu.territory.size"));
		mainStage.getPlaceRobbiTerritoryRadioMenuItem().setText(Messages.getString("Menu.territory.place.robbi"));
		mainStage.getPlaceHollowTerritoryRadioMenuItem().setText(Messages.getString("Menu.territory.place.hollow"));
		mainStage.getPlacePileOfScrapTerritoryRadioMenuItem()
				.setText(Messages.getString("Menu.territory.place.pileOfScrap"));
		mainStage.getPlaceStockpileTerritoryRadioMenuItem()
				.setText(Messages.getString("Menu.territory.place.stockpile"));
		mainStage.getPlaceAccuTerritoryRadioMenuItem().setText(Messages.getString("Menu.territory.place.accu"));
		mainStage.getPlaceScrewTerritoryRadioMenuItem().setText(Messages.getString("Menu.territory.place.screw"));
		mainStage.getPlaceNutTerritoryRadioMenuItem().setText(Messages.getString("Menu.territory.place.nut"));
		mainStage.getDeleteFieldRadioMenuItem().setText(Messages.getString("Menu.territory.delete"));
		mainStage.getTerritoryMenu().setText(Messages.getString("Menu.territory"));
		// robbi Menu
		mainStage.getItemPresentMenuItem().setText(Messages.getString("Menu.robbi.itemPresent"));
		mainStage.getIsStockpileMenuItem().setText(Messages.getString("Menu.robbi.isStockpile"));
		mainStage.getHollowAheadMenuItem().setText(Messages.getString("Menu.robbi.hollowAhead"));
		mainStage.getPileOfScrapAheadMenuItem().setText(Messages.getString("Menu.robbi.pileOfScrapAhead"));
		mainStage.getIsBagFullMenuItem().setText(Messages.getString("Menu.robbi.isBagFull"));
		mainStage.getPushPileOfScrapMenuItem().setText(Messages.getString("Menu.robbi.pushPileOfScrap"));
		mainStage.getMoveMenuItem().setText(Messages.getString("Menu.robbi.move"));
		mainStage.getTurnLeftMenuItem().setText(Messages.getString("Menu.robbi.turnLeft"));
		mainStage.getPutMenuItem().setText(Messages.getString("Menu.robbi.put"));
		mainStage.getTakeMenuItem().setText(Messages.getString("Menu.robbi.take"));
		mainStage.getRobbiMenu().setText(Messages.getString("Menu.robbi"));
		// simulation Menu
		mainStage.getStartMenuItem().setText(Messages.getString("Menu.simulation.start"));
		mainStage.getPauseMenuItem().setText(Messages.getString("Menu.simulation.pause"));
		mainStage.getStopMenuItem().setText(Messages.getString("Menu.simulation.stop"));
		mainStage.getSimulationMenu().setText(Messages.getString("Menu.simulation"));
		// window Meun
		mainStage.getLanguageMenu().setText(Messages.getString("Menu.window.language"));
		mainStage.getEnglishLanguageMenuItem().setText(Messages.getString("Menu.window.language.english"));
		mainStage.getGermanLanguageMenuItem().setText(Messages.getString("Menu.window.language.german"));
		mainStage.getChangeCursorMenuItem().setText(Messages.getString("Menu.window.changeCursor"));
		mainStage.getWindowMenu().setText(Messages.getString("Menu.window"));

		// Tool bar
		mainStage.getNewButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.control.new")));
		mainStage.getLoadButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.control.load")));
		mainStage.getSaveButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.control.save")));
		mainStage.getCompileButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.control.compile")));

		mainStage.getChangeSizeButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.territory.size")));
		mainStage.getPlaceRobbiToggleButtonToolbar()
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeRobbi")));
		mainStage.getPlaceHollowToggleButtonToolbar()
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeHollow")));
		mainStage.getPlacePileOfScrapToggleButtonToolbar()
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placePileOfScrap")));
		mainStage.getPlaceStockpileToggleButtonToolbar()
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeStockpile")));
		mainStage.getPlaceAccuToggleButtonToolbar()
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeAccu")));
		mainStage.getPlaceScrewToggleButtonToolbar()
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeScrew")));
		mainStage.getPlaceNutToggleButtonToolbar()
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeNut")));
		mainStage.getDeleteFieldToggleButtonToolbar()
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.delete")));

		mainStage.getRobbiTurnLeftButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.turnLeft")));
		mainStage.getRobbiMoveButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.move")));
		mainStage.getRobbiPutButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.put")));
		mainStage.getRobbiTakeButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.take")));

		mainStage.getStartToggleButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.action.start")));
		mainStage.getPauseToggleButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.action.pause")));
		mainStage.getStopToggleButtonToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.action.stop")));
		mainStage.getSpeedSliderToolbar().setTooltip(new Tooltip(Messages.getString("Toolbar.action.speed")));

		mainStage.getMessageLabel().setText(Messages.getString("Messages.label.greeting"));
	}
}
