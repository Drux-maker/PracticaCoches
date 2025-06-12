package com.example.practicacoches;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<List<Marca>> marcas = new MutableLiveData<>();
    private final MutableLiveData<List<Coche>> coches = new MutableLiveData<>();
    private final MutableLiveData<List<Pieza>> piezas = new MutableLiveData<>();

    public LiveData<List<Marca>> getMarcas() { return marcas; }
    public LiveData<List<Coche>> getCoches() { return coches; }
    public LiveData<List<Pieza>> getPiezas() { return piezas; }

    public void consultaApi(Context context, String url, String tipo) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        switch (tipo) {
                            case "marca":
                                List<Marca> listaMarca = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    String id = obj.getString("id");
                                    String nombre = obj.getString("nombre");
                                    String imagen = obj.getString("imagen");
                                    listaMarca.add(new Marca(id, imagen, nombre));
                                }
                                marcas.setValue(listaMarca);
                                break;

                            case "pieza":
                                List<Pieza> listaPieza = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
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
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    String marca = obj.getString("marca");
                                    String nombre = obj.getString("nombre");
                                    String imagen = obj.getString("imagen");

                                    listaCoche.add(new Coche(nombre, imagen, marca));
                                }
                                coches.setValue(listaCoche);
                                Log.d("Volley", "Coches cargados: " + listaCoche.size());
                                break;
                        }
                    } catch (JSONException e) {
                        Log.e("Volley", "Error parseando JSON", e);
                    }
                },
                error -> Log.e("Volley", "Error en la petición: " + error.toString())
        );

        requestQueue.add(jsonArrayRequest);
    }

    public void cargarMarcas(Context context) {
        if (marcas.getValue() != null && !marcas.getValue().isEmpty()) return;
        String url = "https://raw.githubusercontent.com/Drux-maker/jsonMoviles/refs/heads/main/marcas.json";
        consultaApi(context, url, "marca");
    }

    public void cargarCoches(Context context) {
        if (coches.getValue() != null && !coches.getValue().isEmpty()) return;
        String url = "https://raw.githubusercontent.com/Drux-maker/jsonMoviles/refs/heads/main/coches.json";
        consultaApi(context, url, "coche");
    }

    public void cargarPiezas(Context context) {
        if (piezas.getValue() != null && !piezas.getValue().isEmpty()) return;
        String url = "https://raw.githubusercontent.com/Drux-maker/jsonMoviles/refs/heads/main/piezas.json";
        consultaApi(context, url, "pieza");
    }
}
