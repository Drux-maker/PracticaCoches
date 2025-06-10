package com.example.practicacoches;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaCochesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdaptadorCoche adaptador;
    private MainViewModel mvm;
    private String marcaNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_coches);

        recyclerView = findViewById(R.id.rvCoches);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        marcaNombre = getIntent().getStringExtra("marcaNombre");

        mvm = new ViewModelProvider(this).get(MainViewModel.class);

        mvm.getCoches().observe(this, coches -> {
            if (coches != null && marcaNombre != null) {
                List<Coche> cochesFiltrados = new ArrayList<>();
                for (Coche coche : coches) {
                    if (marcaNombre.equalsIgnoreCase(coche.getMarca())) {
                        cochesFiltrados.add(coche);
                    }
                }
                adaptador = new AdaptadorCoche(cochesFiltrados);
                recyclerView.setAdapter(adaptador);
            }
        });

        if (mvm.getCoches().getValue() == null) {
            mvm.cargarCoches(this);
        }
    }
}
