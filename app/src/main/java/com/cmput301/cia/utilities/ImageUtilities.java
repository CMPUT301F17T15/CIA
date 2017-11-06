/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 5 2017
 *
 * This class provides utility functions for bitmap related activities
 */

public class ImageUtilities {

    /**
     * Convert an image to base64
     * @param image the image to convert
     * @return the image as a base64 string
     */
    public static String imageToBase64(Bitmap image){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // TODO: if JPEG reduces quality then set to PNG
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Compress an image as much as possible until the size in bytes is no more than the specified amount
     * @param image the image to compress
     * @param maxBytes the maximum size of the image in bytes
     * @return the compressed image if it was possible to get it to that size, or null otherwise
     */
    public static Bitmap compressImageToMax(Bitmap image, int maxBytes){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // attempt to resize the image as much as possible while valid
        while (image != null && image.getByteCount() > maxBytes && image.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)){

            int oldSize = image.getByteCount();

            // TODO: test to make sure no exceptions
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            image = BitmapFactory.decodeStream(is);

            if (image.getByteCount() == oldSize)
                return null;
        }

        return image;
    }

}
