/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.exceptions.VConverterBindingException;

/**
 * The default conversion is to return the lowercase name of the enum instance.
 * Replaces all spaces with an underscore, during the unmarshalling phase.
 * 
 * @author marembo
 */
public class VEnumConverter extends VConverter<Enum<?>> {

	@SuppressWarnings("rawtypes")
	private Class enumClass;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Enum convert(String value) throws VConverterBindingException {
		return Enum
				.valueOf(enumClass, value.toUpperCase().replaceAll(" ", "_"));
	}

	@Override
	public String convert(Enum<?> value) throws VConverterBindingException {
		return value.name().toLowerCase().replaceAll("_", " ");
	}

	@Override
	public boolean isConvertCapable(@SuppressWarnings("rawtypes") Class clazz) {
		enumClass = clazz;
		return Enum.class.isAssignableFrom(clazz);
	}
}
