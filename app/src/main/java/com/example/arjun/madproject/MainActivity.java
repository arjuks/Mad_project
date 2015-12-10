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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<ParseUser> userlist = new ArrayList<>();
    int lsize ;
    AlertDialog simpleA = null;
    static String semail = null;

    final static String SNAME = "sname";
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
                    if( userlist.get(i).getEmail().equals(currentUser.getEmail())) {
                        semail = userlist.get(i).getEmail();
                        Log.d("demo","semail main"+semail);
                        userlist.remove(i);
                    }
                }


                final CharSequence[] items = new CharSequence[userlist.size()];

                for (int i = 0; i < userlist.size(); i++) {
                    String fullname = userlist.get(i).getUsername()+" "+ userlist.get(i).get("Lastname");
                    items[i] = fullname;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pick a Contact")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("demo", "selected" + " " + items[which]);
//                            String rname = items[which].toString();
//                            String sname = ParseUser.getCurrentUser().getUsername()+" "+ParseUser.getCurrentUser().get("Lastname");
//                            Log.d("demo","sname"+sname);
//                            Intent intent = new Intent(MainActivity.this, MessageActivity.class);
//                            intent.putExtra(RNAME, rname);
//                            intent.putExtra(SNAME, sname);
//                            intent.putExtra(SEMAIL, semail);
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
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.editprofile) {
            Log.d("demo", "edit clicked");
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        }
        if(id == R.id.userlist){
            Log.d("demo", "compose clicked");
            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
            startActivity(intent);
        }

        if (id == R.id.logout) {
            Log.d("demo", "logout clicked");
            ParseUser.logOut();
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToAlbum(View view) {
        startActivity(new Intent(MainActivity.this, AlbumActivity.class));
    }
}
