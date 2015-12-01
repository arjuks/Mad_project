package com.example.arjun.madproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ComposeActivity2 extends AppCompatActivity {

    final static String CFNAME = "firstname";
    final static String CLNAME = "lastname";
    String s_fname = null;
    private static final int SELECT_PICTURE = 3;
    Uri uri;
    Bitmap bitmap = null;
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
        ImageView photoImg = (ImageView) findViewById(R.id.photoImg);
        final EditText compose = (EditText) findViewById(R.id.composeText);
        s_fname = getIntent().getExtras().get(MessageActivity.FULLNAME).toString();
        final ParseUser currentUser = ParseUser.getCurrentUser();

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

                if (bitmap == null && picture == null) {

                    if (compose.getText().toString().equals("")) {
                        Toast.makeText(ComposeActivity2.this, "Please type a message", Toast.LENGTH_SHORT).show();
                    } else {
                        ParseObject msg = new ParseObject("Message");
                        msg.put("msg", compose.getText().toString());
                        msg.put("recepient", s_fname);
                        msg.put("sender", currentUser.getUsername() + " " + currentUser.get("Lastname"));
                        msg.put("read", "notseen");
                        msg.saveInBackground();
                        Toast.makeText(ComposeActivity2.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(ComposeActivity2.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                else {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] d = stream.toByteArray();
                    final ParseFile file = new ParseFile("image.jpg", d);
                    file.saveInBackground();

                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if( e == null) {
                                Log.d("demo", "inside savecallback");
                                if (compose.getText().toString().equals("")) {
                                    Toast.makeText(ComposeActivity2.this, "Please type a message", Toast.LENGTH_SHORT).show();
                                } else {
                                    ParseObject msg = new ParseObject("Message");
                                    msg.put("msg", compose.getText().toString());
                                    msg.put("recepient", s_fname);
                                    msg.put("sender", currentUser.getUsername() + " " + currentUser.get("Lastname"));
                                    msg.put("read", "notseen");
                                    msg.put("imagefile", file);
                                    msg.saveInBackground();
                                    Toast.makeText(ComposeActivity2.this, "Message Sent", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(ComposeActivity2.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
