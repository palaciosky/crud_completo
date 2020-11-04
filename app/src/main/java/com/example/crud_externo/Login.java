package com.example.crud_externo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.crud_externo.MainActivity;
import com.example.crud_externo.MySingleton;
import com.example.crud_externo.R;
import com.example.crud_externo.Setting_VAR;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText etemail, etpassword;
    private Button btlog, btexit;
    ArrayList<String> lista = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etemail = findViewById(R.id.username);
        etpassword = findViewById(R.id.password);

        btlog = findViewById(R.id.btnLog);
        btexit = findViewById(R.id.btnExit);

        btlog.setOnClickListener(this);
        btexit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId() ){
            case R.id.btnLog:
                String nom = etemail.getText().toString();
                String pass = etpassword.getText().toString();

                recibirLog(this, nom, pass);
                break;

            case R.id.btnExit:
                exit();
                break;

            default:
        }
    }

    private void recibirLog(final Context context, final String email, final String pass){
        StringRequest request = new StringRequest(Request.Method.POST, Setting_VAR.url_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.length() > 2) {

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(context, "BIENVENIDO "+response, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Correo o Contraseña incorrectas", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btlog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Error 404 Vuelva Mañana", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("correo", email);
                map.put("clave", pass);
                return map;
            }
        };

           MySingleton.getInstance(context).addToRequestQueue(request);
    }

    private  void exit(){
        finish();
    }
}