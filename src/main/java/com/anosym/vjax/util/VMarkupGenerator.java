/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.util;

/**
 *
 * @author Marembo
 */
public interface VMarkupGenerator<T> {

    /**
     * Generates markup for the instance property
     * @param property
     * @return
     */
    String generateMarkup(Object property);

    /**
     * generates markup for the instance object
     * @param instance
     * @return
     */
    String markup(T instance);
}
