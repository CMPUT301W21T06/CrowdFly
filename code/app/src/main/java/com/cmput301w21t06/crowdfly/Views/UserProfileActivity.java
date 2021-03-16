package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Models.User;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileActivity extends AppCompatActivity implements CrowdFlyFirestore.OnDoneGetUserListener{
    private CrowdFlyFirestore crowdFlyFirestore;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private User userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        crowdFlyFirestore = new CrowdFlyFirestore(userID);
        crowdFlyFirestore.getUserProfile(userID, this);
    }

    @Override
    public void onDoneGetUser(User user) {
        this.userProfile = user;
        TextView userIDText = findViewById(R.id.userID);
        userIDText.setText(user.getUserID());
    }
}