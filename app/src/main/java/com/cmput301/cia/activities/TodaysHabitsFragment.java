/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.CheckableListViewAdapter;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;

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

    /**
     * Update the checkable list view adapter for the "today's tasks" list
     */
    private void resetCheckableListAdapter(){
        checkableAdapter = new CheckableListViewAdapter(getContext(), R.layout.checkable_list_view, R.id.CheckedTextView, todaysHabits);
        checkable.setAdapter(checkableAdapter);
    }
}
