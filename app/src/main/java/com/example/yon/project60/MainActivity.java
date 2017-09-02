package com.example.yon.project60;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {
    private String user_id, user_name;
    EditText UsernameEt, PasswordEt;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.register_textview);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        UsernameEt = (EditText) findViewById(R.id.etUsername);
        PasswordEt = (EditText) findViewById(R.id.etPassword);


    }

    @Override
    protected void onResume() {
        sharedpreferences = getSharedPreferences("Tooyen", Context.MODE_PRIVATE);
        user_id = sharedpreferences.getString("user_id", null);
        user_name = sharedpreferences.getString("user_name", null);
        if (user_id != null) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(this, home_all.class);
            editor.putString("user_id", user_id);
            editor.putString("user_name", user_name);
            editor.commit();
            startActivity(intent);
            finish();
        }
        super.onResume();
    }

    public void OnLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(type, username, password);

        //  setContentView(R.layout.home_all);
        //Intent intent = new Intent(MainActivity.this, home_all.class);
        //startActivity(intent);
        //finish();


    }

    public void userReg(View view) {

        startActivity(new Intent(this, register_form.class));

    }


}
