/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.utilities;

import java.util.Date;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 11 2017
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

        return one.getDay() == two.getDay() && one.getMonth() == two.getMonth() && one.getYear() == two.getYear();
    }

}
