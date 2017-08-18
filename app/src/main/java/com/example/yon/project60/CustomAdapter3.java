package com.example.yon.project60;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yon on 17/8/2560.
 */

public class CustomAdapter3 extends BaseAdapter {
    SharedPreferences sharedpreferences;
    private String user_id, group_id, group_name;

    private Activity activity;
    private LayoutInflater Inflater;
    private List<Fresh> freshItems;

    public CustomAdapter3(Activity activity, List<Fresh> freshItems) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CustomAdapter3.ViewHolder holder;
        if (Inflater == null) {
            Inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sharedpreferences = activity.getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
            user_id = sharedpreferences.getString("user_id", null);
            group_id = sharedpreferences.getString("group_id", "0");
            group_name = sharedpreferences.getString("group_name", null);
        }

        if (convertView == null) {
            convertView = Inflater.inflate(R.layout.item3, parent, false);
            holder = new CustomAdapter3.ViewHolder();
            holder.fresh_name = (TextView) convertView.findViewById(R.id.list_freshname);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            //rebind widget
            holder = (CustomAdapter3.ViewHolder) convertView.getTag();
        }

        // getting movie data for the row
        Fresh m = freshItems.get(position);

        holder.fresh_name.setText(m.getfresh_name());//Show name item
        String valueStatus = freshItems.get(position).getmenu_status();
        if (valueStatus.equals("1")) {//Show CheckBox Tick
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//update status
                String valuefresh_list_id = freshItems.get(position).getfresh_list_id();
                if (((CheckBox) v).isChecked()) {
                    String statusvalue = "1";
                    String type = "updatestatus_freshlist";
                    BackgroundTask backgroundTask = new BackgroundTask(activity);
                    backgroundTask.execute(type, user_id, group_id, valuefresh_list_id, statusvalue);
                    freshItems.get(position).setmenu_status("1");
                } else {
                    String statusvalue = "0";
                    String type = "updatestatus_freshlist";
                    BackgroundTask backgroundTask = new BackgroundTask(activity);
                    backgroundTask.execute(type, user_id, group_id, valuefresh_list_id, statusvalue);
                    freshItems.get(position).setmenu_status("0");
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView fresh_name;
        CheckBox checkBox;
    }

}