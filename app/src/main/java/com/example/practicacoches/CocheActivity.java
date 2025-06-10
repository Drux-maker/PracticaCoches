package com.example.practicacoches;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CocheActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nombre;
    private TextView nivelText;
    private String marcaNombre;
    private MainViewModel mvm;

    @Override
    protected void onResume() {
        super.onResume();
        actualizarNivel(); // vuelve a actualizar el nivel
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coche);

        imageView = findViewById(R.id.imgCoche);
        nombre = findViewById(R.id.tvMarca);
        nivelText = findViewById(R.id.tvNivel);

        TextView dineroText = findViewById(R.id.tvDinero);
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        double dinero = Double.longBitsToDouble(prefs.getLong("dinero", Double.doubleToLongBits(10000)));
        dineroText.setText("Dinero: " + dinero + "€");


       marcaNombre = getIntent().getStringExtra("marcaNombre");

        mvm = new ViewModelProvider(this).get(MainViewModel.class);

        mvm.getCoches().observe(this, coches -> {
            if (coches != null && !coches.isEmpty()) {
                mostrarCocheDeMarca(coches);
            }
        });

        if (mvm.getCoches().getValue() == null) {
            mvm.cargarCoches(this);
        }

        actualizarNivel();

        findViewById(R.id.btnDollar).setOnClickListener(v -> {
            startActivity(new Intent(this, ListaCochesActivity.class).putExtra("marcaNombre", marcaNombre));
        });

        findViewById(R.id.btnMejora).setOnClickListener(v -> {
            startActivity(new Intent(this, PiezasActivity.class).putExtra("marcaNombre", marcaNombre));
        });

        findViewById(R.id.btnConfiguracion).setOnClickListener(v -> {
            startActivity(new Intent(this, CompraActivity.class).putExtra("marcaNombre", marcaNombre));
        });
    }

    private void mostrarCocheDeMarca(List<Coche> coches) {
        for (Coche coche : coches) {
            nombre.setText(coche.getNombre());
            Picasso.get()
                    .load(coche.getImagen())
                    .placeholder(R.drawable.imagen_predeterminada)
                    .error(R.drawable.imagen_predeterminada)
                    .into(imageView);
            break;

        }
    }

    private void actualizarNivel() {
        SharedPreferences prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);
        int nivel = prefs.getInt("nivelCoche", 1);
        nivelText.setText("Nivel: " + nivel);
    }
}
