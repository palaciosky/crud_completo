package com.example.crud_externo.ui.categorias;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.crud_externo.MySingleton;
import com.example.crud_externo.R;
import com.example.crud_externo.Setting_VAR;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CategoryFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout ti_idcat, ti_namecat;
    private EditText etidcat, etnomcat;
    private Spinner sp_status;
    private Button bSav, bNew;

    String datoSelect = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_category, container, false);

        ti_idcat = root.findViewById(R.id.ti_idcat);
        ti_namecat = root.findViewById(R.id.ti_namecat);

        etidcat = root.findViewById(R.id.ed_idcat);
        etnomcat = root.findViewById(R.id.ed_namecat);
        sp_status = root.findViewById(R.id.sp_status);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
        R.array.estadoCategoria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_status.setAdapter(adapter);

        sp_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_status.getSelectedItemPosition()>0){
                    datoSelect = sp_status.getSelectedItem().toString();
                }else {
                    datoSelect = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }


        });

        //me he quedado aqui xd
        bSav = root.findViewById(R.id.btnSave);
        bNew = root.findViewById(R.id.btnNew);

        bSav.setOnClickListener(this);
        bNew.setOnClickListener(this);


                return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                String id = etidcat.getText().toString();
                String nom = etnomcat.getText().toString();
                if (id.length()==0){
                    etidcat.setError("Campo Obligatorio");
                }else if (nom.length()==0){
                    etnomcat.setError("Campo obligatorio");
                }else if (sp_status.getSelectedItemPosition()>0){

                    save_server(getContext(), Integer.parseInt(id),nom,Integer.parseInt(datoSelect));
                }else {
                    Toast.makeText(getContext(),"Debe seleccionar el status de la categoria...",Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.btnNew:
                    new_categories();
                    break;

            default:
        }

    }
    private void save_server(final Context context, final int id_categoria, final String nom_categoria, final int estado_categoria){
        StringRequest requestt = new StringRequest(Request.Method.POST, Setting_VAR.url_current, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            if (response.length() > 1) {
                                Toast.makeText(context,"Categoria Guardada", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error "  , Toast.LENGTH_SHORT).show();
                            }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                bSav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Error 404 Vuelva Mañana", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        }){
            protected Map<String, String>getParams() throws AuthFailureError{
                    Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json; charset=utf-8");
                map.put("Accept", "application/json");
                map.put("id", String.valueOf(id_categoria));
                map.put("nombre", nom_categoria);
                map.put("estado", String.valueOf(estado_categoria));
                return map;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(requestt);
    }

    private  void new_categories(){
        etidcat.setText("");
        etnomcat.setText("");
        sp_status.setSelection(0);
    }
}