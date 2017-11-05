/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;

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

    // The error message for when the user tries to create a new profile with a name that already exists
    private TextView duplicateNameText;

    // The error message for when the user tries to login with an invalid profile name
    private TextView invalidNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.loginNameEdit);
        duplicateNameText = (TextView)findViewById(R.id.loginErrorDuplicate);
        invalidNameText = (TextView)findViewById(R.id.loginErrorInvalid);
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
        duplicateNameText.setVisibility(View.INVISIBLE);

        String name = userName.getText().toString();
        Profile dummy = new Profile("dummy");

        Map<String, String> searchTerms = new HashMap<String, String>();
        searchTerms.put("name", name);
        Profile profile = ElasticSearchUtilities.getObject(dummy.getTypeId(), Profile.class, searchTerms);
        if (profile != null){
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(HomePageActivity.ID_USERNAME, name);
            startActivity(intent);
        } else {
            invalidNameText.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Handles the create profile button being clicked
     * @param view
     */
    public void onCreateProfileButtonClicked(View view){

        String name = userName.getText().toString();
        if (name.length() != 0){
            invalidNameText.setVisibility(View.INVISIBLE);
        } else {
            invalidNameText.setVisibility(View.VISIBLE);
            return;
        }

        Profile dummy = new Profile("dummy");

        Map<String, String> searchTerms = new HashMap<String, String>();
        searchTerms.put("name", name);
        Profile profile = ElasticSearchUtilities.getObject(dummy.getTypeId(), Profile.class, searchTerms);
        if (profile != null){
            duplicateNameText.setVisibility(View.VISIBLE);
        } else {
            duplicateNameText.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            intent.putExtra(HomePageActivity.ID_USERNAME, name);
            startActivity(intent);
        }

    }

}
