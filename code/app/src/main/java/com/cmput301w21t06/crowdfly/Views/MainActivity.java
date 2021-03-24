package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmput301w21t06.crowdfly.Database.GodController;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This activity bridges various activites together and provides the starting screen for the user
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = "COM.CMPUT301W21T06.CROWDFLY.EDITABLE";
    private final String userID = FirebaseAuth.getInstance().getUid();
    private Button userProfileButton;
    private Button viewExperimentLogButton;
    private Button userSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GodController.allmightySetup();
        userProfileButton = (Button) findViewById(R.id.userProfileBtn);
        viewExperimentLogButton = (Button) findViewById(R.id.viewExperimentLogBtn);
        userSearch = findViewById(R.id.userSearchButton);

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra(TAG,userID);
                startActivity(intent);
            }
        });

        viewExperimentLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewExperimentLogActivity.class);
                startActivity(intent);
            }
        });

        userSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, searchUserActivity.class);
                startActivity(intent);
            }
        });

    }
}