package com.example.arjun.madproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PrivacyActivity extends AppCompatActivity {

    String boolval = "";
    String boolvalpush = "";
    String boolvalalbum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        final TextView profiletextview = (TextView) findViewById(R.id.profiletextView);
        final TextView pushtextview = (TextView) findViewById(R.id.pushNotetextView);
        final TextView albumtextview = (TextView) findViewById(R.id.albumtextView);


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

                    if (obj.get("albumsecurity").toString().equals("true")) {
                        boolvalalbum = "true";
                        albumtextview.setText("TRUE");
                        albumtextview.setBackgroundColor(Color.GREEN);
                    } else {
                        boolvalalbum = "false";
                        albumtextview.setText("FALSE");
                        albumtextview.setBackgroundColor(Color.RED);
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

        RadioGroup rgalbum = (RadioGroup) findViewById(R.id.radioGroupAlbum);
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
                                obj.put("albumsecurity", "true");
                                boolvalalbum = "true";
                                albumtextview.setText("TRUE");
                                albumtextview.setBackgroundColor(Color.GREEN);
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
                                obj.put("albumsecurity", "false");
                                boolvalalbum = "false";
                                albumtextview.setText("FALSE");
                                albumtextview.setBackgroundColor(Color.RED);
                                Log.d("demo", "push - false");
                                obj.saveInBackground();
                            }
                        }
                    });
                }
            }
        });

    }
}
