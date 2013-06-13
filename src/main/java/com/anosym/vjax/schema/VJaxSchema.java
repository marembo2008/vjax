/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema;

import com.anosym.vjax.annotations.Attribute;
import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.schema.annotations.AccessorType;
import com.anosym.vjax.schema.annotations.AccessorType.AccessType;
import com.anosym.vjax.schema.annotations.Schema;
import com.anosym.vjax.schema.annotations.Use;

/**
 *
 * @author Marembo
 */
@Schema(prefix = "vjax", version = "1.0", uri = "http://www.flemax.com/vjax",
language = "EN", schemaLocation = "http://www.flemax.com/vjax/vjax-schema.xml")
@AccessorType(AccessType.FIELD)
public class VJaxSchema {

  @Markup(name = "class")
  @Use(use = Use.UseType.REQUIRED)
  private String objectClass;
  /**
   * An optionally specified element if the tag name is different from the object field/method
   * property name
   */
  private String property;
  /**
   * This will be specified as an attribute value
   */
  @Attribute
  private String reference;
}
