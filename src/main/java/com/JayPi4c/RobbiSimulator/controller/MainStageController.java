package com.JayPi4c.RobbiSimulator.controller;

import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.model.*;
import com.JayPi4c.RobbiSimulator.utils.*;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import com.JayPi4c.RobbiSimulator.view.MenuBar;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;
import com.JayPi4c.RobbiSimulator.view.Toolbar;
import eu.mihosoft.monacofx.MonacoFX;
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
import org.apache.logging.log4j.Logger;
import org.hibernate.Version;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.i18n;

/**
 * This controller contains all the settings for the mainStage
 *
 * @author Jonas Pohl
 */
@Slf4j
public class MainStageController implements Observer {

    private final ButtonState buttonState;

    private final MainStage mainStage;

    @Setter
    private boolean changeCursor = false;
    @Getter
    private final boolean soundsEnabled = false;

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

        mainStage.setTitle(i18n("Main.title") + ": " + mainStage.getProgram().getName());

        mainStage.setOnCloseRequest(e -> {
            mainStage.getProgram().save(mainStage.getTextArea().getEditor().getDocument().getText());
            ProgramController.close(mainStage.getProgram().getName());
        });

        MenuBar menuBar = mainStage.getMenubar();

        // editor (menuBar)
        menuBar.getNewEditorMenuItem().setOnAction(e -> ProgramController.createAndShow(mainStage));
        menuBar.getOpenEditorMenuItem().setOnAction(e -> ProgramController.openProgram(mainStage));
        menuBar.getSaveEditorMenuItem().setOnAction(e -> {
            mainStage.getProgram().save(mainStage.getTextArea().getEditor().getDocument().getText());
            mainStage.setTitle(getTitle(mainStage.getProgram()));
        });

        menuBar.getFormatSourceCodeMenuItem().setOnAction(e -> {
            mainStage.getNotificationController().showMessage(1500, "not.implemented");
            // For this the following PR should first be merged:
            // https://github.com/miho/MonacoFX/pull/25
        });

        menuBar.getCompileEditorMenuItem().setOnAction(e -> {
            mainStage.getSimulationController().stopSimulation();
            Program program = mainStage.getProgram();
            program.save(mainStage.getTextArea().getEditor().getDocument().getText());
            mainStage.setTitle(getTitle(program));
            ProgramController.compile(program, mainStage);
        });
        // TODO print editor content
        menuBar.getPrintEditorMenuItem()
                .setOnAction(e -> mainStage.getNotificationController().showMessage(1500, "not.implemented"));
        menuBar.getQuitEditorMenuItem().setOnAction(e -> {
            Program program = mainStage.getProgram();
            logger.info("exiting {}", program.getName());
            program.save(mainStage.getTextArea().getEditor().getDocument().getText());
            ProgramController.close(program.getName());
            mainStage.close();
        });
        // Territory (menuBar)
        // save -> TerritorySaveController
        menuBar.getSaveAsPNGMenuItem().setOnAction(e -> {
            String extension = ".png";
            File file = getFile(i18n("Menu.territory.saveAsPic.png.description"), extension);
            if (file == null)
                return;

            if (!saveAsImage(file, extension)) {
                mainStage.getNotificationController().showMessage(3000, "Menu.territory.saveAsPic.error");
            }
        });
        menuBar.getSaveAsGifMenuItem().setOnAction(e -> {
            String extension = ".gif";
            File file = getFile(i18n("Menu.territory.saveAsPic.gif.description"), extension);
            if (file == null)
                return;

            if (!saveAsImage(file, extension)) {
                mainStage.getNotificationController().showMessage(3000, "Menu.territory.saveAsPic.error");
            }
        });
        menuBar.getPrintTerritoryMenuItem().setOnAction(e -> printTerritory());
        menuBar.getChangeSizeTerritoryMenuItem()
                .setOnAction(new ChangeTerritorySizeHandler(mainStage, mainStage.getTerritory()));

        menuBar.getPlaceRobbiTerritoryRadioMenuItem()
                .setOnAction(getRadioHandler(MainStage.menuRobbiImage, ButtonState.ROBBI));
        menuBar.getPlaceHollowTerritoryRadioMenuItem()
                .setOnAction(getRadioHandler(MainStage.menuHollowImage, ButtonState.HOLLOW));
        menuBar.getPlacePileOfScrapTerritoryRadioMenuItem()
                .setOnAction(getRadioHandler(MainStage.menuPileOfScrapImage, ButtonState.PILE_OF_SCRAP));
        menuBar.getPlaceStockpileTerritoryRadioMenuItem()
                .setOnAction(getRadioHandler(MainStage.menuStockpileImage, ButtonState.STOCKPILE));
        menuBar.getPlaceAccuTerritoryRadioMenuItem()
                .setOnAction(getRadioHandler(MainStage.menuAccuImage, ButtonState.ACCU));
        menuBar.getPlaceScrewTerritoryRadioMenuItem()
                .setOnAction(getRadioHandler(MainStage.menuScrewImage, ButtonState.SCREW));
        menuBar.getPlaceNutTerritoryRadioMenuItem()
                .setOnAction(getRadioHandler(MainStage.menuNutImage, ButtonState.NUT));
        menuBar.getDeleteFieldRadioMenuItem()
                .setOnAction(getRadioHandler(MainStage.menuDeleteImage, ButtonState.CLEAR));
        // Robbi (menuBar)
        menuBar.getMoveMenuItem().setOnAction(e -> {
            try {
                mainStage.getTerritory().getRobbi().vor();
            } catch (HollowAheadException ex) {
                mainStage.getNotificationController().showMessage(3000, ex.getMessage());
            }
        });
        menuBar.getTurnLeftMenuItem().setOnAction(e -> mainStage.getTerritory().getRobbi().linksUm());
        menuBar.getPutMenuItem().setOnAction(e -> {
            try {
                mainStage.getTerritory().getRobbi().legeAb();
            } catch (BagIsEmptyException | TileIsFullException ex) {
                mainStage.getNotificationController().showMessage(3000, ex.getMessage());
            }
        });
        menuBar.getTakeMenuItem().setOnAction(e -> {
            try {
                mainStage.getTerritory().getRobbi().nehmeAuf();
            } catch (NoItemException | BagIsFullException ex) {
                mainStage.getNotificationController().showMessage(3000, ex.getMessage());
            }
        });
        menuBar.getPushPileOfScrapMenuItem().setOnAction(e -> {
            try {
                mainStage.getTerritory().getRobbi().schiebeSchrotthaufen();
            } catch (NoPileOfScrapAheadException | TileBlockedException ex) {
                mainStage.getNotificationController().showMessage(3000, ex.getMessage());
            }
        });

        menuBar.getItemPresentMenuItem().setOnAction(e -> {
            boolean itemPresent = mainStage.getTerritory().getRobbi().gegenstandDa();
            mainStage.getNotificationController().showMessage(3000, "Execution.information.itemPresent", itemPresent);
        });
        menuBar.getIsStockpileMenuItem().setOnAction(e -> {
            boolean stockpilePresent = mainStage.getTerritory().getRobbi().istLagerplatz();
            mainStage.getNotificationController().showMessage(3000, "Execution.information.stockpile", stockpilePresent);
        });
        menuBar.getHollowAheadMenuItem().setOnAction(e -> {
            boolean hollowAhead = mainStage.getTerritory().getRobbi().vornKuhle();
            mainStage.getNotificationController().showMessage(3000, "Execution.information.hollow", hollowAhead);
        });
        menuBar.getPileOfScrapAheadMenuItem().setOnAction(e -> {
            boolean pileOfScrapAhead = mainStage.getTerritory().getRobbi().vornSchrotthaufen();
            mainStage.getNotificationController().showMessage(3000, "Execution.information.pileOfScrap", pileOfScrapAhead);
        });
        menuBar.getIsBagFullMenuItem().setOnAction(e -> {
            boolean isBagFull = mainStage.getTerritory().getRobbi().istTascheVoll();
            mainStage.getNotificationController().showMessage(3000, "Execution.information.bag", isBagFull);
        });

        // simulation (menuBar) -> SimualtionController
        // examples (menuBar) -> ExamplesController
        // tutor (menuBar) -> TutorController / StudentController
        // window (menuBar)
        // language -> LangaugeController

        menuBar.getChangeCursorMenuItem().setOnAction(e -> {
            setChangeCursor(menuBar.getChangeCursorMenuItem().isSelected());
            if (!menuBar.getChangeCursorMenuItem().isSelected())
                mainStage.getScene().setCursor(Cursor.DEFAULT);
        });
        menuBar.getDarkModeMenuItem().selectedProperty().bindBidirectional(SceneManager.darkmodeProperty());
        SceneManager.darkmodeProperty().addListener((obs, oldVal, newVal) -> {
            if (Boolean.TRUE.equals(newVal)) {
                mainStage.getScene().getStylesheets().add(SceneManager.getDarkmodeCss());
                mainStage.getTextArea().getEditor().setCurrentTheme("vs-dark");
            } else {
                mainStage.getScene().getStylesheets().remove(SceneManager.getDarkmodeCss());
                mainStage.getTextArea().getEditor().setCurrentTheme("vs-light");
            }
        });
        if (SceneManager.getDarkmode()) {
            mainStage.getScene().getStylesheets().add(SceneManager.getDarkmodeCss());
            mainStage.getTextArea().getEditor().setCurrentTheme("vs-dark");
        }
        menuBar.getEnableSoundsMenuItem().selectedProperty().bindBidirectional(SoundManager.soundProperty());
        menuBar.getInfoMenuItem()
                .setOnAction(e -> AlertHelper.showAlertAndWait(AlertType.INFORMATION, i18n("Menu.window.info.content"),
                        mainStage, Modality.WINDOW_MODAL, i18n("Menu.window.info.title"),
                        i18n("Menu.window.info.header")));
        menuBar.getLibraryMenuItem().setOnAction(e -> {
            String javaFxVersion = System.getProperty("javafx.version");
            String javaVersion = System.getProperty("java.version");
            String derbyVersion = "10.x"; // TODO: read info from derby's info.properties
            String jaxbVersion = JAXBContext.class.getPackage().getImplementationVersion();
            String hibernateVersion = Version.getVersionString();
            String lombokVersion = Generated.class.getPackage().getImplementationVersion();
            String log4jVersion = Logger.class.getPackage().getImplementationVersion();
            String monacoFxVersion = MonacoFX.class.getPackage().getImplementationVersion();
            AlertHelper.showAlertAndWait(AlertType.INFORMATION,
                    i18n("Menu.window.libraries.content", javaVersion, javaFxVersion, monacoFxVersion,
                            derbyVersion, jaxbVersion, hibernateVersion, log4jVersion, lombokVersion),
                    mainStage, Modality.WINDOW_MODAL, i18n("Menu.window.libraries.title"),
                    i18n("Menu.window.libraries.header"));
        });

        Toolbar toolbar = mainStage.getToolbar();

        // Editor (toolbar)
        toolbar.getNewButtonToolbar().onActionProperty().bind(menuBar.getNewEditorMenuItem().onActionProperty());
        toolbar.getLoadButtonToolbar().onActionProperty().bind(menuBar.getOpenEditorMenuItem().onActionProperty());
        toolbar.getSaveButtonToolbar().onActionProperty().bind(menuBar.getSaveEditorMenuItem().onActionProperty());
        toolbar.getCompileButtonToolbar().onActionProperty()
                .bind(menuBar.getCompileEditorMenuItem().onActionProperty());
        // Territory (toolbar)
        toolbar.getChangeSizeButtonToolbar().onActionProperty()
                .bind(menuBar.getChangeSizeTerritoryMenuItem().onActionProperty());
        toolbar.getPlaceRobbiToggleButtonToolbar()
                .setOnAction(getButtonHandler(MainStage.menuRobbiImage, ButtonState.ROBBI));
        toolbar.getPlaceHollowToggleButtonToolbar()
                .setOnAction(getButtonHandler(MainStage.menuHollowImage, ButtonState.HOLLOW));
        toolbar.getPlacePileOfScrapToggleButtonToolbar()
                .setOnAction(getButtonHandler(MainStage.menuPileOfScrapImage, ButtonState.PILE_OF_SCRAP));
        toolbar.getPlaceStockpileToggleButtonToolbar()
                .setOnAction(getButtonHandler(MainStage.menuStockpileImage, ButtonState.STOCKPILE));
        toolbar.getPlaceAccuToggleButtonToolbar()
                .setOnAction(getButtonHandler(MainStage.menuAccuImage, ButtonState.ACCU));
        toolbar.getPlaceScrewToggleButtonToolbar()
                .setOnAction(getButtonHandler(MainStage.menuScrewImage, ButtonState.SCREW));
        toolbar.getPlaceNutToggleButtonToolbar().setOnAction(getButtonHandler(MainStage.menuNutImage, ButtonState.NUT));
        toolbar.getDeleteFieldToggleButtonToolbar()
                .setOnAction(getButtonHandler(MainStage.menuDeleteImage, ButtonState.CLEAR));
        // Robbi (Toolbar)
        toolbar.getRobbiMoveButtonToolbar().onActionProperty().bind(menuBar.getMoveMenuItem().onActionProperty());
        toolbar.getRobbiTurnLeftButtonToolbar().onActionProperty()
                .bind(menuBar.getTurnLeftMenuItem().onActionProperty());
        toolbar.getRobbiPutButtonToolbar().onActionProperty().bind(menuBar.getPutMenuItem().onActionProperty());
        toolbar.getRobbiTakeButtonToolbar().onActionProperty().bind(menuBar.getTakeMenuItem().onActionProperty());
        // Simulation (Toolbar) -> SimulationController

        // editor Panel
        mainStage.getTextArea().getEditor().getDocument().textProperty().addListener((observalble, oldVal, newVal) -> {
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
     * has unsaved changes
     */
    private String getTitle(Program program) {
        return i18n("Main.title") + ": " + program.getName() + (program.isEdited() ? "*" : "");
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
            AlertHelper.showAlertAndWait(AlertType.ERROR, i18n("Menu.territory.print.error"), mainStage);
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
            mainStage.getTextArea().getEditor().getDocument().setText(program.getEditorContent());
        }
    }

}
