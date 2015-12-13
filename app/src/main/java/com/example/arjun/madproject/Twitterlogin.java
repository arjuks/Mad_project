package com.example.arjun.madproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Twitterlogin extends Activity {


    private static final int SELECT_PICTURE = 6;
    Uri uri;
    Bitmap bitmap;
    Bitmap picture;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView phototwit = (ImageView) findViewById(R.id.twitimageView);
                phototwit.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitterlogin);

        final EditText gendertwit= (EditText) findViewById(R.id.gendertwitVal);
        ImageView phototwit = (ImageView) findViewById(R.id.twitimageView);
        Button submit = (Button) findViewById(R.id.twitsubmit);

        final String name = getIntent().getExtras().get(LoginActivity.NAME).toString();
        final EditText emailVal = (EditText) findViewById(R.id.emailtwitVal);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gendertwit.getText().toString().equals("")) {
                    Toast.makeText(Twitterlogin.this, "Please fill in the gender", Toast.LENGTH_SHORT).show();
                }
                if(bitmap == null) {
                    Toast.makeText(Twitterlogin.this, "Please select a photo", Toast.LENGTH_SHORT).show();
                }
                if(emailVal.getText().toString().equals("")) {
                    Toast.makeText(Twitterlogin.this, "Please put your email id", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("demo", "inside else");

                    picture = bitmap;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] d = stream.toByteArray();
                    final ParseFile file = new ParseFile("image.jpg", d);

                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                        if (null == e) {
                            Log.d("demo", "inside savecallback");
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                            query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject obj, com.parse.ParseException e) {
                                    if (e == null) {
                                        obj.put("Gender", gendertwit.getText().toString());
                                        obj.put("profilelisting", "true");
                                        obj.put("email",emailVal.getText().toString());
                                        obj.put("username",name);
                                        obj.put("pushnote","true");
                                        obj.put("messageprivacy","true");
                                        obj.put("imagefile", file);
                                        obj.saveInBackground();
                                        Toast.makeText(Twitterlogin.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Twitterlogin.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        }
                    });
                }
            }
        });
        phototwit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
    }


}
