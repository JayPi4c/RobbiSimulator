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
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.annotations.Default;
import com.JayPi4c.RobbiSimulator.utils.annotations.Invisible;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;

/**
 * Class to for the ContextMenu which opens, when clicked on robbi in the
 * simulator.
 * 
 * @author Jonas Pohl
 *
 */
public class RobbiContextMenu extends ContextMenu {

	private static final String EDITOR_CONTEXTMENU_TOOLTIP = "Editor.contextMenu.tooltip";

	/**
	 * Constructor to create a new RobbiContextMenu. It fills itself with all
	 * methods that are provided by robbi and allows to run a particular method on
	 * its own.
	 * 
	 * @param territory the territory this contextMenu is for
	 * @param parent    the parent window in order to display the alerts relative to
	 *                  the calling window
	 */
	public RobbiContextMenu(Territory territory, MainStage parent) {

		for (Method method : getDefaultMethods()) {
			MenuItem item = getMenuItem(method);

			// if method needs arguments and no Parameterized Annotation is set -> disable
			if (method.getParameterCount() != 0)
				item.setDisable(true);
			else
				// get Annotation parameters
				item.setOnAction(new MethodHandler(method, territory, parent));

			getItems().add(item);
		}

		Method[] methods = getCustomMethods(territory.getRobbi());
		if (methods.length > 0) {
			getItems().add(new SeparatorMenuItem());

			for (Method method : methods) {
				CustomMenuItem item = getMenuItem(method);
				// if method needs arguments and no parameterized Annotation is set -> disable
				if (method.getParameterCount() != 0 && !hasCorrectDefaultAnnotations(method)) {
					item.setDisable(true);
					// https://stackoverflow.com/a/43053529/13670629
					Tooltip tooltip = I18nUtils.createTooltip(EDITOR_CONTEXTMENU_TOOLTIP);
					Tooltip.install(item.getContent(), tooltip);

				} else
					item.setOnAction(new MethodHandler(method, territory, parent));

				getItems().add(item);
			}
		}

	}

	/**
	 * This method checks if the given method m has the @Default annotation for all
	 * parameters set.
	 * 
	 * @param m the method to check for
	 * @return true if all parameters have the default Annotation, false otherwise
	 */
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

	/**
	 * Creates the MenuItem for the given Method. <br>
	 * MenuItems will look like this: <br>
	 * foo(int, int) <br>
	 * bar(long = 7, int = 3)
	 * 
	 * @param m the method to make a menuItem from
	 * @return a CustomMenuItem with the correct text
	 */
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
				if (anno instanceof Default a) {
					bobTheBuilder.append(" = ");
					bobTheBuilder.append(a.value());
				}
			bobTheBuilder.append(", ");
		}
		if (m.getParameterCount() > 0)
			bobTheBuilder.delete(bobTheBuilder.length() - 2, bobTheBuilder.length());
		bobTheBuilder.append(")");
		return new CustomMenuItem(new Label(bobTheBuilder.toString()));
	}

	/**
	 * Get all methods that are part of the default implementation, that are
	 * visibile to the user.
	 * 
	 * @return all methods that are part of the default implementation
	 */
	private Method[] getDefaultMethods() {
		List<Method> methods = new ArrayList<>();

		for (Method m : Robbi.class.getDeclaredMethods()) {
			if (Modifier.isPublic(m.getModifiers()) && !isMainMethod(m))
				methods.add(m);
		}
		return methods.toArray(Method[]::new);
	}

	/**
	 * Get all methods that are implemented by the user and are not part of the
	 * default implementation of robbi.
	 * 
	 * @param robbi the Robbi instance to get the custom methods from
	 * @return all methods that are not part of the default implementation
	 */
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

		return methods.toArray(Method[]::new);
	}

	/**
	 * Checks if a method has the invisible annotation.
	 * 
	 * @param m the method to check for
	 * @return true if the Invisible annotation is set, false otherwise
	 */
	private boolean isInvisible(Method m) {
		for (Annotation anno : m.getAnnotations())
			if (anno instanceof Invisible)
				return true;
		return false;
	}

	/**
	 * Checks if the method is the main-Method.
	 * 
	 * @param m method to check
	 * @return true if the name of the method equals "main", false otherwise
	 */
	private boolean isMainMethod(Method m) {
		return m.getName().equals("main");
	}

}
