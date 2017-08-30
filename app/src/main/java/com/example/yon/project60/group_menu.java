package com.example.yon.project60;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
 * Created by Yon on 26/5/2560.
 */

public class group_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedpreferences;
    private static final String host_ip = "10.105.13.146";
    private static final String get_group_url = "http://" + host_ip + "/webapp/get_group.php";

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    ImageView Join, Create, Leave;
    private String user_id, join_leave_id, group_id, group_name;
    private String mSelected = "ตู้เย็นของฉัน";
    private String[] group_names, group_ids, join_leave_ids;
    private int mSelectedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_menu);

        sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id", null);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                group_menu.this,
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

        Join = (ImageView) findViewById(R.id.JoinGroup);
        Create = (ImageView) findViewById(R.id.CreateGroup);
        Leave = (ImageView) findViewById(R.id.LeaveGroup);

        join();
        create();
        leave();

        //-----------------------------GetGroup----------------------------------------------------//
        getgroup();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                Intent intent3 = new Intent(group_menu.this, home_all.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.suggest:
                Intent intent4 = new Intent(group_menu.this, suggestlist_menu.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(group_menu.this, shoppinglist_menu.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(group_menu.this, group_menu.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(group_menu.this, MainActivity.class);
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
            case R.id.fab:
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

    private void join() {
        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(group_menu.this, join_group.class);
                startActivity(intent);
            }
        });
    }

    private void create() {
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(group_menu.this, create_group.class);
                startActivity(intent);
            }
        });
    }

    private void leave() {
        Leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(group_menu.this);
                builder.setTitle("ออกจากกลุ่ม");


                builder.setSingleChoiceItems(group_names, mSelectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelected = group_names[which];
                        mSelectedIndex = which;
                    }
                });
                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
                        if (group_names.length == 0) {
                            dialog.dismiss();
                        } else {
                            if (mSelected.equals("ตู้เย็นของฉัน")) {
                            } else
                                Toast.makeText(getApplicationContext(), "ออกจากกลุ่ม " +
                                        mSelected, Toast.LENGTH_SHORT).show();

                     /*       String[] getgroup_name = mSelected.split(" ");
                            mSelected = getgroup_name[1];*/
                            group_id = group_ids[mSelectedIndex];
                            String type = "leavegroup";
                            BackgroundTask backgroundTask = new BackgroundTask(group_menu.this);
                            backgroundTask.execute(type, user_id, group_id);

                            dialog.dismiss();
                            recreate();
                        }
                    }
                });

                builder.setNegativeButton("ยกเลิก", null);
                builder.create();

                // สุดท้ายอย่าลืม show() ด้วย
                builder.show();
            }
        });
    }

    private void getgroup() {
        final List<String> group_list = new ArrayList<String>();
        final List<String> group_list2 = new ArrayList<String>();
        final List<String> group_list3 = new ArrayList<String>();
     /*   group_list3.add("0");
        group_list2.add("0");
        group_list.add("ตู้เย็นของฉัน");*/
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
                        group_list.add("ตู้เย็นของ " + jo.getString("group_name"));
                        group_list2.add(jo.getString("group_id"));
                        group_list3.add(jo.getString("join_leave_id"));
                    }
                    group_names = group_list.toArray(new String[group_list.size()]);
                    group_ids = group_list2.toArray(new String[group_list2.size()]);
                    join_leave_ids = group_list3.toArray(new String[group_list3.size()]);
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
                Toast.makeText(group_menu.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }
}
