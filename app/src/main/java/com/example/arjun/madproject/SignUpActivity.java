package com.example.arjun.madproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    public static final int SELECT_PICTURE = 1;
    Uri uri;
    Bitmap picture;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                picture = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView image = (ImageView) findViewById(R.id.profilePhotoEdit);
                image.setImageBitmap(picture);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button canc = (Button) findViewById(R.id.CancelBtn);
        Button signup = (Button) findViewById(R.id.signUpBtn);
        final EditText firstname = (EditText) findViewById(R.id.firstNamesignUp);
        final EditText lastname = (EditText) findViewById(R.id.lastNameDisplay);
        final EditText gender = (EditText) findViewById(R.id.gender);
        final EditText password = (EditText) findViewById(R.id.passwordSignupField);
        final EditText confirmp = (EditText) findViewById(R.id.confirmPassword);
        final EditText email = (EditText) findViewById(R.id.email);
        ImageView img = (ImageView) findViewById(R.id.profilePhotoEdit);

        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstname.getText().toString().equals("") || lastname.getText().toString().equals("")
                        || gender.getText().toString().equals("")
                        || password.getText().toString().equals("") || confirmp.getText().toString().equals("")) {

                    Toast.makeText(SignUpActivity.this, "Please fill in the details above", Toast.LENGTH_SHORT).show();
                }
                else if (!password.getText().toString().equals(confirmp.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Password and confirm Password do not match", Toast.LENGTH_SHORT).show();
                }
                else if(picture == null) {
                    Toast.makeText(SignUpActivity.this, "Please upload a photo", Toast.LENGTH_SHORT).show();
                }
                else {
                    ParseUser.logOut();

                    final ParseUser user = new ParseUser();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] d = stream.toByteArray();
                    final ParseFile file = new ParseFile("image.jpg", d);
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    user.put("imagefile", file);

                    user.setEmail(email.getText().toString());
                    user.setUsername(email.getText().toString());
                    user.put("Lastname", lastname.getText().toString());

                    user.setPassword(password.getText().toString());
                    user.put("Gender", gender.getText().toString());

                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (null == e) {
                                Log.d("demo", "inside savecallback");
                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        Log.d("demo", "inside done");
                                        if (e == null) {
                                            // Hooray! Let them use the app now.
                                            Log.d("demo", "sign up successful");
                                            Toast.makeText(SignUpActivity.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.d("demo", "error" + e);
                                            Log.d("demo", "sign up not successful");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
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
