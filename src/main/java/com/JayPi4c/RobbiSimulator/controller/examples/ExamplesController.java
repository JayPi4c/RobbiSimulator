package com.JayPi4c.RobbiSimulator.controller.examples;

import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.utils.ILanguageChangeListener;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ExamplesController implements ILanguageChangeListener {
	private static final Logger logger = LogManager.getLogger(ExamplesController.class);

	private MainStage stage;

	public ExamplesController(MainStage stage) {
		Messages.registerListener(this);
		this.stage = stage;

		stage.getSaveExampleMenuItem().setOnAction(e -> {
			Optional<String[]> tags = enterTags();
			if (tags.isPresent()) {
				logger.debug("entered tags are:");
				for (String s : tags.get()) {
					logger.debug(s);
				}
			}
			DatabaseManager.getDatabaseManager().store(stage.getProgram(), stage.getTerritory(), tags.get());
		});

		stage.getLoadExampleMenuItem().setOnAction(e -> {
			// TODO query for all distinct tags
			// TODO asks for tags with combobox:
			// https://stackoverflow.com/a/47933342/13670629
			DatabaseManager.getDatabaseManager().query("sad");
		});
	}

	@Override
	public void onLanguageChanged() {
		stage.getExamplesMenu().setText(Messages.getString("Menu.exmaples"));
		stage.getLoadExampleMenuItem().setText(Messages.getString("Menu.exmaples.load"));
		stage.getSaveExampleMenuItem().setText(Messages.getString("Menu.examples.save"));
	}

	private Optional<String[]> enterTags() {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle(Messages.getString("Examples.save.tags.title"));
		dialog.setHeaderText(Messages.getString("Examples.save.tags.header"));
		dialog.initOwner(stage);
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		TextField tagsField = new TextField();
		tagsField.setPromptText(Messages.getString("Examples.save.tags.prompt"));

		tagsField.textProperty().addListener((observable, oldVal, newVal) -> dialog.getDialogPane()
				.lookupButton(ButtonType.OK).setDisable(newVal.isBlank()));

		dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(Messages.getString("Examples.save.tags")), tagsField);

		dialogPane.setContent(grid);
		Platform.runLater(tagsField::requestFocus);
		dialog.setResultConverter(button -> (button == ButtonType.OK) ? tagsField.getText() : null);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			StringTokenizer tokenizer = new StringTokenizer(result.get());
			String tags[] = new String[tokenizer.countTokens()];
			int i = 0;
			while (tokenizer.hasMoreTokens()) {
				tags[i++] = tokenizer.nextToken();
			}
			return Optional.of(tags);
		} else
			return Optional.empty();

	}

}
