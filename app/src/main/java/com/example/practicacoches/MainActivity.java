package com.example.practicacoches;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView imageMarca;
    private ImageButton btnAnterior, btnSiguiente;
    private List<Marca> listaMarcas = new ArrayList<>();
    private int indiceActual = 0;
    private MainViewModel mvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageMarca = findViewById(R.id.imageView);
        btnAnterior = findViewById(R.id.btn_back);
        btnSiguiente = findViewById(R.id.btn_forward);

        mvm = new ViewModelProvider(this).get(MainViewModel.class);

        if (savedInstanceState != null) {
            indiceActual = savedInstanceState.getInt("indice_actual", 0);
        }

        mvm.getMarcas().observe(this, marcas -> {
            if (marcas != null && !marcas.isEmpty()) {
                listaMarcas = marcas;
                mostrarMarca(indiceActual);
            }
        });

        if (mvm.getMarcas().getValue() == null) {
            mvm.cargarMarcas(this);
        }

        btnAnterior.setOnClickListener(v -> {
            if (indiceActual > 0) {
                indiceActual--;
                mostrarMarca(indiceActual);
            }
        });

        btnSiguiente.setOnClickListener(v -> {
            if (listaMarcas != null && indiceActual < listaMarcas.size() - 1) {
                indiceActual++;
                mostrarMarca(indiceActual);
            }
        });

        imageMarca.setOnClickListener(v -> {
            if (!listaMarcas.isEmpty()) {
                Marca marcaSeleccionada = listaMarcas.get(indiceActual);
                Intent intent = new Intent(MainActivity.this, CocheActivity.class);
                intent.putExtra("marcaId", marcaSeleccionada.getId());
                intent.putExtra("marcaNombre", marcaSeleccionada.getNombre());
                startActivity(intent);
            }
        });
    }

    private void mostrarMarca(int indice) {
        Marca marca = listaMarcas.get(indice);
        Picasso.get()
                .load(marca.getImagen())
                .placeholder(R.drawable.imagen_predeterminada)
                .error(R.drawable.imagen_predeterminada)
                .into(imageMarca);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("indice_actual", indiceActual);
    }
}
