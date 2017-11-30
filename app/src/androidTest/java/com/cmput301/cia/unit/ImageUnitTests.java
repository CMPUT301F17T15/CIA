/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.unit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.cmput301.cia.activities.MainActivity;
import com.cmput301.cia.activities.events.CreateHabitEventActivity;
import com.cmput301.cia.utilities.ImageUtilities;

import java.io.IOException;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 13 2017
 *
 * This class tests selecting images to attach to an event or profile
 */

public class ImageUnitTests extends ActivityInstrumentationTestCase2<MainActivity>{

    public ImageUnitTests() {
        super(MainActivity.class);
    }

    /**
     * Testing an image that is already small enough to fit in the system
     */
    public void testSmallImage() throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open("testimages/small.jpg"));
        Bitmap newBitmap = ImageUtilities.compressImageToMax(bitmap, CreateHabitEventActivity.MAX_IMAGE_SIZE);
        assertTrue(newBitmap != null);
        assertTrue(newBitmap.getByteCount() <= CreateHabitEventActivity.MAX_IMAGE_SIZE);
    }

    /**
     * Testing an image that is too large to fit in the system, and must be resized
     */
    public void testLargeImage() throws IOException {
        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getAssets().open("testimages/large.jpg"));
        int size = bitmap.getByteCount();
        Bitmap newBitmap = ImageUtilities.compressImageToMax(bitmap, CreateHabitEventActivity.MAX_IMAGE_SIZE);

        assertTrue(newBitmap != null);
        assertTrue(size > CreateHabitEventActivity.MAX_IMAGE_SIZE);
        assertTrue(newBitmap.getByteCount() <= CreateHabitEventActivity.MAX_IMAGE_SIZE);
    }

}