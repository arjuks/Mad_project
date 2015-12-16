package com.example.arjun.madproject;

import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShareActivity extends AppCompatActivity {
    String albumId;
    ListView listView;
    ParseACL albumACL;
    ArrayList<ParseUser> userList = new ArrayList<>();
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
                userList.remove(ParseUser.getCurrentUser());
                final ParseQuery<ParseObject> albumQuery = ParseQuery.getQuery("Album");
                albumQuery.getInBackground(albumId, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        albumACL = object.getACL();
                        for (ParseUser user : userList) {
                            if (albumACL.getReadAccess(user)) {
                                permissionedUsers.add(user);
                            }
                        }
                        setupData();
                    }
                });

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
        ArrayList<ParseUser> updatedPermissionUserList = new ArrayList<>();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        Log.d("demo", "checked: " + checked);
        final ParseObject album = getAlbum();
        ParseACL updateACL = albumACL;
        for(int i =0; i< listView.getAdapter().getCount(); i++) {
            if(checked.get(i)) {
                updatedPermissionUserList.add(userList.get(i));
                updateACL.setReadAccess(userList.get(i), true);
            }

        }
        permissionedUsers = updatedPermissionUserList;
        album.setACL(updateACL);
        album.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Log.d("demo", "done saving album");
                    ParseQuery userQuery = ParseUser.getQuery();
                    for(ParseUser user : permissionedUsers) {
                        userQuery.whereEqualTo("objectId", user.getObjectId());
                    }
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereMatchesQuery("user", userQuery);

                    Log.d("demo", "user query for parse user: " + userQuery);
                    Log.d("demo", "push query for parse user: " + pushQuery);
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);
                    push.setMessage(ParseUser.getCurrentUser().getUsername() + " has shared " +
                    album.get("name") + " with you.");
                    push.sendInBackground();
                    Toast.makeText(ShareActivity.this, "You have shared this album with " + permissionedUsers.size() + " users", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
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
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
            adapter.setNotifyOnChange(true);
        }
    }
}
