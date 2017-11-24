/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cmput301.cia.models.Habit;
import com.cmput301.cia.utilities.DateUtilities;

import java.util.Date;
import java.util.List;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 23 2017
 *
 * This class represents the ListView adapter for the "today's tasks" checkable list
 * It is created to prevent events from being unchecked after they have been completed
 */

public class CheckableListViewAdapter extends ArrayAdapter<Habit> {

    public CheckableListViewAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<Habit> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    /**
     * Add a new habit into the list
     * @param object the habit to add
     */
    @Override
    public void add(@Nullable Habit object) {
        super.add(object);
    }

    /**
     * @param position the index of the habit to return
     * @return the habit at the specified index
     */
    @Nullable
    @Override
    public Habit getItem(int position) {
        return super.getItem(position);
    }

    /**
     *
     * @param position the index of the habit to get
     * @param convertView (unused, use null)
     * @param parent (unused, use null)
     * @return the displayed view for  the specified habit
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    /**
     * @return whether any item can be clicked or not in the adapter's list
     */
    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    /**
     * @param position the index of the item to check
     * @return whether the specified item can be clicked or not
     */
    @Override
    public boolean isEnabled(int position) {
        return !DateUtilities.isSameDay(getItem(position).getLastCompletionDate(), new Date());//super.isEnabled(position);
    }
}
