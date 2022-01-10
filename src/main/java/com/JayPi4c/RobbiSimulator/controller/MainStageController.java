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
import com.JayPi4c.RobbiSimulator.model.RobbiException;
import com.JayPi4c.RobbiSimulator.model.TileBlockedException;
import com.JayPi4c.RobbiSimulator.model.TileIsFullException;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;

import javafx.collections.ObservableSet;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * This controller contains all the settings for the mainStage
 * 
 * @author Jonas Pohl
 *
 */
public class MainStageController {
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

		// editor actions
		mainStage.getNewEditorMenuItem().setOnAction(e -> ProgramController.createAndShow());
		mainStage.getNewButtonToolbar().onActionProperty().bind(mainStage.getNewEditorMenuItem().onActionProperty());

		mainStage.getOpenEditorMenuItem().setOnAction(e -> ProgramController.openProgram());
		mainStage.getLoadButtonToolbar().onActionProperty().bind(mainStage.getOpenEditorMenuItem().onActionProperty());

		mainStage.getSaveEditorMenuItem().setOnAction(e -> {
			mainStage.getProgram().save(mainStage.getTextArea().getText());
			mainStage.setTitle(getTitle(mainStage.getProgram()));
		});
		mainStage.getSaveButtonToolbar().onActionProperty().bind(mainStage.getSaveEditorMenuItem().onActionProperty());

		mainStage.getCompileEditorMenuItem().setOnAction(e -> {
			Program program = mainStage.getProgram();
			program.save(mainStage.getTextArea().getText());
			mainStage.setTitle(getTitle(program));
			ProgramController.compile(program);
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

		mainStage.getSaveAsPNGMenuItem().setOnAction(e -> {
			String extension = "png";
			File file = getFile(Messages.getString("Menu.territory.saveAsPic.png.description"), extension);
			if (file == null)
				return;

			if (!saveAsImage(file, extension)) {
				showSaveError(Messages.getString("Menu.territory.saveAsPic.error"));
			}
		});

		mainStage.getSaveAsGifMenuItem().setOnAction(e -> {
			String extension = "gif";
			File file = getFile(Messages.getString("Menu.territory.saveAsPic.gif.description"), extension);
			if (file == null)
				return;

			if (!saveAsImage(file, extension)) {
				showSaveError(Messages.getString("Menu.territory.saveAsPic.error"));
			}
		});

		/*
		 * Eigentlich sollte das Drucken hier jetzt funktionieren allerdings kann ich es
		 * nicht testen, da bei mir niemals ein PrinterJob erstellt wird. Ich denke, das
		 * liegt an Linux. Andereseits kann es auch damit zu tun haben, dass ich OpenJDK
		 * verwende und nicht Oracle JDK. Nach einer kurzen Google Recherche habe ich
		 * herausgefunden, das jemanden mit OpenJDK genau die gleichen Probleme hatte,
		 * die sich durch den Wechsel zu Oracle JDK lösen ließen.
		 */
		mainStage.getPrintTerritoryMenuItem().setOnAction(e -> {

			// Create a printer job for the default printer
			ObservableSet<Printer> allPrinters = Printer.getAllPrinters();
			PrinterJob printerJob = PrinterJob.createPrinterJob();
			logger.debug("All available printers: {}", allPrinters);
			/*
			 * for (Printer p : allPrinters) { System.out.println(p.getName()); }
			 */
			if (printerJob != null) {

				// Print the node
				boolean printed = printerJob.showPrintDialog(null);
				// boolean printed = printerJob.printPage(mainStage.getTerritoryPanel());
				if (printed) {
					// End the printer job
					printerJob.endJob();
				} else {
					logger.info("Printing failed or cancled");
				}
			} else {
				logger.info("Failed to create printerJob");
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(Messages.getString("Menu.territory.print.error"));
				alert.showAndWait();
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

	}

	/**
	 * Saves the current territoryPanel in the given file with the given extension.
	 * 
	 * @param file      The file the image should be written into
	 * @param extension The image extension
	 * @return false, if the creation failed, true otherwise
	 */
	private boolean saveAsImage(File file, String extension) {
		TerritoryPanel tPanel = mainStage.getTerritoryPanel();
		WritableImage snapshot = tPanel.snapshot(new SnapshotParameters(), null);

		BufferedImage bufferedImage = new BufferedImage((int) tPanel.getWidth(), (int) tPanel.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		BufferedImage image = SwingFXUtils.fromFXImage(snapshot, bufferedImage);
		try {
			logger.info("Saving territory to {}", file);
			// Graphics2D gd = (Graphics2D) image.getGraphics();
			// gd.translate(tPanel.getWidth(), tPanel.getHeight());
			ImageIO.write(image, extension, file);
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
	 * Shows an error alert with the given message.
	 * 
	 * @param msg the message to be displayed in the alert
	 */
	private void showSaveError(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(msg);
		alert.showAndWait();
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
	 * Shows an alert with the message given as parameter
	 * 
	 * @param ex The RobbiException, which message should be shown
	 */
	private void showWarning(RobbiException ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(ex.getMessage());
		alert.showAndWait();
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
}
