package com.example.yon.project60;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yon on 22/2/2560.
 */

public class shoppinglist_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedpreferences;
    private static final String host_ip = "10.105.27.105";
    private static final String get_shoppinglist_url = "http://" + host_ip + "/webapp/get_shopping_list.php";
    private static final String get_group_url = "http://" + host_ip + "/webapp/get_group.php";
    private String Values_url = get_shoppinglist_url;

    private String user_id, group_id, group_name, status, join_leave_id;
    private List<Fresh> freshList = new ArrayList<Fresh>();
    private ListView listView;
    private CustomAdapter2 adapter;

    private TextView txtlist;

    private String mSelected;
    private String[] group_names, group_ids, join_leave_ids;
    private int mSelectedIndex;
    private Menu menu;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppinglist_menu);
        listView = (ListView) findViewById(R.id.list2);
        adapter = new CustomAdapter2(this, freshList);
        listView.setAdapter(adapter);

        setTitle("Shopping List");
        sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id", null);
        group_id = sharedpreferences.getString("group_id", null);
        group_name = sharedpreferences.getString("group_name",null);
        mSelected = sharedpreferences.getString("group_name", "ตู้เย็นของฉัน");
        mSelectedIndex = sharedpreferences.getInt("mSelectedIndex", 0);

        txtlist = (TextView) findViewById(R.id.text1);
        if(group_name == null || group_name.equals("ตู้เย็นของฉัน")){
            txtlist.setText("รายการสินค้า ตู้เย็นของฉัน");
        }else{
            txtlist.setText("รายการสินค้า "+ group_name);
        }

        //-------------------------------เพิ่มรายการshoppinglist-----------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder =
                        new AlertDialog.Builder(shoppinglist_menu.this);
                LayoutInflater inflater = getLayoutInflater();

                View views = inflater.inflate(R.layout.dialog_custom, null);
                builder.setView(views);

                final EditText list = (EditText) views.findViewById(R.id.edtlist);

                builder.setMessage("เพิ่มรายการไปยังชอปปิงลิสต์");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String namelist = list.getText().toString();
                        status = "0";
                        String type = "shoppinglist";
                        BackgroundTask backgroundTask = new BackgroundTask(shoppinglist_menu.this);
                        if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
                            group_id = "0";
                            backgroundTask.execute(type, namelist, status, user_id, group_id);
                            showShoppingListMyself();
                        } else {
                            backgroundTask.execute(type, namelist, status, user_id, group_id);
                            showShoppingListGroup();
                        }
                        dialog.dismiss();
                      // recreate();
                    }
                });
                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                shoppinglist_menu.this,
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
            showShoppingListMyself();
        } else {
            showShoppingListGroup();
        }
//-----------------------------GetGroup----------------------------------------------------//
        getgroup();




    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                Intent intent3 = new Intent(shoppinglist_menu.this, home_all.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.suggest:
                Intent intent4 = new Intent(shoppinglist_menu.this, suggestlist_menu.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(shoppinglist_menu.this, shoppinglist_menu.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(shoppinglist_menu.this, group_menu.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(shoppinglist_menu.this, MainActivity.class);
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
                        new AlertDialog.Builder(shoppinglist_menu.this);
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
                            txtlist.setText("รายการสินค้า ตู้เย็นของฉัน");
                            showShoppingListMyself();
                        } else {
                            txtlist.setText("รายการสินค้า "+ group_name);
                            //String[] getgroup_name = mSelected.split(" ");
                            //mSelected = getgroup_name[1];
                            showShoppingListGroup();
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
                Toast.makeText(shoppinglist_menu.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }

    private void sharedpreferencesOfGroup() {
        join_leave_id = sharedpreferences.getString("join_leave_id", null);
        group_id = sharedpreferences.getString("group_id", null);
        group_name = sharedpreferences.getString("group_name", null);
        mSelected = sharedpreferences.getString("group_name", "ตู้เย็นของฉัน");
        mSelectedIndex = sharedpreferences.getInt("mSelectedIndex", 0);
    }

    public void showShoppingListMyself() {
        freshList.clear();
        adapter.notifyDataSetChanged();
        //---------------------List View--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Values_url + "?user_id=" + user_id + "&" + "group_id=", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");

                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);

                        Fresh fresh = new Fresh();
                        fresh.setshop_id(jo.getString("shop_id"));
                        fresh.setshop_name(jo.getString("shop_name"));
                        fresh.setstatus(jo.getString("status"));

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
                Toast.makeText(shoppinglist_menu.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }
    public void showShoppingListGroup(){
        freshList.clear();
        adapter.notifyDataSetChanged();
        //---------------------List View--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Values_url + "?user_id=" + "&" + "group_id=" + group_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");

                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);

                        Fresh fresh = new Fresh();
                        fresh.setshop_id(jo.getString("shop_id"));
                        fresh.setshop_name(jo.getString("shop_name"));
                        fresh.setstatus(jo.getString("status"));

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
                Toast.makeText(shoppinglist_menu.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, shoppinglist_menu.class);
            Toast.makeText(this,"เพิ่มไปยังรายการของสดเสร็จสิ้น".toString(),Toast.LENGTH_LONG).show();
            startActivity(refresh);
            this.finish();
        }
    }
}
