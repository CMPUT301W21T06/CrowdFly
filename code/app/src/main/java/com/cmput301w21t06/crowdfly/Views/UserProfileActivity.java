package com.cmput301w21t06.crowdfly.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cmput301w21t06.crowdfly.Database.CrowdFlyListeners;
import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.Models.User;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This activity allows the user to see and edit their profile
 */
public class UserProfileActivity extends AppCompatActivity implements CrowdFlyListeners.OnDoneGetUserListener, NavigationView.OnNavigationItemSelectedListener {
    final private String userID = FirebaseAuth.getInstance().getUid();
    private User user;
    private TextView userIDText;
    private EditText userInfo;
    private Button doneButton;
    private ImageView profilePicView;
    String requestedID;
    private final String TAG = "COM.CMPUT301W21T06.CROWDFLY.EDITABLE";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

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
        //profilePicView = findViewById(R.id.profilePic);
        doneButton.setOnClickListener(doneListener);
        userInfo.setEnabled(canEdit());
        UserController.getUserProfile(requestedID, this);
        //PictureHandler.updatePic("gs://crowdfly-76eb6.appspot.com/smiley.png", profilePicView, getApplicationContext());


        drawerLayout = findViewById(R.id.drawer_profile);
        navigationView = findViewById(R.id.nav_view_profile);
        toolbar = findViewById(R.id.toolbar_profile);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserId = (TextView) headerView.findViewById(R.id.userFBID);
        navUserId.setText(userID);
    }

    /**
     * This handles setting textboxes after user info has been pulled from the database
     * @param userProfile
     * The instantiated user with information from the database
     */
    @Override
    public void onDoneGetUser(User userProfile) {
        user = userProfile;
        userIDText.setText(user.getDisplayID());
        userInfo.setText(user.getContactInfo());
    }

    /**
     * This stores the information into firestore and goes back to the past activity once the user presses the done button
     */
    private void handleDone(){
        user.setContactInfo(String.valueOf(userInfo.getText()));
        UserController.setUserProfile(user);
        goBack();
    }

    /**
     * This handles going back to the past activity
     */
    private void goBack(){
        finish();
    }

    /**
     * This checks if the user profile should be opened in edit or view mode
     * @return
     * This returns a boolean indicating if it is editable
     */
    private boolean canEdit(){
        Intent intent = getIntent();
        requestedID = intent.getStringExtra(TAG);
        boolean condition =  (UserController.reverseConvert(userID).matches(requestedID));
        return condition;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){

        switch (item.getItemId()){

            case R.id.hamHome:
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            case R.id.hamAccount:
                break;
            case R.id.hamExperiment:
                break;

        }
        return true;
    }
}