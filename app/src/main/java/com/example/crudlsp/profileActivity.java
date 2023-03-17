package com.example.crudlsp;

import androidx.annotation.Nullable;
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
    UserMangment userMangment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Name = findViewById(R.id.p_name);
        Email = findViewById(R.id.p_email);
        Phone = findViewById(R.id.p_phone);

        userMangment = new UserMangment(this);
        userMangment.checkLogin();

        progressDialog = new  ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        update = findViewById(R.id.btnUpdate);
        cPass = findViewById(R.id.btnChangePassword);

       HashMap<String, String> user = userMangment.userDatails();
       final String mEmail = user.get(userMangment.EMAIL);
        String mPhone = user.get(userMangment.PHONE);
        String mName = user.get(userMangment.NAME);

        Name.setText(mName);
        Email.setText(mEmail);
        Phone.setText(mPhone);

        cPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View resetpasswordlayout = LayoutInflater.from(profileActivity.this).inflate(R.layout.change_password,null);
                EditText OldPass = resetpasswordlayout.findViewById(R.id.edt_old_password);
                EditText NewPass = resetpasswordlayout.findViewById(R.id.edt_new_password);
                EditText ConformPass = resetpasswordlayout.findViewById(R.id.edt_conform_password);

                AlertDialog.Builder builder = new AlertDialog.Builder(profileActivity.this);
                builder.setTitle("CHANGE PASSWORD");
                builder.setView(resetpasswordlayout);
                builder.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String oldpassword = OldPass.getText().toString().trim();
                        String newpassword = NewPass.getText().toString().trim();
                        String conformpassword = ConformPass.getText().toString().trim();

                        if (oldpassword.isEmpty() || newpassword.isEmpty() || conformpassword.isEmpty()){
                            massage("Terdapat Field Kosong");
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
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("oldpassword", oldpassword);
                                    params.put("newpassword",newpassword);
                                    params.put("conformpassword", conformpassword);
                                    params.put("email", mEmail);

                                    return params;
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(profileActivity.this);
                            queue.add(stringRequest);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String name = Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String phone = Phone.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    massage("some feilds are empty!");
                }else{
                    progressDialog.setTitle("Updating....");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_USER_INFO_URL,
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
                        protected Map<String, String> getParams() throws AuthFailureError{
                            Map<String,String> updateParams = new HashMap<>();
                            updateParams.put("email",email);
                            updateParams.put("name",name);
                            updateParams.put("phone",phone);
                            updateParams.put("myemail",mEmail);
                            return updateParams;

                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(profileActivity.this);
                    queue.add(stringRequest);
                }

            }
        });
    }

    public void massage(String massage){
        Toast.makeText(this,massage,Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        userMangment.logout();
    }
}




//        cPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View resetpasswordlayout= LayoutInflater.from(profileActivity.this).inflate(R.layout.change_password,null);
//                EditText Oldpass = resetpasswordlayout.findViewById(R.id.edt_old_password);
//                EditText Newpass = resetpasswordlayout.findViewById(R.id.edt_new_password);
//                EditText Conformpass = resetpasswordlayout.findViewById(R.id.edt_confirm_password);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(profileActivity.this);
//                builder.setTitle("CHANGE PASSWORD");
//                builder.setView(resetpasswordlayout);
//                builder.setPositiveButton("CHANGE", new DialogInterface .OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                       String oldpassword = Oldpass.getText().toString().trim();
//                       String newpassword = Newpass.getText().toString().trim();
//                       String conformpassword =Conformpass.getText().toString().trim();
//
//                        if(oldpassword.isEmpty()|| newpassword.isEmpty()|| conformpassword.isEmpty()){
//                            massage("some feild are empty");
//
//                        }else {
//                            progressDialog.show();
//                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.RESET_PASSWORD_URL,
//                                    new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String response) {
//                                            progressDialog.dismiss();
//                                              massage(response);
//                                        }
//                                    }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    progressDialog.dismiss();
//                                    massage(error.getMessage());
//                                }
//                            }){
//                                @Override
//                                public Map<String, String> getHeaders() throws AuthFailureError {
//                                   Map<String,String> params = new HashMap<>();
//                                   params.put("oldpassword",oldpassword);
//                                   params.put("newpassword",newpassword);
//                                   params.put("conformpassword",conformpassword);
//                                   params.put("email",mEmail);
//                                   return params;
//                                }
//                            };
//
//                            RequestQueue queue= Volley.newRequestQueue(profileActivity.this);
//                            queue.add(stringRequest);
//
//
//                        }
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.show();
//
//
//            }
//        });