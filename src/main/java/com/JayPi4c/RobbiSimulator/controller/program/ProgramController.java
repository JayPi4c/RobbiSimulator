package com.JayPi4c.RobbiSimulator.controller.program;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.JayPi4c.RobbiSimulator.model.Robbi;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ProgramController {

	private static final Logger logger = Logger.getLogger(ProgramController.class.getName());

	private static final String PATH_TO_PROGRAMS = "programs";
	public static final String DEFAULT_ROBBI_FILE_NAME = "DefaultRobbi";
	public static final String DEFAULT_FILE_EXTENSION = ".java";

	public static final String DEFAULT_CONTENT = "void main(){" + System.lineSeparator() + "\t// place your code here"
			+ System.lineSeparator() + "}";
	public static final String PREFIX_TEMPLATE = "import com.JayPi4c.RobbiSimulator.utils.annotations.*;public class %s extends com.JayPi4c.RobbiSimulator.model.Robbi{public ";
	public static final String POSTFIX_TEMPLATE = "}";

	private static final String VALID_IDENTIFIER_REGEX = "^([a-zA-Z_$][a-zA-Z\\d_$]*)$";

	private static HashMap<String, Stage> programs;

	/**
	 * 
	 * @return true if initialization finished successful, false otherwise
	 */
	public static boolean initialize() {
		File dir = new File(PATH_TO_PROGRAMS);
		if (!dir.exists()) {
			if (!dir.mkdir())
				return false;
		}

		File defaultProgram = new File(
				PATH_TO_PROGRAMS + File.separatorChar + DEFAULT_ROBBI_FILE_NAME + DEFAULT_FILE_EXTENSION);
		if (!defaultProgram.exists()) {
			try {
				if (!defaultProgram.createNewFile())
					return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(defaultProgram))) {
				writer.write(createTemplate(DEFAULT_ROBBI_FILE_NAME, DEFAULT_CONTENT));
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		programs = new HashMap<>();
		return true;
	}

	public static void createAndShow() {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle(Messages.getString("New.dialog.title"));
		dialog.setHeaderText(Messages.getString("New.dialog.header"));
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		TextField nameField = new TextField();
		nameField.setPromptText(Messages.getString("New.dialog.prompt"));

		// get a list of all files in programs directory that end with .java
		File[] files = new File(PATH_TO_PROGRAMS)
				.listFiles(file -> (file.isFile() && file.getName().endsWith(DEFAULT_FILE_EXTENSION)));
		// get a String collection with all filenames in the programs directory
		Collection<String> filenamesInDirectory = Arrays.stream(files)
				.map(file -> file.getName().replace(DEFAULT_FILE_EXTENSION, ""))
				.collect(Collectors.toCollection(ArrayList::new));

		logger.log(Level.INFO, "found following files in 'programs' directory:");
		filenamesInDirectory.forEach(f -> logger.log(Level.INFO, f));

		nameField.textProperty()
				.addListener((observable, oldVal, newVal) -> dialog.getDialogPane().lookupButton(ButtonType.OK)
						.setDisable(!nameField.getText().matches(VALID_IDENTIFIER_REGEX)
								|| SourceVersion.isKeyword(nameField.getText()) // https://stackoverflow.com/a/54141029/13670629
								|| filenamesInDirectory.contains(nameField.getText())));
		dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(Messages.getString("New.dialog.name")), nameField);

		dialogPane.setContent(grid);
		Platform.runLater(nameField::requestFocus);
		dialog.setResultConverter(button -> (button == ButtonType.OK) ? nameField.getText() : null);

		Optional<String> result = dialog.showAndWait();
		result.ifPresent(ProgramController::createAndShow);
	}

	public static void createAndShow(String programName) {
		String content = createTemplate(programName, DEFAULT_CONTENT);
		File f = new File(PATH_TO_PROGRAMS + File.separatorChar + programName + DEFAULT_FILE_EXTENSION);
		if (!f.exists()) {
			try {
				if (!f.createNewFile()) {
					// to something on fail
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
				writer.write(content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Program program = new Program(f, programName);
		Stage stage = new MainStage(program);
		programs.put(programName, stage);
		ProgramController.compile(program, false);

	}

	public static String createPrefix(String name) {
		return String.format(PREFIX_TEMPLATE, name);
	}

	public static String createPostfix() {
		return POSTFIX_TEMPLATE;
	}

	public static String createTemplate(String name, String content) {
		return createPrefix(name) + content + createPostfix();

	}

	public static void openProgram() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("open Program");
		fileChooser.setInitialDirectory(new File(PATH_TO_PROGRAMS));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(Messages.getString("Open.dialog.filter"),
				"*" + DEFAULT_FILE_EXTENSION));

		File f = fileChooser.showOpenDialog(null);
		if (f == null)
			return;
		String name = f.getName().replaceFirst(DEFAULT_FILE_EXTENSION, "");
		if (!programs.containsKey(name)) {
			logger.log(Level.INFO, "Opening '" + name + "' since it is not loaded yet.");
			Program program = new Program(f, name);
			Stage stage = new MainStage(program);
			programs.put(name, stage);
		} else {
			logger.log(Level.INFO, "File is already loaded! Requesting focus...");
			programs.get(name).requestFocus();
		}
	}

	public static void compile(Program program) {
		compile(program, true);
	}

	public static void compile(Program program, boolean showAlerts) {
		// TODO make sure Default annotations have correct value in String
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		StandardJavaFileManager manager = javac.getStandardFileManager(diagnostics, null, null);
		Iterable<? extends JavaFileObject> units = manager
				.getJavaFileObjectsFromFiles(Arrays.asList(program.getFile()));
		CompilationTask task = javac.getTask(null, manager, diagnostics, null, null, units);
		boolean success = task.call();
		try {
			manager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!success) {
			boolean showedAlert = false; // flag to indicate that only one alert is shown
			diagnostics.toString();
			logger.log(Level.WARNING, "Compilation failed");
			for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
				diagnostic.toString();

				logger.log(Level.WARNING, "Kind: %s%n", diagnostic.getKind());
				logger.log(Level.WARNING, "Quelle: %s%n", diagnostic.getSource());
				logger.log(Level.WARNING, String.format("Code und Nachricht: %s: %s%n", diagnostic.getCode(),
						diagnostic.getMessage(null)));
				logger.log(Level.WARNING, "Zeile: %s%n", diagnostic.getLineNumber());
				logger.log(Level.WARNING, String.format("Position/Spalte: %s/%s%n", diagnostic.getPosition(),
						diagnostic.getColumnNumber()));
				logger.log(Level.WARNING, String.format("Startpostion/Endposition: %s/%s%n",
						diagnostic.getStartPosition(), diagnostic.getEndPosition()));
				if (showAlerts && !showedAlert) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Compilation Error");
					alert.setHeaderText(diagnostic.getKind().toString());
					StringBuilder bobTheBuilder = new StringBuilder();
					bobTheBuilder.append(
							String.format(Messages.getString("Compilation.diagnostic.kind"), diagnostic.getKind()));
					// bobTheBuilder.append(String.format("Quelle: %s%n", diagnostic.getSource()));
					bobTheBuilder.append(String.format(Messages.getString("Compilation.diagnostic.CodeAndMessage"),
							diagnostic.getCode(), diagnostic.getMessage(null)));
					bobTheBuilder.append(String.format(Messages.getString("Compilation.diagnostic.row"),
							diagnostic.getLineNumber()));
					// bobTheBuilder.append(
					// String.format("Position/Spalte: %s/%s%n", diagnostic.getPosition(),
					// diagnostic.getColumnNumber()));
					// bobTheBuilder.append(String.format("Startpostion/Endposition: %s/%s%n",
					// diagnostic.getStartPosition(),
					// diagnostic.getEndPosition()));
					alert.setContentText(bobTheBuilder.toString());
					alert.showAndWait();
					showedAlert = true;
				}
			}
		} else {// compilation successful
			logger.log(Level.INFO, "Compilation successful");
			// set new Robbi in territory
			MainStage s = (MainStage) programs.get(program.getName());
			s.getTerritory().setRobbi(loadNewRobbi(program.getName()));
			if (showAlerts) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(Messages.getString("Compilation.success.title"));
				alert.setHeaderText(Messages.getString("Compilation.success.header"));
				alert.setContentText(
						String.format(Messages.getString("Compilation.success.message"), program.getName()));
				alert.showAndWait();
			}
		}
	}

	private static Robbi loadNewRobbi(String name) {
		try (URLClassLoader classLoader = new URLClassLoader(
				new URL[] { new File(PATH_TO_PROGRAMS).toURI().toURL() })) {
			Constructor<?> c = classLoader.loadClass(name).getConstructor();
			Robbi r = (Robbi) c.newInstance();
			return r;
		} catch (MalformedURLException | InstantiationException | IllegalAccessException | InvocationTargetException
				| ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void close(String name) {
		programs.remove(name);
		logger.log(Level.INFO, "Closing program '" + name + "'");
	}

	private ProgramController() {
	}

}
