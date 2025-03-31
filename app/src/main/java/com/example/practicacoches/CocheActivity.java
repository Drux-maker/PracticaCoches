package com.example.practicacoches;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CocheActivity extends AppCompatActivity {
    int[] imagenes = {R.drawable.audi_a3_8p};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coche);

        // Configurar botones
        ImageButton btnVolver = findViewById(R.id.btnVolver);
        ImageButton btnDollar = findViewById(R.id.btnDollar);
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        ImageButton btnUpgrade = findViewById(R.id.btnUpgrade);
        ImageView imgCoche = findViewById(R.id.imgCoche);
        TextView tvMarca = findViewById(R.id.tvMarca);

        // Recibir datos de MainActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("num_i")) {
            //Recibo que marca es Luego lo usare para filtrar la imagenes de los coches disponibles
            String marca = intent.getStringExtra("marca");
            //De alguna froma se eligira el coche principal y este se pondra siempre al inicio del array
            tvMarca.setText(marca);
            imgCoche.setImageResource(imagenes[0]);
        }

        btnVolver.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnDollar.setOnClickListener(v -> {
            Toast.makeText(this, "Compra de vehiculos", Toast.LENGTH_SHORT).show();
            // Lógica
        });

        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Configuración", Toast.LENGTH_SHORT).show();
            // Abrir actividad configuración
        });

        btnUpgrade.setOnClickListener(v -> {
            Toast.makeText(this, "Mejorar versión", Toast.LENGTH_SHORT).show();
            // Lógica
        });
    }
}
