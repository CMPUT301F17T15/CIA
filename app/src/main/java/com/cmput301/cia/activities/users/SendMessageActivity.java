/*
 * Copyright (c) 2017 CMPUT301F17T15. This project is distributed under the MIT license.
 */

package com.cmput301.cia.activities.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cmput301.cia.R;
import com.cmput301.cia.activities.events.FilterHabitsActivity;
import com.cmput301.cia.activities.events.HistoryActivity;
import com.cmput301.cia.controller.TimedClickListener;
import com.cmput301.cia.models.Message;
import com.cmput301.cia.models.Profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SendMessageActivity extends AppCompatActivity {

    public static final String PROFILE_ID = "Profile", USER_ID = "User";

    // the profile being displayed
    private Profile displayed;
    // the currently signed in user
    private Profile viewer;

    private List<Message> allMessage;

    private Message messageClass;

    private EditText newMessage;

    private ListView showMessage;

    private Button sendMessage;

    private ArrayAdapter<String> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Intent intent = getIntent();
        displayed = (Profile) intent.getSerializableExtra(PROFILE_ID);
        viewer = (Profile) intent.getSerializableExtra(USER_ID);

        this.allMessage= viewer.getAllMessage();

        newMessage = (EditText) findViewById(R.id.editMessage);
        showMessage = (ListView) findViewById(R.id.messageList);
        sendMessage = (Button) findViewById(R.id.sendButton);

        for (int j = 0; j < allMessage.size(); j++){
            if (allMessage.get(j).getViewer().equals(viewer.getName()) && allMessage.get(j).getDisplayed().equals(displayed.getName())){
                this.messageClass = allMessage.get(j);
            }
        }
        if (messageClass == null) {
            Message newMessageItem = new Message();

            newMessageItem.setDisplayed(displayed.getName());
            newMessageItem.setViewer(viewer.getName());

            this.messageClass = newMessageItem;

            viewer.addNewMessage(newMessageItem);
            displayed.addNewMessage(newMessageItem);

            viewer.save();
            displayed.save();
        }

        sendMessage.setOnClickListener(new TimedClickListener() {
            @Override
            public void handleClick() {
                messageClass.addMessage(viewer.getName()+" said: "+newMessage.getText().toString());
                viewer.save();
                displayed.save();
                adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        List<String> specificMessage = messageClass.getMessageList();
        this.adapter = new ArrayAdapter<>(this, R.layout.list_item, specificMessage);
        showMessage.setAdapter(adapter);
        setContentView(showMessage);
    }
}