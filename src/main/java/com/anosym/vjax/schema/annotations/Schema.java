/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.schema.annotations;

import java.lang.annotation.*;

/**
 * if this element appears on a class declaration, then the class will be marshalled by the schema
 * generator as a different schema, and the current schema file will use the include/import strategy
 * as defined in the w3c XInclude Recommendation
 *
 * @author Marembo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Schema {

  /**
   * The namespace prefix to be bound to the uri within the context for which this declaration
   * occurs
   *
   * @return
   */
  String prefix() default "";

  /**
   * The namespace unique uri. If not specified, this defaults to the conversion of the class
   * package names to a suitable uri. The following Rules will be used in converting a package to a
   * namespace uri
   * <pre>
   * <ol>
   * <li>If the high level package starts with any of the following (com,net,org or any equivalent internet domain) that will default to the
   * uri high level domain</li>
   * <li>The second high level package, if any will be the domain name</li>
   * <li>Subsequent level packages will default as subdomains but the last</li>
   * <li>the last level package will be used as as resource identifier</li>
   * </ol>
   * </pre>
   *
   * @return
   */
  String uri() default "";

  boolean elementFormQualified() default true;

  boolean attributeFormQualified() default false;

  String version() default "1.0";

  String language() default "EN";

  String defaultNamespace() default "http://www.w3.org/2001/XMLSchema";

  /**
   * When not specified, this will default to the defined namespace
   *
   * @return
   */
  String targetNamespace() default "";

  /**
   * The location of the schema if applicable
   *
   * @return
   */
  String schemaLocation() default "";
}
