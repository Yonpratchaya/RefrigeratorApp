package com.example.yon.project60;

import android.app.AlertDialog;
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
import android.widget.TextView;

/**
 * Created by Yon on 6/7/2560.
 */

public class create_group extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    EditText ET_GNAME,ET_GPASS;
    String groupname,grouppass;

    AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    String user_name;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        alertDialog = new AlertDialog.Builder(this).create();

        sharedPreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_name = sharedPreferences.getString("user_name", null);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                create_group.this,
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

        ET_GNAME = (EditText) findViewById(R.id.groupname_reg);
        ET_GPASS = (EditText) findViewById(R.id.pass_reg);




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
                Intent intent3 = new Intent(create_group.this, home_all.class);
                startActivity(intent3);
                finish();
                return true;
            case R.id.suggest:
                Intent intent4 = new Intent(create_group.this, suggestlist_menu.class);
                startActivity(intent4);
                finish();
                return true;
            case R.id.shopping:
                Intent intent5 = new Intent(create_group.this, shoppinglist_menu.class);
                startActivity(intent5);
                finish();
                return true;
            case R.id.group:
                Intent intent6 = new Intent(create_group.this, group_menu.class);
                startActivity(intent6);
                finish();
                return true;
            case R.id.exit:
                Intent intent7 = new Intent(create_group.this, MainActivity.class);
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

    public void Creategroup(View view){

        groupname = ET_GNAME.getText().toString();
        grouppass = ET_GPASS.getText().toString();
        if(groupname.equals("")||grouppass.equals("")){
            alertDialog.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน");
            alertDialog.show();
        }
        else{
            String type = "creategroup";
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(type,groupname,grouppass,user_name);
        }



    }
}
