/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema;

/**
 * The interface provides a way to generate any default value for an element or attribuet
 *
 * @author Marembo
 */
public interface VSchemaDefault<T> {

  T getDefualt();
}
