/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import com.cmput301.cia.models.Habit;

import java.util.Date;
import java.util.List;

/**
 * Created by Jessica on 2017-11-04.
 */

public class CreateHabitController {
    public static Habit onSaveClicked(String habitName, String reason, Date startDate, List<Integer> daysOfTheWeek, String type) {
        // TODO: save the habit on elastic search/serializable
        // TODO: handle when user also inputs habit type

        return new Habit(habitName, reason, startDate, daysOfTheWeek, type);
    }
}
