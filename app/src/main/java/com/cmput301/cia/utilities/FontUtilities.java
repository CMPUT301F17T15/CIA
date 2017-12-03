/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.utilities;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 27, 2017
 *
 * These utilities are used to change the font of TextView and Button objects to an application
 * wide font for consistency
 */

public class FontUtilities {

    private static Typeface FONT = null;

    /**
     * Load the font the first time it is needed
     * @param activity the activity requesting the font to be set
     */
    private static void init(Activity activity){
        if (FONT == null)
            FONT = Typeface.createFromAsset(activity.getAssets() ,"fonts/Jura-Regular.ttf");
    }

    /**
     * Set a button's font to the application-wide one
     * @param view the view to apply the font to
     */
    private static void setFont(Button view){
        view.setTypeface(FONT);
        view.setTypeface(FONT, Typeface.BOLD);
    }

    /**
     * Set a TextView's font to the application-wide one
     * @param view the view to apply the font to
     */
    private static void setFont(TextView view){
        view.setTypeface(FONT);
        view.setTypeface(FONT, Typeface.BOLD);
    }

    /**
     * Apply the application-wide font to all TextView and Button views in the specified view group
     * @param activity the activity requesting the font to be set
     * @param viewGroup the view group containing all views to change the font of
     */
    public static void applyFontToViews(Activity activity, ViewGroup viewGroup){
        init(activity);
        for (int i = 0; i < viewGroup.getChildCount(); ++i){
            applyFont(viewGroup.getChildAt(i));
        }
    }

    /**
     * Apply the application-wide font to the specified view, if possible
     * @param view the view to attempt to apply the font to
     */
    private static void applyFont(View view){
        if (view instanceof TextView){
            setFont((TextView) view);
        } else if (view instanceof Button){
            setFont((Button) view);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int li = 0; li < viewGroup.getChildCount(); ++li) {
                applyFont(viewGroup.getChildAt(li));
            }
        }
    }

}
