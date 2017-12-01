/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import android.view.View;
import android.widget.AdapterView;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 30, 2017
 *
 * This class represents a listener that prevents items in ListViews from being clicked multiple times within a short
 * period of time. Exact same as AdapterView.OnClickListener besides this.
 */

public abstract class TimedAdapterViewClickListener implements AdapterView.OnItemClickListener {

    // minimum time in milliseconds that must separate clicks
    private static final long MIN_CLICK_INTERVAL = 2500;

    // the time in milliseconds since the epoch the last successful click occurred at
    private long lastClick;

    /**
     * Construct a new TimedAdapterViewClickListener object
     */
    public TimedAdapterViewClickListener(){
        lastClick = 0;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClick > MIN_CLICK_INTERVAL){
            lastClick = currentTime;
            handleClick(view, i);
        }
    }

    /**
     * Handle the unique logic for this view being clicked
     * @param view the view that was clicked
     * @param index the index of the current view in the adapter's list of items
     */
    public abstract void handleClick(View view, int index);

}
