/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import android.view.View;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 27, 2017
 *
 * This class represents a listener that prevents views from being clicked multiple times within a short
 * period of time. Exact same as View.OnClickListener besides this.
 */

public abstract class TimedClickListener implements View.OnClickListener {

    // minimum time in milliseconds that must separate clicks
    private static final long MIN_CLICK_INTERVAL = 2500;

    // the time in milliseconds since the epoch the last successful click occurred at
    private long lastClick;

    /**
     * Construct a new TimedClickListener object
     */
    public TimedClickListener(){
        lastClick = 0;
    }

    @Override
    public void onClick(View view) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClick > MIN_CLICK_INTERVAL){
            lastClick = currentTime;
            handleClick();
        }
    }

    /**
     * Handle the unique logic for this view being clicked
     */
    public abstract void handleClick();

}
