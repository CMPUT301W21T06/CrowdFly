package com.cmput301w21t06.crowdfly.Views;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.cmput301w21t06.crowdfly.Database.CrowdFlyFirestore;
import com.cmput301w21t06.crowdfly.Models.User;
import com.cmput301w21t06.crowdfly.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

public class UserProfileActivity extends AppCompatActivity implements CrowdFlyFirestore.OnDoneGetUserListener, CrowdFlyFirestore.OnDoneGetProfilePicListener {
    private CrowdFlyFirestore crowdFlyFirestore;
    final private String userID = FirebaseAuth.getInstance().getUid();
    private User user;
    private TextView userIDText;
    private EditText userInfo;
    private Button doneButton;
    private ImageView profilePicView;
    String requestedID;
    private final String TAG = "COM.CMPUT301W21T06.CROWDFLY.EDITABLE";

    private View.OnClickListener doneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            handleDone();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        doneButton = findViewById(R.id.doneButton);
        userIDText = findViewById(R.id.userID);
        userInfo = findViewById(R.id.userInfo);
        profilePicView = findViewById(R.id.profilePic);
        doneButton.setOnClickListener(doneListener);
        userInfo.setEnabled(canEdit());
        crowdFlyFirestore = new CrowdFlyFirestore();
        crowdFlyFirestore.getUserProfile(requestedID, this);
        crowdFlyFirestore.getProfilePic(this);


    }

    @Override
    public void onDoneGetUser(User userProfile) {
        user = userProfile;
        userIDText.setText(user.getUserID());
        userInfo.setText(user.getContactInfo());
    }

    @Override
    public void onDoneGetProfilePic(StorageReference pic) {
        Glide.with(this)
                .load(pic)
                .fitCenter()
                .into(profilePicView);

    }

    private void handleDone(){
        user.setContactInfo(String.valueOf(userInfo.getText()));
        crowdFlyFirestore.setUserProfile(user);
        goBack();
    }
    private void goBack(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    private boolean canEdit(){
        Intent intent = getIntent();
        requestedID = intent.getStringExtra(TAG);
        boolean condition =  (userID.matches(requestedID));
        return condition;
    }

}