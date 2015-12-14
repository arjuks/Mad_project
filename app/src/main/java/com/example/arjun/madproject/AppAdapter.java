package com.example.arjun.madproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arjun on 10/5/2015.
 */
public class AppAdapter extends ArrayAdapter<ParseUser>{


    Context mContext;
    int mResource;
    List<ParseUser> mData;
    List<ParseUser> plist = new ArrayList<>();
    //IData galleryactivity;
    ArrayList<ParseObject> list = new ArrayList<>();
    int flag = -1;

    public AppAdapter(Context context, int resource, List<ParseUser> objects) {
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
        ParseUser name = mData.get(position);

        TextView firstname = (TextView) convertView.findViewById(R.id.UserName);
        firstname.setText(name.get("name").toString());


        return convertView;
    }
}