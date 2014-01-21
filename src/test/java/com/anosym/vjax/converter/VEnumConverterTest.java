package com.anosym.vjax.converter;

import junit.framework.Assert;

import org.junit.Test;

import com.anosym.vjax.exceptions.VConverterBindingException;

public class VEnumConverterTest {
	public static enum SimpleEnum {
		FIRST, SECOND, THIRD_OR_FOURTH;
	}

	@Test
	public void testUnmarshalEnum() {
		String type = "first";
		SimpleEnum expected = SimpleEnum.FIRST;
		VEnumConverter conv = new VEnumConverter();
		if (conv.isConvertCapable(SimpleEnum.class)) {
			try {
				SimpleEnum actual = (SimpleEnum) conv.convert(type);
				Assert.assertEquals(expected, actual);
				System.out.print(actual);
			} catch (VConverterBindingException e) {
				e.printStackTrace();
			}
		} else {
			Assert.fail("Invalid converter");
		}
	}

	@Test
	public void testMarshalEnum() {
		String expected = "third or fourth";
		SimpleEnum type = SimpleEnum.THIRD_OR_FOURTH;
		VEnumConverter conv = new VEnumConverter();
		if (conv.isConvertCapable(SimpleEnum.class)) {
			try {
				String actual = conv.convert(type);
				Assert.assertEquals(expected, actual);
				System.out.print(actual);
			} catch (VConverterBindingException e) {
				e.printStackTrace();
			}
		} else {
			Assert.fail("Invalid converter");
		}
	}
}
