/**
 * The following packages relate mostly to the generation of schemas from the classes specified.
 * Even though the generation of instances can use the following annotations to specify certain
 * behaviours during marshalling and unmarsahlling, their main intention is to aid in the developing
 * of schemas for the particular class hierarchy
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * The following is allowed only on array whose component types are primitive a property (of
 * array-type) annotated with this annotation will be marshalled to a list simple type in xml
 * document instance and the schema will define a list type derived form the vjax xml type mapping
 * and then pluralizing the name, after prefixing with V. This happens only if the name of the new
 * element has not been specified. In this scenario, the property will be represented as a single
 * element, with no attributes
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface List {
}
