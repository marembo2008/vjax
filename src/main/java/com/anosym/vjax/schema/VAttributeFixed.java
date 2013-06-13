/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema;

/**
 * The interface defines the way a fixed value is generated when a property annotated as an attribute and fixed is encounterd
 * @author Marembo
 */
public interface VAttributeFixed<T> {

    /**
     * Returns the fixed value of the attribute
     * However, note that the returned value must be annotated with Converter annotation in order that it is possible to
     * convert between the value and String.
     * The returned value class must be the same as the property class
     * @return
     */
    T fixed();
}
