package com.example.yon.project60;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Yon on 30/1/2560.
 */

public class register_form extends AppCompatActivity {
    EditText ET_NAME,ET_USER_NAME,ET_USER_PASS;
    String name,user_name,user_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);

        ET_NAME = (EditText) findViewById(R.id.name_reg);
        ET_USER_NAME = (EditText) findViewById(R.id.username_reg);
        ET_USER_PASS = (EditText) findViewById(R.id.pass_reg);
    }
    public void userReg(View view){

        name = ET_NAME.getText().toString();
        user_name = ET_USER_NAME.getText().toString();
        user_pass = ET_USER_PASS.getText().toString();
        String type = "register";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(type,name,user_name,user_pass);
      finish();


    }


}
