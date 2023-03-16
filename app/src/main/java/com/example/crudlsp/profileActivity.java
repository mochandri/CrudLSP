package com.example.crudlsp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class profileActivity extends AppCompatActivity {
    EditText Name, Email, Phone;
    Button update, cPass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Name = findViewById(R.id.p_name);
        Email = findViewById(R.id.p_email);
        Phone = findViewById(R.id.p_phone);

        progressDialog = new  ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        update = findViewById(R.id.btnUpdate);
        cPass = findViewById(R.id.btnChangePassword);

        Intent i = getIntent();
        String mName = i.getStringExtra("name");
        String mEmail = i.getStringExtra("email");
        String mPhone = i.getStringExtra("phone");

        Name.setText(mName);
        Email.setText(mEmail);
        Phone.setText(mPhone);

        cPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View resetpasswordlayout= LayoutInflater.from(profileActivity.this).inflate(R.layout.change_password,null);
                final EditText Oldpass = resetpasswordlayout.findViewById(R.id.edt_old_password);
               final EditText Newpass = resetpasswordlayout.findViewById(R.id.edt_new_password);
               final EditText Confirmpass = resetpasswordlayout.findViewById(R.id.edt_confirm_password);

                AlertDialog.Builder builder = new AlertDialog.Builder(profileActivity.this);
                builder.setTitle("CHANGE PASSWORD");
                builder.setView(resetpasswordlayout);
                builder.setPositiveButton("CHANGE", new DialogInterface .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       final String oldpassword = Oldpass.getText().toString().trim();
                       final String newpassword = Newpass.getText().toString().trim();
                       final String confirmpassword =Confirmpass.getText().toString().trim();

                        if(oldpassword.isEmpty()|| newpassword.isEmpty()|| confirmpassword.isEmpty()){
                            massage("some feild are empty");

                        }else {
                            progressDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.RESET_PASSWORD_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            progressDialog.dismiss();
                                              massage(response);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    massage(error.getMessage());
                                }
                            }){
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                   Map<String,String> params = new HashMap<>();
                                   params.put("oldpassword",oldpassword);
                                   params.put("newpassword",newpassword);
                                   params.put("confirmpassword",confirmpassword);
                                   params.put("email",mEmail);
                                   return params;
                                }
                            };

                            RequestQueue queue= Volley.newRequestQueue(profileActivity.this);
                            queue.add(stringRequest);


                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

    }
    public void massage(String massage){
        Toast.makeText(this,massage,Toast.LENGTH_SHORT).show();
    }
}