/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema;

import com.anosym.vjax.annotations.Markup;
import com.anosym.vjax.schema.annotations.Global;
import com.anosym.vjax.schema.annotations.ModelGroup;
import com.anosym.vjax.schema.annotations.Use;

/**
 *
 * @author Marembo
 */
@Global
@Markup(name = "primitive")
@ModelGroup(type = ModelGroup.GroupType.SEQUENCE)
public class VPrimitive {
  @Use(use = Use.UseType.REQUIRED)
  @Markup(name = "primitive")
  private VJaxPrimitive primitiveType;
  @Use(use = Use.UseType.REQUIRED)
  @Markup(name = "wrapper")
  private String primitiveWrapper;
}
