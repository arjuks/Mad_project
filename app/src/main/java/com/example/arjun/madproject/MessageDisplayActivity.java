package com.example.arjun.madproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageDisplayActivity extends AppCompatActivity {

    final static String RNAME = "rname";
    Bitmap bitmap;
    SimpleDateFormat format = new SimpleDateFormat("HH:MM MM/dd/yy ");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_display);

        TextView name = (TextView) findViewById(R.id.senderNameMsgDisplay);
        TextView time = (TextView) findViewById(R.id.timeMsgDisplayVal);
        TextView msg = (TextView) findViewById(R.id.MSgDisplayVal);
        Button reply = (Button) findViewById(R.id.ReplyBtn);
        final ImageView img = (ImageView) findViewById(R.id.imgDisplay);
        Button composedisplaymsg = (Button) findViewById(R.id.composeDisplayBtn);

        name.setText(getIntent().getExtras().get(MessageActivity.SNAME).toString());
        String d = format.format(Date.parse(getIntent().getExtras().get(MessageActivity.TIME).toString()));
        time.setText(d);
        msg.setText(getIntent().getExtras().get(MessageActivity.MESSAGE).toString());
        byte[] imgdata = getIntent().getByteArrayExtra(MessageActivity.IMG);
        Log.d("demo", "byte array 2" + imgdata);
        if(imgdata != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
            img.setImageBitmap(bmp);
        }

        if(getIntent().getExtras().get(MessageActivity.SNAME).toString().equals(ParseUser.getCurrentUser().getUsername().toString())) {
            reply.setVisibility(View.INVISIBLE);
            composedisplaymsg.setVisibility(View.VISIBLE);
        }
        else {
            reply.setVisibility(View.VISIBLE);
            composedisplaymsg.setVisibility(View.INVISIBLE);
        }

        composedisplaymsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageDisplayActivity.this, ComposeActivity.class);
                intent.putExtra(RNAME, getIntent().getExtras().get(MessageActivity.RNAME).toString());
                startActivity(intent);
                finish();
            }
        });

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageDisplayActivity.this, ComposeActivity.class);
                intent.putExtra(RNAME,getIntent().getExtras().get(MessageActivity.RNAME).toString());
                startActivity(intent);
                finish();
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
            Intent intent = new Intent(MessageDisplayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        if (id == R.id.logout) {
            Log.d("demo", "logout clicked");

            String objId2 = ParseInstallation.getCurrentInstallation().getObjectId();
            ParseQuery<ParseInstallation> query2 = ParseInstallation.getQuery();
            Log.d("demo", "cuser"+ParseUser.getCurrentUser());
            query2.getInBackground(objId2, new GetCallback<ParseInstallation>() {
                @Override
                public void done(ParseInstallation obj, com.parse.ParseException e) {
                    if (e == null) {
                        obj.put("user", "loggedOut");
                        obj.saveInBackground();
                    }
                }
            });
            ParseTwitterUtils.unlinkInBackground(ParseUser.getCurrentUser());
            ParseUser.logOut();
            finish();
            Intent intent = new Intent(MessageDisplayActivity.this, LoginActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
