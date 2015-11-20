package com.example.arjun.madproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    ArrayList<ParseUser> list = new ArrayList<>();
    Object uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final EditText firstname = (EditText) findViewById(R.id.firstNameEdit);
        final EditText lastname = (EditText) findViewById(R.id.lastNameEdit);
        final EditText gender = (EditText) findViewById(R.id.genderEdit);
        final EditText email = (EditText) findViewById(R.id.emailEdit);
        final ImageView photo = (ImageView) findViewById(R.id.profilePhotoEdit);


        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + objects.size() + " objects");
                    list.addAll(objects);

                     if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            //ParseObject name = list.get(i);
                            ParseUser user = list.get(i);
//                            ParseUser user = name.getParseUser("savedBy");
                            String n2 = null;
                            String e2 = null;
                            try {
                                n2 = user.fetchIfNeeded().getUsername().toString();
                                e2 = user.fetchIfNeeded().getEmail().toString();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            ParseUser cuser = ParseUser.getCurrentUser();
                            if(cuser.getUsername().toString().equals(n2) && cuser.getEmail().toString().equals(e2)){
                                firstname.setText(user.getUsername().toString());
                                lastname.setText(user.get("Lastname").toString());
                                gender.setText(user.get("Gender").toString());
                                email.setText(user.getEmail().toString());

                                uri = user.get("Photo");
                                String [] photo_split= uri.toString().split("%3A");
                                String imageUriBasePath = "content://media/external/images/media/"+photo_split[1];
                                uri= Uri.parse(imageUriBasePath);
                                photo.setImageURI((Uri) uri);

                            }
                        }
//                        ListView lv = (ListView) findViewById(R.id.listView2);
//                        adapter = new favouriteAdapter(Favorites.this, R.layout.itemlayout, newlist);
//                        lv.setAdapter(adapter);
//                        adapter.setNotifyOnChange(true);
//                        Log.d("demo", "user match");

                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

























//                userlist.addAll(objects);
//
//                    ParseObject msg = new ParseObject("SharedList");
//                    msg.put("photo_title", title_val);
//                    msg.put("photo_url", getIntent().getExtras().get(GalleryActivity.URL).toString());
//                    msg.put("count", c);
//                    msg.put("owner_img", getIntent().getExtras().get(GalleryActivity.OWNER_IMG).toString());
//                    msg.put("owner_name", oname);
//                    msg.put("time_taken", t);
//                    msg.put("currentuser", ParseUser.getCurrentUser().get("fullName"));
//                    msg.put("usersharedto", sharedto);
//                    msg.saveInBackground();
//                    Toast.makeText(DetailsActivity.this, "Shared Successfully", Toast.LENGTH_SHORT).show();
//                }
//            });

            }

}
