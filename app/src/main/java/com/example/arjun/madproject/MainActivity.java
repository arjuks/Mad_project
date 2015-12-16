package com.example.arjun.madproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
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
        Button editprofile = (Button) findViewById(R.id.editprofileBtn);
        Button viewlist = (Button) findViewById(R.id.viewuserlistBtn);
        Button privacy = (Button) findViewById(R.id.privacyBtn);

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                userlist.addAll(objects);
                ParseUser currentUser = ParseUser.getCurrentUser();

                for (int i = 0; i < userlist.size(); i++) {
                    Log.d("demo","user obj id: "+ userlist.get(i).getObjectId());
                    Log.d("demo","current user obj id: "+ currentUser.getObjectId());
                    if( userlist.get(i).getObjectId().toString().equals(currentUser.getObjectId().toString())) {
                        semail = currentUser.getEmail().toString();
                        userlist.remove(i);
                    }
                }

                lsize = userlist.size();
                final CharSequence[] items = new CharSequence[lsize];

                for (int i = 0; i < userlist.size(); i++) {
                    String names = userlist.get(i).get("FullName").toString();
                    items[i] = names;
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

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "edit clicked");
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);

            }
        });

        viewlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "view list clicked");
                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                startActivity(intent);

            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "privacy clicked");
                Intent intent = new Intent(MainActivity.this, PrivacyActivity.class);
                startActivity(intent);

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
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void goToAlbum(View view) {
        startActivity(new Intent(MainActivity.this, AlbumActivity.class));
    }


}
