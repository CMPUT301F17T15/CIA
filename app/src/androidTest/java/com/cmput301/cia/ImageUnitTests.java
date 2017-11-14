/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.runner.AndroidJUnit4;

import com.cmput301.cia.activities.events.CreateHabitEventActivity;
import com.cmput301.cia.utilities.ImageUtilities;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests selecting images to attach to an event or profile
 */

// TODO: make these work

@RunWith(AndroidJUnit4.class)
public class ImageUnitTests {

    /**
     * Testing an image that is already small enough to fit in the system
     */
    @Test
    public void testSmallImage() {
        Bitmap bitmap = BitmapFactory.decodeFile("testimages/small.jpg");
        int size = bitmap.getByteCount();
        Bitmap newBitmap = ImageUtilities.compressImageToMax(bitmap, CreateHabitEventActivity.MAX_IMAGE_SIZE);
        assertTrue(newBitmap != null);

        // make sure the image was not resized
        assertTrue(size == newBitmap.getByteCount());
        assertTrue(size <= CreateHabitEventActivity.MAX_IMAGE_SIZE);
    }

    /**
     * Testing an image that is too large to fit in the system, and must be resized
     */
    @Test
    public void testLargeImage() {
        Bitmap bitmap = BitmapFactory.decodeFile("testimages/large.jpg");
        int size = bitmap.getByteCount();
        Bitmap newBitmap = ImageUtilities.compressImageToMax(bitmap, CreateHabitEventActivity.MAX_IMAGE_SIZE);

        assertTrue(newBitmap != null);
        assertTrue(size > CreateHabitEventActivity.MAX_IMAGE_SIZE);
        assertTrue(newBitmap.getByteCount() <= CreateHabitEventActivity.MAX_IMAGE_SIZE);
    }

}