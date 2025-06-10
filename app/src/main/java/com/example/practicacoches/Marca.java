package com.example.practicacoches;

public class Marca {
    private String id;
    private String imagen;
    private String nombre;

    public Marca(String id, String imagen, String nombre) {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }
}
