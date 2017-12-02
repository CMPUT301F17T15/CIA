/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301.cia.R;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;

public class DashboardActivity extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private Profile user;

    public static final String ID_PROFILE = "User";

    private DashboardTabsAdapter dashboardTabsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dashboard, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Habits");

        user = (Profile) getArguments().getSerializable(ID_PROFILE);

        viewPager = (ViewPager) view.findViewById(R.id.container);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        setUpViewPager();
    }

    private void setUpViewPager() {
        dashboardTabsAdapter = new DashboardTabsAdapter(getChildFragmentManager());
        viewPager.setAdapter(dashboardTabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void updateHabits() {
        Fragment habitsFragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 0);
        if (habitsFragment instanceof HabitsFragment) {
            ((HabitsFragment) habitsFragment).updateAllHabits();
        }

        Fragment todaysHabitsFragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 1);
        if (todaysHabitsFragment instanceof TodaysHabitsFragment) {
            ((TodaysHabitsFragment) todaysHabitsFragment).updateTodaysList();
        }

    }

    public static DashboardActivity create(Profile user) {
        DashboardActivity dashboardActivity = new DashboardActivity();
        Bundle args = new Bundle();
        args.putSerializable(ID_PROFILE, user);
        dashboardActivity.setArguments(args);
        return dashboardActivity;
    }

    public class DashboardTabsAdapter extends FragmentPagerAdapter {

        public DashboardTabsAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return HabitsFragment.create(user);
                case 1: return TodaysHabitsFragment.create(user);
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.habits);
                case 1: return getString(R.string.todays_task);
                default: return null;
            }
        }
    }
}
