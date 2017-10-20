/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import com.cmput301.cia.models.HabitEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jessica Prieto on 2017-10-20.
 * Version 1.0
 *
 * tests for the HabitEvent class
 *   Note: this test involves testing with the Location and Image classes which are
 *   part of the android package. That's why this test is included in the androidTest
 *   and not the local JUnit test
 *
 *   @see HabitEvent
 */
@RunWith(AndroidJUnit4.class)
public class HabitEventTest {

    /** testing method(s): getComment()
     *  for the base constructor with only comment and photo parameters:
     *    HabitEvent(comment, photo)
     */
    @Test
    public void testGettersOnBaseConstructor() {
        HabitEvent habitEvent = new HabitEvent("comment", null);
        assertEquals("comment", habitEvent.getComment());
    }

    /** testing method(s): getComment(), getDate()
     *  for the constructor with date parameter:
     *    HabitEvent(comment, photo, date)
     */
    @Test
    public void testGetCommentsTestWithDateParams() {
        Date testDate = new Date();

        HabitEvent habitEvent = new HabitEvent("comment", null, testDate);
        assertEquals("wrong comment", "comment", habitEvent.getComment());
        assertEquals("wrong date", testDate, habitEvent.getDate());
    }

    /** testing method(s): getComment(), getDate(), getLocation()
     *  for the constructor with date and location parameters:
     *    HabitEvent(comment, photo, date, location)
     */
    @Test
    public void testGetCommentsTestWithDateAndLocationParams() {
        Date testDate = new Date();

        Location testLocation = new Location("");
        testLocation.setLatitude(53.525283d);
        testLocation.setLongitude(-113.525612d);

        HabitEvent habitEvent = new HabitEvent("comment", null, testDate, testLocation);
        assertEquals("comment", habitEvent.getComment());
        assertEquals("wrong date", testDate, habitEvent.getDate());
        assertEquals("wrong location", testLocation, habitEvent.getLocation());
    }
}