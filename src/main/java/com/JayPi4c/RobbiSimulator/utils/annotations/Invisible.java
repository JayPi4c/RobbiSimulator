package com.JayPi4c.RobbiSimulator.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to hide methods from the ContextMenu. Methods annotated with this
 * annotation will not show up in the ContextMenu. Nonetheless, they still can
 * be used in the code without any restrictions.
 * 
 * @author Jonas Pohl
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Invisible {

}
