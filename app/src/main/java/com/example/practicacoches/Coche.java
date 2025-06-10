package com.example.practicacoches;

import java.io.Serializable;

public class Coche implements Serializable {
    private String marca;
    private String nombre;
    private String imagen;

    public Coche(String nombre, String imagen, String marca) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.marca = marca;
    }

    public String getMarca() {
        return marca;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}

