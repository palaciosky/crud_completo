package com.example.crud_externo.ui.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.crud_externo.DTO.DTO_cat;
import com.example.crud_externo.MySingleton;
import com.example.crud_externo.R;
import com.example.crud_externo.Setting_VAR;
import com.example.crud_externo.detallelist;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListFragment extends Fragment {
    private TextInputLayout ti_idcat, ti_namecat;
    private EditText etidcat, etnomcat;
    private ListView listy;
    private Button bSav, bNew;
    ArrayAdapter adaptadar;
    private ListViewModel myVm;
    private TextView TFIN;

    ArrayList<String> list;
    ArrayList<DTO_cat> list2;
    ArrayAdapter adapter;

    String datoSelect = "";

    public  static  ListFragment newInstance(){
        return new ListFragment();
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_list, container, false);
        listy = root.findViewById(R.id.lsview);
        TFIN = root.findViewById(R.id.tfin);

        recibirAllCat();
        return root;
    }

    private void recibirAllCat(){
        list2 = new ArrayList<DTO_cat>();
        list = new ArrayList<String>();
        String urlConsultaCategoria = Setting_VAR.url_list;
        StringRequest request = new StringRequest(Request.Method.POST, urlConsultaCategoria, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // TFIN.setText(response);

                try {
                    JSONArray respuestaJSOn = new JSONArray(response);
                    int totalEnct = respuestaJSOn.length();



                    DTO_cat objCategorias = null;
                    for (int i = 0; i < respuestaJSOn.length(); i++){
                        JSONObject categoriaObj = respuestaJSOn.getJSONObject(i);
                        int idC = categoriaObj.getInt("id_categoria");
                        String name = categoriaObj.getString("nom_categoria");
                        int stado = categoriaObj.getInt("estado_categoria");

                        objCategorias = new DTO_cat(idC, name, stado);

                        list2.add(objCategorias);

                        list.add(list2.get(i).getIdCategoria()+" -- "+list2.get(i).getNombre());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
                        listy.setAdapter(adapter);

                        listy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String codigo = "" + list2.get(i).getIdCategoria();
                                String nombre = "" + list2.get(i).getNombre();
                                String estado = "" + list2.get(i).getEstado();


                                Activity activity = new Activity();
                                Intent intent = new Intent(getActivity(), detallelist.class);
                                intent.putExtra("codigo", codigo);
                                intent.putExtra("nombre", nombre);
                                intent.putExtra("estado", estado);
                                startActivity(intent);
                            }
                        });

                        Log.i("Id Categoria:    ", String.valueOf(objCategorias.getIdCategoria()));
                        Log.i("Nombre:    ", String.valueOf(objCategorias.getNombre()));



                    }
                    //resultado.setText("Datos: " + response.toString());
                    //Toast.makeText(getContext(), "Id: " + idCategori + "\nNombre: " + nombreCat + "\nEstado: " + estadoCat, Toast.LENGTH_SHORT).show();

                } catch (JSONException ex){
                    String none = ex.toString();
                    Toast.makeText(getContext(),"Error de conexion ",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String err = volleyError.toString();
                Log.i("No se pudo **********", err);
            }
        });
        //tiempo de respuesta, establece politica de reintentos
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myVm = new ViewModelProvider(this).get(ListViewModel.class);
        myVm.getListcat(getContext(), listy, this);
    }




}