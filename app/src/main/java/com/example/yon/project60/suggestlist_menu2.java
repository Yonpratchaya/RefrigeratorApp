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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
 * Created by Yon on 18/8/2560.
 */

public class suggestlist_menu2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedpreferences;
    String user_id, group_id, group_name, join_leave_id;
    private List<Fresh> freshList = new ArrayList<Fresh>();
    private ListView listView;
    private CustomAdapter4 adapter;

    private static final String host_ip = "10.105.27.105";
    private static final String get_product_url = "http://" + host_ip + "/webapp/get_product.php";
    private static final String get_group_url = "http://" + host_ip + "/webapp/get_group.php";
    private static final String get_test_url = "http://" + host_ip + "/webapp/test.php";

    private TextView txtlist;
    private String mSelected;
    private String[] group_names, group_ids, join_leave_ids;
    private int mSelectedIndex;
    private Menu menu;

    private String[] fresh_name,menu_status;
    private List<String> getfresh_name,getfreshlist_status;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    ArrayAdapter<String> dataAdapter;
    private String GetTextSearch, searchuser_id, searchgroup_id;
    int countvalue;
    private String[] listindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggestlist_menu);
        listView = (ListView) findViewById(R.id.list4);
        adapter = new CustomAdapter4(this, freshList);
     //   listView.setAdapter(adapter);

        sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id", null);
        group_id = sharedpreferences.getString("group_id", null);
        group_name = sharedpreferences.getString("group_name", null);
        mSelected = sharedpreferences.getString("group_name", "ตู้เย็นของฉัน");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fresh_name = bundle.getStringArray("freshname");
        }
        if (fresh_name!=null){
            for (int i = 0; i < fresh_name.length; i++) {
                Log.i("valueis:", fresh_name[i]);
            }
        }

        txtlist = (TextView) findViewById(R.id.suggest_txt);
        txtlist.setText("เมนูอาหาร");
        setTitle(mSelected);// setTitle

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                suggestlist_menu2.this,
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
            //showSuggestlist_Myself();
        } else {
            setTitle(group_name);
            //showItemGroup();
        }



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
                Intent intent3 = new Intent(suggestlist_menu2.this, home_all.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.suggest:
                Intent intent4 = new Intent(suggestlist_menu2.this, suggestlist_menu.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(suggestlist_menu2.this, shoppinglist_menu.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(suggestlist_menu2.this, group_menu.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(suggestlist_menu2.this, MainActivity.class);
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

    private void showSuggestlist_Myself() {
        freshList.clear();
        adapter.notifyDataSetChanged();
        //---------------------List View--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_test_url + "?user_id=" + user_id + "&" + "group_id=", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");

                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);

                   /*     byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
                        String t = jo.getString("picture");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);


                        String datemenu = jo.getString("exp");
                        LocalDate datemenu2 = LocalDate.parse(datemenu);
                        LocalDate dateNow = LocalDate.now();
                        int days = Days.daysBetween(dateNow, datemenu2).getDays();
                        String daysexe = (days + " วัน");

                        Fresh fresh = new Fresh();
                        fresh.setfresh_list_id(jo.getString("fresh_list_id"));
                        fresh.setfresh_name(jo.getString("fresh_name"));
                        fresh.setamount(jo.getString("amount"));
                        fresh.setunit(jo.getString("unit"));
                        fresh.setcal(jo.getString("calories"));
                        fresh.setpicture(bitmap);
                        if (days <= 30) {
                            fresh.setexp(daysexe);
                        } else
                            fresh.setexp(jo.getString("exp"));

                        // adding movie to movies array
                        freshList.add(fresh);*/

                    }
                    /*listView.setAdapter(adapter);
                    Log.i("showitem", "value is");*/
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
                Toast.makeText(suggestlist_menu2.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }

    private void showItemGroup(){

    }
}
