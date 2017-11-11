/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.SetUtilities;

import java.util.Set;

/**
 * Created by gsp on 2017/11/6.
 * Adapter for expandable list view for habit type
 * Also displaying habits as child for related habit type
 * The hard coded list are for testing purposes.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Profile user;

    private Set<String> categories;

    public ExpandableListViewAdapter(Context context, Profile user){
        this.context = context;
        this.user = user;
        refresh();
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

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

    @Override
    public Object getGroup(int i) {
        return SetUtilities.getItemAtIndex(categories, i);
    }

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

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setText((String)getGroup(i));
        textView.setPadding(100, 0, 0, 0);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(30);
        return textView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final TextView textView = new TextView(context);
        textView.setText((String)getChild(i, i1));
        textView.setPadding(200, 30, 30, 0);
        textView.setTextColor(Color.DKGRAY);
        textView.setTextSize(20);

        /*textView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(context,"Habit " + textView.getText().toString() + " selected", Toast.LENGTH_SHORT).show();

            }
        });*/
        return textView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * Refresh the user's habit categories list
     */
    // TODO: call this whenever returning to the home screen
    public void refresh(){
        categories = user.getHabitCategories();
    }

}
