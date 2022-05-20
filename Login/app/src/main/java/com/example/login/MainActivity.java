package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    EditText EditText_id, EditText_password;
    Button Button_login1, Button_login2, Button_login3;
    String idOK = "id";
    String passwordOK = "password";
    String inputid = "";
    String inputpassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       EditText_id = findViewById(R.id.EditText_id);
       EditText_password = findViewById(R.id.EditText_password);
       Button_login1 = findViewById(R.id.Button_login1);
       Button_login2 = findViewById(R.id.Button_login2);
       Button_login3 = findViewById(R.id.Button_login3);

       Button_login1.setClickable(false);
       EditText_id.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
              if(s != null) {
                  inputid = s.toString();
                  Button_login1.setClickable(validation());
              }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

       EditText_password.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(s != null) {
                   inputpassword = s.toString();
                   Button_login1.setClickable(validation());
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

       Button_login1.setClickable(true);
       Button_login1.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v) {
               String id = EditText_id.getText().toString();
               String password = EditText_password.getText().toString();

               Intent intent = new Intent(MainActivity.this, loginresult.class);
               intent.putExtra("id", id);
               intent.putExtra("password", password);
               startActivity(intent);
           }
       });

    }

    public boolean validation() {
        return inputid.equals(idOK) && inputpassword.equals(passwordOK);
    }
}