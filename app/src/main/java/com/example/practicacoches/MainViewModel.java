package com.example.practicacoches;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Marca>> marcas = new MutableLiveData<>();
    private final MutableLiveData<List<Coche>> coches = new MutableLiveData<>();
    private final MutableLiveData<List<Pieza>> piezas = new MutableLiveData<>();

    public LiveData<List<Marca>> getMarcas() { return marcas; }
    public LiveData<List<Coche>> getCoches() { return coches; }
    public LiveData<List<Pieza>> getPiezas() { return piezas; }

    public void consultaApi(Context context, String url, String tipo) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("data");

                        switch (tipo) {
                            case "marca":
                                List<Marca> listaMarca = new ArrayList<>();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);
                                    String id = obj.getString("id");
                                    String nombre = obj.getString("nombre");
                                    String imagen = obj.getString("imagen");
                                    listaMarca.add(new Marca(id, imagen, nombre));
                                }
                                marcas.setValue(listaMarca);
                                break;

                            case "pieza":
                                List<Pieza> listaPieza = new ArrayList<>();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);
                                    String id = obj.getString("id");
                                    String nombre = obj.getString("nombre");
                                    double precio = obj.getDouble("precio");
                                    String imagen = obj.getString("imagenUrl");
                                    String categoria = obj.getString("categoria");
                                    listaPieza.add(new Pieza(id, nombre, precio, imagen, categoria));
                                }
                                piezas.setValue(listaPieza);
                                break;

                            case "coche":
                                List<Coche> listaCoche = new ArrayList<>();
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject obj = dataArray.getJSONObject(i);
                                    String marca = obj.getString("marca");
                                    String nombre = obj.getString("nombre");
                                    String imagen = obj.getString("imagen");

                                    listaCoche.add(new Coche(nombre, imagen, marca));
                                }
                                coches.setValue(listaCoche);
                                Log.d("Volley", "Coches cargados (formato sin ID): " + listaCoche.size());
                                break;

                        }
                    } catch (JSONException e) {
                        Log.e("Volley", "Error parseando JSON", e);
                    }
                },
                error -> Log.e("Volley", "Error en la petición: " + error.toString())
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void cargarMarcas(Context context) {
        if (marcas.getValue() != null && !marcas.getValue().isEmpty()) return;
        String url = "https://api.myjson.online/v1/records/4479c1dc-b8c5-4e01-a863-2a7ac27d14a1";
        consultaApi(context, url, "marca");
    }

    public void cargarCoches(Context context) {
        if (coches.getValue() != null && !coches.getValue().isEmpty()) return;
        String url = "https://api.myjson.online/v1/records/9f2b293a-9cb5-4238-a6f9-5fbc9f5e7270";
        consultaApi(context, url, "coche");
    }

    public void cargarPiezas(Context context) {
        if (piezas.getValue() != null && !piezas.getValue().isEmpty()) return;
        String url = "https://api.myjson.online/v1/records/86b5f2fe-a9bd-4860-b0ed-e72bda567a8f";
        consultaApi(context, url, "pieza");
    }
}
