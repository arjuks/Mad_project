package com.example.arjun.madproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
}
