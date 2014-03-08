/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import com.anosym.vjax.annotations.v3.Wrapped;

/**
 *
 * @author marembo
 */
@Wrapped
public class Require extends Extended {

  private String name;
  private int index;
  private Option[] options;

  public Require() {
  }

  public Require(String name, int index) {
    this.name = name;
    this.index = index;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

}
