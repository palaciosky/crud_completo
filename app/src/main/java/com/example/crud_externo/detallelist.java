package com.example.crud_externo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class detallelist extends AppCompatActivity implements View.OnClickListener {

    private TextView tdcod,tdname,tdstat;
    private Button btdel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallelist);

        btdel = (Button ) findViewById(R.id.btnDel);

        tdcod = ( TextView ) findViewById(R.id.tv_cod);
        tdname = (TextView) findViewById(R.id.tv_desc);
        tdstat = (TextView) findViewById(R.id.tv_precio);
        //dddddddddddddddddddddddddddddddddddddddddddddddddddd

        Bundle bundle = getIntent().getExtras();

        String id = bundle.getString("codigo");
        String nom = bundle.getString("nombre");
        String status = bundle.getString("estado");

        btdel.setOnClickListener(this);

        tdcod.setText(id);
        tdname.setText(nom);
        tdstat.setText(status);




    }

    private void delcat (final Context context, final int iddel ){
        StringRequest request = new StringRequest(Request.Method.POST, Setting_VAR.url_del, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.length() > 7) {

                    Toast.makeText(context, "ha sido "+response, Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(context, "Error"+response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                btdel.setOnClickListener(new View.OnClickListener() {
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
                map.put("id", String.valueOf(iddel));

                return map;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        String id = tdcod.getText().toString();
        if (v.getId() == R.id.btnDel){
            delcat(this, Integer.parseInt(id));
            finish();
        }

    }

    public   void finish(View v){
        finish();
    }
}