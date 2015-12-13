package com.example.arjun.madproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends AppCompatActivity {
    String albumId;
    ListView listView;
    ParseACL albumACL;
    List<ParseUser> userList = new ArrayList<>();
    ArrayList<ParseUser> permissionedUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        albumId = getIntent().getExtras().get(AlbumActivity.ALBUM_ID).toString();
        listView = (ListView) findViewById(R.id.shareUserListView);

        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                userList.addAll(objects);
                final ParseQuery<ParseObject> albumQuery = ParseQuery.getQuery("Album");
                albumQuery.getInBackground(albumId, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        albumACL = object.getACL();
                        for(ParseUser user : userList) {
                            if(albumACL.getReadAccess(user))
                                permissionedUsers.add(user);
                        }
                        setupData();
                    }
                });

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
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

    public void updateAlbumPrivacy(View view) {
    }

    public ParseObject getAlbum() {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
            return query.get(albumId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setupData() {
        if(userList != null) {
            Log.d("demo", "setup data");
            ShareAdapter adapter = new ShareAdapter(ShareActivity.this, R.layout.single_user_layout, userList, permissionedUsers);
            listView.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
        }
    }
}
