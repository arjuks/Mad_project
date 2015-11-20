package com.example.arjun.madproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
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
        if (id == R.id.editprofile) {
            Log.d("demo", "edit clicked");
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        }
        if(id == R.id.userlist){
            Log.d("demo", "compose clicked");

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
}
