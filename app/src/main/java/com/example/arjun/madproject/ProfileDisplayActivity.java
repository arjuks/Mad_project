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
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileDisplayActivity extends AppCompatActivity {

    Uri img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        String uname = getIntent().getExtras().get(UserListActivity.FNAME).toString();
        String gender = getIntent().getExtras().get(UserListActivity.GENDER).toString();
        String email = getIntent().getExtras().get(UserListActivity.EMAIL).toString();
        //String photo = getIntent().getExtras().get(UserListActivity.PHOTO).toString();

        TextView fnameD = (TextView) findViewById(R.id.unameVal);
        TextView genderD = (TextView) findViewById(R.id.genderVal);
        TextView emailD = (TextView) findViewById(R.id.emailVal);
        ImageView photoD = (ImageView) findViewById(R.id.photoDisplay);

        fnameD.setText(uname);
        genderD.setText(gender);
        emailD.setText(email);

//        String[] photo_split = photo.split("%3A");
//        String imageUriBasePath = "content://media/external/images/media/" + photo_split[1];
//        img = Uri.parse(imageUriBasePath);
//        photoD.setImageURI(img);

        byte[] imgdata = getIntent().getByteArrayExtra(UserListActivity.PHOTO);
        Log.d("demo", "byte array pd" + imgdata);
        if(imgdata != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
            photoD.setImageBitmap(bmp);
        }

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
            Intent intent = new Intent(ProfileDisplayActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.logout) {
            Log.d("demo", "logout clicked");

            String objId2 = ParseInstallation.getCurrentInstallation().getObjectId();
            ParseQuery<ParseInstallation> query2 = ParseInstallation.getQuery();
            Log.d("demo", "cuser"+ ParseUser.getCurrentUser());
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
            finish();
            Intent intent = new Intent(ProfileDisplayActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
