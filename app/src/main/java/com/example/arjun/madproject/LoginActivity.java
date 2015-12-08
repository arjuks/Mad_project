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

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button newAcc = (Button) findViewById(R.id.newAccBtn);
        Button login = (Button) findViewById(R.id.loginBtn);
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

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject obj, com.parse.ParseException e) {
                            if (e == null) {
                                obj.put("profilelisting", "true");
                                obj.put("pushnote","true");
                                obj.put("messageprivacy","true");
                                obj.saveInBackground();
                               // Toast.makeText(LoginActivity.this, "Profile listing is set as true", Toast.LENGTH_SHORT).show();

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
