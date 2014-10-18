package com.example.android.plitto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by batman on 18/10/14.
 */
public class FriendAdapter extends ArrayAdapter<FriendModel> {

    private List<FriendModel> itemList;
    private Context ctx;
    public ImageLoader imageLoader;

    public FriendAdapter(List<FriendModel> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_expandable_list_item_1, itemList);
        this.itemList = itemList;
        this.ctx = ctx;
        imageLoader = new ImageLoader(ctx);
    }

    public int getCount() {
        return itemList.size();
    }

    public FriendModel getItem(int position) {
        return itemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView name;
        public TextView left;
        public TextView right;
        public ImageView profile_pic;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.friend_layout, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) v.findViewById(R.id.name);
            holder.left=(TextView)v.findViewById(R.id.left_text);
            holder.right=(TextView)v.findViewById(R.id.right_text);
            holder.profile_pic=(ImageView)v.findViewById(R.id.profile_pic);

            v.setTag( holder );
        }
        else
            holder=(ViewHolder)v.getTag();

        FriendModel c = itemList.get(position);
        String id = c.getFbuid();
        holder.name.setText(c.getName());
        holder.left.setText(c.getShared());
        holder.right.setText(c.getDittoable());
        ImageView image = holder.profile_pic;
        imageLoader.DisplayImage("https://graph.facebook.com/"+id+"/picture/", image);
        return v;
    }

   public Drawable LoadImageFromWebOperations(String id) {
        String url = "http://graph.facebook.com/"+id+"/picture";
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.e("QQURL","ERROR");
            e.printStackTrace();
            return null;
        }
    }



}