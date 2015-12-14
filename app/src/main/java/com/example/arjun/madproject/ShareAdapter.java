package com.example.arjun.madproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by batesjernigan on 12/13/15.
 */
public class ShareAdapter extends ArrayAdapter<ParseUser> {
    Context mContext;
    int mResource;
    ArrayList<ParseUser> users, usersWithPermission;

    public ShareAdapter(Context context, int resource, ArrayList<ParseUser> objects, ArrayList<ParseUser> permissionedUsers) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.users = objects;
        this.usersWithPermission = permissionedUsers;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        final ParseUser user = users.get(position);

        final ListView listView = (ListView) parent;

        final CheckBox userCheckBox = (CheckBox) convertView.findViewById(R.id.single_user_check_box);
        userCheckBox.setText(user.get("FullName").toString());

        if(usersWithPermission.contains(user)) {
            userCheckBox.setChecked(true);
            listView.setItemChecked(position, true);
        }

        userCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("demo", "in on set checked change listener");
                if(isChecked) {
                    Log.d("demo", "listview: " + listView);
                    listView.setItemChecked(position, true);
                    Log.d("demo", "is checked");
                    for(ParseUser checkedUser : users) {
                        String fullName = checkedUser.get("FullName").toString();
                        Log.d("demo", "usercheck box get text " + userCheckBox.getText().toString());
                        Log.d("demo", "checkedUser get fullname " + checkedUser.get("FullName"));
                        Log.d("demo", "full name equals cb: " + fullName.equals(userCheckBox.getText().toString()));
                        Log.d("demo", "checkedUser contains key " + checkedUser.containsKey(userCheckBox.getText().toString()));
                    }

//                    usersWithPermission.add()
                } else {
                    listView.setItemChecked(position, false);
                }
            }
        });
        return convertView;
    }
}
