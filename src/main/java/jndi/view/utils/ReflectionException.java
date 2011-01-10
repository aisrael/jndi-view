/**
 * Copyright (c) 2011 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Jan 10, 2011
 */
package jndi.view.utils;

import org.springframework.core.NestedRuntimeException;

/**
 * @author Alistair A. Israel
 */
public class ReflectionException extends NestedRuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -6794463459424094362L;

    /**
     * @param message
     *        the detail message
     * @param cause
     *        the cause
     * @see NestedRuntimeException#NestedRuntimeException(String, Throwable)
     */
    public ReflectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     *        the detail message
     * @see NestedRuntimeException#NestedRuntimeException(String)
     */
    public ReflectionException(final String message) {
        super(message);
    }

    /**
     * @param cause
     *        the cause
     * @see NestedRuntimeException#NestedRuntimeException(Throwable)
     */
    public ReflectionException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

}