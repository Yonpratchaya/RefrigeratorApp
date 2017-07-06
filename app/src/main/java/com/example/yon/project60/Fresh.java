package com.example.yon.project60;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Yon on 30/6/2560.
 */

public class Fresh {
    private String fresh_list_id;
    private String fresh_name;
    private String amount;
    private String unit;
    private String exp;
    private Bitmap picture;

    public Fresh() {
    }

    public Fresh(String fresh_list_id, String fresh_name, String amount, String unit, String exp, Bitmap picture) {
        this.fresh_list_id = fresh_list_id;
        this.fresh_name = fresh_name;
        this.amount = amount;
        this.unit = unit;
        this.exp = exp;
        this.picture = picture;
    }
    //Getter
    public String getfresh_list_id() {
        return fresh_list_id;
    }
    public String getfresh_name() {
        return fresh_name;
    }
    public String getamount() {
        return amount;
    }
    public String getunit() {
        return unit;
    }
    public String getexp() {
        return exp;
    }
    public Bitmap getpicture() {
        return picture;
    }

    //Setter
    public void setfresh_list_id(String fresh_list_id) {
        this.fresh_list_id = fresh_list_id;
    }
    public void setfresh_name(String fresh_name) {
        this.fresh_name = fresh_name;
    }
    public void setamount(String amount) {
        this.amount = amount;
    }
    public void setunit(String unit) {
        this.unit = unit;
    }
    public void setexp(String exp) {
        this.exp = exp;
    }
    public void setpicture(Bitmap picture) {
        this.picture = picture;
    }

}