package com.example.android.plitto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by batman on 11/10/14.
 */
public class SimpleAdapter extends ArrayAdapter<RowInfo> {

    private List<RowInfo> itemList;
    private Context ctx;

    public SimpleAdapter(List<RowInfo> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_1, itemList);
        this.itemList = itemList;
        this.ctx = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return (itemList.get(position).getId() - 1);

    }

    public RowInfo getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    public long getItemId(int position) {
        if (itemList != null)
            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        int type = getItemViewType(position);
        if (v == null) {
            // Inflate the layout according to the view type
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (type == 0) {
                v = inflater.inflate(R.layout.header, parent, false);
            } else if (type == 1) {
                v = inflater.inflate(R.layout.subheader, parent, false);
            } else {
                v = inflater.inflate(R.layout.main_content, parent, false);
            }
        }
        v.setTag(""+type);
        RowInfo c = itemList.get(position);
        TextView txt = (TextView) v.findViewById(R.id.txt);
        ImageView img = (ImageView) v.findViewById(R.id.img);
        if (type == 2) {
            TextView tvv = (TextView) v.findViewById(R.id.date);
            tvv.setText(c.getDate());

        }
        txt.setText(c.getName());
        txt.setTextSize(17);
        img.setImageResource(R.drawable.check_pending);
        return v;
    }

    public List<RowInfo> getItemList() {
        return itemList;
    }

    public void setItemList(List<RowInfo> itemList) {
        this.itemList = itemList;
    }


}