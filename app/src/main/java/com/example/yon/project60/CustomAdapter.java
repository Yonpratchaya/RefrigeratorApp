package com.example.yon.project60;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yon on 30/6/2560.
 */

public class CustomAdapter extends BaseAdapter {


    private Activity activity;
    private LayoutInflater Inflater;
    private List<Fresh> freshItems;

    public CustomAdapter(Activity activity, List<Fresh> freshItems) {
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
        ViewHolder holder;
        if (Inflater == null)
            Inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = Inflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            // holder.Order = (TextView) convertView.findViewById(R.id.rowid);
            holder.Picture = (ImageView) convertView.findViewById(R.id.image_Layout);
            holder.Name = (TextView) convertView.findViewById(R.id.content);
            holder.Amount = (TextView) convertView.findViewById(R.id.content2);
            holder.Exp = (TextView) convertView.findViewById(R.id.content3);
            convertView.setTag(holder);
        } else {
            //rebind widget
            holder = (ViewHolder) convertView.getTag();
        }

        // getting movie data for the row
        Fresh m = freshItems.get(position);
        //---------check วันหมดอายุ------------------//
        String checkexp = m.getexp();

        if(Integer.parseInt(checkexp) < 0){
            holder.Exp.setTextColor(Color.RED);
            String[] showexp = checkexp.split("-");
            holder.Exp.setText("หมดอายุแล้ว"+"\n"+showexp[1]+" วัน");
        }else if (Integer.parseInt(checkexp) <= 3){
            holder.Exp.setTextColor(Color.parseColor("#FFC400"));
            holder.Exp.setText(checkexp + " วัน");
        }else{
            holder.Exp.setTextColor(Color.parseColor("#64DD17"));
            holder.Exp.setText(checkexp + " วัน");
        }
        // holder.Order.setText(m.getfresh_list_id());
        holder.Name.setText(m.getfresh_name());
        holder.Amount.setText(m.getamount() + m.getunit()+"\n"+m.getcal()+"แคลต่อ100กรัม");
       // holder.Exp.setText(m.getexp());
        holder.Picture.setImageBitmap(m.getpicture());

        return convertView;
    }

    public class ViewHolder {
        TextView Order;
        ImageView Picture;
        TextView Name;
        TextView Amount;
        TextView Exp;
    }
}

