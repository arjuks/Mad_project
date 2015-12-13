package com.example.arjun.madproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Collection;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else {
            setContentView(R.layout.activity_login);
            Log.d("demo", "in login act");
            Button newAcc = (Button) findViewById(R.id.newAccBtn);
            Button login = (Button) findViewById(R.id.loginBtn);
            Button facebookLogin = (Button) findViewById(R.id.facebookLogin);
            final EditText firstname = (EditText) findViewById(R.id.firstname);
            final EditText password = (EditText) findViewById(R.id.passwordField);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(firstname.getText().toString().equals("") || password.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "PLease fill in the above details", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ParseUser.logInInBackground(firstname.getText().toString().trim(), password.getText().toString().trim(), new LogInCallback() {
                            public void done(ParseUser user, com.parse.ParseException e) {
                                if (user != null) {
                                    // Hooray! The user is logged in.
                                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Signup failed. Look at the ParseException to see what happened.
                                    Log.d("demo", "error" + e.getMessage());
                                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

            newAcc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    public void loginFacebook(View v) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, null, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                Log.d("demo", "in login with read permissions");
                if(err == null) {
                    if (user == null) {
                        Log.d("demo", "Uh oh. The user cancelled the Facebook login.");
                    } else if (user.isNew()) {
                        Log.d("demo", "user is new");
                        String email = user.getEmail();
                        Log.d("demo", "user email: " + email);
                        String username = user.getUsername();
                        Log.d("demo", "username: " + username);
                        String password = user.getSessionToken();

                        Log.d("demo", "User signed up and logged in through Facebook!");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Log.d("demo", "user is not new");
                        Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_LONG).show();
                        Log.d("demo", "User logged in through Facebook!");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                } else {
                    Log.d("demo", "SERIOUS ERROR");
                    err.printStackTrace();
                }

            }
        });
    }

    public void loginTwitter(View v) {
    }

    public boolean isLoggedIn() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null)
            return true;
        return false;
    }
}
