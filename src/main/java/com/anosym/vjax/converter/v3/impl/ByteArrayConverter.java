/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter.v3.impl;

import com.anosym.vjax.converter.v3.Converter;

/**
 * This library uses a simple byte array encoding/decoding. This is because we have set vjax to be
 * compatible with mobile phones, and hence including strenuous libraries here may break the
 * compatibility.
 *
 * @author marembo
 */
public class ByteArrayConverter implements Converter<byte[], String> {

  private final static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

  private static final int[] toInt = new int[128];

  static {
    for (int i = 0; i < ALPHABET.length; i++) {
      toInt[ALPHABET[i]] = i;
    }
  }

  /**
   * Translates the specified byte array into Base64 string.
   *
   * @param buf the byte array (not null)
   * @return the translated Base64 string (not null)
   */
  private static String encode(byte[] buf) {
    int size = buf.length;
    char[] ar = new char[((size + 2) / 3) * 4];
    int a = 0;
    int i = 0;
    while (i < size) {
      byte b0 = buf[i++];
      byte b1 = (i < size) ? buf[i++] : 0;
      byte b2 = (i < size) ? buf[i++] : 0;

      int mask = 0x3F;
      ar[a++] = ALPHABET[(b0 >> 2) & mask];
      ar[a++] = ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
      ar[a++] = ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
      ar[a++] = ALPHABET[b2 & mask];
    }
    switch (size % 3) {
      case 1:
        ar[--a] = '=';
      case 2:
        ar[--a] = '=';
    }
    return new String(ar);
  }

  /**
   * Translates the specified Base64 string into a byte array.
   *
   * @param s the Base64 string (not null)
   * @return the byte array (not null)
   */
  private static byte[] decode(String s) {
    int delta = s.endsWith("==") ? 2 : s.endsWith("=") ? 1 : 0;
    byte[] buffer = new byte[s.length() * 3 / 4 - delta];
    int mask = 0xFF;
    int index = 0;
    for (int i = 0; i < s.length(); i += 4) {
      int c0 = toInt[s.charAt(i)];
      int c1 = toInt[s.charAt(i + 1)];
      buffer[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
      if (index >= buffer.length) {
        return buffer;
      }
      int c2 = toInt[s.charAt(i + 2)];
      buffer[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
      if (index >= buffer.length) {
        return buffer;
      }
      int c3 = toInt[s.charAt(i + 3)];
      buffer[index++] = (byte) (((c2 << 6) | c3) & mask);
    }
    return buffer;
  }

  @Override
  public String convertFrom(byte[] value) {
    return encode(value);
  }

  @Override
  public byte[] convertTo(String value) {
    return decode(value);
  }

}
