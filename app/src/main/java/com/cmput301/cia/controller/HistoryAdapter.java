/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.CompletedEventDisplay;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.HabitEvent;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by George on 2017-12-02.
 */

public class HistoryAdapter extends ArrayAdapter<CompletedEventDisplay> {

    public HistoryAdapter(@NonNull Context context, int resource, @NonNull List<CompletedEventDisplay> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = getListItemView(convertView, parent);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.completedHabitNameDynamic);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.completedHabitDate);

        CompletedEventDisplay habitEvent = getItem(position);
        nameTextView.setText(habitEvent.getHabitName());
        dateTextView.setText(SimpleDateFormat.getDateInstance().format(habitEvent.getCompletionDate()));

        return listItemView;
    }

    private View getListItemView(@Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView != null) {
            return convertView;
        }
        return LayoutInflater.from(getContext()).inflate(R.layout.list_history_item, parent, false);
    }

}
