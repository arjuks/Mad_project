package com.example.arjun.madproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    ArrayList<ParseUser> list = new ArrayList<>();
    AppAdapter adapter;
    final static String FNAME = "fname";
    final static String LNAME = "lname";
    final static String GENDER = "gender";
    final static String EMAIL = "email";
    final static String PHOTO = "photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    final ArrayList<ParseUser> ulist = new ArrayList<ParseUser>();
                    ulist.addAll(objects);
                    Log.d("demo", "userlist-size before"+ ulist.size());

                    int i = 0;
                    for(i = 0; i < ulist.size(); i++) {
                        Log.d("demo", "userlist-values"+" "+i+" "+ ulist.get(i).getUsername().toString());
                        if (ulist.get(i).get("profilelisting").equals("false")) {
                            Log.d("demo","match found- false"+" "+i);
                            ulist.remove(i);
                            Log.d("demo", "false removed" + " " + i);
                        }
                        else {
                            Log.d("demo","match not found");
                            continue;
                        }
                    }
                    Log.d("demo","final value of i"+" "+i);

//                    for(int i = 0; i < ulist.size(); i++) {
//                        if(ulist.get(i).get("profilelisting").equals("false") &&
//                            ulist.get(i).getEmail().equals(ParseUser.getCurrentUser().getEmail())) {
//                            ulist.remove(i);
//                        }
//                    }
//                    Log.d("demo", "userlist-size after"+ ulist.size());

                    ListView lv = (ListView) findViewById(R.id.listView2);
                    adapter = new AppAdapter(UserListActivity.this, R.layout.itemlayout, ulist);
                    lv.setAdapter(adapter);
                    adapter.setNotifyOnChange(true);

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Log.d("demo", "list view click");

                            final Intent intent = new Intent(UserListActivity.this,ProfileDisplayActivity.class);
                            intent.putExtra(FNAME, ulist.get(position).getUsername().toString());
                            intent.putExtra(GENDER,ulist.get(position).get("Gender").toString());
                            intent.putExtra(EMAIL, ulist.get(position).getEmail().toString());
                            //intent.putExtra(PHOTO, list.get(position).get("Photo").toString());

                            final ParseFile img = (ParseFile)ulist.get(position).get("imagefile");
                            if(img == null) {
                                Log.d("demo", "no image");
                                startActivity(intent);
                            }
                            else {
                                img.getDataInBackground(new GetDataCallback() {
                                    public void done(byte[] data, com.parse.ParseException e) {
                                    if (e == null) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                                        byte[] byteArray = bStream.toByteArray();
                                        Log.d("demo","byte array ul"+byteArray);
                                        intent.putExtra(PHOTO, byteArray);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.d("demo", "image error" + e.getMessage());
                                    }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Log.d("score", "Error: " + e.getMessage());
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
            Intent intent = new Intent(UserListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        if (id == R.id.logout) {
            Log.d("demo", "logout clicked");

            String objId2 = ParseInstallation.getCurrentInstallation().getObjectId();
            ParseQuery<ParseInstallation> query2 = ParseInstallation.getQuery();
            Log.d("demo", "cuser"+ParseUser.getCurrentUser());
            query2.getInBackground(objId2, new GetCallback<ParseInstallation>() {
                @Override
                public void done(ParseInstallation obj, com.parse.ParseException e) {
                    if (e == null) {
                        obj.put("user", "loggedOut");
                        obj.saveInBackground();
                    }
                }
            });
            ParseTwitterUtils.unlinkInBackground(ParseUser.getCurrentUser());
            ParseUser.logOut();
            finish();
            Intent intent = new Intent(UserListActivity.this, LoginActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
