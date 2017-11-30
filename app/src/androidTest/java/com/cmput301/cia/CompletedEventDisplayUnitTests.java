/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;

import com.cmput301.cia.models.CompletedEventDisplay;
import com.cmput301.cia.models.HabitEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 29, 2017
 *
 * Tests the CompletedEventDisplay model class
 */

@RunWith(AndroidJUnit4.class)
public class CompletedEventDisplayUnitTests {

    @Test
    public void testGetters() {
        HabitEvent event = new HabitEvent("Z", "", new Date(), 25, -25);
        String title = "Title";
        String user = "User";

        CompletedEventDisplay display = new CompletedEventDisplay(event, title, user);
        assertTrue(display.getCompletionDate().equals(event.getDate()));
        assertTrue(display.getLocation().getLatitude() == event.getLocation().getLatitude());
        assertTrue(display.getLocation().getLongitude() == event.getLocation().getLongitude());
        assertTrue(display.getEvent().equals(event));
        assertTrue(display.getHabitName().equals(title));
    }

}