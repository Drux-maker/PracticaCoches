package com.example.practicacoches;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class CompraActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);

        // Configurar RecyclerView
        RecyclerView rvCoches = findViewById(R.id.rvCoches);
        rvCoches.setLayoutManager(new LinearLayoutManager(this));

        // Datos de ejemplo
        List<Coche> coches = Arrays.asList(
                new Coche("Volkswagen Golf","Motor: 1.9tdi, Cv: 100", R.drawable.vw),
                new Coche("BMW Serie 3","Motor: m47 d20, Cv: 150", R.drawable.bmw),
                new Coche("Audi A4","Motor: 2.0tdi, Cv: 140", R.drawable.audi)
        );

        // Configurar Adaptador
        AdaptadorCoche adapter = new AdaptadorCoche(coches);
        rvCoches.setAdapter(adapter);

    }
}
