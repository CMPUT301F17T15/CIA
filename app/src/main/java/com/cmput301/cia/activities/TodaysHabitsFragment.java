/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;

/**
 * Created by Jessica on 2017-12-01.
 */

public class TodaysHabitsFragment extends Fragment {
    public static final String ID_PROFILE = "User";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_todays_habit, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
    public static Fragment create(Profile user) {
        TodaysHabitsFragment todaysHabitsFragment = new TodaysHabitsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ID_PROFILE, user);
        todaysHabitsFragment.setArguments(args);

        return todaysHabitsFragment;
    }
}
