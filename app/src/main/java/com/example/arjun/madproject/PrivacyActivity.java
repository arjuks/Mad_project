package com.example.arjun.madproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

public class PrivacyActivity extends AppCompatActivity {

    String boolval = "";
    String boolvalpush = "";
    String boolvalmsg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        final TextView profiletextview = (TextView) findViewById(R.id.profiletextView);
        final TextView pushtextview = (TextView) findViewById(R.id.pushNotetextView);
        final TextView messagetextview = (TextView) findViewById(R.id.messagetextView);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject obj, com.parse.ParseException e) {
                if (e == null) {

                    if (obj.get("profilelisting").toString().equals("true")) {
                        boolval = "true";
                        profiletextview.setText("TRUE");
                        profiletextview.setBackgroundColor(Color.GREEN);
                    } else {
                        boolval = "false";
                        profiletextview.setText("FALSE");
                        profiletextview.setBackgroundColor(Color.RED);
                    }

                    if (obj.get("pushnote").toString().equals("true")) {
                        boolvalpush = "true";
                        pushtextview.setText("TRUE");
                        pushtextview.setBackgroundColor(Color.GREEN);
                    } else {
                        boolvalpush = "false";
                        pushtextview.setText("FALSE");
                        pushtextview.setBackgroundColor(Color.RED);
                    }

                    if (obj.get("messageprivacy").toString().equals("true")) {
                        boolvalmsg = "true";
                        messagetextview.setText("TRUE");
                        messagetextview.setBackgroundColor(Color.GREEN);
                    } else {
                        boolvalmsg = "false";
                        messagetextview.setText("FALSE");
                        messagetextview.setBackgroundColor(Color.RED);
                    }
                }
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroupPrivacy);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton r = (RadioButton) findViewById(checkedId);
                String value = r.getText().toString();//value will contain the text of the radiobutton which was checked
                Log.d("demo", value);

                if (value.equals("True")) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject obj, com.parse.ParseException e) {
                            if (e == null) {
                                obj.put("profilelisting", "true");
                                boolval = "true";
                                profiletextview.setText("TRUE");
                                profiletextview.setBackgroundColor(Color.GREEN);
                                Log.d("demo", "privacy - true");
                                obj.saveInBackground();

                            }
                        }
                    });
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject obj, com.parse.ParseException e) {
                            if (e == null) {
                                obj.put("profilelisting", "false");
                                boolval = "false";
                                profiletextview.setText("FALSE");
                                profiletextview.setBackgroundColor(Color.RED);
                                Log.d("demo", "privacy - false");
                                obj.saveInBackground();
                            }
                        }
                    });
                }
            }
        });


        RadioGroup rgpush = (RadioGroup) findViewById(R.id.radioGroupPushnote);
        rgpush.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton r = (RadioButton) findViewById(checkedId);
                String value = r.getText().toString();//value will contain the text of the radiobutton which was checked
                Log.d("demo", value);

                if (value.equals("True")) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject obj, com.parse.ParseException e) {
                            if (e == null) {
                                obj.put("pushnote", "true");
                                boolvalpush = "true";
                                pushtextview.setText("TRUE");
                                pushtextview.setBackgroundColor(Color.GREEN);
                                Log.d("demo", "push - true");
                                obj.saveInBackground();

                            }
                        }
                    });
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject obj, com.parse.ParseException e) {
                            if (e == null) {
                                obj.put("pushnote", "false");
                                boolvalpush = "false";
                                pushtextview.setText("FALSE");
                                pushtextview.setBackgroundColor(Color.RED);
                                Log.d("demo", "push - false");
                                obj.saveInBackground();
                            }
                        }
                    });
                }
            }
        });

        RadioGroup rgalbum = (RadioGroup) findViewById(R.id.radioGroupMessages);
        rgalbum.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton r = (RadioButton) findViewById(checkedId);
                String value = r.getText().toString();//value will contain the text of the radiobutton which was checked
                Log.d("demo", value);

                if (value.equals("True")) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject obj, com.parse.ParseException e) {
                            if (e == null) {
                                obj.put("messageprivacy", "true");
                                boolvalmsg = "true";
                                messagetextview.setText("TRUE");
                                messagetextview.setBackgroundColor(Color.GREEN);
                                Log.d("demo", "push - true");
                                obj.saveInBackground();

                            }
                        }
                    });
                } else {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject obj, com.parse.ParseException e) {
                            if (e == null) {
                                obj.put("messageprivacy", "false");
                                boolvalmsg = "false";
                                messagetextview.setText("FALSE");
                                messagetextview.setBackgroundColor(Color.RED);
                                Log.d("demo", "push - false");
                                obj.saveInBackground();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.homepage) {
            Log.d("demo", "homepage clicked");
            Intent intent = new Intent(PrivacyActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.logout) {
            Log.d("demo", "logout clicked");

            String objId2 = ParseInstallation.getCurrentInstallation().getObjectId();
            ParseQuery<ParseInstallation> query2 = ParseInstallation.getQuery();
            Log.d("demo", "cuser" + ParseUser.getCurrentUser());
            query2.getInBackground(objId2, new GetCallback<ParseInstallation>() {
                @Override
                public void done(ParseInstallation obj, com.parse.ParseException e) {
                    if (e == null) {
                        obj.put("user", "loggedOut");
                        obj.saveInBackground();
                    }
                }
            });

            ParseUser.logOut();
            Intent intent = new Intent(PrivacyActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
