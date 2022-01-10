package com.JayPi4c.RobbiSimulator.controller.program;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Program {

	private String name;

	private String editorContent;
	private File file;

	private boolean edited = false;

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
			editorContent = editorContent.substring(0, editorContent.length() - 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public String getName() {
		return this.name;
	}

	public String getEditorContent() {
		return this.editorContent;
	}

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

	public void setEdited(boolean flag) {
		edited = flag;
	}

	public boolean isEdited() {
		return edited;
	}

}
