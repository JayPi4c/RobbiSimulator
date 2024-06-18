package com.JayPi4c.RobbiSimulator.controller;

import com.JayPi4c.RobbiSimulator.model.RobbiException;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.I18nUtils;
import com.JayPi4c.RobbiSimulator.utils.annotations.Default;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * MethodHandler to handle a methods invocation
 *
 * @author Jonas Pohl
 */
@Slf4j
public class MethodHandler implements EventHandler<ActionEvent> {

    private final Method method;
    private final Territory territory;
    private final MainStage parent;

    /**
     * Creates a new MethodHandler with the method and the territory the message is
     * for. With the handle method, the handler will be invoked.
     *
     * @param method    the method for this handler
     * @param territory the territory to invoke this method in
     * @param parent    the parent window, in order to place alerts relative to the
     *                  window
     */
    public MethodHandler(Method method, Territory territory, MainStage parent) {
        this.method = method;
        this.territory = territory;
        this.parent = parent;
    }

    @Override
    public void handle(ActionEvent event) {
        this.territory.deactivateNotification();
        this.method.setAccessible(true); // allow calling private/package-private and protected
        try {
            List<Object> args = new ArrayList<>();
            for (Parameter p : method.getParameters()) {
                for (Annotation anno : p.getAnnotations()) {
                    if (anno instanceof Default a) {
                        String val = a.value();
                        if (!addToList(args, p, val))
                            return;
                        break;
                    }
                }
            }
            Object[] arr = args.toArray(Object[]::new);
            Object result = this.method.invoke(this.territory.getRobbi(), arr);

            if (result != null) {
                AlertHelper.showAlertAndWait(AlertType.INFORMATION,
                        I18nUtils.i18n("Execution.information.result") + result, parent);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            logger.error("Failed to invoke method", e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RobbiException) {
                parent.getNotificationController().showMessage(3000, e.getCause().getLocalizedMessage());
            } else {
                AlertHelper.showAlertAndWait(AlertType.ERROR, e.getCause().getMessage(), parent);
            }
        } finally {
            this.territory.activateNotification();
        }
    }

    /**
     * Helper to add the correct value type to the arguments list.
     *
     * @param args the list of arguments for the annotated method
     * @param p    the parameter, the value was taken from
     * @param val  the value to add to the list
     * @return true, if the addition was successful, false otherwise
     */
    private boolean addToList(List<Object> args, Parameter p, String val) {
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
            // TODO fix i18n
            AlertHelper.showAlertAndWait(AlertType.ERROR,
                    String.format(I18nUtils.i18n("Editor.contextMenu.executionError"), val, method.getName()), parent);
            return false;
        }
        return true;
    }

}
