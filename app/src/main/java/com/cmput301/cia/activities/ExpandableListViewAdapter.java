/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by gsp on 2017/11/6.
 * Adapter for expandable list view for habit type
 * Also displaying habits as child for related habit type
 * The hard coded list are for testing purposes.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    //Those data should be obtained from java serialization
    String[] habitTypes = {"Fitness", "Thankful", "Diet"};
    String[][] habits = {{"10km Running", "100 push-up", "100 sit-up", "100 squats"},{"Happiness"},
            {"No burger", "No coke", "No frise"}};
    Context context;
    public ExpandableListViewAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return habitTypes.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return habits[i].length;
    }

    @Override
    public Object getGroup(int i) {
        return habitTypes[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return habits[i][i1];  //i = habitType position, i1 = habits position
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
        textView.setText(habitTypes[i]);
        textView.setPadding(100, 0, 0, 0);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(30);
        return textView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final TextView textView = new TextView(context);
        textView.setText(habits[i][i1]);
        textView.setPadding(150, 0, 0, 0);
        textView.setTextColor(Color.DKGRAY);
        textView.setTextSize(20);

        textView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(context, textView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return textView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
