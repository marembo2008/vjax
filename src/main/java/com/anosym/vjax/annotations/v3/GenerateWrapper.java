package com.anosym.vjax.annotations.v3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.atteo.classindex.IndexAnnotated;

/**
 * If found on a class level, indicates to the wrapper that the class should generate a wrapper for
 * the class;
 *
 * @author marembo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@IndexAnnotated
@Inherited
public @interface GenerateWrapper {

    /**
     * If class name is not provided, appends 'W' to the classname.
     * @return
     */
    boolean appendClassPrefix() default true;

    /**
     * The className of the wrapper class. Default pre-appends 'W' to the className of the original
     * class.
     *
     * @return
     */
    String className() default "";

    /**
     * If packagge is not provided, appends the suffix 'Wrapper' to the package name.
     * @return
     */
    boolean appendPackageSuffix() default true;

    /**
     * The package name of the generated wrapper. Default appends .wrapper to current class package.
     *
     * @return
     */
    String packageName() default "";

    /**
     * By default, the wrapper will implement the serializable.
     *
     * @return
     */
    boolean serializable() default true;

}
