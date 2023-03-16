package com.example.crudlsp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class profileActivity extends AppCompatActivity {
    EditText Name, Email, Phone;
    Button update, cPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Name = findViewById(R.id.p_name);
        Email = findViewById(R.id.p_email);
        Phone = findViewById(R.id.p_phone);

        update = findViewById(R.id.btnUpdate);
        cPass = findViewById(R.id.btnChangePassword);

        Intent i = getIntent();
        String mName = i.getStringExtra("name");
        String mEmail = i.getStringExtra("email");
        String mPhone = i.getStringExtra("phone");

        Name.setText(mName);
        Email.setText(mEmail);
        Phone.setText(mPhone);


    }
}