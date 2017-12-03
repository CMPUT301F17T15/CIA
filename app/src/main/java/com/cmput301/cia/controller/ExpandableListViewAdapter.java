/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.SetUtilities;

import java.util.Set;

/**
 * @author Shipin Guan
 * @version 2.1
 *
 * Modified by Jessica Prieto on 2017/12/01
 *
 * Created by gsp on 2017/11/6.
 * Adapter for expandable list view for habit type
 * Also displaying habits as child for related habit type
 * The hard coded list are for testing purposes.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;

    // the signed in user
    private Profile user;

    // the habit categories the user has created
    private Set<String> categories;

    public ExpandableListViewAdapter(Context context, Profile user){
        this.context = context;
        this.user = user;
        refresh();
    }

    /**
     * @return the number of habit categories
     */
    @Override
    public int getGroupCount() {
        return categories.size();
    }

    /**
     * Get the number of habits in the specified category
     * @param i the index of the habit category
     * @return the number of habits in the category with the specified index
     */
    @Override
    public int getChildrenCount(int i) {
        int index = 0;
        for (String value : categories){
            if (index == i)
                return user.getHabitsInCategory(value).size();
            ++index;
        }

        throw new IndexOutOfBoundsException();
    }

    /**
     * @param i the index of the category to get
     * @return the object representing the category at the specified index
     */
    @Override
    public Object getGroup(int i) {
        return SetUtilities.getItemAtIndex(categories, i);
    }

    /**
     * @param i the index of the category type
     * @param i1 the index of the child object to get within that category
     * @return the object representing the child at the specified index
     */
    @Override
    public Object getChild(int i, int i1) {

        int index = 0;
        for (String value : categories){
            if (index == i)
                return user.getHabitsInCategory(value).get(i1).getTitle();
            ++index;
        }

        throw new IndexOutOfBoundsException();
    }

    /**
     * @param i the index of the habit category
     * @return the unique ID of the specified habit category
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }

    /**
     * @param i the index of the habit category
     * @param i1 the index of the habit within that category
     * @return the unique ID of the specified habit within that category
     */
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Get the text display for a specified habit category in the list
     * @param i the index of the habit category
     * @param b (unused)
     * @param view (unused)
     * @param parent (unused)
     * @return the view displaying the specified habit category in the list
     */
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setText((String)getGroup(i));
        textView.setPadding(100, 36, 10, 36);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        return textView;
    }

    /**
     * Get the text display for a specified habit in the list
     * @param i the index of the habit category
     * @param i1 the index of the habit within that category
     * @param b (unused)
     * @param view (unused)
     * @param viewGroup (unused)
     * @return the view displaying a specified habit in the list
     */
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final TextView textView = new TextView(context);
        textView.setText((String)getChild(i, i1));
        textView.setPadding(200, 24, 0, 24);
        textView.setTextColor(Color.DKGRAY);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        return textView;
    }

    /**
     * @param i the index of the habit category
     * @param i1 the index of the habit within that category
     * @return whether the specified object is selectable
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * Refresh the user's habit categories list
     */
    public void refresh(){
        categories = user.getHabitCategories();
    }

}
