package com.cmput301.cia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The main activity of the application.
 * Keeps track of all counters the user has created and displays them as a list.
 * It also passes information to other activities and handles their results.
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
        // TODO: if valid name
        if (true){
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
        // TODO: if used name
        if (true){
            duplicateNameText.setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra(HomePageActivity.ID_USERNAME, name);
            startActivity(intent);
            duplicateNameText.setVisibility(View.INVISIBLE);
        }

    }

}
