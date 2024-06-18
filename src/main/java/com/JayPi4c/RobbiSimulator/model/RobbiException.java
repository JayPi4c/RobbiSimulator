package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.I18nUtils;

import java.io.Serial;

/**
 * Abstract Exception class for almost all exceptions thrown in this
 * application.
 *
 * @author Jonas Pohl
 */
public abstract class RobbiException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for a new RobbiException with the given message key.
     *
     * @param key a key for a message to provide further details of the exception
     */
    protected RobbiException(String key) {
        super(key);
    }

    @Override
    public String getLocalizedMessage() {
        return I18nUtils.i18n(getMessage());
    }

}
