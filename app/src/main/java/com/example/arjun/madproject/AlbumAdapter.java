package com.example.arjun.madproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by batesjernigan on 12/7/15.
 */
public class AlbumAdapter extends ArrayAdapter<ParseObject> {

    Context mContext;
    int mResource;
    List<ParseObject> mData;

    public AlbumAdapter(Context context, int resource, List<ParseObject> data) {
        super(context, resource, data);
        this.mContext = context;
        this.mResource = resource;
        this.mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("demo", "in get view");
        if(convertView == null) {
            Log.d("demo", "convert view == null");
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        final ParseObject album = mData.get(position);

        TextView albumOwner = (TextView) convertView.findViewById(R.id.albumOwner);
        TextView albumDateModified = (TextView) convertView.findViewById(R.id.albumDateModified);
        TextView albumName = (TextView) convertView.findViewById(R.id.albumName);
        final ImageView coverImage = (ImageView) convertView.findViewById(R.id.albumCoverPhoto);

        albumOwner.setText(album.get("username").toString());
        Log.d("demo", "album: " + album.toString());
        albumDateModified.setText(album.getUpdatedAt().toString());
        albumName.setText(album.get("name").toString());

        List<String> pictureIdList = album.getList("pictureIdList");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Picture");
        query.getInBackground(pictureIdList.get(0), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.d("demo", "after query done");
                if(e == null) {
                    Log.d("demo", "e == null");
                    Log.d("demo", "parse object id: " + parseObject.getObjectId());
                    ParseFile img = parseObject.getParseFile("file");
                    Log.d("demo", "parse file: " + img);

                    Picasso.with(mContext).load(img.getUrl()).into(coverImage);

//                    coverImage.setParseFile(img);
//                    coverImage.loadInBackground();
                } else {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }
}
