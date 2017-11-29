/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cmput301.cia.R;
import com.cmput301.cia.controller.ButtonClickListener;
import com.cmput301.cia.models.Profile;
import com.cmput301.cia.utilities.ElasticSearchUtilities;
import com.cmput301.cia.utilities.FontUtilities;
import com.cmput301.cia.utilities.SerializableUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adil Malik
 * @version 3
 * Date: Nov 17, 2017
 *
 * The main activity of the application.
 * Allows the user to sign into their profile
 */
public class MainActivity extends AppCompatActivity {

    // The view where the user enters their name
    private EditText userName;
    private VideoView backGround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        backGround = (VideoView) findViewById(R.id.backGround);
        userName = (EditText) findViewById(R.id.loginNameEdit);
        SerializableUtilities.initializeFilesDir(getFilesDir().getPath());

        //setting the video in raw as background.
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cia);
        backGround.setVideoURI(uri);
        backGround.start();
        backGround.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        Button login = (Button)findViewById(R.id.loginButton);
        login.setOnClickListener(new ButtonClickListener() {
            @Override
            public void handleClick() {
                String name = userName.getText().toString().toLowerCase();

                // Attempt to search for a profile with the selected name
                Map<String, String> searchTerms = new HashMap<>();
                searchTerms.put("name", name);
                Pair<Profile, Boolean> profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, searchTerms);

                // Profile found -> sign in
                if (profile.first != null && profile.second){
                    Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                    intent.putExtra(HomePageActivity.ID_PROFILE, profile.first);
                    startActivity(intent);
                } else if (!profile.second){
                    Toast.makeText(MainActivity.this, "Could not connect to the database.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "There exists no user with that name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button register = (Button)findViewById(R.id.newProfileButton);
        register.setOnClickListener(new ButtonClickListener() {
            @Override
            public void handleClick() {
                String name = userName.getText().toString().toLowerCase();
                if (name.length() == 0){
                    Toast.makeText(MainActivity.this, "The profile name can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Attempt to search for a profile with the selected name
                Map<String, String> searchTerms = new HashMap<>();
                searchTerms.put("name", name);
                Pair<Profile, Boolean> profile = ElasticSearchUtilities.getObject(Profile.TYPE_ID, Profile.class, searchTerms);

                // Found, can't create an account with same name
                if (profile.first != null){
                    Toast.makeText(MainActivity.this, "This name is already taken. Names are case insensitive", Toast.LENGTH_SHORT).show();
                } else if (!profile.second){
                    Toast.makeText(MainActivity.this, "Could not connect to the database.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // No profile found, sign the user in with their new account
                    Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                    intent.putExtra(HomePageActivity.ID_PROFILE, new Profile(name));
                    startActivity(intent);
                }
            }
        });

        FontUtilities.applyFontToViews(this, (ViewGroup)findViewById(R.id.loginPageLayout));
    }

    //play video when back to login page.
    @Override
    protected void onResume(){
        super.onResume();
        backGround.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
