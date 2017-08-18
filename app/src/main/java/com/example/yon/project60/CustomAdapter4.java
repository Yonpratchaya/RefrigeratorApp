package com.example.yon.project60;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yon on 18/8/2560.
 */

public class CustomAdapter4 extends BaseAdapter {


    private Activity activity;
    private LayoutInflater Inflater;
    private List<Fresh> freshItems;

    public CustomAdapter4(Activity activity, List<Fresh> freshItems) {
        this.activity = activity;
        this.freshItems = freshItems;
    }

    @Override
    public int getCount() {
        return freshItems.size();
    }

    @Override
    public Object getItem(int position) {
        return freshItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomAdapter4.ViewHolder holder;
        if (Inflater == null)
            Inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = Inflater.inflate(R.layout.item4, parent, false);
            holder = new CustomAdapter4.ViewHolder();
            // holder.Order = (TextView) convertView.findViewById(R.id.rowid);
            holder.Picture = (ImageView) convertView.findViewById(R.id.image_Layout);
            holder.Name = (TextView) convertView.findViewById(R.id.content);
            holder.Name2 = (TextView) convertView.findViewById(R.id.content2);
            convertView.setTag(holder);
        } else {
            //rebind widget
            holder = (CustomAdapter4.ViewHolder) convertView.getTag();
        }

        // getting movie data for the row
        Fresh m = freshItems.get(position);

       // holder.Picture.setImageBitmap(m.getpic_menu());
        holder.Name.setText(m.gettitle_menu());
        holder.Name2.setText(m.geturl_menu());

        return convertView;
    }

    public class ViewHolder {
        ImageView Picture;
        TextView Name;
        TextView Name2;

    }
}
