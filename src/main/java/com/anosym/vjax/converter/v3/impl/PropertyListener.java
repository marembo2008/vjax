/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

/**
 *
 * @author marembo
 * @param <T> the object to which the property belongs
 * @param <P> the property to set
 */
public interface PropertyListener<T, P> {

    void onSet(T object, P property);
}
