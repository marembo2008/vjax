/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.exceptions;

/**
 *
 * @author Marembo
 */
public class VConverterBindingException extends Exception {

    public VConverterBindingException(Throwable cause) {
        super(cause);
    }

    public VConverterBindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public VConverterBindingException(String message) {
        super(message);
    }

    public VConverterBindingException() {
    }
}
