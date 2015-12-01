package com.example.arjun.madproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arjun on 10/5/2015.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject>{


    Context mContext;
    int mResource;
    List<ParseObject> mData;
    List<ParseObject> plist = new ArrayList<>();
    //IData galleryactivity;
    ArrayList<ParseObject> list = new ArrayList<>();
    int flag = -1;
    Bitmap bitmap;

    public MessageAdapter(Context context, int resource, List<ParseObject> objects) {
        super(context, resource, objects);

        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
        // galleryactivity = (IData) context;
    }

    @Override // this is for every row in the list view
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        final ParseObject name = mData.get(position);
        ParseUser currentUser = ParseUser.getCurrentUser();

        TextView sendername = (TextView) convertView.findViewById(R.id.senderName);
        sendername.setText(name.get("sender").toString());
        TextView msgcontent = (TextView) convertView.findViewById(R.id.msgContent);
        msgcontent.setText(name.get("msg").toString());
        TextView time = (TextView) convertView.findViewById(R.id.timeSent);
        time.setText(name.getCreatedAt().toString());
        ImageView iv = (ImageView) convertView.findViewById(R.id.deleteImg);
        final ImageView photo = (ImageView) convertView.findViewById(R.id.msgImg);

        ParseFile img = (ParseFile)name.get("imagefile");
        if(img == null) {
            Log.d("demo","no image");
        }
        else {
            img.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, com.parse.ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        Log.d("demo", "barray" + data);
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        photo.setImageBitmap(bitmap);
                    } else {
                        // something went wrong
                        Log.d("demo", "imag error" + e.getMessage());
                    }
                }
            });
        }

        if(!(name.get("sender").toString().equals(currentUser.getUsername().toString()+" "+currentUser.get("Lastname").toString())) &&
                name.get("read").toString().equals("notseen")){
            convertView.setBackgroundColor(Color.GREEN);
            //iv.setVisibility(View.VISIBLE);
        }
        else {
            convertView.setBackgroundColor(Color.WHITE);
        }

//        if(name.get("sender").toString().equals(currentUser.getUsername().toString()+" "+currentUser.get("Lastname").toString())
//                && MainActivity.semail.toString().equals(currentUser.getEmail().toString())) {
//            //iv.setVisibility(View.INVISIBLE);
//
//            Log.d("demo","match");
//
//        }
//        else if(name.get("read").toString().equals("notseen")){
//
//            Log.d("demo","no match");
//            convertView.setBackgroundColor(Color.GREEN);
//        }

        if(name.get("sender").toString().equals(currentUser.getUsername().toString()+" "+currentUser.get("Lastname").toString())
                ) {
            iv.setVisibility(View.INVISIBLE);
        }
        else {
            iv.setVisibility(View.VISIBLE);

        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "click pos"+position);
                name.deleteInBackground();
                Toast.makeText(mContext, "Message Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}