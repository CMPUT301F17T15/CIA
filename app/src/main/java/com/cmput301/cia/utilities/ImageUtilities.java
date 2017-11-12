/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.utilities;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Nov 12 2017
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
        int oldSize = image.getByteCount();

        // attempt to resize the image as much as possible while valid
        while (image != null && image.getByteCount() > maxBytes){

            // Prevent image from becoming too small
            if (image.getWidth() <= 20 || image.getHeight() <= 20)
                return null;

            // scale down the image by a factor of 2
            image = Bitmap.createScaledBitmap(image, image.getWidth() / 2, image.getHeight() / 2, false);

            // the byte count did not change for some reason, can not be made any smaller
            if (image.getByteCount() == oldSize)
                return null;

            oldSize = image.getByteCount();
        }

        return image;
    }

}
