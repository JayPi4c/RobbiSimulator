package com.JayPi4c.RobbiSimulator.controller.program;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class contains the contents, the name of program and whether it is
 * edited or not
 * 
 * @author Jonas Pohl
 *
 */
public class Program {

	private String name;

	private String editorContent;
	private File file;

	private boolean edited = false;

	/**
	 * Creates and loads a program from a given file and the program name
	 * 
	 * @param f    the file on the fileSystem storing the program
	 * @param name the name of the program
	 */
	public Program(File f, String name) {
		this.file = f;
		this.name = name;
		try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
			StringBuilder bobTheBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				bobTheBuilder.append(line);
				bobTheBuilder.append(System.lineSeparator());
			}

			String content = bobTheBuilder.toString();

			editorContent = content.replace(ProgramController.createPrefix(name), "");
			int endIndex = editorContent.lastIndexOf('}');
			bobTheBuilder.setLength(0);
			editorContent = bobTheBuilder.append(editorContent).deleteCharAt(endIndex).toString().trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return the file corresponding to this program
	 */
	public File getFile() {
		return file;
	}

	/**
	 * 
	 * @return the name of the program
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @return the real content of the editor
	 */
	public String getEditorContent() {
		return this.editorContent;
	}

	/**
	 * Sets the new editor content.
	 * 
	 * @param content the content to replace the editor with.
	 */
	public void setEditorContent(String content) {
		this.editorContent = content;
	}

	/**
	 * saves the text of the editor-content into the corresponding file It does only
	 * save the given text, if the changes were made
	 * 
	 * @param text the text to save in the file
	 */
	public void save(String text) {
		if (!edited)
			return;

		editorContent = text;
		String content = ProgramController.createTemplate(name, text);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		edited = false;
	}

	/**
	 * Sets the edited flag
	 * 
	 * @param flag the value of the edited flag
	 */
	public void setEdited(boolean flag) {
		edited = flag;
	}

	/**
	 * returns whether the editor-content has changed
	 * 
	 * @return true if content is edited, false otherwise
	 */
	public boolean isEdited() {
		return edited;
	}

}
