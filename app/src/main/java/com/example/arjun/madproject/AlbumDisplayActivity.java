package com.example.arjun.madproject;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AlbumDisplayActivity extends AppCompatActivity {
    final int ADD_PICTURE_TO_ALBUM = 201;
    List<ParseObject> pictures;

    GridView gridView;
    ImageView newImageView;
    String albumId;
    CheckBox cb;
    Button addPhotoButton, updateAlbumButton, deleteAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_display);
        gridView = (GridView) findViewById(R.id.displayAlbumGrid);
        albumId = getIntent().getExtras().get(AlbumActivity.ALBUM_ID).toString();
        cb = (CheckBox) findViewById(R.id.privacyCheckBox);
        addPhotoButton = (Button) findViewById(R.id.add_photo_btn);
        updateAlbumButton = (Button) findViewById(R.id.update_album);
        deleteAlbum = (Button) findViewById(R.id.delete_album);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Picture");
        Log.d("demo", "album id: " + albumId);
        query.whereEqualTo("albumId", albumId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Log.d("demo", "received " + list.size() + " pictures");
                pictures = list;
                setupData();
                ParseACL acl = pictures.get(0).getACL();
                String albumName = pictures.get(0).get("albumName").toString();
                setTitle(albumName);
                if (acl.getWriteAccess(ParseUser.getCurrentUser())) {
                    cb.setChecked(!acl.getPublicReadAccess());
                    cb.setVisibility(View.VISIBLE);
                    addPhotoButton.setVisibility(View.VISIBLE);
                    updateAlbumButton.setVisibility(View.VISIBLE);
                    deleteAlbum.setVisibility(View.VISIBLE);
                } else {
                    TextView tv = (TextView) findViewById(R.id.privacyTextBox);
                    String privacySetting = acl.getPublicReadAccess() ? "Public" : "Private";
                    tv.setText(privacySetting);
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == ADD_PICTURE_TO_ALBUM) {
            Uri uri = data.getData();
            try {
                Bitmap file = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                pictures.add(savePhotoToParse(file));
                setupData();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void setupData() {
        AlbumDisplayAdapter adapter = new AlbumDisplayAdapter(AlbumDisplayActivity.this, R.layout.single_picture_layout, pictures);
        gridView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
    }

    public void addPictureToAlbum(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
            "Select Picture"), ADD_PICTURE_TO_ALBUM);
    }

    public void updateAlbum(View view) {

        final ArrayList<String> pictureIds = new ArrayList<>();
        final ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
        for(ParseObject pic : pictures) {
            acl.setWriteAccess(ParseUser.getCurrentUser(), true);
            if(cb.isChecked()) {
                acl.setPublicReadAccess(false);
                acl.setReadAccess(ParseUser.getCurrentUser(), true);
            } else {
                acl.setPublicReadAccess(true);
            }
            pic.setACL(acl);
        }

        Log.d("demo", "size of pictures about to save" + pictures.size());
        ParseObject.saveAllInBackground(pictures, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ParseObject album = getAlbum();
                for(ParseObject pic : pictures) {
                    pictureIds.add(pic.getObjectId());
                }
                album.put("pictureIdList", pictureIds);
                album.setACL(acl);
                album.saveInBackground();
                Toast.makeText(AlbumDisplayActivity.this, "Album Update Successfully!", Toast.LENGTH_LONG).show();
                Log.d("demo", "pictures done saving");
                finish();
            }
        });
    }

    public void deleteAlbum(View view) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Album");
        Log.d("demo", "album id: " + albumId);
        ParseObject.deleteAllInBackground(pictures);
        query.getInBackground(albumId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(AlbumDisplayActivity.this, "Album Destroyed Successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
    }

    public ParseObject savePhotoToParse(Bitmap picture) {
        try {
            ParseObject singlePictureObj = pictures.get(0);
            String albumName = singlePictureObj.get("albumName").toString();
            String albumId = singlePictureObj.get("albumId").toString();
            ParseACL acl = singlePictureObj.getACL();
            final ParseObject parsePicture = new ParseObject("Picture");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapData = stream.toByteArray();
            final ParseFile file = new ParseFile("image.jpg", bitmapData);

            file.save();

            Log.d("demo", "file saved to parse");
            parsePicture.put("file", file);
            parsePicture.put("albumName", albumName);
            parsePicture.put("albumId", albumId);
            parsePicture.setACL(acl);

            return parsePicture;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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
}
