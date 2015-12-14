package com.example.arjun.madproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;



public class LoginActivity extends Activity {

    ProgressDialog progressDialog;

    public static final List<String> mPermissions = new ArrayList<String>(){{
        add("public_profile");
        add("email");
    }};

    EditText username = null;
    EditText password = null;
    String emailId = null , userNameText = null , gender = null;
    final static String NAME = "name";
    final static String SOCIALNET = "socialnet";



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        Log.d("demo", "fb data" + data.getData());
//        progressDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(
                    "com.example.arjun.madproject",
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (Signature signature : info.signatures) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }

        Button newAcc = (Button) findViewById(R.id.newAccBtn);
        Button login = (Button) findViewById(R.id.loginBtn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.passwordField);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "PLease fill in the above details", Toast.LENGTH_SHORT).show();
                }
                else {
                    ParseUser.logInInBackground(username.getText().toString().trim(), password.getText().toString().trim(), new LogInCallback() {
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

//                    String objId2 = ParseInstallation.getCurrentInstallation().getObjectId();
//                    ParseQuery<ParseInstallation> query2 = ParseInstallation.getQuery();
//                    Log.d("demo", "cuser"+ParseUser.getCurrentUser());
//                    query2.getInBackground(objId2, new GetCallback<ParseInstallation>() {
//                        @Override
//                        public void done(ParseInstallation obj, com.parse.ParseException e) {
//                            if (e == null) {
//                                obj.put("user", ParseUser.getCurrentUser().getEmail().toString());
//                                obj.saveInBackground();
//                                Toast.makeText(LoginActivity.this, "saved", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });



                }
            }
        });

        Button submitButton = (Button) findViewById(R.id.facebooklogin);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, mPermissions, new LogInCallback() {

                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                       // progressDialog.show();
                        if (parseUser == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (parseUser.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            getUserDetailsFromFB();
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            //getUserDetailsFromParse();
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        Button twitter = (Button) findViewById(R.id.twitterlogin);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseTwitterUtils.logIn(LoginActivity.this, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, com.parse.ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                            //Toast.makeText(LoginActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Twitter!");
                            getUserDetailsFromTwitter();

                        } else {
                            Log.d("MyApp", "User logged in through Twitter!");
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        newAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getUserDetailsFromTwitter() {
        String name = ParseTwitterUtils.getTwitter().getScreenName().toString();
        Intent intent = new Intent(LoginActivity.this, Twitterlogin.class);
        intent.putExtra(NAME , name);
        startActivity(intent);
        finish();
    }


    private void getUserDetailsFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                try {
                    userNameText = jsonObject.getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    emailId = jsonObject.getString("email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                saveNewUser();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void saveNewUser() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.setUsername(emailId);
        parseUser.setEmail(emailId);

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Intent intent = new Intent(LoginActivity.this, Facebooklogin.class);
                intent.putExtra(NAME , userNameText);
                startActivity(intent);
                finish();
            }
        });


    }
}
