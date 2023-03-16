package com.example.crudlsp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText Email, Password;
    Button register, login;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = (EditText) findViewById(R.id.log_email);
        Password = (EditText) findViewById(R.id.log_password);
        register = (Button) findViewById(R.id.btnregister);
        login = (Button) findViewById(R.id.btnlogin);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegistrationProccess();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLoginProcces();
            }
        });

    }

    private void UserLoginProcces() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()){
            massage("Terdapat Field yang Kosong");
        }else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, Urls.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString("status");

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if(result.equals("success")){
                                    progressDialog.dismiss();
                                    for (int i= 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String name = object.getString("name");
                                        String email = object.getString("email");
                                        String phone = object.getString("phone");

                                        Intent intent = new Intent(MainActivity.this, profileActivity.class);
                                        intent.putExtra("name",name);
                                        intent.putExtra("email",email);
                                        intent.putExtra("phone",phone);
                                        startActivity(intent);finish();

                                        massage("Login Berhasil");

                                    }

                                }else {
                                    progressDialog.dismiss();
                                    massage("Login Gagal!!");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                    Map<String,String> params  = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(request);
        }
    }

    private void UserRegistrationProccess() {
        LayoutInflater inflater = getLayoutInflater();
        View register_layout = inflater.inflate(R.layout.register_layout, null);
        final EditText Name = register_layout.findViewById(R.id.req_name);
        final EditText Email = register_layout.findViewById(R.id.req_email);
        final EditText Phone = register_layout.findViewById(R.id.req_phone);
        final EditText Password = register_layout.findViewById(R.id.req_password);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(register_layout);
        builder.setTitle("Registrasi");
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog.show();
                final String name = Name.getText().toString().trim();
                final String email = Email.getText().toString().trim();
                final String phone = Phone.getText().toString().trim();
                final String password = Password.getText().toString().trim();

                if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()){
                    massage("Field Ada yang Kosong..");
                    progressDialog.dismiss();
                }else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.REGISTER_URL,
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
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", name);
                            params.put("email", email);
                            params.put("phone", phone);
                            params.put("password", password);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(stringRequest);
                }
            }
        });
        builder.setNegativeButton("cancal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void massage (String massage){
        Toast.makeText(this, massage, Toast.LENGTH_LONG).show();

    }
}