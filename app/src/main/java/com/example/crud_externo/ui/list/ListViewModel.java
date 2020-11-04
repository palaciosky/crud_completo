package com.example.crud_externo.ui.list;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.crud_externo.MySingleton;
import com.example.crud_externo.Setting_VAR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListViewModel() {

    }

    public void getListcat(final Context context, final ListView listView, final Fragment fragment){
        StringRequest requestt = new StringRequest(Request.Method.POST, Setting_VAR.url_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONArray datArray = null;
                JSONObject datos = null ;

                try {
                    datArray = new JSONArray(response);
                    final ArrayList<String> listArt = new ArrayList<>();

                    for (int i=0; i<datArray.length(); i++){
                        datos = new JSONObject(datArray.getString(i));
                        datos.getString("id_categoria");
                        datos.getString("nom_categoria");
                        datos.getString("estado_categoria");
                        listArt.add(datos.toString());


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(context, "Sin conexion a internet", Toast.LENGTH_SHORT).show();

            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");

                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(requestt);
    }

    public LiveData<String> getText() {
        return mText;
    }
}