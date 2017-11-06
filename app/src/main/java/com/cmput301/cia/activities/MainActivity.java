/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.cmput301.cia.utilities.SerializableUtilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Version 2
 * Author: Adil Malik
 * Date: Oct 18 2017
 *
 * The main activity of the application.
 * Allows the user to sign into their profile
 */
public class MainActivity extends AppCompatActivity {

    // The text displaying the total number of counters
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.loginNameEdit);
        SerializableUtilities.initializeFilesDir(getFilesDir().getPath());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Handles the login button being clicked
     * @param view
     */
    public void onLoginButtonClicked(View view){

        String name = userName.getText().toString();

        Map<String, String> searchTerms = new HashMap<String, String>();
        searchTerms.put("name", name);
        Profile profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, searchTerms);
        if (profile != null){
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(HomePageActivity.ID_USERNAME, name);
            startActivity(intent);
        } else {
            Toast.makeText(this, "There exists no user with that name", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Handles the create profile button being clicked
     * @param view
     */
    public void onCreateProfileButtonClicked(View view){

        String name = userName.getText().toString();
        if (name.length() == 0){
            Toast.makeText(this, "This name is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> searchTerms = new HashMap<String, String>();
        searchTerms.put("name", name);
        Profile profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, searchTerms);
        if (profile != null){
            Toast.makeText(this, "This name is already taken", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            intent.putExtra(HomePageActivity.ID_USERNAME, name);
            startActivity(intent);
        }

    }

}
