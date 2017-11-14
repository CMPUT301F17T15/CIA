/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import android.content.Intent;
import com.cmput301.cia.activities.events.CreateHabitEventActivity;

/**
 * Version 1
 * Author: Adil Malik
 * Date: Nov 7 2017
 *
 * This class represents the controller functions for creating new habit events
 */

public class CreateHabitEventController {

    /**
     * Start an activity where the user selects an image
     * @param activity the activity that started the new one
     */
    public static void clickImage(CreateHabitEventActivity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, CreateHabitEventActivity.SELECT_IMAGE_CODE);
    }

}
