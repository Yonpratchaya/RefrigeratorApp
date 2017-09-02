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
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.action;


public class home_all extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedpreferences;
    private ImageButton buttonall;
    private ImageButton buttonmeat;
    private ImageButton buttonvegetable;
    private ImageButton buttonother;
    private static final String host_ip = "35.186.157.180";
    private static final String get_product_url = "http://" + host_ip + "/webapp/get_product.php";
    private static final String get_meat_url = "http://" + host_ip + "/webapp/get_meat.php";
    private static final String get_vegetable_url = "http://" + host_ip + "/webapp/get_vegetablesandfruits.php";
    private static final String get_other_url = "http://" + host_ip + "/webapp/get_other.php";
    private static final String get_group_url = "http://" + host_ip + "/webapp/get_group.php";
    private static final String get_expandcal_url = "http://" + host_ip + "/webapp/query_exp_cal.php";
    private static final String get_search_url = "http://" + host_ip + "/webapp/get_search.php";
    private String Values_url = get_product_url;

    private String user_id, user_name, join_leave_id, group_id, group_name;
    private List<Fresh> freshList = new ArrayList<Fresh>();
    private ListView listView;
    private CustomAdapter adapter;
    private Menu menu;
    private String mSelected;
    private String[] group_names, group_ids, join_leave_ids;
    private int mSelectedIndex;
    private String[] dialogitems = {"เพิ่มเข้าชอปปิงลิสต์", "แก้ไขรายการ", "ลบรายการ"};
    private String[] listindex, fresh_name_index, amount_index, unit_index, typename_index, exp_index; //Listindex คือ Fresh_List_Id
    private List<String> getfresh_list_id, getfresh_name, getamount, getunit, gettype_name, getexp;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FragmentTransaction fragmentTransaction;
    //Bitmap bitmap;
    List<Bitmap> bitmaps;
    int i;

    ArrayAdapter<String> dataAdapter;
    MaterialSearchView searchView;
    private String GetTextSearch, searchuser_id, searchgroup_id;
    int countvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this);
        setContentView(R.layout.home_all);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomAdapter(this, freshList);
        listView.setAdapter(adapter);
        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            group_names = bundle.getString("group_name");}*//// รับค่า MyValue จาก BackgroundTask มา
        sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id", null);
        user_name = sharedpreferences.getString("user_name", null);
        sharedpreferencesOfGroup();
        i = 0;


       /* TextView txtuser_name = (TextView) findViewById(R.id.usernametext2);//-----setuser_id in intent Home_all
        txtuser_name.setText(user_name);
        TextView txtgroup_name = (TextView) findViewById(R.id.groupnametext2);
        txtgroup_name.setText(mSelected);*/
        setTitle(mSelected);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_all.this, add_menu.class);
                startActivity(intent);
            }
        });

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


//-----------------------------ShowItemListView--------------------------------------------//
        if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
            showItemMyself();
        } else {
            setTitle(group_name);
            showItemGroup();
        }
//-----------------------------AlertdialogListview-----------------------------------------//
        getdialogList();
//-----------------------------GetGroup----------------------------------------------------//
        getgroup();
//-----------------------------GetExpandCalAvg----------------------------------------------------//
        getExpandCalAvg();

//-----------------------------ButtonsTypes-------------------------------------------------//
        buttonall = (ImageButton) findViewById(R.id.buall);
        buttonmeat = (ImageButton) findViewById(R.id.bumeat);
        buttonvegetable = (ImageButton) findViewById(R.id.buvegetablesandfruits);
        buttonother = (ImageButton) findViewById(R.id.buother);

        showall();
        showmeat();
        showvegetableandfruit();
        showother();

//-----------------------------SearchView---------------------------------------------------//
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                GetTextSearch = query;
                search();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                if(newText.equals("")) {
                    searchView.setAdapter(null);
                }
                else{
                    searchView.setAdapter(dataAdapter);
                    }
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
                    searchuser_id = user_id;
                    searchgroup_id = "";
                    getAutocomplete();//โหลดข้อมูลจากfreashlistมาค้นหา
                } else {
                    searchuser_id = "";
                    searchgroup_id = group_id;
                    getAutocomplete();//โหลดข้อมูลจากfreashlistมาค้นหา
                }
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(home_all.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                searchView.setQuery(parent.getItemAtPosition(position).toString(), false);
                searchView.setAdapter(null);
            }
        });

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
            case R.id.action_search:

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
                            setTitle(mSelected);
                            showItemMyself();
                        } else {
                            //String[] getgroup_name = mSelected.split(" ");
                            //mSelected = getgroup_name[1];
                            setTitle(mSelected);
                            showItemGroup();
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
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

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
                drawerLayout.closeDrawers();
                // finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(home_all.this, shoppinglist_menu.class);
                startActivity(intent5);
                drawerLayout.closeDrawers();
                //   finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(home_all.this, group_menu.class);
                startActivity(intent6);
                drawerLayout.closeDrawers();
                //     finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(home_all.this, MainActivity.class);
                sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
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

    private void getExpandCalAvg() {
        final List<String> Exp_list = new ArrayList<String>();
        final List<String> Cal_list = new ArrayList<String>();
        //---------------------GetExpandCalAvg------------------------------------------------------------
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_expandcal_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");
                    //Log.i("valueis:",productArray.toString(productArray.length()));
                    // properties from the JSONObjects
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);
                        Exp_list.add(jo.getString("type_avg_exp"));
                        Cal_list.add(jo.getString("type_avg_cal"));
                    }
                    String[] Exp_array = Exp_list.toArray(new String[Exp_list.size()]);
                    String[] Cal_array = Cal_list.toArray(new String[Cal_list.size()]);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("expmeat", Exp_array[0]);
                    editor.putString("expvetg", Exp_array[1]);
                    editor.putString("expother", Exp_array[2]);
                    editor.putString("calmeat", Cal_array[0]);
                    editor.putString("calvetg", Cal_array[1]);
                    editor.putString("calother", Cal_array[2]);
                    editor.commit();
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
                Values_url = get_product_url;
                if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
                    showItemMyself();
                } else {
                    showItemGroup();
                }
            }
        });
    }

    private void showmeat() {
        buttonmeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Values_url = get_meat_url;
                if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
                    showItemMyself();
                } else {
                    showItemGroup();
                }
            }
        });

    }

    private void showvegetableandfruit() {
        buttonvegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Values_url = get_vegetable_url;
                if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
                    showItemMyself();
                } else {
                    showItemGroup();
                }
            }
        });

    }

    private void showother() {
        buttonother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Values_url = get_other_url;
                if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
                    showItemMyself();
                } else {
                    showItemGroup();
                }
            }
        });

    }

    private void showItemMyself() {
        freshList.clear();
        adapter.notifyDataSetChanged();
        getfresh_list_id = new ArrayList<String>();
        getfresh_name = new ArrayList<String>();
        getamount = new ArrayList<String>();
        getunit = new ArrayList<String>();
        gettype_name = new ArrayList<String>();
        getexp = new ArrayList<String>();
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

                        byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
                        String t = jo.getString("picture");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);


                        String datemenu = jo.getString("exp");
                        LocalDate datemenu2 = LocalDate.parse(datemenu);
                        LocalDate dateNow = LocalDate.now();
                        int days = Days.daysBetween(dateNow, datemenu2).getDays();
                        String daysexe = String.valueOf(days);

                        getfresh_list_id.add(jo.getString("fresh_list_id"));
                        getfresh_name.add(jo.getString("fresh_name"));
                        getamount.add(jo.getString("amount"));
                        getunit.add(jo.getString("unit"));
                        gettype_name.add(jo.getString("type_name"));
                        getexp.add(jo.getString("exp"));

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
                        freshList.add(fresh);

                    }
                    listView.setAdapter(adapter);
                    listindex = getfresh_list_id.toArray(new String[getfresh_list_id.size()]);
                    fresh_name_index = getfresh_name.toArray(new String[getfresh_name.size()]);//----แก้ไขรายการ
                    amount_index = getamount.toArray(new String[getamount.size()]);
                    unit_index = getunit.toArray(new String[getunit.size()]);
                    typename_index = gettype_name.toArray(new String[gettype_name.size()]);
                    exp_index = getexp.toArray(new String[getexp.size()]);
                    //Log.i("showitem", "value is" + Arrays.toString(listindex));
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

    private void showItemGroup() {
        freshList.clear();
        adapter.notifyDataSetChanged();
        getfresh_list_id = new ArrayList<String>();
        getfresh_name = new ArrayList<String>();
        getamount = new ArrayList<String>();
        getunit = new ArrayList<String>();
        gettype_name = new ArrayList<String>();
        getexp = new ArrayList<String>();
        //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(home_all.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Values_url + "?user_id=" + "&" + "group_id=" + group_id, null, new Response.Listener<JSONObject>() {
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
                        String daysexe = String.valueOf(days);
                        // System.out.println(days);
                        //  System.out.println(dateNow);
                        getfresh_list_id.add(jo.getString("fresh_list_id"));
                        getfresh_name.add(jo.getString("fresh_name"));
                        getamount.add(jo.getString("amount"));
                        getunit.add(jo.getString("unit"));
                        gettype_name.add(jo.getString("type_name"));
                        getexp.add(jo.getString("exp"));

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
                        freshList.add(fresh);
                    }
                    listView.setAdapter(adapter);
                    listindex = getfresh_list_id.toArray(new String[getfresh_list_id.size()]);
                    fresh_name_index = getfresh_name.toArray(new String[getfresh_name.size()]);//----แก้ไขรายการ
                    amount_index = getamount.toArray(new String[getamount.size()]);
                    unit_index = getunit.toArray(new String[getunit.size()]);
                    typename_index = gettype_name.toArray(new String[gettype_name.size()]);
                    exp_index = getexp.toArray(new String[getexp.size()]);
                    // Log.i("showitem", "value is" + Arrays.toString(listindex));
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

    private void sharedpreferencesOfGroup() {
        join_leave_id = sharedpreferences.getString("join_leave_id", null);
        group_id = sharedpreferences.getString("group_id", null);
        group_name = sharedpreferences.getString("group_name", null);
        mSelected = sharedpreferences.getString("group_name", "ตู้เย็นของฉัน");
        mSelectedIndex = sharedpreferences.getInt("mSelectedIndex", 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
        else {
            super.onBackPressed();
        }
    }

    private void getAutocomplete() {
        final List<String> list = new ArrayList<String>();
        //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(home_all.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_product_url + "?user_id=" + searchuser_id + "&" + "group_id=" + searchgroup_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);
                        String a = jo.getString("fresh_name");
                        list.add(a);
                    }
                    ///------------------------------Autoconplete-----------------------------
                    dataAdapter = new ArrayAdapter<String>(home_all.this, android.R.layout.simple_dropdown_item_1line, list);
                   /* MaterialSearchView searchView = (MaterialSearchView) findViewById(R.id.search_view);
                    searchView.setAdapter(dataAdapter);*/

                   /* AutoCompleteTextView autocomplete = (AutoCompleteTextView) home_all.this.findViewById(R.id.edtSearch);
                    autocomplete.setAdapter(dataAdapter);*/

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

    private void search() {
        freshList.clear();
        adapter.notifyDataSetChanged();
        //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
        RequestQueue queue = Volley.newRequestQueue(home_all.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_search_url + "?user_id=" + searchuser_id + "&" + "group_id=" + searchgroup_id + "&" + "word_search=" + GetTextSearch, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);

                        byte[] ba2 = Base64.decode(jo.getString("picture"), Base64.DEFAULT);
                        String t = jo.getString("picture");
                        Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);

                        countvalue += 1;

                        String datemenu = jo.getString("exp");
                        LocalDate datemenu2 = LocalDate.parse(datemenu);
                        LocalDate dateNow = LocalDate.now();
                        int days = Days.daysBetween(dateNow, datemenu2).getDays();
                        String daysexe = String.valueOf(days);

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
                        freshList.add(fresh);

                    }
                    // Show รายการที่ Search
                    GetTextSearch = null;
                    listView.setAdapter(adapter);
                    if (countvalue == 0) {
                        Toast.makeText(home_all.this, "ไม่มีรายการในตู้เย็น", Toast.LENGTH_LONG).show();
                    }
                    countvalue = 0;


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

    private void getdialogList() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(home_all.this);
                builder.setTitle("เมนู");
                builder.setItems(dialogitems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = dialogitems[which];
                        if (selected.equals("แก้ไขรายการ")) {
                            String ValFreshlistID = listindex[position];
                            String ValFreshName = fresh_name_index[position];
                            String ValAmount = amount_index[position];
                            String Valunit = unit_index[position];
                            String ValTypeName = typename_index[position];
                            String Valexp = exp_index[position];
                            Intent intent = new Intent(home_all.this, rectify.class);
                            intent.putExtra("ValFreshlistID", ValFreshlistID);
                            intent.putExtra("ValFreshName", ValFreshName);
                            intent.putExtra("ValAmount", ValAmount);
                            intent.putExtra("Valunit", Valunit);
                            intent.putExtra("ValTypeName", ValTypeName);
                            intent.putExtra("Valexp", Valexp);
                            startActivity(intent);

                        } else if (selected.equals("ลบรายการ")) {

                            AlertDialog.Builder builder =
                                    new AlertDialog.Builder(home_all.this);
                            builder.setMessage("ลบรายการนี้ออกจากตู้เย็น?");
                            builder.setPositiveButton("ลบ", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String ValFreshlistID = listindex[position];
                                    String type = "DelFreshList";
                                    BackgroundTask backgroundTask = new BackgroundTask(home_all.this);
                                    if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
                                        group_id = "0";
                                        backgroundTask.execute(type, user_id, group_id, ValFreshlistID); //
                                        //---Delay Time Before ShowItem
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Do something after 5s = 5000ms
                                                showItemMyself();
                                            }
                                        }, 300);

                                    } else {
                                        backgroundTask.execute(type, user_id, group_id, ValFreshlistID); //
                                        //---Delay Time Before ShowItem
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Do something after 5s = 5000ms
                                                showItemGroup();
                                            }
                                        }, 300);

                                    }
                                }
                            });
                            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        } else if (selected.equals("เพิ่มเข้าชอปปิงลิสต์")) {
                            String ValFreshName = fresh_name_index[position];
                            String type = "shoppinglist";
                            BackgroundTask backgroundTask = new BackgroundTask(home_all.this);
                            if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
                                group_id = "0";
                                backgroundTask.execute(type, ValFreshName, "0", user_id, group_id);
                                showItemMyself();
                            } else {
                                backgroundTask.execute(type, ValFreshName, "0", user_id, group_id);
                                showItemGroup();
                            }
                        }

                    }
                });
                builder.create();

                // สุดท้ายอย่าลืม show() ด้วย
                builder.show();
                return false;
            }
        });
    }

}