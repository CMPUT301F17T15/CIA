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
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.habits.HabitViewActivity;
import com.cmput301.cia.controller.ExpandableListViewAdapter;
import com.cmput301.cia.models.Habit;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.DateUtilities;
import com.cmput301.cia.utilities.SetUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author Jessica Prieto
 * Created on 2017-12-01
 *
 * originally from HomePageActivity
 * @author Adil Malik, Shipin Guan
 * @version 6
 * Date: Nov 12 2017
 *
 * version 1
 *
 * This fragment displays an extendable list of habits
 */

public class HabitsFragment extends Fragment {
    public static final String ID_PROFILE = "User";
    private Profile user;

    private ExpandableListView expandableListView;
    private ExpandableListViewAdapter adapter;
    private TextView noHabits;

    /**
     * a helper for starting the fragment (similar to starting a new Intent)
     *
     * @param user the current user logged-in
     * @return HabitsFragment
     */
    public static Fragment create(Profile user) {
        HabitsFragment habitsFragment = new HabitsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ID_PROFILE, user);
        habitsFragment.setArguments(args);

        return habitsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_habits, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        user = (Profile) getArguments().getSerializable(ID_PROFILE);
        noHabits = (TextView) view.findViewById(R.id.noHabits);
        if (user.hasValidId())
            user.load();

        if (user.getHabits().size() > 0) {
            noHabits.setVisibility(View.GONE);
        }

        user.synchronize();

        // handle any habits that may have been missed since the user's last login
        Date currentDate = new Date();
        if (user.getLastLogin() != null && !DateUtilities.isSameDay(user.getLastLogin(), currentDate)) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(user.getLastLogin());

            // go through each date between the user's last login and the current date
            while (!DateUtilities.isSameDay(calendar.getTime(), currentDate)){
                // update all events at the end of that date, to make sure they are marked as missed if they weren't completed
                // on that day
                user.onDayEnd(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
        }

        user.setLastLogin(currentDate);
        user.save();

        // initialize the list displaying all habits the user has
        expandableListView = (ExpandableListView) view.findViewById(R.id.HabitTypeExpandableListView);
        adapter = new ExpandableListViewAdapter(getContext(), user);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int group, int child, long childRowId) {

                String category = SetUtilities.getItemAtIndex(user.getHabitCategories(), group);
                Habit habit = user.getHabitsInCategory(category).get(child);

                ArrayList<String> types = new ArrayList<>();
                types.addAll(user.getHabitCategories());

                startDetailsActivity(habit, types);

                return false;
            }
        });

    }

    /**
     * updates the view on data changes
     */
    public void updateAllHabits() {
        adapter = new ExpandableListViewAdapter(getContext(), user);
        expandableListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * starts CreateHabitEvent whenever a user completes a task
     * @param habit
     * @param categories
     */
    public void startDetailsActivity(Habit habit, ArrayList<String> categories) {
        Activity activity = getActivity();
        if (activity instanceof HomeTabbedActivity) {
            ((HomeTabbedActivity) activity).onStartHabitDetails(habit, categories);
        }
    }
}
