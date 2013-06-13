/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.util;

/**
 * Determines the condition for which a property/type and its attribute will be accepted The
 * implementing class must have a public no-arg constructor and must not be an member class Use
 * case:
 * <pre>
 *    @Condition(condition = Administrator.class)
 *    private Person person;
 * </pre> With the following definitions:
 * <pre>
 *    class Administrator implements VConditional&lt:Person&gt; {
 *      public boolean accept(Person instance) { return instance.isAdministrator(); } //Marshall only if the Person is an administrator
 *      public boolean accept(Object prop) { return true; } // If the instance is accepted return true to all its property
 *    }
 * </pre>
 *
 * @author Administrator
 */
public interface VConditional<T> {

  /**
   * Returns true if the instance meets the condition
   *
   * @param instance
   * @return
   */
  public boolean accept(T instance);

  /**
   * Returns true if the condition accepts the property of the instance
   *
   * @param prop
   * @return
   */
  public boolean acceptProperty(Object prop);
}
