package com.example.arjun.madproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by batesjernigan on 12/9/15.
 */
public class NewAlbumAdapter extends ArrayAdapter<Bitmap>{
    Context mContext;
    int mResource;
    List<Bitmap> images;

    public NewAlbumAdapter(Context context, int resource, List<Bitmap> objects) {
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

        imageView.setImageBitmap(images.get(position));
        return convertView;
    }
}