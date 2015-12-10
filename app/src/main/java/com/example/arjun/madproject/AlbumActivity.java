package com.example.arjun.madproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    List<ParseObject> albums;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        listView = (ListView) findViewById(R.id.albumListView);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Log.d("demo", "in doen of album activity on create");
                if(e ==  null) {
                    Log.d("demo", "e == null");
                    albums = list;
                    setupData();
                } else {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startNewAlbum(View view) {
        startActivity(new Intent(AlbumActivity.this, NewAlbumActivity.class));
    }

    public void setupData() {
        Log.d("demo", "in setup data");
        AlbumAdapter adapter = new AlbumAdapter(AlbumActivity.this, R.layout.album_item_layout, albums);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
    }
}
