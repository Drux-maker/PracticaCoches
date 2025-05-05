package com.example.practicacoches;

public class Coche {
    private String modelo;
    private String descripcion;
    private int imagenResId;

    public Coche(String modelo, String descripcion, int imagenResId) {
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
    }

    // Getters
    public String getModelo() { return modelo; }

    public String getDescripcion() { return descripcion; }
    public int getImagenResId() { return imagenResId; }
}