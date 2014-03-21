/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.v3.wrapper;

import com.anosym.vjax.annotations.Id;
import com.anosym.vjax.annotations.v3.Wrapped;

/**
 *
 * @author marembo
 */
@Wrapped
public class WithId {

  @Id
  private Long id;
  private String value;
}
