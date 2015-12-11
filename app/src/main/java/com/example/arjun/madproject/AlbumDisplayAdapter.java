package com.example.arjun.madproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by batesjernigan on 12/11/15.
 */
public class AlbumDisplayAdapter extends ArrayAdapter<ParseObject> {

    Context mContext;
    int mResource;
    List<ParseObject> images;

    public AlbumDisplayAdapter(Context context, int resource, List<ParseObject> objects) {
        super(context, resource, objects);
        this.images = objects;
        this.mResource = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.newAlbumSinglePicture);
        ParseObject pictures = images.get(position);
        ParseFile image = pictures.getParseFile("file");
        Picasso.with(mContext).load(image.getUrl()).into(imageView);
        return convertView;
    }
}
