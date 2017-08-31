package com.example.yon.project60;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
 * Created by Yon on 22/2/2560.
 */

public class suggestlist_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedpreferences;
    String user_id, group_id, group_name, join_leave_id;
    private List<Fresh> freshList = new ArrayList<Fresh>();
    private ListView listView;
    private CustomAdapter3 adapter;

    private static final String host_ip = "35.186.157.180";
    private static final String get_product_url = "http://" + host_ip + "/webapp/get_product.php";
    private static final String get_group_url = "http://" + host_ip + "/webapp/get_group.php";
    private static final String get_test_url = "http://" + host_ip + "/webapp/test.php";

    private TextView txtlist;
    private String mSelected;
    private String[] group_names, group_ids, join_leave_ids;
    private int mSelectedIndex;
    private Menu menu;

    private String[] fresh_name_index, menu_status_index;
    private List<String> getfresh_name, getfreshlist_status;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestlist_menu);
        listView = (ListView) findViewById(R.id.list3);
        adapter = new CustomAdapter3(this, freshList);
        listView.setAdapter(adapter);

        sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id", null);
        group_id = sharedpreferences.getString("group_id", null);
        group_name = sharedpreferences.getString("group_name", null);
        mSelected = sharedpreferences.getString("group_name", "ตู้เย็นของฉัน");

        /*txtlist = (TextView) findViewById(R.id.suggest_txt);
        txtlist.setText("เลือกรายการเพื่อแนะนำเมนูอาหาร");*/
        setTitle(mSelected);// setTitle

        //-------------------------------นำรายการที่เลือกไปยังหน้าแนะนำอาหาร-----------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getfresh_name = new ArrayList<String>();
                getfreshlist_status = new ArrayList<String>();
                for (int i = 0; i < freshList.size(); i++) {
                    Fresh m = freshList.get(i);
                    if (m.getmenu_status().equals("1")) {
                        getfresh_name.add(m.getfresh_name()+",");
                        getfreshlist_status.add(m.getmenu_status());
                    }
                }
                fresh_name_index = getfresh_name.toArray(new String[getfresh_name.size()]);//----ชื่อรายการFreshname
                menu_status_index = getfreshlist_status.toArray(new String[getfreshlist_status.size()]);//----สถานะFreshlist
               /* for (int i = 0; i < getfresh_name.size(); i++) {
                    Toast.makeText(suggestlist_menu.this, getfresh_name.get(i), Toast.LENGTH_SHORT).show();
                    Log.i("valueis:", getfresh_name.get(i) + getfreshlist_status.get(i));
                }*/
                Intent intent = new Intent(suggestlist_menu.this,suggestlist_menu2.class);
                intent.putExtra("freshname",fresh_name_index);
                startActivity(intent);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                suggestlist_menu.this,
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

//-----------------------------ShowItemListView--------------------------------------------//
        if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
            getfreshlist_Myself();
            //showSuggestlist_Myself();
        } else {
            setTitle(group_name);
            getfreshlist_Group();
            //showItemGroup();
        }
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
                Intent intent3 = new Intent(suggestlist_menu.this, home_all.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.suggest:
                Intent intent4 = new Intent(suggestlist_menu.this, suggestlist_menu.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(suggestlist_menu.this, shoppinglist_menu.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(suggestlist_menu.this, group_menu.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(suggestlist_menu.this, MainActivity.class);
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
            case R.id.action_find:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(suggestlist_menu.this);
                builder.setTitle("เลือกกลุ่ม");


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
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("join_leave_id", join_leave_ids[mSelectedIndex]);
                        editor.putString("group_id", group_ids[mSelectedIndex]);
                        editor.putString("group_name", mSelected);
                        editor.putInt("mSelectedIndex", mSelectedIndex);
                        editor.commit();
                        sharedpreferencesOfGroup();
                        Toast.makeText(getApplicationContext(), "เข้าสู่กลุ่ม " +
                                mSelected, Toast.LENGTH_SHORT).show();
                        if (mSelected.equals("ตู้เย็นของฉัน")) {
                            //txtlist.setText("แนะนำรายการอาหาร ตู้เย็นของฉัน");
                            getfreshlist_Myself();
                        } else {
                            //txtlist.setText("แนะนำรายการอาหาร "+ group_name);
                            getfreshlist_Group();
                        }

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("ยกเลิก", null);
                builder.create();

                // สุดท้ายอย่าลืม show() ด้วย
                builder.show();
                return true;
        }

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void sharedpreferencesOfGroup() {
        join_leave_id = sharedpreferences.getString("join_leave_id", null);
        group_id = sharedpreferences.getString("group_id", null);
        group_name = sharedpreferences.getString("group_name", null);
        mSelected = sharedpreferences.getString("group_name", "ตู้เย็นของฉัน");
        mSelectedIndex = sharedpreferences.getInt("mSelectedIndex", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        //Log.i("getlog", "onCreateOptionsMenu");
        // ขยายเมนูให้แสดงใน action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_shoppinglist, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void getgroup() {
        final List<String> group_list = new ArrayList<String>();
        final List<String> group_list2 = new ArrayList<String>();
        final List<String> group_list3 = new ArrayList<String>();
        group_list3.add("0");
        group_list2.add("0");
        group_list.add("ตู้เย็นของฉัน");
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
                Toast.makeText(suggestlist_menu.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }

    private void getfreshlist_Myself() {
        freshList.clear();
        adapter.notifyDataSetChanged();
        //---------------------List View--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_product_url + "?user_id=" + user_id + "&" + "group_id=", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");

                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);

                        Fresh fresh = new Fresh();
                        fresh.setfresh_list_id(jo.getString("fresh_list_id"));
                        fresh.setfresh_name(jo.getString("fresh_name"));
                        fresh.setmenu_status(jo.getString("menu_status"));

                        // adding movie to movies array
                        freshList.add(fresh);

                    }
                    listView.setAdapter(adapter);

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
                Toast.makeText(suggestlist_menu.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }

    private void getfreshlist_Group() {
        freshList.clear();
        adapter.notifyDataSetChanged();
        //---------------------List View--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_product_url + "?user_id=" + "&" + "group_id=" + group_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");

                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);

                        Fresh fresh = new Fresh();
                        fresh.setfresh_list_id(jo.getString("fresh_list_id"));
                        fresh.setfresh_name(jo.getString("fresh_name"));
                        fresh.setmenu_status(jo.getString("menu_status"));

                        // adding movie to movies array
                        freshList.add(fresh);

                    }
                    listView.setAdapter(adapter);

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
                Toast.makeText(suggestlist_menu.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }
}
