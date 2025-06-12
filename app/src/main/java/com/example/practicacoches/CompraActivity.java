package com.example.practicacoches;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CompraActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnGuardar;
    private AdaptadorPieza adaptador;
    private MainViewModel mvm;
    private ArrayList<Pieza> piezasSeleccionadasRestauradas;
    private TextView tvDinero;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);

        recyclerView = findViewById(R.id.rvPiezas);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tvDinero = findViewById(R.id.tvDinero);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mvm = new ViewModelProvider(this).get(MainViewModel.class);

        mvm.getPiezas().observe(this, piezas -> {
            if (piezas != null) {
                adaptador = new AdaptadorPieza(piezas);
                recyclerView.setAdapter(adaptador);

                if (piezasSeleccionadasRestauradas != null) {
                    adaptador.marcarSeleccionadas(piezasSeleccionadasRestauradas);
                }
            }
        });

        if (mvm.getPiezas().getValue() == null) {
            mvm.cargarPiezas(this);
        }

        tabLayout.addTab(tabLayout.newTab().setText("Todos"));
        tabLayout.addTab(tabLayout.newTab().setText("Pieza"));
        tabLayout.addTab(tabLayout.newTab().setText("Carrocería"));
        tabLayout.addTab(tabLayout.newTab().setText("Interior"));
        tabLayout.addTab(tabLayout.newTab().setText("Favoritos"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String categoriaSeleccionada = tab.getText().toString();
                List<Pieza> piezasFiltradas = filtrarPorCategoria(mvm.getPiezas().getValue(), categoriaSeleccionada);
                adaptador.actualizarPiezas(piezasFiltradas);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> {
            ArrayList<Pieza> piezasSeleccionadas = adaptador.getPiezasSeleccionadas();
            String piezasJson = prefs.getString("piezas_guardadas", null);

            ArrayList<Pieza> piezasGuardadas = new ArrayList<>();

            if (piezasJson != null) {
                piezasGuardadas = SerializadorPiezas.deserializar(piezasJson);
            }

            piezasSeleccionadas.addAll(piezasGuardadas);

            double totalPrecio = 0;
            for (Pieza pieza : piezasSeleccionadas) {
                totalPrecio += pieza.getPrecio();
            }

            prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
            double dineroActual = Double.longBitsToDouble(prefs.getLong("dinero", Double.doubleToLongBits(5000)));

            if (dineroActual >= totalPrecio) {
                dineroActual -= totalPrecio;

                prefs.edit()
                        .putLong("dinero", Double.doubleToLongBits(dineroActual))
                        .putString("piezas_guardadas", SerializadorPiezas.serializar(piezasSeleccionadas))
                        .apply();

                Intent intent = new Intent(CompraActivity.this, CocheActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Dinero insuficiente", Toast.LENGTH_SHORT).show();
            }
        });

        if (savedInstanceState != null) {
            piezasSeleccionadasRestauradas = (ArrayList<Pieza>) savedInstanceState.getSerializable("piezas_seleccionadas");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarDinero();
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
    private void actualizarDinero() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        double dinero = Double.longBitsToDouble(prefs.getLong("dinero", Double.doubleToLongBits(5000)));
        DecimalFormat formato = new DecimalFormat("#.##");
        tvDinero.setText("Dinero: " + formato.format(dinero) + "€");
    }

}