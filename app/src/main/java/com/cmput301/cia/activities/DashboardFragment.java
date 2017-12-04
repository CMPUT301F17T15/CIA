/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;


/**
 * @author Jessica Prieto
 *
 * version 1
 *
 * This is a fragment that displayes a tabbed activity: one tab for all habits and one tab
 * for viewing today's tasks
 */
public class DashboardFragment extends Fragment {
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

    /**
     * sets up the adapter to properly display the tabs fragment: habits list and today's habit tasks
     */
    private void setUpViewPager() {
        dashboardTabsAdapter = new DashboardTabsAdapter(getChildFragmentManager());
        viewPager.setAdapter(dashboardTabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * updates the data changes in the habits so that the data changes is reflected on the dashboard activity
     */
    public void updateHabits() {
        Fragment habitsFragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 0);
        if (habitsFragment instanceof HabitsFragment) {
            ((HabitsFragment) habitsFragment).updateAllHabits();
        }

        Fragment todaysHabitsFragment = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 1);
        if (todaysHabitsFragment instanceof TodaysHabitsFragment) {
            ((TodaysHabitsFragment) todaysHabitsFragment).updateTodaysList(user.getTodaysHabits());
            ((TodaysHabitsFragment) todaysHabitsFragment).checkCompletedEvents();
        }

    }

    /**
     * chooses between the two tab views:
     *
     * @param index 0: habits list, 1: today's tasks
     * @return the chosen view depending on the index
     */
    public View getTabView(int index) {
        return ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(index);
    }

    /**
     * fetches the dashboard activity of the current user logged in
     * @param user the user logged in
     * @return the dashboard activity of the current user
     */
    public static DashboardFragment create(Profile user) {
        DashboardFragment dashboardFragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ID_PROFILE, user);
        dashboardFragment.setArguments(args);
        return dashboardFragment;
    }

    /**
     * class for selecting the Fragment to be displayed
     */
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
