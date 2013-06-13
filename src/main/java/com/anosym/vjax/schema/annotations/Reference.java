/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * This determines that the specified element will be unique in the entire document instance This
 * applies to the entire element's contents and its children. That is, it applies to all primitive
 * types and java objects. This implies that the instance is Singleton, and the marshaller will
 * expect it to be so.
 *
 * <p> In marshalling an instance document based on the generated schema, any instance whose
 * reference id is equal to an already marshalled instance will be referenced to the previously
 * marshalled instance. However, if the two are found not to be equal, an exception will be raised.
 * </p>
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Reference {
}
