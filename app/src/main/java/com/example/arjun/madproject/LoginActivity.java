package com.example.arjun.madproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.parse.LogInCallback;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        CallbackManager callbackManager = CallbackManager.Factory.create();

        Button newAcc = (Button) findViewById(R.id.newAccBtn);
        Button login = (Button) findViewById(R.id.loginBtn);
        final EditText firstname = (EditText) findViewById(R.id.firstname);
        final EditText password = (EditText) findViewById(R.id.passwordField);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstname.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Please fill in the above details", Toast.LENGTH_SHORT).show();
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

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("demo", "logged in using facebook");
                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Log.d("demo", "error" + e.getMessage());
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
