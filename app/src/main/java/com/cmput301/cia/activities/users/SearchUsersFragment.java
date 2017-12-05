/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.TimedAdapterViewClickListener;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 13 2017
 *
 * This activity allows the user to search for other users
 */

public class SearchUsersFragment extends Fragment {

    // Intent identifiers for passed in data
    public static final String ID_USER = "User";

    // the currently signed in user
    private Profile user;

    // list of displayed users
    private ListView userList;
    private ArrayAdapter<Profile> listAdapter;
    private List<Profile> users;

    private EditText nameEditText;

    private MaterialSearchView searchView;

    public static SearchUsersFragment create(Profile currentUser) {
        SearchUsersFragment searchUsersFragment = new SearchUsersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ID_USER, currentUser);
        searchUsersFragment.setArguments(args);
        return searchUsersFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_search_users, parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().setTitle("Search");

        searchView = (MaterialSearchView) view.findViewById(R.id.search_view);

        user = (Profile) getArguments().getSerializable(ID_USER);

        userList = (ListView) view.findViewById(R.id.searchUsersList);
        nameEditText = (EditText) view.findViewById(R.id.searchNameDynamicText);

        Button search = (Button) view.findViewById(R.id.searchSearchButton);
        search.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                searchForProfiles();
                listAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item, users);
                userList.setAdapter(listAdapter);
            }
        });

        // view a profile when it is clicked
        userList.setOnItemClickListener(new TimedAdapterViewClickListener() {
            @Override
            public void handleClick(View view, int index) {
                Intent profileIntent = new Intent(getContext(), UserProfileActivity.class);
                profileIntent.putExtra(UserProfileActivity.PROFILE_ID, users.get(index));
                profileIntent.putExtra(UserProfileActivity.USER_ID, user);
                startActivity(profileIntent);
            }
        });

        users = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();
        searchForProfiles();
        listAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item, users);
        userList.setAdapter(listAdapter);
    }

    /**
     * Update the list of profiles that will be displayed on the screen
     */
    private void searchForProfiles(){
        Map<String, String> values = new HashMap<>();
        List<Profile> profiles = ElasticSearchUtilities.getListOf(Profile.TYPE_ID, Profile.class, values);

        String searchText = nameEditText.getText().toString().toLowerCase();

        //Set<String> includedNames = new HashSet<>();

        users.clear();
        for (Profile profile : profiles) {

            // note: this is only for removing invalid profiles, ignore it
            /*if (profile.getName() == null || includedNames.contains(profile.getName()) || !profile.getName().equals(profile.getName().toLowerCase())
                    || profile.getName().contains(".") || profile.getName().equals("aas")){
                ElasticSearchUtilities.delete(profile);
                continue;
            }*/

            // don't include the current user
            if (profile.equals(user))
                continue;

            // include this profile if it's name contains the search text, ignoring case
            if (profile.getName().toLowerCase().contains(searchText)) {
                users.add(profile);
                //includedNames.add(profile.getName());
            }
        }
    }

}
