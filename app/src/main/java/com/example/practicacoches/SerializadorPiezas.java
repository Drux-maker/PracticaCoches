package com.example.practicacoches;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SerializadorPiezas {

    private static final Gson gson = new Gson();

    public static String serializar(ArrayList<Pieza> piezas) {
        return gson.toJson(piezas);
    }

    public static ArrayList<Pieza> deserializar(String json) {
        Type type = new TypeToken<ArrayList<Pieza>>() {}.getType();
        return gson.fromJson(json, type);
    }
}