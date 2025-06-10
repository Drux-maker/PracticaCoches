package com.example.practicacoches;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PiezasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorPieza adaptador;
    private ArrayList<Pieza> piezasCompradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piezas);

        recyclerView = findViewById(R.id.rvPiezas);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        String piezasJson = prefs.getString("piezas_guardadas", null);
        if (piezasJson != null) {
            piezasCompradas = SerializadorPiezas.deserializar(piezasJson);
        } else {
            piezasCompradas = new ArrayList<>();
        }

        adaptador = new AdaptadorPieza(piezasCompradas, true);
        recyclerView.setAdapter(adaptador);

        // Añadir las categorías al TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Pieza"));
        tabLayout.addTab(tabLayout.newTab().setText("Carrocería"));
        tabLayout.addTab(tabLayout.newTab().setText("Interior"));
        tabLayout.addTab(tabLayout.newTab().setText("Favoritos"));

        // Filtrado según la categoría seleccionada
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String categoriaSeleccionada = tab.getText().toString();
                List<Pieza> piezasFiltradas = filtrarPorCategoria(piezasCompradas, categoriaSeleccionada);
                adaptador.actualizarPiezas(piezasFiltradas);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No hacer nada cuando la pestaña no esté seleccionada
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No hacer nada cuando la pestaña ya esté seleccionada
            }
        });

        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> {
            ArrayList<Pieza> seleccionadas = adaptador.getPiezasSeleccionadas();

            double totalVenta = 0;
            for (Pieza pieza : seleccionadas) {
                totalVenta += pieza.getPrecio();
            }

            double dineroActual = Double.longBitsToDouble(prefs.getLong("dinero", Double.doubleToLongBits(10000)));
            dineroActual += totalVenta;
            prefs.edit().putLong("dinero", Double.doubleToLongBits(dineroActual)).apply();

            piezasCompradas.removeAll(seleccionadas);
            prefs.edit().putString("piezas_guardadas", SerializadorPiezas.serializar(piezasCompradas)).apply();

            int nuevoNivel = calcularNivel(piezasCompradas);
            prefs.edit().putInt("nivelCoche", nuevoNivel).apply();

            finish();
        });
    }

    private List<Pieza> filtrarPorCategoria(List<Pieza> listaPiezas, String categoria) {
        List<Pieza> piezasFiltradas = new ArrayList<>();
        for (Pieza pieza : listaPiezas) {
            if (pieza.getCategoria().equalsIgnoreCase(categoria)) {
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
}
