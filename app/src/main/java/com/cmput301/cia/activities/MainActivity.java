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

        // TODO: query using name
        Profile profile = ElasticSearchUtilities.getObject(dummy.getTypeId(), Profile.class, "");
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
        invalidNameText.setVisibility(View.INVISIBLE);

        String name = userName.getText().toString();
        Profile dummy = new Profile("dummy");

        // TODO: query using name
        Profile profile = ElasticSearchUtilities.getObject(dummy.getTypeId(), Profile.class, "");
        if (profile != null){
            duplicateNameText.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(HomePageActivity.ID_USERNAME, name);
            startActivity(intent);
            duplicateNameText.setVisibility(View.INVISIBLE);
        }

    }

}
