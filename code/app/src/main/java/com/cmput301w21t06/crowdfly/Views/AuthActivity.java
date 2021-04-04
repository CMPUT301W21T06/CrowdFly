// Portions of the code for this Activity for Anonymous User Authentication
// are Adapted from the Official Firebase Documentation
// Author: Google
// URL: https://firebase.google.com/docs/auth/android/anonymous-auth?authuser=1
// Licensed under the Apache 2.0 License

package com.cmput301w21t06.crowdfly.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301w21t06.crowdfly.Database.GodController;
import com.cmput301w21t06.crowdfly.Database.UserController;
import com.cmput301w21t06.crowdfly.Models.User;
import com.cmput301w21t06.crowdfly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Processes anonymous authentication
 */
public class AuthActivity extends AppCompatActivity {
    private FirebaseAuth authManager;
    private Button authButton;
    private TextView subtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authManager = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_auth);

        authButton = (Button) findViewById(R.id.generate);
        subtitle = (TextView) findViewById(R.id.authButtonSubtitle);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAuth();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authManager.getCurrentUser();
        if( currentUser != null){
            updateUI(currentUser);
        }
        else {
            loadingAuth();
        }

    }

    /**
     * Handles authentication for new users
     */
    private void loadingAuth() {
        authButton.setText(R.string.generating);
        authButton.setClickable(false);
        subtitle.setText(R.string.authButtonSubtitleLoading);
        authManager.signInAnonymously().addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = authManager.getCurrentUser();
                    createUser(user);
                    updateUI(user);
                } else {
                    System.out.println(task.getException());
                    Toast.makeText(AuthActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    loadingAuth();
                }


            }
        });
    }

    /**
     * Handles UI updates based on authentication
     * Either lets them into the app or asks them to press generate since the automatic id generation process failed
     * @param user
     * A firebase authenticated user
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            System.out.println(user.getUid());
            GodController.allmightySetup();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            authButton.setClickable(true);
            authButton.setText(R.string.generate);
            subtitle.setText(R.string.authButtonSubtitle);
        }


    }

    /**
     * Creates the User locally and on Firestore
     * @param user
     * The firestore authenticated user
     */
    private void createUser(FirebaseUser user) {
        String userID = user.getUid();
        User newUser = new User(userID);
        UserController.addUserProfile(newUser);
    }


}