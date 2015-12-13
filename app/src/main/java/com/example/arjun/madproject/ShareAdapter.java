package com.example.arjun.madproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by batesjernigan on 12/13/15.
 */
public class ShareAdapter extends ArrayAdapter<ParseUser> {
    Context mContext;
    int mResource;
    List<ParseUser> users, usersWithPermission;

    public ShareAdapter(Context context, int resource, List<ParseUser> objects, List<ParseUser> permissionedUsers) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.users = objects;
        this.usersWithPermission = permissionedUsers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            Log.d("demo", "convert view == null");
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        final ParseUser user = users.get(position);

        CheckBox userCheckBox = (CheckBox) convertView.findViewById(R.id.single_user_check_box);
        userCheckBox.setText(user.get("FullName").toString());

        return convertView;
    }
}
