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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ComposeActivity extends AppCompatActivity {

    final static String CFNAME = "firstname";
    String r_fname = null;
    private static final int SELECT_PICTURE = 2;
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
                ImageView image = (ImageView) findViewById(R.id.msgImageDisplay);
                image.setImageBitmap(bitmap);
                picture = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        Button send = (Button) findViewById(R.id.sendBtn);
        final EditText compose = (EditText) findViewById(R.id.composeText);
        ImageView photoImg = (ImageView) findViewById(R.id.photoImg);
        final ParseUser currentUser = ParseUser.getCurrentUser();
        r_fname = getIntent().getExtras().get(MessageDisplayActivity.RNAME).toString();

        photoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","bitmap"+bitmap);
                Log.d("demo","picture"+picture);

                if (bitmap == null && picture == null) {

                    if (compose.getText().toString().equals("")) {
                        Toast.makeText(ComposeActivity.this, "Please type a message", Toast.LENGTH_SHORT).show();
                    } else {
                        ParseObject msg = new ParseObject("Message");
                        msg.put("msg", compose.getText().toString());
                        msg.put("recepient", r_fname);
                        msg.put("sender", currentUser.get("FullName").toString());
                        msg.put("read", "notseen");
                        msg.saveInBackground();
                        Toast.makeText(ComposeActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ComposeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                else {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] d = stream.toByteArray();
                    final ParseFile file = new ParseFile("image.jpg", d);

                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (null == e) {
                                Log.d("demo", "inside savecallback");
                                if (compose.getText().toString().equals("")) {
                                    Toast.makeText(ComposeActivity.this, "Please type a message", Toast.LENGTH_SHORT).show();
                                } else {
                                    ParseObject msg = new ParseObject("Message");
                                    msg.put("msg", compose.getText().toString());
                                    msg.put("recepient", r_fname);
                                    msg.put("sender", currentUser.get("FullName").toString());
                                    msg.put("read", "notseen");
                                    msg.put("imagefile", file);
                                    msg.saveInBackground();
                                    Toast.makeText(ComposeActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ComposeActivity.this, MainActivity.class);
                                    startActivity(intent);

                                }
                            }
                        }
                    });
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
            Intent intent = new Intent(ComposeActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.logout) {
            Log.d("demo", "logout clicked");

            String objId2 = ParseInstallation.getCurrentInstallation().getObjectId();
            ParseQuery<ParseInstallation> query2 = ParseInstallation.getQuery();
            Log.d("demo", "cuser" + ParseUser.getCurrentUser());
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
            Intent intent = new Intent(ComposeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
