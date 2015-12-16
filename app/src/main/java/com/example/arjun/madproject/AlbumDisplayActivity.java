package com.example.arjun.madproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
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
import java.util.List;

public class AlbumDisplayActivity extends AppCompatActivity {
    final int ADD_PICTURE_TO_ALBUM = 201;
    final int UPDATE_ALBUM_ACL = 200;
    public static final String USER_FULL_NAME_LIST = "user_full_name_list";
    TextView emptyAlbumLabel;
    List<ParseObject> pictures = new ArrayList<>();
    List<ParseUser> userList = new ArrayList<>();
    Button addPhotoButton, updateAlbumButton, deleteAlbum, shareButton;
    ProgressDialog progressDialog;
    GridView gridView;
    String albumId;
    AlertDialog setPrivacyAlert;
    CheckBox cb;
    ParseObject album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_display);
        gridView = (GridView) findViewById(R.id.displayAlbumGrid);
        albumId = getIntent().getExtras().get(AlbumActivity.ALBUM_ID).toString();
        cb = (CheckBox) findViewById(R.id.privacyCheckBox);
        addPhotoButton = (Button) findViewById(R.id.add_photo_btn);
        updateAlbumButton = (Button) findViewById(R.id.update_album);
        emptyAlbumLabel = (TextView) findViewById(R.id.empty_album_label);
        deleteAlbum = (Button) findViewById(R.id.delete_album);
        shareButton = (Button) findViewById(R.id.shareAlbum);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Picture");
        Log.d("demo", "album id: " + albumId);
        query.whereEqualTo("albumId", albumId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null && list.size() > 0) {
                    emptyAlbumLabel.setVisibility(View.INVISIBLE);
                    Log.d("demo", "received " + list.size() + " pictures");
                    pictures = list;
                    setupData();
                }
                album = getAlbum();
                ParseACL acl = album.getACL();
                String albumName = album.get("name").toString();
//                setTitle(albumName);
                if (acl.getWriteAccess(ParseUser.getCurrentUser())) {
                    cb.setChecked(!acl.getPublicReadAccess());
                    cb.setVisibility(View.VISIBLE);
                    addPhotoButton.setVisibility(View.VISIBLE);
                    updateAlbumButton.setVisibility(View.VISIBLE);
                    deleteAlbum.setVisibility(View.VISIBLE);
                    shareButton.setVisibility(View.VISIBLE);
                } else {
                    TextView tv = (TextView) findViewById(R.id.privacyTextBox);
                    String privacySetting = acl.getPublicReadAccess() ? "Public" : "Private";
                    tv.setText(privacySetting);
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AlbumDisplayActivity.this);
                alertDialog.setMessage("Delete Picture?");
                alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("demo", "close dialog");
                        dialog.dismiss();
                    }
                });
                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(AlbumDisplayActivity.this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Deleting Picture ...");
                        progressDialog.show();
                        pictures.get(position).deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    pictures.remove(position);
                                    Log.d("demo", "done callback e == null");
                                    setupData();
                                    progressDialog.dismiss();
                                    emptyAlbumLabel.setVisibility(View.VISIBLE);
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });

                alertDialog.show();
                return false;
            }
        });

        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery("_User");
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                Log.d("demo", "objects: " + objects);
                userList.addAll(objects);
                ParseUser currentUser = ParseUser.getCurrentUser();
                userList.remove(userList.indexOf(currentUser));
                final CharSequence[] names = new CharSequence[userList.size()];

                final int index = 0;
                for (int i = 0; i < userList.size(); i++) {
                    names[index] = userList.get(i).get("FullName").toString();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AlbumDisplayActivity.this);
                builder.setTitle("Pick a User")
                    .setItems(names, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("demo", "selected" + " " + names[which]);
                            ParseACL updatedAcl = album.getACL();
                            updatedAcl.setReadAccess(userList.get(which).getObjectId(), true);
                            album.setACL(updatedAcl);
                            album.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    setPrivacyAlert.dismiss();
                                    Intent intent = new Intent(AlbumDisplayActivity.this, ShareActivity.class);
                                    intent.putExtra(AlbumActivity.ALBUM_ID, albumId);
                                    intent.putExtra("d", new ArrayList<String>());
                                    startActivityForResult(intent, UPDATE_ALBUM_ACL);
                                }
                            });
//                            Intent intent = new Intent(AlbumDisplayActivity.this, MessageActivity.class);
//                            intent.putExtra(FULLNAME, fullname);
//                            startActivity(intent);
                        }
                    });
                setPrivacyAlert = builder.create();
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
                ParseObject pic = savePhotoToParse(file);
                pictures.add(pic);
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
        if(pictures != null) {
            Log.d("demo", "setup data");
            emptyAlbumLabel.setVisibility(View.INVISIBLE);
            AlbumDisplayAdapter adapter = new AlbumDisplayAdapter(AlbumDisplayActivity.this, R.layout.single_picture_layout, pictures);
            gridView.setAdapter(adapter);
            adapter.setNotifyOnChange(true);
        }
    }

    public void addPictureToAlbum(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
            "Select Picture"), ADD_PICTURE_TO_ALBUM);
    }

    public void updateAlbum(View view) {
        progressDialog = new ProgressDialog(AlbumDisplayActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Adding Picture ...");
        progressDialog.show();

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
                for (ParseObject pic : pictures) {
                    pictureIds.add(pic.getObjectId());
                }
                album.put("pictureIdList", pictureIds);
                album.setACL(acl);
                album.saveInBackground();
                Toast.makeText(AlbumDisplayActivity.this, "Album Update Successfully!", Toast.LENGTH_LONG).show();
                Log.d("demo", "pictures done saving");
                progressDialog.dismiss();
                finish();
            }
        });
    }

    public void deleteAlbum(View view) {
        progressDialog = new ProgressDialog(AlbumDisplayActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Adding Album ...");
        progressDialog.show();
        Log.d("demo", "album id: " + albumId);
        ParseObject.deleteAllInBackground(pictures);
        album.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(AlbumDisplayActivity.this, "Album Destroyed Successfully!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    progressDialog.dismiss();
                    finish();
                }
            }
        });
    }

    public ParseObject savePhotoToParse(Bitmap picture) {
        progressDialog = new ProgressDialog(AlbumDisplayActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Adding Picture ...");
        progressDialog.show();
        try {
            String albumName = album.get("name").toString();
            String albumId = album.getObjectId();
            ParseACL acl = album.getACL();
            ParseObject parsePicture = new ParseObject("Picture");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapData = stream.toByteArray();
            ParseFile file = new ParseFile("image.jpg", bitmapData);

            file.save();

            Log.d("demo", "file saved to parse");
            parsePicture.put("file", file);
            parsePicture.put("albumName", albumName);
            parsePicture.put("albumId", albumId);
            parsePicture.setACL(acl);
            progressDialog.dismiss();
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

    public void shareAlbum(View view) {
//        setPrivacyAlert.show();
        ArrayList<String> nameList = new ArrayList<>();
        for(ParseUser user : userList) {
            nameList.add(user.get("FullName").toString());
        }
        Intent intent = new Intent(AlbumDisplayActivity.this, ShareActivity.class);
        intent.putExtra(AlbumActivity.ALBUM_ID, albumId);
        intent.putExtra(USER_FULL_NAME_LIST, nameList);
        startActivityForResult(intent, UPDATE_ALBUM_ACL);
    }
}
