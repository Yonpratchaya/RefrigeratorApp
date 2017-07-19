package com.example.yon.project60;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class home_all extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedpreferences;
    private ImageButton buttonall;
    private ImageButton buttonmeat;
    private ImageButton buttonvegetable;
    private ImageButton buttonother;
    private static final String host_ip = "10.105.24.132";
    private static final String get_product_url = "http://" + host_ip + "/webapp/get_product.php";
    private static final String get_meat_url = "http://" + host_ip + "/webapp/get_meat.php";
    private static final String get_vegetable_url = "http://" + host_ip + "/webapp/get_vegetablesandfruits.php";
    private static final String get_other_url = "http://" + host_ip + "/webapp/get_other.php";
    private static final String get_group_url = "http://" + host_ip + "/webapp/get_group.php";

    private String user_id, user_name, join_leave_id, group_id;
    private List<Fresh> freshList = new ArrayList<Fresh>();
    private ListView listView;
    private CustomAdapter adapter;
    private Menu menu;
    private String mSelected = "ตู้เย็นของฉัน";
    private String[] group_names, group_ids, join_leave_ids;
    private int mSelectedIndex = 0;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;
    //Bitmap bitmap;
    List<Bitmap> bitmaps;
    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        setContentView(R.layout.home_all);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomAdapter(this, freshList);
        listView.setAdapter(adapter);
        Log.i("getlog", "onCreate");
        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            group_names = bundle.getString("group_name");}*//// รับค่า MyValue จาก BackgroundTask มา
        sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id", null);
        user_name = sharedpreferences.getString("user_name", null);
        join_leave_id = sharedpreferences.getString("join_leave_id", null);
        group_id = sharedpreferences.getString("group_id", null);
        i = 0;

        TextView txtuser_name = (TextView) findViewById(R.id.usernametext2);//-----setuser_id in intent Home_all
        txtuser_name.setText(user_name);
        TextView txtgroup_name = (TextView) findViewById(R.id.groupnametext2);
        txtgroup_name.setText(mSelected);

        // updateMenuTitles();//----updateMenuTitles

        //Set the fragment initially
        MainFragment fragment = new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        bitmaps = new ArrayList<Bitmap>();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                home_all.this,
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


        //---------------------List View--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_product_url + "?user_id=" + user_id +"&"+ "group_id=", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");

                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);

                        byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
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
                        fresh.setpicture(bitmap);
                        if (days <= 30) {
                            fresh.setexp(daysexe);
                        } else
                            fresh.setexp(jo.getString("exp"));

                        // adding movie to movies array
                        freshList.add(fresh);

                    }
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
                Toast.makeText(home_all.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);

//-----------------------------GetGroup----------------------------------------------------//
        getgroup();

//-----------------------------ButtonsTypes-------------------------------------------------//
        buttonall = (ImageButton) findViewById(R.id.buall);
        buttonmeat = (ImageButton) findViewById(R.id.bumeat);
        buttonvegetable = (ImageButton) findViewById(R.id.buvegetablesandfruits);
        buttonother = (ImageButton) findViewById(R.id.buother);

        showall();
        showmeat();
        showvegetableandfruit();
        showother();

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
        // เก็บค่า id ของปุ่ม action button ที่กดเลือก
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                Intent intent = new Intent(home_all.this, add_menu.class);
                String type = "getExpCalAvg";
                BackgroundTask backgroundTask = new BackgroundTask(this);
                backgroundTask.execute(type);
                startActivity(intent);
                finish();
                // setContentView(R.layout.add_menu);
                return true;
            case R.id.action_find:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(home_all.this);
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
                        Toast.makeText(getApplicationContext(), "เข้าสู่กลุ่ม " +
                                mSelected, Toast.LENGTH_SHORT).show();
                        if (mSelected.equals("ตู้เย็นของฉัน")) {
                            recreate();
                        } else {
                            String[] getgroup_name = mSelected.split(" ");
                            mSelected = getgroup_name[1];
                            TextView txtgroup_name = (TextView) findViewById(R.id.groupnametext2);
                            txtgroup_name.setText(mSelected);
                        }
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("join_leave_id", join_leave_ids[mSelectedIndex]);
                        editor.putString("group_id", group_ids[mSelectedIndex]);
                        editor.putString("group_name", mSelected);
                        editor.commit();
                        join_leave_id = sharedpreferences.getString("join_leave_id", null);
                        group_id = sharedpreferences.getString("group_id", null);
                        showProductGroup();


                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("ยกเลิก", null);
                builder.create();

                // สุดท้ายอย่าลืม show() ด้วย
                builder.show();
        }

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Log.i("getlog", "onCreateOptionsMenu");
        // ขยายเมนูให้แสดงใน action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
               /* Intent intent1 = new Intent(home_all.this, home_all.class);
                startActivity(intent1);
                finish();
                return true;*/
                MainFragment fragment = new MainFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                item.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.suggest:
                Intent intent4 = new Intent(home_all.this, suggestlist_menu.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(home_all.this, shoppinglist_menu.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(home_all.this, group_menu.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(home_all.this, MainActivity.class);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(intent7);
                finish();
                return true;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                Toast.makeText(home_all.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }

    private void showall() {
        buttonall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freshList.clear();
                adapter.notifyDataSetChanged();
                //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
                RequestQueue queue = Volley.newRequestQueue(home_all.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_product_url + "?user_id=" + user_id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONArray productArray = response.getJSONArray("result");

                            // properties from the JSONObjects
                            for (int i = 0; i < productArray.length(); i++) {
                                JSONObject jo = productArray.getJSONObject(i);

                                byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
                                String t = jo.getString("picture");
                                Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);


                                String datemenu = jo.getString("exp");
                                LocalDate datemenu2 = LocalDate.parse(datemenu);
                                LocalDate dateNow = LocalDate.now();
                                int days = Days.daysBetween(dateNow, datemenu2).getDays();
                                String daysexe = (days + " วัน");
                                // System.out.println(days);
                                //  System.out.println(dateNow);

                                Fresh fresh = new Fresh();
                                fresh.setfresh_list_id(jo.getString("fresh_list_id"));
                                fresh.setfresh_name(jo.getString("fresh_name"));
                                fresh.setamount(jo.getString("amount"));
                                fresh.setunit(jo.getString("unit"));
                                fresh.setpicture(bitmap);
                                if (days <= 30) {
                                    fresh.setexp(daysexe);
                                } else
                                    fresh.setexp(jo.getString("exp"));

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
                        Toast.makeText(home_all.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                });
                queue.add(jsonObjectRequest);
            }
        });
    }

    private void showmeat() {
        buttonmeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freshList.clear();
                adapter.notifyDataSetChanged();
                //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
                RequestQueue queue = Volley.newRequestQueue(home_all.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_meat_url + "?user_id=" + user_id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONArray productArray = response.getJSONArray("result");

                            // properties from the JSONObjects
                            for (int i = 0; i < productArray.length(); i++) {
                                JSONObject jo = productArray.getJSONObject(i);

                                byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
                                String t = jo.getString("picture");
                                Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);


                                String datemenu = jo.getString("exp");
                                LocalDate datemenu2 = LocalDate.parse(datemenu);
                                LocalDate dateNow = LocalDate.now();
                                int days = Days.daysBetween(dateNow, datemenu2).getDays();
                                String daysexe = (days + " วัน");
                                // System.out.println(days);
                                //  System.out.println(dateNow);

                                Fresh fresh = new Fresh();
                                fresh.setfresh_list_id(jo.getString("fresh_list_id"));
                                fresh.setfresh_name(jo.getString("fresh_name"));
                                fresh.setamount(jo.getString("amount"));
                                fresh.setunit(jo.getString("unit"));
                                fresh.setpicture(bitmap);
                                if (days <= 30) {
                                    fresh.setexp(daysexe);
                                } else
                                    fresh.setexp(jo.getString("exp"));

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
                        Toast.makeText(home_all.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                });
                queue.add(jsonObjectRequest);
            }
        });

    }

    private void showvegetableandfruit() {
        buttonvegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freshList.clear();
                adapter.notifyDataSetChanged();
                //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
                RequestQueue queue = Volley.newRequestQueue(home_all.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_vegetable_url + "?user_id=" + user_id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONArray productArray = response.getJSONArray("result");

                            // properties from the JSONObjects
                            for (int i = 0; i < productArray.length(); i++) {
                                JSONObject jo = productArray.getJSONObject(i);

                                byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
                                String t = jo.getString("picture");
                                Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);


                                String datemenu = jo.getString("exp");
                                LocalDate datemenu2 = LocalDate.parse(datemenu);
                                LocalDate dateNow = LocalDate.now();
                                int days = Days.daysBetween(dateNow, datemenu2).getDays();
                                String daysexe = (days + " วัน");
                                // System.out.println(days);
                                //  System.out.println(dateNow);

                                Fresh fresh = new Fresh();
                                fresh.setfresh_list_id(jo.getString("fresh_list_id"));
                                fresh.setfresh_name(jo.getString("fresh_name"));
                                fresh.setamount(jo.getString("amount"));
                                fresh.setunit(jo.getString("unit"));
                                fresh.setpicture(bitmap);
                                if (days <= 30) {
                                    fresh.setexp(daysexe);
                                } else
                                    fresh.setexp(jo.getString("exp"));

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
                        Toast.makeText(home_all.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                });
                queue.add(jsonObjectRequest);
            }
        });

    }

    private void showother() {
        buttonother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freshList.clear();
                adapter.notifyDataSetChanged();
                //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
                RequestQueue queue = Volley.newRequestQueue(home_all.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_other_url + "?user_id=" + user_id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONArray productArray = response.getJSONArray("result");

                            // properties from the JSONObjects
                            for (int i = 0; i < productArray.length(); i++) {
                                JSONObject jo = productArray.getJSONObject(i);

                                byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
                                String t = jo.getString("picture");
                                Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);


                                String datemenu = jo.getString("exp");
                                LocalDate datemenu2 = LocalDate.parse(datemenu);
                                LocalDate dateNow = LocalDate.now();
                                int days = Days.daysBetween(dateNow, datemenu2).getDays();
                                String daysexe = (days + " วัน");
                                // System.out.println(days);
                                //  System.out.println(dateNow);

                                Fresh fresh = new Fresh();
                                fresh.setfresh_list_id(jo.getString("fresh_list_id"));
                                fresh.setfresh_name(jo.getString("fresh_name"));
                                fresh.setamount(jo.getString("amount"));
                                fresh.setunit(jo.getString("unit"));
                                fresh.setpicture(bitmap);
                                if (days <= 30) {
                                    fresh.setexp(daysexe);
                                } else
                                    fresh.setexp(jo.getString("exp"));

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
                        Toast.makeText(home_all.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                });
                queue.add(jsonObjectRequest);
            }
        });

    }

    private void showProductGroup() {
        freshList.clear();
        adapter.notifyDataSetChanged();
        //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(home_all.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_product_url + "?user_id=" +"&"+ "group_id=" + group_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");

                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);

                        byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
                        String t = jo.getString("picture");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);


                        String datemenu = jo.getString("exp");
                        LocalDate datemenu2 = LocalDate.parse(datemenu);
                        LocalDate dateNow = LocalDate.now();
                        int days = Days.daysBetween(dateNow, datemenu2).getDays();
                        String daysexe = (days + " วัน");
                        // System.out.println(days);
                        //  System.out.println(dateNow);

                        Fresh fresh = new Fresh();
                        fresh.setfresh_list_id(jo.getString("fresh_list_id"));
                        fresh.setfresh_name(jo.getString("fresh_name"));
                        fresh.setamount(jo.getString("amount"));
                        fresh.setunit(jo.getString("unit"));
                        fresh.setpicture(bitmap);
                        if (days <= 30) {
                            fresh.setexp(daysexe);
                        } else
                            fresh.setexp(jo.getString("exp"));

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
                Toast.makeText(home_all.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);
    }


}