package com.example.practicacoches;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private MainViewModel mvm;
    private boolean marcasCargadas = false;
    private boolean cochesCargados = false;
    private boolean piezasCargadas = false;

    private int totalImagenes = 0;
    private int imagenesCargadas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mvm = new ViewModelProvider(this).get(MainViewModel.class);

        mvm.getMarcas().observe(this, marcas -> {
            if (marcas != null && !marcas.isEmpty()) {
                marcasCargadas = true;
                revisarCargaCompleta();
            }
        });

        mvm.getCoches().observe(this, coches -> {
            if (coches != null && !coches.isEmpty()) {
                cochesCargados = true;
                revisarCargaCompleta();
            }
        });

        mvm.getPiezas().observe(this, piezas -> {
            if (piezas != null && !piezas.isEmpty()) {
                piezasCargadas = true;
                revisarCargaCompleta();
            }
        });

        mvm.cargarMarcas(this);
        mvm.cargarCoches(this);
        mvm.cargarPiezas(this);
    }

    private void revisarCargaCompleta() {
        if (marcasCargadas && cochesCargados && piezasCargadas) {
            precargarImagenesCoches(mvm.getCoches().getValue());
        }
    }

    private void precargarImagenesCoches(List<Coche> listaCoches) {
        if (listaCoches == null || listaCoches.isEmpty()) {
            lanzarMain();
            return;
        }

        totalImagenes = listaCoches.size();
        imagenesCargadas = 0;

        for (Coche coche : listaCoches) {
            Picasso.get()
                    .load(coche.getImagen())
                    .fetch(new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            verificarImagenesCargadas();
                        }

                        @Override
                        public void onError(Exception e) {
                            verificarImagenesCargadas();
                        }
                    });
        }
    }

    private void verificarImagenesCargadas() {
        imagenesCargadas++;
        if (imagenesCargadas == totalImagenes) {
            lanzarMain();
        }
    }

    private void lanzarMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
