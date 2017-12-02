/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.CreateHabitEventActivity;
import com.cmput301.cia.controller.CheckableListViewAdapter;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;

import java.util.Date;
import java.util.List;

/**
 * Created by Jessica on 2017-12-01.
 */

public class TodaysHabitsFragment extends Fragment {
    public static final String ID_PROFILE = "User";
    // the habits the user must do today
    private List<Habit> todaysHabits;
    private Profile user;
    private ListView checkable;
    private CheckableListViewAdapter checkableAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_todays_habit, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        user = (Profile) getArguments().getSerializable(ID_PROFILE);
        todaysHabits = user.getTodaysHabits();

        // today's tasks listview (checkable)
        checkable = (ListView) view.findViewById(R.id.TodayToDoListView);
        checkable.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        resetCheckableListAdapter();
    }
    public static Fragment create(Profile user) {
        TodaysHabitsFragment todaysHabitsFragment = new TodaysHabitsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ID_PROFILE, user);
        todaysHabitsFragment.setArguments(args);

        return todaysHabitsFragment;
    }

    public void updateTodaysList() {
        todaysHabits = user.getTodaysHabits();
        resetCheckableListAdapter();
    }

    /**
     * Update the checkable list view adapter for the "today's tasks" list
     */
    private void resetCheckableListAdapter(){
        checkableAdapter = new CheckableListViewAdapter(getContext(), R.layout.checkable_list_view, R.id.CheckedTextView, todaysHabits);
        checkable.setAdapter(checkableAdapter);
    }

    /**
     * Automatically check all habits that the user has completed today
     */
    public void checkCompletedEvents(){
        // reset the listener so that a new event is not created
        checkable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        // automatically check the events that have already been completed today
        for (int index = 0; index < todaysHabits.size(); ++index) {
            if (DateUtilities.isSameDay(todaysHabits.get(index).getLastCompletionDate(), new Date())) {
                checkable.performItemClick(checkableAdapter.getView(index, null, null), index, checkableAdapter.getItemId(index));
            }
        }

        // set the listener to handle event completions
        checkable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // This item is already clicked, prevent it from being disabled
                if (DateUtilities.isSameDay(todaysHabits.get(i).getLastCompletionDate(), new Date())){
                    return;
                }

                // start the activity to create a habit event for this completion
                String checkedItems = ((TextView)view).getText().toString();
                String habitId = todaysHabits.get(i).getId();

                startCreateEventActivity(checkedItems, habitId);
            }
        });
    }

    public void startCreateEventActivity(String completion, String habitId) {
        Activity activity = getActivity();
        if (activity instanceof HomeTabbedActivity) {
            ((HomeTabbedActivity) activity).onHabitClicked(completion, habitId);
        }
    }
}
