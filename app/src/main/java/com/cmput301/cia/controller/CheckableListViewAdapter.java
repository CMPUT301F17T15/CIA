/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cmput301.cia.models.Habit;

import java.util.List;

/**
 * Created by Adil on Nov 17 2017.
 */

public class CheckableListViewAdapter extends ArrayAdapter<Habit> {

    public CheckableListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Habit> objects) {
        super(context, resource, objects);
    }

    @Override
    public void add(@Nullable Habit object) {
        super.add(object);
    }

    @Nullable
    @Override
    public Habit getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }
}
