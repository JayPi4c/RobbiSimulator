package com.JayPi4c.RobbiSimulator.controller.program;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.JayPi4c.RobbiSimulator.model.Robbi;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.annotations.Default;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * This controller contains all functionality to initialize the application,
 * load, save and compile a program. Furthermore, it creates and opens a new
 * stage, if a new Program is loaded, or it loads the DefaultProgram on the
 * Application start
 * 
 * @author Jonas Pohl
 *
 */
public class ProgramController {

	private static final Logger logger = LoggerFactory.getLogger(ProgramController.class);
	/**
	 * Constant String with the Path name for the programs directory.
	 */
	public static final String PATH_TO_PROGRAMS = "programs";
	/**
	 * Constant String for the Default Robbi File name.
	 */
	public static final String DEFAULT_ROBBI_FILE_NAME = "DefaultRobbi";
	/**
	 * Constant String for the default file extension name.
	 */
	public static final String DEFAULT_FILE_EXTENSION = ".java";
	/**
	 * Constant String for the default content of the editor when the file is newly
	 * created.
	 */
	public static final String DEFAULT_CONTENT = "void main(){" + System.lineSeparator() + "\t// place your code here"
			+ System.lineSeparator() + "}";
	/**
	 * Constant String for the editor prefix which is needed to compile the class
	 * and load into the simulator. This String will not be shown in the editor
	 * content.
	 */
	public static final String PREFIX_TEMPLATE = "import com.JayPi4c.RobbiSimulator.utils.annotations.*;public class %s extends com.JayPi4c.RobbiSimulator.model.Robbi{";
	/**
	 * Constant String for the editor postfix, to close the class and make it
	 * compilable. This postfix will not be shown in the editor content.
	 */
	public static final String POSTFIX_TEMPLATE = System.lineSeparator() + "}";

	private static HashMap<String, Stage> programs;

	private ProgramController() {
	}

	/**
	 * Initializes the Application on startup and makes sure the programs folder and
	 * the DefaultRobbi class exists
	 * 
	 * @return true if initialization finished successfully, false otherwise
	 */
	public static boolean initialize() {
		File dir = new File(PATH_TO_PROGRAMS);
		if (!dir.exists() && !dir.mkdir()) {
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

	/**
	 * Looks into the programs directory and collects all java filenames.
	 * 
	 * @return Collection of java filenames in programs directory
	 */
	private static Collection<String> getFilenamesInDirectory() {
		// get a list of all files in programs directory that end with .java
		File[] files = new File(PATH_TO_PROGRAMS)
				.listFiles(file -> (file.isFile() && file.getName().endsWith(DEFAULT_FILE_EXTENSION)));
		// get a String collection with all filenames in the programs directory
		Collection<String> filenamesInDirectory = Arrays.stream(files)
				.map(file -> file.getName().replace(DEFAULT_FILE_EXTENSION, ""))
				.collect(Collectors.toCollection(ArrayList::new));

		logger.debug("Found following files in 'programs' directory:");
		filenamesInDirectory.forEach(logger::debug);
		return filenamesInDirectory;
	}

	/**
	 * Creates a Dialog and asks the user to for the name of the new program.
	 * Afterwards, it initiates the creation of a new stage for the program.
	 * 
	 * @param parent the parent window to show alerts relative to the parent window
	 * @see ProgramController#createAndShow(String)
	 */
	public static void createAndShow(Window parent) {
		Optional<String> result = getNameForProgram(parent);
		result.ifPresent(ProgramController::createAndShow);
	}

	/**
	 * Asks the user to enter a name for the program. It only allows names, that are
	 * no java identifiers and are not already used in the programs folder
	 * 
	 * @param parent window to show the dialog relative to
	 * @return the new name for the program
	 */
	private static Optional<String> getNameForProgram(Window parent) {
		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle(I18nUtils.i18n("New.dialog.title"));
		dialog.setHeaderText(I18nUtils.i18n("New.dialog.header"));
		dialog.initOwner(parent);
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		TextField nameField = new TextField();
		nameField.setPromptText(I18nUtils.i18n("New.dialog.prompt"));

		Collection<String> filenamesInDirectory = getFilenamesInDirectory();

		nameField.textProperty()
				.addListener((observable, oldVal, newVal) -> dialog.getDialogPane().lookupButton(ButtonType.OK)
						.setDisable(SourceVersion.isKeyword(nameField.getText()) // https://stackoverflow.com/a/54141029/13670629
								|| !SourceVersion.isIdentifier(nameField.getText())
								// || !nameField.getText().matches(VALID_IDENTIFIER_REGEX)
								|| filenamesInDirectory.contains(nameField.getText())));

		dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(I18nUtils.i18n("New.dialog.name")), nameField);

		dialogPane.setContent(grid);
		Platform.runLater(nameField::requestFocus);
		dialog.setResultConverter(button -> (button == ButtonType.OK) ? nameField.getText() : null);

		return dialog.showAndWait();
	}

	/**
	 * Creates a new file with default content, if it does not exist. Afterwards, it
	 * creates a program and a new Stage. Finally, it compiles the code and loads
	 * the Robbi instance into the simulation
	 * 
	 * @param programName the name of the program to create
	 */
	public static void createAndShow(String programName) {
		String content = createTemplate(programName, DEFAULT_CONTENT);
		File f = new File(PATH_TO_PROGRAMS + File.separatorChar + programName + DEFAULT_FILE_EXTENSION);
		if (!f.exists()) {
			try {
				if (!f.createNewFile())
					logger.debug("Could not create file '{}'", f.getAbsolutePath());
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
		ProgramController.compile(program, false, stage);
	}

	/**
	 * Creates a stage with the given programName, the programCode provided and the
	 * territory encoded as XML. If the user has already a stage with the same name,
	 * he will be warned, that his program will be overwritten. Optionally, he can
	 * save his program and territory under another name. After that a new Stage
	 * will be created according to the given information. <br>
	 * This method might only be used to load an example from the database.
	 * 
	 * @param programName  name of the program
	 * @param programCode  code of the program
	 * @param territoryXML XML-encoded territory
	 */
	public static void createAndShow(String programName, String programCode, String territoryXML) {

		String content = createTemplate(programName, DEFAULT_CONTENT);
		File f = new File(PATH_TO_PROGRAMS + File.separatorChar + programName + DEFAULT_FILE_EXTENSION);
		if (f.exists()) {
			logger.debug("There is a file with the name of the example");
			if (programs.containsKey(programName)) {
				logger.debug("Program is opened: closing...");
				MainStage stage = (MainStage) programs.get(programName);
				Program p = stage.getProgram();
				p.save(p.getEditorContent());
				stage.close();
			}

			Alert alert = AlertHelper.createAlert(AlertType.INFORMATION, I18nUtils.i18n("Examples.duplication.message"),
					null);
			alert.setHeaderText(I18nUtils.i18n("Examples.duplication.header"));
			alert.setTitle(I18nUtils.i18n("Examples.duplication.title"));
			alert.getButtonTypes().remove(ButtonType.OK);
			alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent()) {
				ButtonType button = result.get();
				if (button.equals(ButtonType.YES)) {
					Optional<String> newName = getNameForProgram(null);
					if (newName.isPresent()) {
						String name = newName.get();

						StringBuilder bobTheBuilder = new StringBuilder();
						try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
							String line = null;
							while ((line = reader.readLine()) != null) {
								bobTheBuilder.append(line);
								bobTheBuilder.append(System.lineSeparator());
							}
							bobTheBuilder.replace(0, createPrefix(programName).length(), createPrefix(name));

						} catch (IOException e) {
							logger.debug("Failed to read class contents");
						}
						try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
							writer.write(bobTheBuilder.toString());
						} catch (IOException e) {
							logger.debug("Failed to write new classname in file");
						}
						if (!renameFile(f, name)) {
							logger.debug("Failed to rename file");
						}
					}
				}
			}
		}

		if (!f.exists()) {
			try {
				if (!f.createNewFile())
					logger.debug("Could not create file '{}'", f.getAbsolutePath());
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
		program.setEdited(true);
		program.save(programCode);
		MainStage stage = new MainStage(program);
		stage.getTerritory().fromXML(new ByteArrayInputStream(territoryXML.getBytes()));
		programs.put(programName, stage);
		ProgramController.compile(program, false, stage);
	}

	/**
	 * Renames a java file in the programs directory to the new name.
	 * 
	 * @param f       the file to rename
	 * @param newName the new name of the file
	 * @return true if the renaming was successful, false otherwise
	 */
	public static boolean renameFile(File f, String newName) {
		File newFile = new File(PATH_TO_PROGRAMS + File.separatorChar + newName + DEFAULT_FILE_EXTENSION);

		if (newFile.exists())
			return false;
		return f.renameTo(newFile);
	}

	/**
	 * Creates the prefix for the program-file
	 * 
	 * @param name the name of the class
	 * @return the prefix for the class
	 */
	public static String createPrefix(String name) {
		return String.format(PREFIX_TEMPLATE, name);
	}

	/**
	 * Creates the postfix for the program-file
	 * 
	 * @return the postfix for the class
	 */
	public static String createPostfix() {
		return POSTFIX_TEMPLATE;
	}

	/**
	 * Creates the template for a compilable class
	 * 
	 * @param name    the name of the class
	 * @param content the content of the class
	 * @return a String containing a compilable java code with the given content
	 */
	public static String createTemplate(String name, String content) {
		return createPrefix(name) + content + createPostfix();

	}

	/**
	 * Opens a FileChooser and loads the selected file. If the file is already
	 * loaded, the ProgramController requests the focus for the loaded program.
	 * Otherwise it creates a new program and a new stage.
	 * 
	 * @param parent the window to open the chooser relative to the calling window
	 */
	public static void openProgram(Window parent) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(I18nUtils.i18n("Open.dialog.title"));
		fileChooser.setInitialDirectory(new File(PATH_TO_PROGRAMS));
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter(I18nUtils.i18n("Open.dialog.filter"), "*" + DEFAULT_FILE_EXTENSION));

		File file = fileChooser.showOpenDialog(parent);
		if (file != null) {
			String name = file.getName().replaceFirst(DEFAULT_FILE_EXTENSION, "");
			if (!programs.containsKey(name)) {
				logger.debug("Opening '{}' since it is not loaded yet.", name);
				Program program = new Program(file, name);
				Stage stage = new MainStage(program);
				programs.put(name, stage);
				compile(program, false, parent);
			} else {
				logger.debug("File is already loaded! Requesting focus...");
				programs.get(name).requestFocus();
			}
		}

	}

	/**
	 * Compiles the given Program and shows an error Alert if the Compilation failed
	 * otherwise it shows an success alert. If the compilation finished
	 * successfully, the new Robbi will be loaded into the territory.
	 * 
	 * @param program the program to compile
	 * @param parent  the calling window to show alerts relative to the calling
	 *                window
	 */
	public static void compile(Program program, Window parent) {
		compile(program, true, parent);
	}

	/**
	 * Compiles the given Program and shows an error Alert if the Compilation failed
	 * otherwise it shows an success alert. If the compilation finished
	 * successfully, the new Robbi will be loaded into the territory. Before it does
	 * this, it checks if the Annotations are set correctly and if the main-Method
	 * is overwritten. An error with this post-compile check will be visualized with
	 * an Alert.
	 * 
	 * @param program    the program to compile
	 * @param showAlerts flag to determine if the alerts need to be shown or not
	 * @param parent     the window calling the method in order to show the alerts
	 *                   relative to it
	 */
	public static void compile(Program program, boolean showAlerts, Window parent) {
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

		try (StandardJavaFileManager manager = javac.getStandardFileManager(diagnostics, null, null)) {
			Iterable<? extends JavaFileObject> units = manager
					.getJavaFileObjectsFromFiles(Arrays.asList(program.getFile()));
			CompilationTask task = javac.getTask(null, manager, diagnostics, null, null, units);

			if (Boolean.FALSE.equals(task.call())) {
				boolean showedAlert = false; // flag to indicate that only one alert is shown
				diagnostics.toString();
				logger.error("Compilation failed");
				for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
					diagnostic.toString();

					logger.error("Kind: {}", diagnostic.getKind());
					logger.error("Quelle: {}", diagnostic.getSource());
					logger.error("Code und Nachricht: {}: {}", diagnostic.getCode(), diagnostic.getMessage(null));
					logger.error("Zeile: {}", diagnostic.getLineNumber());
					logger.error("Position/Spalte: {}/{}", diagnostic.getPosition(), diagnostic.getColumnNumber());
					logger.error("Startpostion/Endposition: {}/{}", diagnostic.getStartPosition(),
							diagnostic.getEndPosition());
					if (showAlerts && !showedAlert) {
						StringBuilder bobTheBuilder = new StringBuilder();
						bobTheBuilder.append(
								String.format(I18nUtils.i18n("Compilation.diagnostic.kind"), diagnostic.getKind()));
						bobTheBuilder.append(String.format(I18nUtils.i18n("Compilation.diagnostic.CodeAndMessage"),
								diagnostic.getCode(), diagnostic.getMessage(null)));
						bobTheBuilder.append(String.format(I18nUtils.i18n("Compilation.diagnostic.row"),
								diagnostic.getLineNumber()));
						AlertHelper.showAlertAndWait(AlertType.ERROR, bobTheBuilder.toString(), parent,
								Modality.WINDOW_MODAL, I18nUtils.i18n("Compilation.diagnostic.title"),
								diagnostic.getKind().toString());
						showedAlert = true;
					}
				}
			} else {// compilation successful
				logger.info("Compilation successful");
				Optional<Robbi> robbi = loadNewRobbi(program.getName());

				robbi.ifPresentOrElse(r -> {
					Diagnostics diag = new Diagnostics();
					if (!hasValidAnnotations(r, diag)) {
						List<Diagnostics.Diagnostic> diags = diag.getDiagnosis();
						String val = null;
						String type = null;
						if (!diags.isEmpty()) {
							Diagnostics.Diagnostic diagnostic = diags.get(0);
							val = diagnostic.value();
							type = diagnostic.type();
							logger.error("[Annotation Error]: {} is not applicable for {}", val, type);
						} else
							logger.error("[Annotation Error]: Error has been found but could not be diagnosed.");
						if (showAlerts) {
							String msg = I18nUtils.i18n("Compilation.annotations.msg.default");
							if (val != null && type != null) {
								msg = String.format(I18nUtils.i18n("Compilation.annotations.msg.info"), val, type);
							}
							AlertHelper.showAlertAndWait(AlertType.WARNING, msg, parent, Modality.WINDOW_MODAL,
									I18nUtils.i18n("Compilation.annotations.title"),
									I18nUtils.i18n("Compilation.annotations.header"));
						}
					} else {
						if (overwritesMainMethod(r)) {
							// set new Robbi in territory
							MainStage s = (MainStage) programs.get(program.getName());
							s.getTerritory().setRobbi(r);
							if (showAlerts) {
								AlertHelper.showAlertAndWait(AlertType.INFORMATION,
										String.format(I18nUtils.i18n("Compilation.success.message"), program.getName()),
										parent, Modality.WINDOW_MODAL, I18nUtils.i18n("Compilation.success.title"),
										I18nUtils.i18n("Compilation.success.header"));
							}
						} else {
							AlertHelper.showAlertAndWait(AlertType.ERROR,
									I18nUtils.i18n("Compilation.diagnostic.override"), parent);
							logger.error("The custom Robbi class does not overwrite the main-Method");
						}
					}
				}, () -> logger.error("Failed to load new Robbi instance..."));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Checks if the robbi overwrites the Main-Method.
	 * 
	 * @param robbi the robbi to check if it overwrites the Main-Method
	 * @return true if robbi overwrites it, false otherwise
	 */
	private static boolean overwritesMainMethod(Robbi robbi) {
		if (Robbi.class == robbi.getClass()) {
			return true; // Default Robbi always has main-Method overwritten
		}
		for (Method m : robbi.getClass().getDeclaredMethods()) {
			if (m.getName().equals("main")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the given robbi has valid annotations set. Currently it checks if
	 * the default Annotation has values matching its parameter
	 * 
	 * @param robbi Instance of the users robbi implementation to check
	 * @return true if the annotions are valid, false otherwise
	 */
	private static boolean hasValidAnnotations(Robbi robbi, Diagnostics diag) {
		Method[] methods = getCustomMethods(robbi);
		boolean result = true;
		for (Method method : methods) {
			if (!hasValidDefaultAnnotation(method, diag)) {
				result = false;

				// use return to fasten things up. Following annotation errors are ignored
			}
		}
		return result;
	}

	/**
	 * Checks if the method has Default annotations matching the type of its
	 * parameter
	 * 
	 * @param method the method to check the annotations for
	 * @return true if and only if all Default annotations match their parameter
	 *         type, false otherwise
	 */
	private static boolean hasValidDefaultAnnotation(Method method, Diagnostics diag) {
		boolean result = true;
		for (Parameter parameter : method.getParameters()) {
			Annotation[] annotations = parameter.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof Default anno && !valueAcceptable(parameter, anno, diag)) {
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * Checks if the parameter and the Default Annotation match types. This is done
	 * by checking, if the value of the Default annotation can be parsed to the type
	 * of the parameter. If it fails, this method returns false.
	 * 
	 * @param parameter  The parameter to check the type of
	 * @param annotation The annotation whose value has to be checked against the
	 *                   parameter type
	 * @return true if the annotation value matches the parameter type, false
	 *         otherwise
	 */
	private static boolean valueAcceptable(Parameter parameter, Default annotation, Diagnostics diag) {
		String val = annotation.value();
		String type = parameter.getType().getName();
		try {
			switch (type) {
			case "int":
				Integer.parseInt(val);
				return true;
			case "boolean":
				return val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false");
			case "char":
				return !val.isBlank();
			case "double":
				Double.parseDouble(val);
				return true;
			case "float":
				Float.parseFloat(val);
				return true;
			case "long":
				Long.parseLong(val);
				return true;
			case "String":
			default:
				return true;
			}
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			diag.add(new Diagnostics.Diagnostic(type, val));
			return false;
		}

	}

	/**
	 * Returns all methods, that are not part of the default robbi implementation,
	 * i.e. returns all methods the user has implemented
	 * 
	 * @param robbi the new robbi instance
	 * @return an ArrayList of all new methods of the robbi
	 */
	private static Method[] getCustomMethods(Robbi robbi) {
		List<Method> methods = new ArrayList<>();
		// if robbi is custom class
		if (Robbi.class != robbi.getClass()) {
			methods.addAll(Arrays.asList(robbi.getClass().getDeclaredMethods()));
		}
		return methods.toArray(new Method[0]);
	}

	/**
	 * Loads a new Robbi by the name of the class
	 * 
	 * @param name the name of the class
	 * @return an Optional of the given robbi class
	 */
	private static Optional<Robbi> loadNewRobbi(String name) {
		Optional<Robbi> robbi;
		try (URLClassLoader classLoader = new URLClassLoader(
				new URL[] { new File(PATH_TO_PROGRAMS).toURI().toURL() })) {
			Constructor<?> c = classLoader.loadClass(name).getConstructor();
			Robbi r = (Robbi) c.newInstance();
			robbi = Optional.ofNullable(r);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException
				| NoSuchMethodException | SecurityException | IOException e) {
			logger.error("Could not load class of '{}'. Message: {}", name, e.getMessage());
			robbi = Optional.ofNullable(null);
		}
		return robbi;
	}

	/**
	 * Removes the program from the programsList in order to make sure it can be
	 * reopened later
	 * 
	 * @param name the name of the program which is closed
	 */
	public static void close(String name) {
		programs.remove(name);
		logger.debug("Closing program '{}'", name);
	}

}
