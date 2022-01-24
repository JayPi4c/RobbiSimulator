package com.JayPi4c.RobbiSimulator.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.JayPi4c.RobbiSimulator.model.RobbiException;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.annotations.Default;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

/**
 * MethodHandler to handle a methods invocation
 * 
 * @author Jonas Pohl
 *
 */
public class MethodHandler implements EventHandler<ActionEvent> {

	private Method method;
	private Territory territory;
	private Window parent;

	/**
	 * Creates a new MethodHandler with the method and the territory the message is
	 * for. With the handle method, the handler will be invoked.
	 * 
	 * @param method    the method for this handler
	 * @param territory the territory to invoke this method in
	 * @param parent    the parent window, in order to place alerts relative to the
	 *                  window
	 */
	public MethodHandler(Method method, Territory territory, Window parent) {
		this.method = method;
		this.territory = territory;
		this.parent = parent;
	}

	@Override
	public void handle(ActionEvent event) {
		this.territory.deactivateNotification();
		this.method.setAccessible(true); // allow calling private/pacakge-private and protected
		try {
			List<Object> args = new ArrayList<>();
			for (Parameter p : method.getParameters()) {
				for (Annotation anno : p.getAnnotations()) {
					if (anno instanceof Default) {
						String val = ((Default) anno).value();
						try {
							switch (p.getType().getName()) {
							case "int":
								args.add(Integer.parseInt(val));
								break;
							case "boolean":
								args.add(Boolean.parseBoolean(val));
								break;
							case "char":
								args.add(val.subSequence(0, 1));
								break;
							case "double":
								args.add(Double.parseDouble(val));
								break;
							case "float":
								args.add(Float.parseFloat(val));
								break;
							case "long":
								args.add(Long.parseLong(val));
								break;
							case "String":
							default:
								args.add(val);

							}
						} catch (IllegalArgumentException e) {
							AlertHelper.showAlertAndWait(AlertType.ERROR, String
									.format(I18nUtils.i18n("Editor.contextMenu.executionError"), val, method.getName()),
									parent);
							return;
						}
						break;
					}
				}
			}
			Object arr[] = args.toArray(new Object[0]);
			Object result = this.method.invoke(this.territory.getRobbi(), arr);

			if (result != null) {
				AlertHelper.showAlertAndWait(AlertType.INFORMATION,
						I18nUtils.i18n("Execution.information.result") + result.toString(), parent);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if (e.getCause().getClass() != ThreadDeath.class) {
				if (e.getCause() instanceof RobbiException) {
					AlertHelper.showAlertAndWait(AlertType.WARNING, e.getCause().getMessage(), parent);
				} else {
					AlertHelper.showAlertAndWait(AlertType.ERROR, e.getCause().getMessage(), parent);
				}
			}
		} finally {
			this.territory.activateNotification();
		}
	}

}
