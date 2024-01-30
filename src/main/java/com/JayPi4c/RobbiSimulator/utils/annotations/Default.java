package com.JayPi4c.RobbiSimulator.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to set the default value for a parameter in a method. This is
 * needed to run methods with parameters from the context Menu without asking
 * for values by the user.
 *
 * @author Jonas Pohl
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Default {

    /**
     * Returns the default value for the annotated parameter as a String.
     *
     * @return the default value for the annotated parameter
     */
    String value();

}
