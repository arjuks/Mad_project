package com.example.arjun.madproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    ArrayList<ParseUser> list = new ArrayList<>();
    Object uri;
    String obj_id = null;
    Bitmap bitmap, bmp, picture;
    private static final int SELECT_PICTURE = 1;
    Uri image;
    Uri org_img;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            org_img = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                ImageView image = (ImageView) findViewById(R.id.profilePhotoEdit);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final EditText username = (EditText) findViewById(R.id.userNameEdit);
        final EditText gender = (EditText) findViewById(R.id.genderEdit);
        final EditText email = (EditText) findViewById(R.id.emailEdit);
        final ImageView photo = (ImageView) findViewById(R.id.profilePhotoEdit);
        Button save = (Button) findViewById(R.id.saveBtn);


        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + objects.size() + " objects");

                    list.addAll(objects);

                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            ParseUser user = list.get(i);
                            String n2 = null;
                            String e2 = null;
                            try {
                                n2 = user.fetchIfNeeded().getUsername().toString();
                                e2 = user.fetchIfNeeded().getEmail().toString();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            ParseUser cuser = ParseUser.getCurrentUser();
                            if (cuser.getUsername().toString().equals(n2) && cuser.getEmail().toString().equals(e2)) {

                                obj_id = cuser.getObjectId();
                                username.setText(user.getUsername().toString());
                                gender.setText(user.get("Gender").toString());
                                email.setText(user.getEmail().toString());

//                                uri = user.get("Photo");
//                                String[] photo_split = uri.toString().split("%3A");
//                                String imageUriBasePath = "content://media/external/images/media/" + photo_split[1];
//                                uri = Uri.parse(imageUriBasePath);
//                                image = (Uri) uri;
//                                photo.setImageURI(image);

                                final ParseFile img = (ParseFile) user.get("imagefile");
                                if (img == null) {
                                    Log.d("demo", "no image");
                                } else {
                                    img.getDataInBackground(new GetDataCallback() {
                                        public void done(byte[] data, com.parse.ParseException e) {
                                            if (e == null) {
//                                                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                                                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
//                                                byte[] byteArray = bStream.toByteArray();
                                                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                photo.setImageBitmap(bmp);
                                                //img.setImageBitmap(bmp);


                                            } else {
                                                // something went wrong
                                                Log.d("demo", "imag error" + e.getMessage());
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    }

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
                query.getInBackground(obj_id, new GetCallback<ParseUser>() {
                    @Override
                    public void done(final ParseUser user, com.parse.ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            if (bitmap != null) {
                                picture = bitmap;
                            } else {
                                picture = bmp;
                            }

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] d = stream.toByteArray();
                            final ParseFile file = new ParseFile("image.jpg", d);
                            file.saveInBackground();

                            file.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (null == e) {
                                        Log.d("demo", "inside savecallback");
                                        user.setUsername(username.getText().toString());
                                        user.put("Gender", gender.getText().toString());
                                        user.setEmail(email.getText().toString());
                                        user.put("imagefile", file);
                                        user.saveInBackground();

                                        Toast.makeText(EditActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}


