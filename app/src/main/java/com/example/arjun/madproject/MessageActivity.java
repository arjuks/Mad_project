package com.example.arjun.madproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    final static String SNAME = "sname";
    final static String FULLNAME = "fullname";
    final static String RNAME = "rname";
    final static String TIME = "time";
    final static String IMG = "img";
    final static String MESSAGE = "message";
    final static String OBJID = "objId";
    final static String POS = "pos";
    static String EMAIL = "email";

    ArrayList<ParseObject> msglist = new ArrayList<>();
    ArrayList<ParseObject> finallist = new ArrayList<>();
    MessageAdapter adapter;
    String s_fname = null;
    String r_fname = null;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        if(!getIntent().getExtras().get(MainActivity.FULLNAME).toString().equals("")){
            s_fname = getIntent().getExtras().get(MainActivity.FULLNAME).toString();
//            r_fname = getIntent().getExtras().get(MainActivity.RNAME).toString();
//            EMAIL = getIntent().getExtras().get(MainActivity.SEMAIL).toString();
        }
        else {
            s_fname = getIntent().getExtras().get(ComposeActivity.CFNAME).toString();
            // s_lname = " ";
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> objects, com.parse.ParseException e) {
                msglist.addAll(objects);
//                Log.d("demo", "msg size 1" + finallist.size());
//                if (msglist.size() == 0) {
//                    Log.d("demo", "size is" + msglist.size());
//                }
                final ParseUser currentUser = ParseUser.getCurrentUser();

                for (int i = 0; i < msglist.size(); i++) {
                    if ((msglist.get(i).get("sender").equals(currentUser.getUsername() + " " + currentUser.get("Lastname"))
                            || msglist.get(i).get("recepient").equals(currentUser.getUsername() + " " + currentUser.get("Lastname")))
                            && (msglist.get(i).get("sender").equals(s_fname)
                            || msglist.get(i).get("recepient").equals(s_fname))) {

                        ParseObject msg = msglist.get(i);
                        finallist.add(msg);
                        Log.d("demo", "list msgs" + msg.getObjectId());
                    }
                }

                ListView lv = (ListView) findViewById(R.id.messagelistView);
                adapter = new MessageAdapter(MessageActivity.this, R.layout.messageitemlayout, finallist);
                lv.setAdapter(adapter);
                adapter.setNotifyOnChange(true);
            }
        });
        ListView lv = (ListView) findViewById(R.id.messagelistView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ParseUser currentUser = ParseUser.getCurrentUser();

                final Intent intent = new Intent(MessageActivity.this, MessageDisplayActivity.class);
                intent.putExtra(SNAME, finallist.get(position).get("sender").toString());
                intent.putExtra(RNAME, getIntent().getExtras().get(MainActivity.FULLNAME).toString());
                intent.putExtra(TIME, finallist.get(position).getCreatedAt().toString());
                intent.putExtra(MESSAGE, finallist.get(position).get("msg").toString());
                intent.putExtra(OBJID, finallist.get(position).getObjectId().toString());

                ParseFile img = (ParseFile)finallist.get(position).get("imagefile");
                if(img == null) {
                    Log.d("demo","no image");
                    startActivity(intent);
                }
                else {
                    img.getDataInBackground(new GetDataCallback() {
                        public void done(byte[] data, com.parse.ParseException e) {
                            if (e == null) {
                                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                                byte[] byteArray = bStream.toByteArray();
                                intent.putExtra(IMG, byteArray);
                                Log.d("demo","byte array"+byteArray);
                                startActivity(intent);
                            } else {
                                // something went wrong
                                Log.d("demo", "imag error" + e.getMessage());
                            }
                        }
                    });
                }


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
                Log.d("demo", "inmsg inbox");
                query.getInBackground(finallist.get(position).getObjectId().toString(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject obj, com.parse.ParseException e) {
                        if (e == null) {
                            if (finallist.get(position).get("recepient").toString()
                                    .equals(currentUser.getUsername().toString() + " " + currentUser.get("Lastname").toString())) {
                                String seen = "seen";
                                Log.d("demo", "seen before" + seen);
                                obj.put("read", seen);
                                obj.saveInBackground();
                            }
                                //Toast.makeText(MessageActivity.this, "saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                startActivity(intent);
            }
        });

       // if (finallist.size() == 0) {
            Log.d("demo", "msg size 2" + finallist.size());
            Button composebtn = (Button) findViewById(R.id.composeBtn);
//            composebtn.setVisibility(View.VISIBLE);
            composebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MessageActivity.this, ComposeActivity2.class);
                    intent.putExtra(FULLNAME, getIntent().getExtras().get(MainActivity.FULLNAME).toString());
                    startActivity(intent);
                }
            });
       // }
    }
}
