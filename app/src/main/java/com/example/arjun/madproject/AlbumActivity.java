package com.example.arjun.madproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {
    public static final String ALBUM_ID = "album_id";
    public final int DISPLAY_ALBUM = 1;
    List<ParseObject> albums;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("demo", "in on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        listView = (ListView) findViewById(R.id.albumListView);
        getAlbumData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AlbumActivity.this, AlbumDisplayActivity.class);
                intent.putExtra(ALBUM_ID, albums.get(position).getObjectId());
                startActivityForResult(intent, DISPLAY_ALBUM);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("demo", "in on activity result");
        Log.d("demo", "result code: " + resultCode);
        Log.d("demo", "result ok code: " + RESULT_OK);
        Log.d("demo", "result canceled" + RESULT_CANCELED);
        Log.d("demo", "result first user" + RESULT_FIRST_USER);
        if(resultCode == RESULT_OK) {
            Log.d("demo", "result code is ok!");
            getAlbumData();
        }
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
        startActivityForResult(new Intent(AlbumActivity.this, NewAlbumActivity.class), 201);
        Log.d("demo", "in start new album");
    }

    public void setupData() {
        Log.d("demo", "in setup data");
        AlbumAdapter adapter = new AlbumAdapter(AlbumActivity.this, R.layout.album_item_layout, albums);
        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
    }

    public void getAlbumData() {
        Log.d("demo", "in get album data");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Log.d("demo", "in done of album activity on create");
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
}
