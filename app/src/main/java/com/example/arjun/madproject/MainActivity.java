package com.example.arjun.madproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<ParseUser> userlist = new ArrayList<>();
    int lsize ;
    AlertDialog simpleA = null;
    static String semail = null;

    final static String FULLNAME = "fullname";
    final static String RNAME = "rname";
    final static String SEMAIL = "semail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        Button inbox = (Button) findViewById(R.id.inboxBtn);

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                userlist.addAll(objects);
                ParseUser currentUser = ParseUser.getCurrentUser();

                for (int i = 0; i < userlist.size(); i++) {
                    if( userlist.get(i).getEmail().toString().equals(currentUser.getEmail().toString())) {
                        semail = userlist.get(i).getEmail().toString();
                        Log.d("demo","semail main"+semail);
                        userlist.remove(i);
                    }
                }

                for (int i = 0; i < userlist.size(); i++) {
                    if(userlist.get(i).get("messageprivacy").toString().equals("false")) {
                        userlist.remove(i);
                    }
                }
                
                lsize = userlist.size();
                final CharSequence[] items = new CharSequence[lsize];

                for (int i = 0; i < userlist.size(); i++) {
                    String fullname = userlist.get(i).getUsername().toString()+" "+ userlist.get(i).get("Lastname");
                    items[i] = fullname;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a Contact")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("demo", "selected" + " " + items[which]);
                            String fullname = items[which].toString();
                            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                            intent.putExtra(FULLNAME, fullname);
                            intent.putExtra(SEMAIL, semail);
                            startActivity(intent);
                        }
                    });
                simpleA = builder.create();
            }
        });

        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "share clicked");
                simpleA.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.inboxMsg) {
            Log.d("demo", "inbox clicked");
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(intent);

            Log.d("demo","obj id"+ParseInstallation.getCurrentInstallation().getObjectId());
            Log.d("demo", "ins id" + ParseInstallation.getCurrentInstallation().getInstallationId());
           // Log.d("demo","obj id"+ParseInstallation.getCurrentInstallation().get);

            String objId = ParseInstallation.getCurrentInstallation().getObjectId();
           // ParsePush.subscribeInBackground("Coding");

            ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
            Log.d("demo", "cuser" + ParseUser.getCurrentUser());

            query.getInBackground(objId, new GetCallback<ParseInstallation>() {
                @Override
                public void done(ParseInstallation obj, com.parse.ParseException e) {
                    if (e == null) {
                        obj.put("user", ParseUser.getCurrentUser().getEmail());
                        obj.saveInBackground();
                        Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Find devices associated with these users


//            ParseQuery<ParseObject> query = ParseQuery.getQuery("_Installation");
//            query.findInBackground(new FindCallback<ParseObject>() {
//                public void done(final List<ParseObject> objects, com.parse.ParseException e) {
//                    if (e == null) {
//
//                        Log.d("demo", "object id" + objects.get(0).getObjectId().toString());
//
//
//                    } else {
//                        Log.d("score", "Error: " + e.getMessage());
//                    }
//                }
//            });


            return true;
        }
        if (id == R.id.editprofile) {
            Log.d("demo", "edit clicked");
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        }

        if (id == R.id.privacysetting) {
            Log.d("demo", "edit clicked");
            Intent intent = new Intent(MainActivity.this, PrivacyActivity.class);
            startActivity(intent);
        }

        if(id == R.id.userlist){
            Log.d("demo", "compose clicked");
            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
            startActivity(intent);
        }

//        if(id == R.id.pushnoti){
//            Log.d("demo", "push clicked");
//
//            ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
//            Log.d("demo", "cuser" + ParseUser.getCurrentUser());
//            String objId = ParseInstallation.getCurrentInstallation().getObjectId();
//
//            query.getInBackground(objId, new GetCallback<ParseInstallation>() {
//                @Override
//                public void done(ParseInstallation obj, com.parse.ParseException e) {
//                    if (e == null) {
//                        if (obj.get("user").toString().equals(ParseUser.getCurrentUser().getEmail().toString())) {
//
//                            ParseQuery pushQuery = ParseInstallation.getQuery();
//                            pushQuery.whereEqualTo("user", obj.get("user").toString()); //
//
//                            ParsePush push = new ParsePush();
//                            push.setQuery(pushQuery); // Set our Installation query
//                            push.setMessage("Free hotdogs at the Parse concession stand!");
//                            push.sendInBackground();
//                            Toast.makeText(MainActivity.this, "push noti sent", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
////
////                ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
////                query.findInBackground(new FindCallback<ParseInstallation>()
////
////                {
////                    public void done ( final List<ParseInstallation> objects, com.
////                    parse.ParseException e){
////                    ArrayList<ParseInstallation> objlist = new ArrayList<ParseInstallation>();
////                    Log.d("demo", "objects" + objects.get(0).get("user").toString());
////
////                    for (int i = 0; i < objlist.size(); i++) {
////                        String email = objlist.get(i).get("user").toString();
////                        if (email.equals(ParseUser.getCurrentUser().getEmail().toString())) {
////
////
////                        } else {
////                            Toast.makeText(MainActivity.this, "user email not found", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                }
////                }
//
//              //  );
//            }


            if (id == R.id.logout) {
            Log.d("demo", "logout clicked");

            String objId = ParseInstallation.getCurrentInstallation().getObjectId();
            ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
            Log.d("demo", "cuser"+ParseUser.getCurrentUser());
            query.getInBackground(objId, new GetCallback<ParseInstallation>() {
                @Override
                public void done(ParseInstallation obj, com.parse.ParseException e) {
                    if (e == null) {
                        obj.put("user", "loggedOut");
                        obj.saveInBackground();
                        Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ParseUser.logOut();
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
