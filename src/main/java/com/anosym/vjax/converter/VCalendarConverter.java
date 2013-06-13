/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.converter;

import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.alias.VAlias;
import com.anosym.vjax.exceptions.VConverterBindingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The calendar alias used in this calendar assumes that the calendar object refers
 * to the Locale being used by the virtual machine.
 * Unless otherwise specified, this calendar converter will use the Locale and TimeZone as at the time
 * the calendar instance was converted to the Alias
 * @author Marembo
 */
public class VCalendarConverter extends VConverter<Calendar> implements VAlias<Calendar, VCalendarConverter.CalendarAlias> {

    public CalendarAlias getAlias(Calendar instance) throws VConverterBindingException {
        return new CalendarAlias(instance);
    }

    public Calendar getInstance(CalendarAlias alias) throws VConverterBindingException {
        if (alias instanceof CalendarAlias) {
            return ((CalendarAlias) alias).getInstance();
        }
        throw new VConverterBindingException("Invalid Alias for Calendar Specified: " + alias.getClass().getName());
    }

    public static class CalendarAlias {

        private String date;
        private String timeZoneId;
        private String localeCountry;
        private String localeLanguage;
        private String localeVariant;

        private CalendarAlias(String date, String timeZoneId, String localeCountry, String localeLanguage, String localeVariant) {
            this.date = date;
            this.timeZoneId = timeZoneId;
            this.localeCountry = localeCountry;
            this.localeLanguage = localeLanguage;
            this.localeVariant = localeVariant;
        }

        public CalendarAlias(Calendar cal) throws VConverterBindingException {
            this(convertToString(cal), cal.getTimeZone().getID(), Locale.getDefault().getCountry(), Locale.getDefault().getLanguage(), Locale.getDefault().getVariant());
        }

        public CalendarAlias() {
        }

        private Calendar getInstance() throws VConverterBindingException {
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
            Locale locale = new Locale(localeLanguage, localeCountry, localeVariant);
            Calendar instance = Calendar.getInstance(timeZone, locale);
            //set the time
            instance.setTime(convertToCalendar(date).getTime());
            return instance;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLocaleCountry() {
            return localeCountry;
        }

        public void setLocaleCountry(String localeCountry) {
            this.localeCountry = localeCountry;
        }

        public String getLocaleLanguage() {
            return localeLanguage;
        }

        public void setLocaleLanguage(String localeLanguage) {
            this.localeLanguage = localeLanguage;
        }

        public String getLocaleVariant() {
            return localeVariant;
        }

        public void setLocaleVariant(String localeVariant) {
            this.localeVariant = localeVariant;
        }

        public String getTimeZoneId() {
            return timeZoneId;
        }

        public void setTimeZoneId(String timeZoneId) {
            this.timeZoneId = timeZoneId;
        }
    }
    private VDateConverter converter = new VDateConverter();

    public Calendar convert(String value) throws VConverterBindingException {
        try {
            Date date = converter.convert(value);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    private static String convertToString(Calendar value) throws VConverterBindingException {
        return new VCalendarConverter().convert(value);
    }

    private static Calendar convertToCalendar(String value) throws VConverterBindingException {
        return new VCalendarConverter().convert(value);
    }

    public String convert(Calendar value) throws VConverterBindingException {
        try {
            return converter.convert(value.getTime());
        } catch (Exception e) {
            throw new VConverterBindingException(e);
        }
    }

    public boolean isConvertCapable(Class<Calendar> clazz) {
        //ensure that the clazz is either Calendar or subclassses
        return Calendar.class.isAssignableFrom(clazz);
    }
}
