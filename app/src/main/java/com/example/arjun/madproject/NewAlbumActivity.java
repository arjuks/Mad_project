package com.example.arjun.madproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.SingleLaunchActivityTestCase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.math.*;

public class NewAlbumActivity extends AppCompatActivity {
    static final int ADD_PICTURE_CODE = 2;
    ArrayList<Bitmap> pictures = new ArrayList<>();
    GridView gridView;
    Uri uri;
    Bitmap somePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album);
        gridView = (GridView) findViewById(R.id.pictureGrid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_album, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PICTURE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                somePicture = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d("demo", "picture: " + somePicture);
                pictures.add(somePicture);
                setupData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPictureToAlbum(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
            "Select Picture"), ADD_PICTURE_CODE);
    }

    public void createAlbum(View view) {
        EditText editText = (EditText) findViewById(R.id.albumName);
        CheckBox checkBox = (CheckBox) findViewById(R.id.privacyCheckBox);
        String albumName = editText.getText().toString();
        if(albumName.isEmpty()) {
            Toast.makeText(NewAlbumActivity.this, "Please Specify an Album Name", Toast.LENGTH_LONG).show();
        } else if (pictures.size() == 0) {
            Toast.makeText(NewAlbumActivity.this, "Please Add Some Pictures", Toast.LENGTH_LONG).show();
        } else {
            ParseACL acl = new ParseACL();
            if(checkBox.isChecked()) {
                acl.setPublicReadAccess(true);
            } else {
                acl.setPublicReadAccess(false);
                acl.setReadAccess(ParseUser.getCurrentUser(), true);
                acl.setWriteAccess(ParseUser.getCurrentUser(), true);
            }

            try {
                saveToParse(albumName, acl);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO make album name be unique?
    public void saveToParse(final String albumName, final ParseACL acl) throws ParseException {
        final ParseObject album = new ParseObject("Album");
        final List<ParseObject> parsePictures = new ArrayList<>();
        final ArrayList<String> pictureIds = new ArrayList<>();



        for(int i = 0; i<pictures.size(); i++) {
            final int index = i;
            final ParseObject picture = new ParseObject("Picture");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            pictures.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapData = stream.toByteArray();
            final ParseFile file = new ParseFile("image.jpg", bitmapData);

            file.save();

            picture.put("file", file);
            picture.put("albumName", albumName);
            picture.setACL(acl);
            parsePictures.add(picture);
        }

        ParseObject.saveAllInBackground(parsePictures, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("demo", "pictures done saving");
                    Toast.makeText(NewAlbumActivity.this, "Finished all pictures ", Toast.LENGTH_SHORT).show();
                    for (ParseObject pic : parsePictures) {
                        pictureIds.add(pic.getObjectId());
                    }
                    album.put("name", albumName);
                    album.setACL(acl);
                    album.put("pictureIdList", pictureIds);
                    album.put("userId", ParseUser.getCurrentUser().getObjectId());
                    album.put("username", ParseUser.getCurrentUser().getUsername());
                    album.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                for (ParseObject pic : parsePictures) {
                                    pic.put("albumId", album.getObjectId());
                                }
                                ParseObject.saveAllInBackground(parsePictures);
                                Toast.makeText(NewAlbumActivity.this, "Album saved successfully!",
                                    Toast.LENGTH_LONG).show();

                                finish();
                            } else {
                                Toast.makeText(NewAlbumActivity.this, "Whoops! Something wen wrong" +
                                    "\nAlbum not saved", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    Toast.makeText(NewAlbumActivity.this, "Whoops! Something went wrong!" +
                        "\nPictures not saved", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


//        ParseObject.saveAllInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                Log.d("demo", "file saving done");
////                picture.saveInBackground(new SaveCallback() {
////                    @Override
////                    public void done(ParseException e) {
////                        Log.d("demo", "pictur saving done");
////                    }
////                });
//            }
//        });
    }

    public void setupData() {
        NewAlbumAdapter adapter = new NewAlbumAdapter(NewAlbumActivity.this, R.layout.single_picture_layout, pictures);
        gridView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
    }

    public void saveAllFiles() {

    }
}































//        final ParseObject picture = new ParseObject("Picture");
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        byte[] d = stream.toByteArray();
//        final ParseFile file = new ParseFile("image.jpg", d);
//        somePicture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        picture.put("file", file);
//
//
//        picture.put("albumName", albumName);
//        parsePictures.add(picture);
//
//        file.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d("demo", "after file saved");
//                    picture.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null) {
//                                Toast.makeText(NewAlbumActivity.this, "Pictures saved successfully!",
//                                    Toast.LENGTH_LONG).show();
//                                for (ParseObject pic : parsePictures) {
//                                    pictureIds.add(pic.getObjectId());
//                                }
//                                album.put("name", albumName);
//                                album.setACL(acl);
//                                album.put("pictureIdList", pictureIds);
//                                album.put("userId", ParseUser.getCurrentUser().getObjectId());
//                                album.put("username", ParseUser.getCurrentUser().getUsername());
//                                album.saveInBackground(new SaveCallback() {
//                                    @Override
//                                    public void done(ParseException e) {
//                                        if (e == null) {
//                                            for (ParseObject pic : parsePictures) {
//                                                pic.put("albumId", album.getObjectId());
//                                            }
//                                            ParseObject.saveAllInBackground(parsePictures);
//                                            Toast.makeText(NewAlbumActivity.this, "Album saved successfully!",
//                                                Toast.LENGTH_LONG).show();
//
//                                            finish();
//                                        } else {
//                                            Toast.makeText(NewAlbumActivity.this, "Whoops! Something wen wrong" +
//                                                "\nAlbum not saved", Toast.LENGTH_LONG).show();
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                });
//                            } else {
//                                Toast.makeText(NewAlbumActivity.this, "Whoops! Something went wrong!" +
//                                    "\nPictures not saved", Toast.LENGTH_LONG).show();
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//            }
//        });


//        for(int i = 0; i<pictures.size(); i++) {
//            ParseObject picture = new ParseObject("Picture");
//            long randomNumber = (long) (Math.random() * 9999999);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            byte[] d = stream.toByteArray();
//            ParseFile file = new ParseFile("album.jpg", d);
//
//            picture.put("file", file);
//            picture.put("albumName", albumName);
//            parsePictures.add(picture);
//        }
