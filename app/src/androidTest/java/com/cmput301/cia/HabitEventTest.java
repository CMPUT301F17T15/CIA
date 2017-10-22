/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;

import com.cmput301.cia.models.HabitEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jessica Prieto on 2017-10-20.
 * Version 1.0
 *
 * tests for the HabitEvent class
 *   Note: this test involves testing with the Location class which is
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
        String testBase64Image = getMockImage();

        HabitEvent habitEvent = new HabitEvent("comment", testBase64Image);

        assertEquals("wrong comment", "comment", habitEvent.getComment());
        assertEquals("wrong image", testBase64Image, habitEvent.getBase64EncodedPhoto());
    }

    /** testing method(s): getComment(), getDate()
     *  for the constructor with date parameter:
     *    HabitEvent(comment, photo, date)
     */
    @Test
    public void testGetCommentsTestWithDateParams() {
        Date testDate = new Date();
        String testBase64Image = getMockImage();

        HabitEvent habitEvent = new HabitEvent("comment", testBase64Image, testDate);

        assertEquals("wrong comment", "comment", habitEvent.getComment());
        assertEquals("wrong image", testBase64Image, habitEvent.getBase64EncodedPhoto());
        assertEquals("wrong date", testDate, habitEvent.getDate());
    }

    /** testing method(s): getComment(), getDate(), getLocation()
     *  for the constructor with date and location parameters:
     *    HabitEvent(comment, photo, date, location)
     */
    @Test
    public void testGetCommentsTestWithDateAndLocationParams() {
        Date testDate = new Date();
        String testBase64Image = getMockImage();
        Location testLocation = getMockLocation();

        HabitEvent habitEvent = new HabitEvent("comment", testBase64Image, testDate, testLocation);

        assertEquals("wrong comment", "comment", habitEvent.getComment());
        assertEquals("wrong image", testBase64Image, habitEvent.getBase64EncodedPhoto());
        assertEquals("wrong date", testDate, habitEvent.getDate());
        assertEquals("wrong location", testLocation, habitEvent.getLocation());
    }

    /**
     * method for creating a mock image to test habitEvent.getBase64EncodedPhoto()
     * @return
     */
    public String getMockImage() {
        String text = "test image string";
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        return base64;
    }

    public Location getMockLocation() {
        Location testLocation = new Location("");
        testLocation.setLatitude(53.525283d);
        testLocation.setLongitude(-113.525612d);
        return testLocation;
    }
}