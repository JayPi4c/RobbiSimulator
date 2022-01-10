package com.JayPi4c.RobbiSimulator.view;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.JayPi4c.RobbiSimulator.controller.MethodHandler;
import com.JayPi4c.RobbiSimulator.model.Robbi;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.utils.annotations.Default;
import com.JayPi4c.RobbiSimulator.utils.annotations.Invisible;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;

public class RobbiContextMenu extends ContextMenu {

	public RobbiContextMenu(Territory territory) {

		for (Method method : getDefaultMethods(territory.getRobbi())) {
			MenuItem item = getMenuItem(method);

			// if method needs arguments and no Parameterized Annotation is set -> disable
			if (method.getParameterCount() != 0)
				item.setDisable(true);
			else
				// get Annotation parameters
				item.setOnAction(new MethodHandler(method, territory));

			getItems().add(item);
		}

		Method methods[] = getCustomMethods(territory.getRobbi());
		if (methods.length > 0) {
			getItems().add(new SeparatorMenuItem());

			for (Method method : methods) {
				CustomMenuItem item = getMenuItem(method);
				// if method needs arguments and no parameterized Annotation is set -> disable
				if (method.getParameterCount() != 0 && !hasCorrectDefaultAnnotations(method)) {
					item.setDisable(true);
					// https://stackoverflow.com/a/43053529/13670629
					Tooltip tooltip = new Tooltip(Messages.getString("Editor.contextMenu.tooltip"));
					Tooltip.install(item.getContent(), tooltip);

				} else
					item.setOnAction(new MethodHandler(method, territory));

				getItems().add(item);
			}
		}

	}

	private boolean hasCorrectDefaultAnnotations(Method m) {
		if (m.getParameterCount() != m.getAnnotatedParameterTypes().length)
			return false;

		for (Parameter parameter : m.getParameters()) {
			boolean valid = false;
			for (Annotation anno : parameter.getAnnotations()) {
				if (anno instanceof Default) {
					valid = true;
					break;
				}
			}
			if (!valid)
				return false;
		}
		return true;
	}

	private CustomMenuItem getMenuItem(Method m) {
		StringBuilder bobTheBuilder = new StringBuilder();
		bobTheBuilder.append(m.getReturnType().toString());
		bobTheBuilder.append(" ");
		bobTheBuilder.append(m.getName());
		bobTheBuilder.append("(");
		for (Parameter parameter : m.getParameters()) {
			bobTheBuilder.append(parameter.getType());
			List<Annotation> annos = Arrays.asList(parameter.getAnnotations());
			for (Annotation anno : annos)
				if (anno instanceof Default) {
					bobTheBuilder.append(" = ");
					bobTheBuilder.append(((Default) anno).value());
				}
			// bobTheBuilder.append(" ");
			// bobTheBuilder.append(parameter.getName());
			bobTheBuilder.append(", ");
		}
		if (m.getParameterCount() > 0)
			bobTheBuilder.delete(bobTheBuilder.length() - 2, bobTheBuilder.length());
		bobTheBuilder.append(")");
		CustomMenuItem item = new CustomMenuItem(new Label(bobTheBuilder.toString()));
		return item;
	}

	private Method[] getDefaultMethods(Robbi robbi) {
		List<Method> methods = new ArrayList<>();

		for (Method m : Robbi.class.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers()) && !isMainMethod(m))
				methods.add(m);
		}
		return methods.toArray(new Method[0]);
	}

	private Method[] getCustomMethods(Robbi robbi) {
		List<Method> methods = new ArrayList<>();
		// if robbi is custom class
		if (Robbi.class != robbi.getClass()) {
			for (Method m : robbi.getClass().getDeclaredMethods()) {

				int modifiers = m.getModifiers();

				if (!Modifier.isPrivate(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isAbstract(modifiers)
						&& !isInvisible(m))
					methods.add(m);
			}
		}

		return methods.toArray(new Method[0]);
	}

	private boolean isInvisible(Method m) {
		for (Annotation anno : m.getAnnotations())
			if (anno instanceof Invisible)
				return true;
		return false;
	}

	private boolean isMainMethod(Method m) {
		return m.getName().equals("main");
	}

}
