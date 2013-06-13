/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import com.anosym.vjax.schema.VJaxXmlTypeMapping;
import java.lang.annotation.*;

/**
 * The restriction specified on an object property
 *
 * @author Marembo
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Restriction {

  public static enum Facet {

    LENGTH("length"),
    MINIMUM_LENGTH("minLength"),
    MAXIMUM_LENGTH("maxLength"),
    PATTERN("pattern"),
    ENUMERATION("enumeration"),
    WHITE_SPACE("whiteSpace"),
    MAXIMUM_INCLUSIVE("maxInclusive"),
    MINIMUM_INCLUSIVE("minInclusive"),
    MAXIMUM_EXCLUSIVE("maxExclusive"),
    MINIMUM_EXCLUSIVE("minExclusive"),
    TOTAL_DIGITS("totalDigits"),
    FRACTION_DIGITS("fractionDigits");
    private String name;

    private Facet(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  /**
   * The facets to apply to the restriction for defining a new simple element type
   *
   * @return
   */
  Facet[] facets();

  /**
   * The values of the facets. Note that this must be the same as length as the facets and in the
   * same order
   *
   * @return
   */
  String[] values();

  /**
   * The base of the new simple type If not specified, the default is the Vjax mapping to xml types
   *
   * @see VJaxXmlTypeMapping
   * @return
   */
  String base() default "";
}
