package com.example.yon.project60;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yon on 10/7/2560.
 */

public class leave_group extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private String[] group_id,group_names;

    SharedPreferences sharedPreferences;
    String user_id;
    ImageButton group1,group2,group3;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private static final String host_ip = "10.105.12.146";
    private static final String get_group_url = "http://" + host_ip + "/webapp/get_group.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_group);

        sharedPreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", null);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                leave_group.this,
                drawerLayout,
                R.string.open_menu,
                R.string.close_menu
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         //---------------getgroup-------------------
        final List<String> groupid_list = new ArrayList<String>();
        final List<String> group_list = new ArrayList<String>();
        group_id = new String[3];
        group_names = new String[3];
        //---------------------Getgroup------------------------------------------------------------
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_group_url + "?user_id=" + user_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");

                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);
                        groupid_list.add(jo.getString("group_id"));
                        group_list.add(jo.getString("group_name"));

                    }
                    for (int i = 0; i < groupid_list.size(); i++) {
                        group_id[i] = groupid_list.get(i);
                    }
                    for (int i = 0; i < group_list.size(); i++) {
                        group_names[i] = group_list.get(i);
                    }

                   /* int i = 0;
                    for (String temp : group_list) {
                        group_names[i] = temp;
                        i++;
                    }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    // Print Error!
                }
                Toast.makeText(leave_group.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);



        TextView txtgroup_name = (TextView) findViewById(R.id.txtgroup1);
        TextView txtgroup_name2 = (TextView) findViewById(R.id.txtgroup2);
        TextView txtgroup_name3 = (TextView) findViewById(R.id.txtgroup3);
        if(group_names[0] != null){
            txtgroup_name.setText("กลุ่ม:" + group_names[0]);
        }
        if(group_names[1] != null){
            txtgroup_name2.setText("กลุ่ม:" + group_names[1]);
        }
        if(group_names[2] != null){
            txtgroup_name3.setText("กลุ่ม:" + group_names[2]);
        }

        group1 = (ImageButton) findViewById(R.id.iconleavegroup1);
        group2 = (ImageButton) findViewById(R.id.iconleavegroup2);
        group3 = (ImageButton) findViewById(R.id.iconleavegroup3);
        leavegroup1();
        leavegroup2();
        leavegroup3();





    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                Intent intent3 = new Intent(leave_group.this, home_all.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.suggest:
                Intent intent4 = new Intent(leave_group.this, suggestlist_menu.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(leave_group.this, shoppinglist_menu.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(leave_group.this, group_menu.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(leave_group.this, MainActivity.class);
                startActivity(intent7);
                finish();
                return true;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onPostCreate(@Nullable Bundle saveInstanceState) {
        super.onPostCreate(saveInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // เก็บค่า id ของปุ่ม action butoon ที่กดเลือก
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                return true;
            case R.id.action_find:
                Intent intent2 = new Intent(this, find_menu.class);
                startActivity(intent2);
                //setContentView(R.layout.find_menu);
                return true;
        }

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    private void leavegroup1(){
        group1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group_names != null){
                    String type = "leavegroup";
                    BackgroundTask backgroundTask = new BackgroundTask(leave_group.this);
                    backgroundTask.execute(type,user_id,group_id[0],group_names[0]);
                }
                else
                    Toast.makeText(leave_group.this,"ไม่มีกลุ่มไม่สามารถออกกลุ่มได้",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void leavegroup2(){
        group2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group_names[1] != null){
                    String type = "leavegroup";
                    BackgroundTask backgroundTask = new BackgroundTask(leave_group.this);
                    backgroundTask.execute(type,user_id,group_id[1],group_names[1]);
                }
                else
                    Toast.makeText(leave_group.this,"ไม่มีกลุ่มไม่สามารถออกกลุ่มได้",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void leavegroup3(){
        group3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group_names[2] != null){
                    String type = "leavegroup";
                    BackgroundTask backgroundTask = new BackgroundTask(leave_group.this);
                    backgroundTask.execute(type,user_id,group_id[2],group_names[2]);
                }
                else
                    Toast.makeText(leave_group.this,"ไม่มีกลุ่มไม่สามารถออกกลุ่มได้",Toast.LENGTH_LONG).show();
            }
        });
    }


}
