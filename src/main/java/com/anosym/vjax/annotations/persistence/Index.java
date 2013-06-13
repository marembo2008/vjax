/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.annotations.persistence;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Shows an application preference to index the specified column for faster retrieval
 * during query look-ups.
 * If the index entity is not a simple type, it may require that the data is decoded, which may result
 * in a number of callbacks and marshalling of references and thus slows down the system greatly.
 * @author marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Index {
}
