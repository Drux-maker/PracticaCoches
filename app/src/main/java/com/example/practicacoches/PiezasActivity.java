package com.example.practicacoches;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PiezasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorPieza adaptador;
    private ArrayList<Pieza> piezasCompradas = new ArrayList<>();
    private ArrayList<Pieza> piezasEquipadas = new ArrayList<>();
    private boolean mostrarPiezasEquipadas = false;
    private Button btnGuardar;
    private Button btnVender;
    private TextView tvDinero;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piezas);

        recyclerView = findViewById(R.id.rvPiezas);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVender = findViewById(R.id.btnVender);
        tabLayout = findViewById(R.id.tabLayout);
        tvDinero = findViewById(R.id.tvDinero);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mostrarDialogoSeleccion();  // Espera la selección del usuario
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarDinero();
    }


    private void mostrarDialogoSeleccion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona las piezas a ver")
                .setMessage("¿Quieres ver las piezas compradas o las piezas equipadas?")
                .setPositiveButton("Piezas Compradas", (dialog, id) -> {
                    cargarPiezasCompradas();
                    mostrarPiezasEquipadas = false;
                    btnGuardar.setText("Equipar Piezas");
                    configurarFiltrosYEventos();
                })
                .setNegativeButton("Piezas Equipadas", (dialog, id) -> {
                    cargarPiezasEquipadas();
                    mostrarPiezasEquipadas = true;
                    btnGuardar.setText("Desequipar Piezas");
                    configurarFiltrosYEventos();
                })
                .create()
                .show();
    }

    private void cargarPiezasCompradas() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        String piezasJson = prefs.getString("piezas_guardadas", null);
        piezasCompradas = (piezasJson != null) ? SerializadorPiezas.deserializar(piezasJson) : new ArrayList<>();
        adaptador = new AdaptadorPieza(piezasCompradas);
        recyclerView.setAdapter(adaptador);
    }

    private void cargarPiezasEquipadas() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        String piezasJson = prefs.getString("piezas_equipadas", null);
        piezasEquipadas = (piezasJson != null) ? SerializadorPiezas.deserializar(piezasJson) : new ArrayList<>();
        adaptador = new AdaptadorPieza(piezasEquipadas);
        recyclerView.setAdapter(adaptador);
    }

    private void configurarFiltrosYEventos() {
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Todos"));
        tabLayout.addTab(tabLayout.newTab().setText("Pieza"));
        tabLayout.addTab(tabLayout.newTab().setText("Carrocería"));
        tabLayout.addTab(tabLayout.newTab().setText("Interior"));
        tabLayout.addTab(tabLayout.newTab().setText("Favoritos"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String categoriaSeleccionada = tab.getText().toString();
                List<Pieza> listaActual = mostrarPiezasEquipadas ? piezasEquipadas : piezasCompradas;
                List<Pieza> piezasFiltradas = filtrarPorCategoria(listaActual, categoriaSeleccionada);
                adaptador.actualizarPiezas(piezasFiltradas);
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        btnGuardar.setOnClickListener(v -> {
            ArrayList<Pieza> seleccionadas = adaptador.getPiezasSeleccionadas();

            if (mostrarPiezasEquipadas) {
                piezasEquipadas.removeAll(seleccionadas);
                piezasCompradas.addAll(seleccionadas);
                btnGuardar.setText("Equipar Piezas");
            } else {
                piezasCompradas.removeAll(seleccionadas);
                piezasEquipadas.addAll(seleccionadas);
                btnGuardar.setText("Desequipar Piezas");
            }

            SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
            prefs.edit().putString("piezas_guardadas", SerializadorPiezas.serializar(piezasCompradas)).apply();
            prefs.edit().putString("piezas_equipadas", SerializadorPiezas.serializar(piezasEquipadas)).apply();

            int nuevoNivel = calcularNivel(mostrarPiezasEquipadas ? piezasEquipadas : piezasCompradas);
            prefs.edit().putInt("nivelCoche", nuevoNivel).apply();

            adaptador.actualizarPiezas(mostrarPiezasEquipadas ? piezasEquipadas : piezasCompradas);
            Toast.makeText(PiezasActivity.this, "Piezas actualizadas", Toast.LENGTH_SHORT).show();

            finish();
        });

        btnVender.setOnClickListener(v -> {
            ArrayList<Pieza> seleccionadas = adaptador.getPiezasSeleccionadas();

            if (mostrarPiezasEquipadas) {
                piezasEquipadas.removeAll(seleccionadas);
            } else {
                piezasCompradas.removeAll(seleccionadas);
            }

            SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
            prefs.edit().putString("piezas_guardadas", SerializadorPiezas.serializar(piezasCompradas)).apply();
            prefs.edit().putString("piezas_equipadas", SerializadorPiezas.serializar(piezasEquipadas)).apply();

            double totalVenta = 0;
            for (Pieza pieza : seleccionadas) {
                totalVenta += pieza.getPrecio();
            }

            double dineroActual = Double.longBitsToDouble(prefs.getLong("dinero", Double.doubleToLongBits(5000)));
            dineroActual += totalVenta;
            prefs.edit().putLong("dinero", Double.doubleToLongBits(dineroActual)).apply();

            int nuevoNivel = calcularNivel(mostrarPiezasEquipadas ? piezasEquipadas : piezasCompradas);
            prefs.edit().putInt("nivelCoche", nuevoNivel).apply();

            Toast.makeText(PiezasActivity.this, "Piezas vendidas. Dinero actualizado.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private List<Pieza> filtrarPorCategoria(List<Pieza> listaPiezas, String categoria) {
        List<Pieza> piezasFiltradas = new ArrayList<>();
        for (Pieza pieza : listaPiezas) {
            if (categoria.equalsIgnoreCase("Todos") || pieza.getCategoria().equalsIgnoreCase(categoria)) {
                piezasFiltradas.add(pieza);
            } else if (categoria.equalsIgnoreCase("Favoritos") && pieza.isFavorito()) {
                piezasFiltradas.add(pieza);
            }
        }
        return piezasFiltradas;
    }

    private int calcularNivel(ArrayList<Pieza> piezas) {
        double totalPrecio = 0;
        for (Pieza p : piezas) totalPrecio += p.getPrecio();

        if (totalPrecio >= 5000) return 10;
        else if (totalPrecio >= 4000) return 9;
        else if (totalPrecio >= 3500) return 8;
        else if (totalPrecio >= 3000) return 7;
        else if (totalPrecio >= 2500) return 6;
        else if (totalPrecio >= 2000) return 5;
        else if (totalPrecio >= 1500) return 4;
        else if (totalPrecio >= 1000) return 3;
        else if (totalPrecio >= 500) return 2;
        else return 1;
    }
    private void actualizarDinero() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        double dinero = Double.longBitsToDouble(prefs.getLong("dinero", Double.doubleToLongBits(5000)));
        DecimalFormat formato = new DecimalFormat("#.##");
        tvDinero.setText("Dinero: " + formato.format(dinero) + "€");
    }

}