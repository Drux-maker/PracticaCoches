package com.example.practicacoches;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListaCochesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
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

                AdaptadorCoche adaptador = new AdaptadorCoche(cochesFiltrados, cocheSeleccionado -> {
                    // Al hacer clic, abrir CocheActivity con el coche
                    Intent intent = new Intent(ListaCochesActivity.this, CocheActivity.class);
                    intent.putExtra("cocheSeleccionado", cocheSeleccionado);
                    startActivity(intent);
                });

                recyclerView.setAdapter(adaptador);
            }
        });

        if (mvm.getCoches().getValue() == null) {
            mvm.cargarCoches(this);
        }
    }
}
