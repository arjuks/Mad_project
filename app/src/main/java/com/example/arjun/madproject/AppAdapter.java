//package com.example.arjun.madproject;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.parse.FindCallback;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//import com.parse.ParseUser;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by arjun on 10/5/2015.
// */
//public class AppAdapter extends ArrayAdapter<Photos>{
//
//
//    Context mContext;
//    int mResource;
//    List<Photos> mData;
//    List<Photos> plist = new ArrayList<>();
//    //IData galleryactivity;
//    ArrayList<ParseObject> list = new ArrayList<>();
//    int flag = -1;
//
//    public AppAdapter(Context context, int resource, List<Photos> objects) {
//        super(context, resource, objects);
//
//        this.mData = objects;
//        this.mContext = context;
//        this.mResource = resource;
//        // galleryactivity = (IData) context;
//    }
//
//    @Override // this is for every row in the list view
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        if(convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(mResource,parent,false);
//        }
//        Photos name = mData.get(position);
//
//        TextView photoname = (TextView) convertView.findViewById(R.id.photo_title);
//        photoname.setText(name.getTitle());
//        Picasso.with(mContext).load(name.getUrl()).into((ImageView) convertView.findViewById(R.id.imageView));
//
////
////        ImageView iv = (ImageView) convertView.findViewById(R.id.deleteimg);
////        iv.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                ParseObject st = new ParseObject("SavedTitles");
////                st.put("title",mData.get(position).getTitle());
////                st.put("savedBy", ParseUser.getCurrentUser());
////                st.saveInBackground();
////                mData.remove(position);
////                plist.addAll(mData);
////                Toast.makeText(mContext, "Title saved successfully", Toast.LENGTH_SHORT).show();
////                galleryactivity.setMainView((ArrayList<Photos>) plist);
////
////            }
////        });
//
//        final ImageView iv = (ImageView) convertView.findViewById(R.id.starimg);
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Favourites");
//            query.include("savedBy");
//            query.findInBackground(new FindCallback<ParseObject>() {
//                public void done(List<ParseObject> objects, com.parse.ParseException e) {
//                    if (e == null) {
//                        Log.d("score", "Retrieved " + objects.size() + " objects");
////                        Log.d("demo", "title" + objects.get(0).get("title"));
//                        list.addAll(objects);
//////
////                        ParseObject name = objects. ;
////                        ParseUser user = name.getParseUser("savedBy");
//////                        String photourl = (String) name.get("photo_item");
////                       // Log.d("demo", "photo" + name.get("photo_item"));
////                        try {
////                            Log.d("demo", "createdBy" + user.fetchIfNeeded().get("fullName"));
////                        } catch (com.parse.ParseException e1) {
////                            e1.printStackTrace();
////                        }
//                        String clickedphoto = mData.get(position).getUrl();
//
//                        if(list.size() == 0) {
//                            ParseObject fav = new ParseObject("FavoriteList");
//                            fav.put("photo_item",mData.get(position).getUrl());
//                            fav.put("photo_title",mData.get(position).getTitle());
//                            fav.put("savedBy", ParseUser.getCurrentUser());
//                            fav.saveInBackground();
//                            Drawable drawable = mContext.getResources().getDrawable(R.drawable.goldstar);
//                            iv.setImageDrawable(drawable);
//                            Toast.makeText(mContext, "Photo saved as favourite", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        else if(list.size() !=0) {
//                                for (int i = 0; i < list.size(); i++) {
//                                if (list.get(i).get("photo_item").equals(clickedphoto)) {
//                                    ParseObject na = list.get(i);
//                                    na.deleteInBackground();
//                                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.greystar);
//                                    iv.setImageDrawable(drawable);
//                                    Toast.makeText(mContext, "Photo deleted", Toast.LENGTH_SHORT).show();
//                                    flag = 0;
//                                }
//                                else {
//                                    flag = 1;
//                                }
//                            }
//                            if(flag == 1) {
//                                ParseObject fav = new ParseObject("FavoriteList");
//                                fav.put("photo_title",mData.get(position).getTitle());
//                                fav.put("photo_item",mData.get(position).getUrl());
//                                fav.put("savedBy", ParseUser.getCurrentUser());
//                                fav.saveInBackground();
//                                Drawable drawable = mContext.getResources().getDrawable(R.drawable.goldstar);
//                                iv.setImageDrawable(drawable);
//                                Toast.makeText(mContext, "Photo saved as favourite", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    } else {
//                        Log.d("score", "Error: " + e.getMessage());
//                    }
//                }
//            });
//
//
////                Log.d("demo","star clicked"+ position);
////                ParseObject fav = new ParseObject("Favourites");
////                fav.put("photo_item",mData.get(position).getUrl());
////                fav.put("savedBy", ParseUser.getCurrentUser());
////                fav.saveInBackground();
////                Drawable drawable = mContext.getResources().getDrawable(R.drawable.goldstar);
////                iv.setImageDrawable(drawable);
////                Toast.makeText(mContext, "Photo saved as favourite", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//
//
//////
////        iv.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.d("demo", "button click");
////                View parentRow = (View) v.getParent();
////                ListView listView = (ListView) parentRow.getParent();
////                final int pos = listView.getPositionForView(parentRow);
////                Log.d("demo", "position" + pos);
////
////                String title = GalleryActivity.apps.get(pos).getTitle().toString();
////                String owner_name = GalleryActivity.apps.get(pos).getOwner_name().toString();
////                String date = GalleryActivity.apps.get(pos).getTimetaken().toString();
////
////                Photos p = new Photos();
////                p.setTitle(title);
////                p.setOwner_name(owner_name);
////                p.setTimetaken(date);
////
////                plist.add(p);
////
////                SharedPreferences.Editor editor = mContext.getSharedPreferences("final_list",
////                        mContext.MODE_PRIVATE).edit();
////                Gson gson = new Gson();
////                String jsonFavorites = gson.toJson(plist);
////                editor.putString("FAVORITES", jsonFavorites);
////                editor.commit();
////
////                Log.d("demo","added to sharedpref");
////            }
////        });
//
//
//
//        return convertView;
//    }
//
////    static public interface IData{
////        public void setMainView(ArrayList<Photos> items);
////    }
//}