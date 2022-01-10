package com.JayPi4c.RobbiSimulator.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.JayPi4c.RobbiSimulator.model.RobbiException;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.utils.annotations.Default;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MethodHandler implements EventHandler<ActionEvent> {

	private Method method;
	private Territory territory;

	public MethodHandler(Method method, Territory territory) {
		this.method = method;
		this.territory = territory;
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
							Alert alert = new Alert(AlertType.ERROR);
							alert.setContentText(String.format(Messages.getString("Editor.contextMenu.executionError"),
									val, method.getName()));
							alert.showAndWait();
							return;
						}
						break;
					}
				}
			}
			Object arr[] = args.toArray(new Object[0]);
			Object result = this.method.invoke(this.territory.getRobbi(), arr);

			if (result != null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText(Messages.getString("Execution.information.result") + result.toString());
				alert.showAndWait();
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if (e.getCause().getClass() != ThreadDeath.class) {
				if (e.getCause() instanceof RobbiException) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setContentText(e.getCause().getMessage());
					alert.showAndWait();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText(e.getCause().getMessage());
					alert.showAndWait();
				}
			}
		} finally {
			this.territory.activateNotification();
		}
	}

}
