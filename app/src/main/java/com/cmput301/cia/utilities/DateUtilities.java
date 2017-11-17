/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Adil Malik
 * @version 2
 * Date: Nov 12 2017
 *
 * Contains utilities for dates
 */

public class DateUtilities {

    /**
     * @param one the first date to compare
     * @param two the second date to compare
     * @return whether the two dates are on the same day if both are non-null. If at least one is null, return false
     */
    public static boolean isSameDay(Date one, Date two){

        if (one == null || two == null)
            return false;

        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.setTime(one);
        int dayOne = gc1.get(Calendar.DATE);
        int monthOne = gc1.get(Calendar.MONTH);
        int yearOne = gc1.get(Calendar.YEAR);
        gc1.setTime(two);

        return dayOne == gc1.get(Calendar.DATE) && monthOne == gc1.get(Calendar.MONTH) && yearOne == gc1.get(Calendar.YEAR);
    }

    /**
     * Compare two dates without looking at their time component
     * @param one the first date to compare
     * @param two the second date to compare
     * @return whether the first date is before the second one
     */
    public static boolean isBefore(Date one, Date two){
        if (one == null || two == null)
            return false;

        GregorianCalendar gc1 = new GregorianCalendar();
        gc1.setTime(one);
        gc1.set(Calendar.HOUR, 2);
        gc1.set(Calendar.MINUTE, 1);
        gc1.set(Calendar.SECOND, 1);

        GregorianCalendar gc2 = new GregorianCalendar();
        gc2.setTime(two);
        gc2.set(Calendar.HOUR, 2);
        gc2.set(Calendar.MINUTE, 1);
        gc2.set(Calendar.SECOND, 1);

        return gc1.getTime().before(gc2.getTime());
    }

    /**
     * Format the specified date as "MMM dd, YYYY" for consistency across the application
     * @param date the date to format
     * @return the string in the above format
     */
    public static String formatDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        return df.format(date);
    }

}
