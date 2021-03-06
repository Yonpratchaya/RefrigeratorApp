package com.example.yon.project60;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Environment;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Manifest;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.text.InputType;
import android.widget.DatePicker;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.yon.project60.R.id.autocompleteadd;
import static com.example.yon.project60.R.id.imagepicture;
import static com.example.yon.project60.R.id.radioButton1;


/**
 * Created by Yon on 22/2/2560.
 */

public class add_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedpreferences;
    String user_id, expcheck, typecheck, b, ExpAvgMeat, ExpAvgVetg, ExpAvgOther, CalAvgMeat, CalAvgVetg, CalAvgOther;
    EditText ET_FRESH_NAME, ET_AMOUNT, ET_EXP, ET_PICTURE;
    String Fresh_Name, Amount, S_Unit, S_Type_name, Exp, Calorie, a1;
    Spinner UNIT, TYPENAME;
    LocalDate dateNow;
    int avgdays;
    private String join_leave_id, group_id, group_name;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private Spinner unit;
    private Spinner type;
    //radiobutton
    TextView selectdb, avgexp, txtadd;

    //UI References
    private EditText DateEtxt;
    private DatePickerDialog DatePickerDialog;
    private SimpleDateFormat dateFormatter;
    //Camera
    private static final int CAMERA_REQUEST = 1888;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private ImageView imageView;
    String ba1;
    String mCurrentPhotoPath;
    Bitmap image_freshbase;
    ///**************-----*-*-*-*-*-*-* Autocomplete ดึงฐานข้อมูลจาก freshbase-*-*-*-*-*-*-*-*-*-*-*-*-*-
    private static final String host_ip = "35.198.241.100";
    private static final String get_baseAdd = "http://" + host_ip + "/webapp/get_baseAdd.php";
    private static final String get_basePicture = "http://" + host_ip + "/webapp/get_basePicture.php";

    // Autocomplete
    private AutoCompleteTextView Autocomplete;
    String aaa = null;
    private List<String> list;
    private List<String> listb;
    private List<String> listc;
    private List<String> picture_freshbase;
    String freshname,pictureall;
    AlertDialog alertDialog;
    String shop_id, shop_name; //Comefrom Theadd of Shoppinglist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_menu);

        sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id", null);
        join_leave_id = sharedpreferences.getString("join_leave_id", null);
        group_id = sharedpreferences.getString("group_id", null);
        group_name = sharedpreferences.getString("group_name", null);
        ExpAvgMeat = sharedpreferences.getString("expmeat", null);
        ExpAvgVetg = sharedpreferences.getString("expvetg", null);
        ExpAvgOther = sharedpreferences.getString("expother", null);
        CalAvgMeat = sharedpreferences.getString("calmeat", null);
        CalAvgVetg = sharedpreferences.getString("calvetg", null);
        CalAvgOther = sharedpreferences.getString("calother", null);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            shop_id = bundle.getString("shop_id");
            shop_name = bundle.getString("shop_name");
        }
        // Log.i("valueis:",shop_id + " " + shop_name);
        txtadd = (TextView) findViewById(R.id.text1);
        if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
            txtadd.setText("เพิ่มรายการ ตู้เย็นของฉัน");
        } else {
            txtadd.setText("เพิ่มรายการ " + group_name);
        }
        //Spinner
        spinner();

        //Datedialog
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        defaultsetdate();
     /*   setDateTimeField(); */
        //Radiobutton
        defaultradiobutton();
        selectdb = (TextView) (findViewById(R.id.output));
        selectdb.setEnabled(false);
        avgexp = (TextView) (findViewById(R.id.avgoutput));
        avgexp.setEnabled(false);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                add_menu.this,
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


        Autocomplete = (AutoCompleteTextView) findViewById(autocompleteadd);
        ET_AMOUNT = (EditText) findViewById(R.id.editText2);
        ET_EXP = (EditText) findViewById(R.id.editTextdate);
        UNIT = (Spinner) findViewById(R.id.spinner1);
        TYPENAME = (Spinner) findViewById(R.id.spinner2);
        if (shop_id != null) {
            Autocomplete.setText(shop_name);
        }
        //---------------------เลือกวันหมดอายุจากฐานข้อมูลและแคลลอรี------------------------------//
        dateNow = LocalDate.now();
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getExpDateFromDatabase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //---------------------------Premise Camera-----------------------------------------
        //---------------------------CaptureImage-------------------------------------------
        this.imageView = (ImageView) this.findViewById(R.id.imagepicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();

            }
        });


        ///------------------------------Autocomplete-----------------------------
        Autocomplete = (AutoCompleteTextView) findViewById(autocompleteadd);
        list = new ArrayList<String>();
        listb = new ArrayList<String>();
        listc = new ArrayList<String>();


        Autocomplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //---------------------List viwe--------*****--*-*-*-*-*-*-*-*-
                RequestQueue queue = Volley.newRequestQueue(add_menu.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_baseAdd + "?user_id=" + user_id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONArray productArray = response.getJSONArray("result");
                            for (int i = 0; i < productArray.length(); i++) {
                                JSONObject jo = productArray.getJSONObject(i);
                                String a = jo.getString("fresh_name");
                                String b = jo.getString("cal_gram");
                                String c = jo.getString("exp");
                                //String d = jo.getString("picture");
                                list.add(a);
                                listb.add(b);
                                listc.add(c);
                                //picture_freshbase.add(d);
                            }
                            ///------------------------------Autoconplete-----------------------------
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(add_menu.this, android.R.layout.simple_dropdown_item_1line, list);
                            AutoCompleteTextView autocomplete = (AutoCompleteTextView) add_menu.this.findViewById(autocompleteadd);
                            autocomplete.setAdapter(dataAdapter);

                            aaa = Autocomplete.getText().toString();

                           /* byte[] ba2 = Base64.decode(picture_freshbase.get(0), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);
                            imageView.setImageBitmap(bitmap);*/

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
                        Toast.makeText(add_menu.this, error.toString(), Toast.LENGTH_LONG).show();
                    }

                });
                queue.add(jsonObjectRequest);
            }
        });

        //---------------------Load Picture from Fresh_base-------------------------------------------------------------//
        picture_freshbase = new ArrayList<String>();
        RequestQueue queue = Volley.newRequestQueue(add_menu.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_basePicture, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(home_all.this,response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONArray productArray = response.getJSONArray("result");
                    for (int i = 0; i < productArray.length(); i++) {
                        JSONObject jo = productArray.getJSONObject(i);
                        String d = jo.getString("picture");
                        picture_freshbase.add(d);
                    }
                    /*byte[] ba2 = Base64.decode(picture_freshbase.get(0), Base64.DEFAULT);
                    image_freshbase = BitmapFactory.decodeByteArray(ba2, 0, ba2.length);
                    imageView.setImageBitmap(bitmap);*/

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
                Toast.makeText(add_menu.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });
        queue.add(jsonObjectRequest);


    }

    public void spinner() {
        unit = (Spinner) findViewById(R.id.spinner1);
        type = (Spinner) findViewById(R.id.spinner2);

        final String[] Spinner1 = getResources().getStringArray(R.array.unitarray);
        ArrayAdapter<String> adapterSpinner1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Spinner1);
        unit.setAdapter(adapterSpinner1);

        final String[] Spinner2 = getResources().getStringArray(R.array.typearray);
        ArrayAdapter<String> adapterSpinner2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Spinner2);
        type.setAdapter(adapterSpinner2);
    }

    public void AddMenu(View view) {//------------------------------------------Addmenu-------------
        if (group_name == null || group_name.equals("ตู้เย็นของฉัน")) {
            join_leave_id = "0";
            group_id = "0";
            addedmenu();
        } else {
            addedmenu();
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
                alertDialog = new AlertDialog.Builder(add_menu.this).create();
                alertDialog.setMessage("สามารถค้นหารายการได้ในหน้าแรก");
                alertDialog.show();
                return true;
            case R.id.action_find:
                Intent intent2 = new Intent(this, home_all.class);
                startActivity(intent2);
                finish();
                //setContentView(R.layout.find_menu);
                return true;
        }

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                Intent intent3 = new Intent(add_menu.this, home_all.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.suggest:
                Intent intent4 = new Intent(add_menu.this, suggestlist_menu.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(add_menu.this, shoppinglist_menu.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(add_menu.this, group_menu.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(add_menu.this, MainActivity.class);
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

    private void defaultsetdate() {
        DateEtxt = (EditText) findViewById(R.id.editTextdate);
        LocalDate dateNow = LocalDate.now();
        LocalDate datethen = dateNow.plusDays(7);
        String defaultdate = datethen.toString("yyyy-MM-dd");
        DateEtxt.setText(defaultdate);
        DateEtxt.setInputType(InputType.TYPE_NULL);
    }

    public void defaultradiobutton() {
        RadioButton radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton1.setChecked(true);
        DateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == DateEtxt) {
                    DatePickerDialog.show();
                }

            }
        });
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                DateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        expcheck = "1";
    }

    public void rbClick(View view) {//---------------------------------------------------------------Radio Buttonclick-----------
        // Is the button now checked?
        Boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButton1:
                if (checked) {
                    selectdb.setText("");
                    avgexp.setText("");
                    DateEtxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (v == DateEtxt) {
                                DatePickerDialog.show();
                            }

                        }
                    });


                    Calendar newCalendar = Calendar.getInstance();
                    DatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            DateEtxt.setText(dateFormatter.format(newDate.getTime()));
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                    expcheck = "1";
                }
                break;
            case R.id.radioButton2:
                if (checked) {
                    //selectdb.setText("เลือกวันหมดอายุจากฐานข้อมูล");
                    //avgexp.setText("หมดอายุในอีก " + avgdays + " วัน");
                    getExpDateFromDatabase();
                    expcheck = "0";

                }
                break;


        }
    }

    //----------------เลือกวันหมดอายุเฉลี่ยและแคลลอรี่เฉลี่ย-----------------------------
    public void getExpDateFromDatabase() {
        final String aaa = Autocomplete.getText().toString();
        typecheck = TYPENAME.getSelectedItem().toString();

        //------------------------------- ค้นหาชื่อใน get baseAdd เพื่อเอาวันหมดอายุของสดมาแสดง -------------------------------
        for (int i = 0; i < list.size(); i++) {
            if (aaa.equals(list.get(i))) {
                avgdays = Integer.parseInt(listc.get(i));
                Calorie = listb.get(i);
                freshname = list.get(i);
                pictureall = picture_freshbase.get(i);
                break;
            } else {
                freshname = null;
            }
        }

        if (freshname != null) {
            LocalDate datethen = dateNow.plusDays(avgdays);
            b = datethen.toString("yyyy-MM-dd");

        } else {
            if (typecheck.equals("เนื้อ")) {
                avgdays = Integer.parseInt(ExpAvgMeat);
                LocalDate datethen = dateNow.plusDays(avgdays);
                b = datethen.toString("yyyy-MM-dd");
                Calorie = CalAvgMeat;
            } else if (typecheck.equals("ผักและผลไม้")) {
                avgdays = Integer.parseInt(ExpAvgVetg);
                LocalDate datethen = dateNow.plusDays(avgdays);
                b = datethen.toString("yyyy-MM-dd");
                Calorie = CalAvgVetg;
            } else {
                avgdays = Integer.parseInt(ExpAvgOther);
                LocalDate datethen = dateNow.plusDays(avgdays);
                b = datethen.toString("yyyy-MM-dd");
                Calorie = CalAvgOther;
            }
        }
        RadioButton b2 = (RadioButton) findViewById(R.id.radioButton2);
        if (b2.isChecked()) {
            selectdb.setText("ค่าเฉลี่ยวันหมดอายุจากฐานข้อมูล");
            avgexp.setText("หมดอายุในอีก " + avgdays + " วัน");

            LocalDate dateNow = LocalDate.now();
            LocalDate datethen = dateNow.plusDays(avgdays);
            String defaultdate = datethen.toString("yyyy-MM-dd");
            DateEtxt.setText(defaultdate);
            DateEtxt.setInputType(InputType.TYPE_NULL);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // Get the dimensions of the View
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            imageView.setImageBitmap(bitmap);

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("Getpath", "Cool" + mCurrentPhotoPath);
        return image;
    }

    public void addedmenu() {
        final String aaa = Autocomplete.getText().toString();
        //------------------------------- ค้นหาชื่อใน get baseAdd เพื่อเอาวันหมดอายุของสดมาแสดง -------------------------------
        for (int i = 0; i < list.size(); i++) {
            if (aaa.equals(list.get(i))) {
                avgdays = Integer.parseInt(listc.get(i));
                Calorie = listb.get(i);
                freshname = list.get(i);
                pictureall = picture_freshbase.get(i);
                break;
            } else {
                freshname = null;
            }
        }

        //----------------CameraUpload-------------------------------
        if (mCurrentPhotoPath != null) {
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / 100, photoH / 100);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            int h = bm.getHeight();
            int w = bm.getWidth();
            int scaled_w = 100 * w / h;
            bm = Bitmap.createScaledBitmap(bm, scaled_w, 100, false);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] ba = bao.toByteArray();
            ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
        } else if (pictureall != null){
            ba1 = pictureall;
        } else
            ba1 = "";

        Fresh_Name = Autocomplete.getText().toString();
        Amount = ET_AMOUNT.getText().toString();
        Exp = ET_EXP.getText().toString();
        S_Unit = UNIT.getSelectedItem().toString();
        S_Type_name = TYPENAME.getSelectedItem().toString();

        if (expcheck.equals("1")) {

        } else {
            Exp = b;
        }

        String type = "addMenu";
        BackgroundTask backgroundTask = new BackgroundTask(this);

        if (shop_id != null) {
            backgroundTask.execute(type, Fresh_Name, Amount, S_Unit, S_Type_name, Exp, ba1, user_id, Calorie, join_leave_id, group_id, shop_id);

            String type2 = "DelShoppingList";
            BackgroundTask backgroundTask2 = new BackgroundTask(this);
            backgroundTask2.execute(type2, user_id, group_id, shop_id);
            setResult(RESULT_OK, null);

            //---Delay Time Before ShowItem
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    finish();
                }
            }, 300);

        } else {
            shop_id = "0";
            backgroundTask.execute(type, Fresh_Name, Amount, S_Unit, S_Type_name, Exp, ba1, user_id, Calorie, join_leave_id, group_id, shop_id);

            //---Delay Time Before ShowItem
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    Intent intent = new Intent(add_menu.this, home_all.class);
                    startActivity(intent);
                    finish();
                }
            }, 300);
        }

    }

    public void takePhoto() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ");
        if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE");

        if (permissionsList.size() > 0) {
            /*if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(add_menu.this,permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }*/
            ActivityCompat.requestPermissions(add_menu.this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        callCameraApp();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(add_menu.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void callCameraApp() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    callCameraApp();
                } else {
                    // Permission Denied
                    Toast.makeText(add_menu.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
