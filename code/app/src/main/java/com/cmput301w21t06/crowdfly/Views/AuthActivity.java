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
import android.widget.Toast;

import com.cmput301w21t06.crowdfly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {
    private FirebaseAuth authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authManager = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_auth);

        Button loginButton = (Button) findViewById(R.id.authButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authManager.signInAnonymously().addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = authManager.getCurrentUser();

                            updateUI(user);
                        } else {
                            System.out.println(task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }


                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = authManager.getCurrentUser();
        updateUI(currentUser);

    }



    public void updateUI(FirebaseUser user){
        if(user !=null){
            System.out.println(user.getUid());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }



}