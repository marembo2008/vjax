/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * When the root element is annotated with this element, then it instructs the VJax Marshaller that
 * a vjax schema be generated that can be used to unmarshall the document without the necessity of
 * generating specific classes from the document schema if the classes from which the document was
 * generated already exists in the user application class path
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VJaxInterface {
}
