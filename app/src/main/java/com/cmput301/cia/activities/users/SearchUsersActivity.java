/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Adil Malik
 * @version 1
 * Date: Nov 13 2017
 *
 * This activity allows the user to search for other users
 */

public class SearchUsersActivity extends AppCompatActivity {

    // Intent identifiers for passed in data
    public static final String ID_USER = "User";

    // Return identifiers for the activity result
    public static final String RETURNED_PROFILE = "Followed";

    // Result codes for other activities
    private static final int VIEW_PROFILE = 0;

    // the currently signed in user
    private Profile user;

    // list of displayed users
    private ListView userList;
    private ArrayAdapter<Profile> listAdapter;
    private List<Profile> users;

    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        user = (Profile) getIntent().getSerializableExtra(ID_USER);

        userList = (ListView)findViewById(R.id.searchUsersList);
        nameEditText = (EditText)findViewById(R.id.searchNameDynamicText);

        Button search = (Button)findViewById(R.id.searchSearchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForProfiles();
                listAdapter = new ArrayAdapter<>(SearchUsersActivity.this, R.layout.list_item, users);
                userList.setAdapter(listAdapter);
            }
        });

        // view a profile when it is clicked
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent profileIntent = new Intent(SearchUsersActivity.this, UserProfileActivity.class);
                profileIntent.putExtra(UserProfileActivity.PROFILE_ID, users.get(position));
                profileIntent.putExtra(UserProfileActivity.USER_ID, user);
                startActivityForResult(profileIntent, VIEW_PROFILE);
            }
        });

        users = new ArrayList<>();
    }

    /**
     * Handle the back button being pressed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(RETURNED_PROFILE, user);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchForProfiles();
        listAdapter = new ArrayAdapter<>(this, R.layout.list_item, users);
        userList.setAdapter(listAdapter);
    }

    /**
     * Handle the results of an activity that has finished
     * @param requestCode the activity's identifying code
     * @param resultCode the result status of the finished activity
     * @param data the activity's returned intent information
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // When a new habit event is created
        if (requestCode == VIEW_PROFILE) {
            if (resultCode == RESULT_OK) {
                Profile newProfile = (Profile) data.getSerializableExtra(UserProfileActivity.RESULT_PROFILE_ID);
                user.copyFrom(newProfile);
            }
        }
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
            /*if (profile.getName() == null || includedNames.contains(profile.getName()) || !profile.getName().equals(profile.getName().toLowerCase())){
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
