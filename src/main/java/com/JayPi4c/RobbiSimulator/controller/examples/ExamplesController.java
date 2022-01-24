package com.JayPi4c.RobbiSimulator.controller.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * This controller contains all settings for the examples menus.
 * 
 * @author Jonas Pohl
 *
 */
public class ExamplesController {
	private static final Logger logger = LogManager.getLogger(ExamplesController.class);

	private MainStage stage;

	/**
	 * Constructor to create a new ExamplesController for the mainStage.
	 * 
	 * @param stage the mainStage, this controller is for
	 */
	public ExamplesController(MainStage stage) {
		this.stage = stage;

		stage.getSaveExampleMenuItem().setOnAction(e -> {
			Optional<List<String>> tags = enterTags();
			tags.ifPresentOrElse(ts -> {
				String territoryXML = stage.getTerritory().toXML().toString();
				if (!DatabaseManager.getDatabaseManager().store(stage.getProgram().getName(),
						stage.getProgram().getEditorContent(), territoryXML, ts))
					logger.debug("Could not save example in database");
			}, () -> logger.debug("No tags were entered"));
		});

		stage.getLoadExampleMenuItem().setOnAction(e -> {
			Optional<List<String>> tagsOpt = DatabaseManager.getDatabaseManager().getAllTags();
			tagsOpt.ifPresentOrElse(tags -> {
				Optional<String> s = showTagSelection(tags);
				s.ifPresentOrElse(selectedTag -> {
					Optional<List<Pair<Integer, String>>> programsOpt = DatabaseManager.getDatabaseManager()
							.query(selectedTag);
					if (programsOpt.isPresent()) {
						Optional<Integer> idOpt = showProgramSelection(programsOpt.get());
						idOpt.ifPresentOrElse(id -> {
							Optional<Example> exOpt = DatabaseManager.getDatabaseManager().loadExample(id);
							exOpt.ifPresentOrElse(Example::load,
									() -> logger.debug("Could not load example from database"));
						}, () -> logger.debug("No example selected"));
					}
				}, () -> logger.debug("No tag selected"));
			}, () -> {
				logger.info("No tags are stored in database");
				AlertHelper.showAlertAndWait(AlertType.WARNING, I18nUtils.i18n("Examples.load.dialog.tags.fail"),
						stage);
			});

		});
	}

	/**
	 *
	 * Shows an DialogWindow listing all programNames and its IDs. The user can
	 * select a program, which will be returned as an Optional containing the id.
	 * 
	 * @param programs a list of pairs of IDs and programNames, representing a
	 *                 program
	 * @return the id of the selected Program
	 */
	public Optional<Integer> showProgramSelection(List<Pair<Integer, String>> programs) {
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setTitle(I18nUtils.i18n("Examples.load.dialog.program.title"));
		dialog.setHeaderText(I18nUtils.i18n("Examples.load.dialog.program.header"));
		dialog.initOwner(stage);
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		ComboBox<HideableItem<Pair<Integer, String>>> comboBox = createComboBoxWithAutoCompletionSupport(programs);
		comboBox.getSelectionModel().select(0);
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(I18nUtils.i18n("Examples.load.dialog.program.name")), comboBox);

		dialogPane.setContent(grid);
		Platform.runLater(comboBox::requestFocus);
		dialog.setResultConverter(
				button -> (button == ButtonType.OK) ? comboBox.getValue().getObject().getKey() : null);
		Optional<Integer> result = dialog.showAndWait();
		return result;
	}

	/**
	 * Shows an DialogWindow listing all tags. The user can select a tag, which will
	 * be returned as an Optional.
	 * 
	 * @param tags a list of tags to be shown to the user
	 * @return the selected tag
	 */
	public Optional<String> showTagSelection(List<String> tags) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle(I18nUtils.i18n("Examples.load.dialog.tags.title"));
		dialog.setHeaderText(I18nUtils.i18n("Examples.load.dialog.tags.header"));
		dialog.initOwner(stage);
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		ComboBox<HideableItem<String>> comboBox = createComboBoxWithAutoCompletionSupport(tags);
		comboBox.getSelectionModel().select(0);
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(I18nUtils.i18n("Examples.load.dialog.tags.name")), comboBox);

		dialogPane.setContent(grid);
		Platform.runLater(comboBox::requestFocus);
		dialog.setResultConverter(button -> (button == ButtonType.OK) ? comboBox.getValue().toString() : null);
		Optional<String> result = dialog.showAndWait();
		return result;
	}

	/**
	 * Opens a dialog for the user to enter tags for the example to save them
	 * by.<br>
	 * Tags must be entered comma-separated.
	 * 
	 * @return a List<String> of tags
	 */
	private Optional<List<String>> enterTags() {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle(I18nUtils.i18n("Examples.save.tags.title"));
		dialog.setHeaderText(I18nUtils.i18n("Examples.save.tags.header"));
		dialog.initOwner(stage);
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		TextField tagsField = new TextField();
		tagsField.setPromptText(I18nUtils.i18n("Examples.save.tags.prompt"));

		tagsField.textProperty().addListener((observable, oldVal, newVal) -> dialog.getDialogPane()
				.lookupButton(ButtonType.OK).setDisable(newVal.isBlank()));

		dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(I18nUtils.i18n("Examples.save.tags.name")), tagsField);

		dialogPane.setContent(grid);
		Platform.runLater(tagsField::requestFocus);
		dialog.setResultConverter(button -> (button == ButtonType.OK) ? tagsField.getText() : null);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			StringTokenizer tokenizer = new StringTokenizer(result.get());
			List<String> tags = new ArrayList<>();
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (!tags.contains(token))
					tags.add(token);
			}
			return Optional.of(tags);
		} else
			return Optional.empty();
	}

	/**
	 * Taken from
	 * <a href="https://stackoverflow.com/a/47933342/13670629">Stackoverflow</a>.
	 * This class holds information for the entries of the AutocompletionComboBox.
	 * 
	 * @author Eng.Fouad
	 *
	 * @param <T> Type of the objects stored in the instance
	 */
	private static class HideableItem<T> {
		private final ObjectProperty<T> object = new SimpleObjectProperty<>();
		private final BooleanProperty hidden = new SimpleBooleanProperty();

		/**
		 * Constructor to create a new HideableItem.
		 * 
		 * @param object Object to be stored in the item.
		 */
		private HideableItem(T object) {
			setObject(object);
		}

		/**
		 * Getter for the ObjectProperty attribute
		 * 
		 * @return the items objectProperty
		 */
		private ObjectProperty<T> objectProperty() {
			return this.object;
		}

		/**
		 * Getter for the value stored in the item.
		 * 
		 * @return the items value
		 */
		private T getObject() {
			return this.objectProperty().get();
		}

		/**
		 * Setter for the value stored in the item.
		 * 
		 * @param object the items new value
		 */
		private void setObject(T object) {
			this.objectProperty().set(object);
		}

		/**
		 * Getter for the hiddenProperty attribute
		 * 
		 * @return the items hiddenProperty
		 */
		private BooleanProperty hiddenProperty() {
			return this.hidden;
		}

		/**
		 * Getter for the hidden value of this item.
		 * 
		 * @return true if the item is hidden, false otherwise
		 */
		private boolean isHidden() {
			return this.hiddenProperty().get();
		}

		/**
		 * Setter for the hidden value of this item.
		 * 
		 * @param hidden the new hidden value
		 */
		private void setHidden(boolean hidden) {
			this.hiddenProperty().set(hidden);
		}

		@Override
		public String toString() {
			return getObject() == null ? null : getObject().toString();
		}
	}

	/**
	 * Taken from
	 * <a href="https://stackoverflow.com/a/47933342/13670629">Stackoverflow</a>.
	 * <br>
	 * In order to allow a quick search over the attributes in the comboBox, this
	 * comboBox has the option to type in the name of the entry and if it is present
	 * it will show the entry and select it.
	 * 
	 * @param <T>   Type of the items stored in the comboBox
	 * @param items the items to store in the ComboBox.
	 * @return a comboBox, that supports Auto-Completion
	 */
	private static <T> ComboBox<HideableItem<T>> createComboBoxWithAutoCompletionSupport(List<T> items) {
		ObservableList<HideableItem<T>> hideableHideableItems = FXCollections
				.observableArrayList(hidealbeItem -> new Observable[] { hidealbeItem.hiddenProperty() });

		items.forEach(item -> {
			HideableItem<T> hideableItem = new HideableItem<>(item);
			hideableHideableItems.add(hideableItem);
		});

		FilteredList<HideableItem<T>> filteredHideableItems = new FilteredList<>(hideableHideableItems,
				t -> !t.isHidden());

		ComboBox<HideableItem<T>> comboBox = new ComboBox<>();
		comboBox.setItems(filteredHideableItems);

		@SuppressWarnings("unchecked")
		HideableItem<T>[] selectedItem = (HideableItem<T>[]) new HideableItem[1];

		comboBox.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (!comboBox.isShowing())
				return;
			comboBox.setEditable(true);
			comboBox.getEditor().clear();
		});

		comboBox.showingProperty().addListener((obs, oldVal, newVal) -> {
			if (Boolean.TRUE.equals(newVal)) {
				@SuppressWarnings("unchecked")
				ListView<HideableItem<T>> lv = (ListView<HideableItem<T>>) ((ComboBoxListViewSkin<?>) comboBox
						.getSkin()).getPopupContent();

				Platform.runLater(() -> {
					if (selectedItem[0] == null) { // first use
						double cellHeight = ((Control) lv.lookup(".list-cell")).getHeight();
						lv.setFixedCellSize(cellHeight);
					}
				});
				lv.scrollTo(comboBox.getValue());
			} else {
				HideableItem<T> value = comboBox.getValue();
				if (value != null)
					selectedItem[0] = value;
				comboBox.setEditable(false);
				Platform.runLater(() -> {
					comboBox.getSelectionModel().select(selectedItem[0]);
					comboBox.setValue(selectedItem[0]);
				});
			}
		});

		comboBox.setOnHidden(event -> hideableHideableItems.forEach(item -> item.setHidden(false)));

		comboBox.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
			if (!comboBox.isShowing())
				return;

			Platform.runLater(() -> {
				if (comboBox.getSelectionModel().getSelectedItem() == null) {
					hideableHideableItems.forEach(item -> item
							.setHidden(!item.getObject().toString().toLowerCase().contains(newVal.toLowerCase())));
				} else {
					boolean validText = false;
					for (HideableItem<?> hideableItem : hideableHideableItems) {
						if (hideableItem.getObject().toString().equals(newVal)) {
							validText = true;
							break;
						}
					}
					if (!validText)
						comboBox.getSelectionModel().select(null);
				}
			});
		});
		return comboBox;
	}

}
