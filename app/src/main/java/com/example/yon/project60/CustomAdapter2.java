package com.example.yon.project60;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.ContentValues.TAG;
import static com.example.yon.project60.R.id.list;

/**
 * Created by Yon on 9/8/2560.
 */

public class CustomAdapter2 extends BaseAdapter {
    SharedPreferences sharedpreferences;
    private String user_id, group_id, group_name;

    private Activity activity;
    private LayoutInflater Inflater;
    private List<Fresh> freshItems;

    public CustomAdapter2(Activity activity, List<Fresh> freshItems) {
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
        final CustomAdapter2.ViewHolder holder;
        if (Inflater == null) {
            Inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sharedpreferences = activity.getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
            user_id = sharedpreferences.getString("user_id", null);
            group_id = sharedpreferences.getString("group_id", "0");
            group_name = sharedpreferences.getString("group_name", null);
        }

        if (convertView == null) {
            convertView = Inflater.inflate(R.layout.item2, parent, false);
            holder = new CustomAdapter2.ViewHolder();
            holder.Shop_name = (TextView) convertView.findViewById(R.id.itemshopping);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.budel);
            holder.button = (Button) convertView.findViewById(R.id.buttonToadd);
            convertView.setTag(holder);
        } else {
            //rebind widget
            holder = (CustomAdapter2.ViewHolder) convertView.getTag();
        }

        // getting movie data for the row
        Fresh m = freshItems.get(position);

        holder.Shop_name.setText(m.getshop_name());//Show name item
        String valueStatus = freshItems.get(position).getstatus();
        if (valueStatus.equals("1")) {//Show CheckBox Tick
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//update status
                String valueShop_id = freshItems.get(position).getshop_id();
                if (((CheckBox) v).isChecked()) {
                    String statusvalue = "1";
                    String type = "updatestatus";
                    BackgroundTask backgroundTask = new BackgroundTask(activity);
                    backgroundTask.execute(type, user_id, group_id, valueShop_id, statusvalue);
                    freshItems.get(position).setstatus("1");
                } else {
                    String statusvalue = "0";
                    String type = "updatestatus";
                    BackgroundTask backgroundTask = new BackgroundTask(activity);
                    backgroundTask.execute(type, user_id, group_id, valueShop_id, statusvalue);
                    freshItems.get(position).setstatus("0");
                }
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Delete item
                String valueShop_id = freshItems.get(position).getshop_id();
                //Log.i("valueis:", valueShop_id);
                String type = "DelShoppingList";
                BackgroundTask backgroundTask = new BackgroundTask(activity);
                backgroundTask.execute(type, user_id, group_id, valueShop_id);
                freshItems.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valueShop_id = freshItems.get(position).getshop_id();
                String valueShop_name = freshItems.get(position).getshop_name();
                String valueStatus = freshItems.get(position).getstatus();
                if (valueStatus.equals("1")) {//Show CheckBox Tick
                    Intent intent = new Intent(activity, add_menu.class);
                    intent.putExtra("shop_id", valueShop_id);
                    intent.putExtra("shop_name", valueShop_name);
                    activity.startActivityForResult(intent,1);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView Shop_name;
        CheckBox checkBox;
        ImageButton imageButton;
        Button button;
    }

}