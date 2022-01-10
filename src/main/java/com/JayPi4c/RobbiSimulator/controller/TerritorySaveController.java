package com.JayPi4c.RobbiSimulator.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.model.DIRECTION;
import com.JayPi4c.RobbiSimulator.model.Item;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.stage.FileChooser;

public class TerritorySaveController {

	private static final Logger logger = LogManager.getLogger(TerritorySaveController.class);

	private static final String DEFAULT_FILE_EXTENSION = ".ter";

	private MainStage mainStage;

	public TerritorySaveController(MainStage mainStage) {
		this.mainStage = mainStage;
		this.mainStage.getSaveSerialTerritoryMenuItem().setOnAction(e -> serialize());
		this.mainStage.getLoadSerialTerritoryMenuItem().setOnAction(e -> deserialize());

	}

	private void serialize() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("serialize");
		chooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				Messages.getString("Territory.save.dialog.filter"), "*" + DEFAULT_FILE_EXTENSION));
		File file = chooser.showSaveDialog(null);
		if (file != null) {
			logger.debug("serialize in file {}", file);
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
				oos.writeObject(mainStage.getTerritory());
				oos.writeObject(mainStage.getTerritory().getRobbiItem());
				oos.writeInt(mainStage.getTerritory().getRobbiX());
				oos.writeInt(mainStage.getTerritory().getRobbiY());
				oos.writeObject(mainStage.getTerritory().getRobbiDirection());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				logger.debug("finished serialization");
			}
		}
	}

	private void deserialize() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("open Program"); // TODO Change internationalization
		fileChooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				Messages.getString("Territory.load.dialog.filter"), "*" + DEFAULT_FILE_EXTENSION));

		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
				logger.debug("deserialize from file {}", file);
				Territory t = (Territory) ois.readObject();
				Item item = (Item) ois.readObject();
				int x = ois.readInt();
				int y = ois.readInt();
				DIRECTION facing = (DIRECTION) ois.readObject();
				mainStage.getTerritory().update(t, item, x, y, facing);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				logger.debug("finished deserialization");

			}

		}
	}
}
