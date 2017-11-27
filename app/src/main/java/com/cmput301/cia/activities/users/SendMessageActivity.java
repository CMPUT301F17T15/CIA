/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.HomePageActivity;
import com.cmput301.cia.models.HabitEvent;
import com.cmput301.cia.models.Profile;


public class SendMessageActivity extends AppCompatActivity {
    private EditText message_will_send;
    private String user;
    private int position;
    private Profile host_user;
    private HabitEvent event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_send_message);
//        Intent intent = getIntent();
//
//
//        this.user = (Profile) intent.getSerializableExtra(USER_ID);
//        this.position = intent.getExtras().getInt("pos");
//        this.host_user = (Profile) intent.getSerializableExtra(SearchUsersActivity.ID_USER);
//
//        this.event = host_user.getFollowedHabitHistory().get(position).first;
//
//        TextView message=(TextView)findViewById(R.id.name);
//        message.setText("Send Message to "+user);
//
//
//        Button sendButton = (Button) findViewById(R.id.send);
//        this.message_will_send = (EditText) findViewById(R.id.body1);
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String message_string = message_will_send.getText().toString();
//                event.setMessage(message_string);
//            }
//        });

    }
}
