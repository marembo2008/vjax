/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax;

import com.anosym.vjax.annotation.IgnoreUrlParam;
import com.anosym.vjax.annotation.Url;
import com.anosym.vjax.annotation.UrlEncodeInheritedParam;
import com.anosym.vjax.annotation.UrlParam;
import com.flemax.vjax.annotation.UrlParamValue;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marembo
 */
public class HttpUrlEncoder<T> {

  private List<Field> getAllFields(Class<?> clazz, int currentLevel, boolean encodeSuper, int maxEncodeLevel) {
    List<Field> fields = new ArrayList<Field>();
    Field[] ff = clazz.getDeclaredFields();
    if (ff != null && ff.length > 0) {
      fields.addAll(Arrays.asList(ff));
    }
    currentLevel++;
    if (encodeSuper && currentLevel < maxEncodeLevel) {
      Class<?> superClass = clazz.getSuperclass();
      if (!superClass.isAssignableFrom(Object.class)) {
        fields.addAll(getAllFields(superClass, currentLevel, encodeSuper, maxEncodeLevel));
      }
    }
    return fields;
  }

  public String encodeUrl(final T object) throws HttpUrlEncodingException {
    if (object == null) {
      throw new IllegalArgumentException("null reference");
    }
    try {
      Class<?> clazz = object.getClass();
      //get members
      //private or public
      String urlEncoded = "";
      String urlParams = "";
      boolean baseUrlEncoded = false;
      UrlEncodeInheritedParam inheritedParam = clazz.getAnnotation(UrlEncodeInheritedParam.class);
      List<Field> members = getAllFields(clazz, -1, inheritedParam != null
              && inheritedParam.encodeInheritedParam(), inheritedParam != null
              && inheritedParam.encodeInheritedParam() ? inheritedParam.encodeInheritedLevel() : 0);
      if (members != null && !members.isEmpty()) {
        for (final Field f : members) {
          f.setAccessible(true);
          //ensure field is not ignored
          IgnoreUrlParam ignoreUrlParam = f.getAnnotation(IgnoreUrlParam.class);
          if (ignoreUrlParam != null) {
            continue;
          }
          //get url annotation.
          if (!baseUrlEncoded) {
            Url url = f.getAnnotation(Url.class);
            if (url != null) {
              Object urlValue = AccessController.doPrivileged(new PrivilegedAction<Object>() {
                @Override
                public Object run() {
                  try {
                    return f.get(object);
                  } catch (IllegalArgumentException ex) {
                    Logger.getLogger(HttpUrlEncoder.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (IllegalAccessException ex) {
                    Logger.getLogger(HttpUrlEncoder.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  return null;
                }
              });
              if (urlValue != null) {
                urlEncoded = urlValue.toString();
                baseUrlEncoded = true;
              }
              continue;
            }
          }
          String urlParamName;
          UrlParam urlParam = f.getAnnotation(UrlParam.class);
          if (urlParam != null) {
            urlParamName = urlParam.value();
          } else {
            urlParamName = f.getName();
          }
          Object value = AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
              try {
                return f.get(object);
              } catch (IllegalArgumentException ex) {
                Logger.getLogger(HttpUrlEncoder.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IllegalAccessException ex) {
                Logger.getLogger(HttpUrlEncoder.class.getName()).log(Level.SEVERE, null, ex);
              }
              return null;
            }
          });
          String urlParamValue = "" + value;
          boolean encodeNullValue = true;
          boolean encodeEmptyValue = true;
          UrlParamValue paramValue = f.getAnnotation(UrlParamValue.class);
          if (paramValue != null) {
            encodeNullValue = paramValue.encodeNull();
            encodeEmptyValue = paramValue.encodeEmpty();
          }
          if (encodeNullValue || (encodeEmptyValue && urlParamValue.isEmpty()) || value != null) {
            if (!urlParams.isEmpty()) {
              urlParams += "&";
            }
            urlParams += urlParamName;
            urlParams += "=";
            urlParams += urlParamValue;
          }
        }
      }
      if (urlEncoded == null || urlEncoded.isEmpty()) {
        throw new IllegalArgumentException("Url for encoding not specified");
      }
      return urlParams != null && !urlParams.isEmpty() ? urlEncoded + "?" + urlParams : urlEncoded;
    } catch (Exception es) {
      throw new HttpUrlEncodingException("Error Encoding url for object: " + object.getClass(), es);
    }
  }
}
