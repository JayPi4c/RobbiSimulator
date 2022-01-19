package com.JayPi4c.RobbiSimulator.controller.examples;

import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.utils.ILanguageChangeListener;
import com.JayPi4c.RobbiSimulator.utils.Messages;
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

public class ExamplesController implements ILanguageChangeListener {
	private static final Logger logger = LogManager.getLogger(ExamplesController.class);

	private MainStage stage;

	public ExamplesController(MainStage stage) {
		Messages.registerListener(this);
		this.stage = stage;

		stage.getSaveExampleMenuItem().setOnAction(e -> {
			Optional<String[]> tags = enterTags();
			tags.ifPresentOrElse(ts -> {
				String territoryXML = stage.getTerritory().toXML().toString();
				DatabaseManager.getDatabaseManager().store(stage.getProgram().getName(),
						stage.getProgram().getEditorContent(), territoryXML, ts);
			}, () -> {
				logger.debug("No tags were entered");
			});
		});

		stage.getLoadExampleMenuItem().setOnAction(e -> {
			Optional<List<String>> tagsOpt = getAllDistinctTags();
			tagsOpt.ifPresentOrElse(tags -> {
				Optional<String> s = showTagSelection(tags);
				s.ifPresentOrElse(selectedTag -> {
					List<Pair<Integer, String>> programs = DatabaseManager.getDatabaseManager().query(selectedTag);
					Optional<Integer> idOpt = showProgramSelection(programs);
					idOpt.ifPresentOrElse(id -> DatabaseManager.getDatabaseManager().loadExample(id),
							() -> logger.debug("no example selected"));
				}, () -> logger.debug("No tag selected"));
			}, () -> {
				logger.info("No tags are stored in database");
				// TODO show alert
			});

		});
	}

	public Optional<Integer> showProgramSelection(List<Pair<Integer, String>> programs) {
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setTitle(Messages.getString("Examples.load.dialog.program.title"));
		dialog.setHeaderText(Messages.getString("Examples.load.dialog.program.header"));
		// dialog.initOwner(parent);
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		ComboBox<HideableItem<Pair<Integer, String>>> comboBox = createComboBoxWithAutoCompletionSupport(programs);
		comboBox.getSelectionModel().select(0);
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(Messages.getString("Examples.load.dialog.program.name")), comboBox);

		dialogPane.setContent(grid);
		Platform.runLater(comboBox::requestFocus);
		dialog.setResultConverter(
				button -> (button == ButtonType.OK) ? comboBox.getValue().getObject().getKey() : null);
		Optional<Integer> result = dialog.showAndWait();
		return result;
	}

	public Optional<String> showTagSelection(List<String> tags) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle(Messages.getString("Examples.load.dialog.tags.title"));
		dialog.setHeaderText(Messages.getString("Examples.load.dialog.tags.header"));
		// dialog.initOwner(parent);
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		ComboBox<HideableItem<String>> comboBox = createComboBoxWithAutoCompletionSupport(tags);
		comboBox.getSelectionModel().select(0);
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(Messages.getString("Examples.load.dialog.tags.name")), comboBox);

		dialogPane.setContent(grid);
		Platform.runLater(comboBox::requestFocus);
		dialog.setResultConverter(button -> (button == ButtonType.OK) ? comboBox.getValue().toString() : null);
		Optional<String> result = dialog.showAndWait();
		return result;
	}

	@Override
	public void onLanguageChanged() {
		stage.getExamplesMenu().setText(Messages.getString("Menu.examples"));
		stage.getLoadExampleMenuItem().setText(Messages.getString("Menu.examples.load"));
		stage.getSaveExampleMenuItem().setText(Messages.getString("Menu.examples.save"));
	}

	private Optional<List<String>> getAllDistinctTags() {
		return Optional.ofNullable(DatabaseManager.getDatabaseManager().getAllTags());
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
		grid.addRow(0, new Label(Messages.getString("Examples.save.tags.name")), tagsField);

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

	private static class HideableItem<T> {
		private final ObjectProperty<T> object = new SimpleObjectProperty<>();
		private final BooleanProperty hidden = new SimpleBooleanProperty();

		private HideableItem(T object) {
			setObject(object);
		}

		private ObjectProperty<T> objectProperty() {
			return this.object;
		}

		private T getObject() {
			return this.objectProperty().get();
		}

		private void setObject(T object) {
			this.objectProperty().set(object);
		}

		private BooleanProperty hiddenProperty() {
			return this.hidden;
		}

		private boolean isHidden() {
			return this.hiddenProperty().get();
		}

		private void setHidden(boolean hidden) {
			this.hiddenProperty().set(hidden);
		}

		@Override
		public String toString() {
			return getObject() == null ? null : getObject().toString();
		}
	}

	/**
	 * <a href="https://stackoverflow.com/a/47933342/13670629">Stackoverflow</a>
	 * 
	 * @param <T>
	 * @param items
	 * @return
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
			if (newVal) {
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
