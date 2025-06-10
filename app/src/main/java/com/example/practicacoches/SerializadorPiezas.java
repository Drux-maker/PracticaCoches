package com.example.practicacoches;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class SerializadorPiezas {
    public static String serializar(ArrayList<Pieza> piezas) {
        return new Gson().toJson(piezas);
    }

    public static ArrayList<Pieza> deserializar(String json) {
        return new Gson().fromJson(json, new TypeToken<ArrayList<Pieza>>() {}.getType());
    }
}
