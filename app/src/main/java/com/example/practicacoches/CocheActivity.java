package com.example.practicacoches;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CocheActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView nombre;
    private TextView nivelText;
    private TextView dineroText;
    private String marcaNombre;
    private MainViewModel mvm;
    private SharedPreferences prefs;
    private VideoView videoCoche;

    private Coche cocheSeleccionado;

    @Override
    protected void onResume() {
        super.onResume();
        actualizarNivel();
        actualizarDinero();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coche);

        imageView = findViewById(R.id.imgCoche);
        videoCoche = findViewById(R.id.videoCoche);
        nombre = findViewById(R.id.tvMarca);
        nivelText = findViewById(R.id.tvNivel);
        dineroText = findViewById(R.id.tvDinero);
        prefs = getSharedPreferences("MisDatos", MODE_PRIVATE);

        mvm = new ViewModelProvider(this).get(MainViewModel.class);

        // Reproducir video
        FrameLayout videoContainer = findViewById(R.id.videoContainer);
        videoCoche = findViewById(R.id.videoCoche);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoCoche.setVideoURI(videoUri);

        videoCoche.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            MediaController mediaController = new MediaController(this);
            // ANCLA al contenedor, no solo al video
            mediaController.setAnchorView(videoContainer);
            // Asegura que se limite dentro del contenedor
            mediaController.setLayoutParams(
                    new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            );

            videoCoche.setMediaController(mediaController);
        });

        videoCoche.start();

        // Comprobar si se seleccionó un coche directamente
        cocheSeleccionado = (Coche) getIntent().getSerializableExtra("cocheSeleccionado");

        if (cocheSeleccionado != null) {
            mostrarCocheSeleccionado();
        } else {
            marcaNombre = getIntent().getStringExtra("marcaNombre");

            mvm.getCoches().observe(this, coches -> {
                if (coches != null && !coches.isEmpty()) {
                    mostrarCocheDeMarca(coches);
                }
            });

            if (mvm.getCoches().getValue() == null) {
                mvm.cargarCoches(this);
            }
        }

        actualizarNivel();
        actualizarDinero();

        findViewById(R.id.btnDollar).setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaCochesActivity.class);
            intent.putExtra("marcaNombre", cocheSeleccionado != null
                    ? cocheSeleccionado.getMarca() : marcaNombre);
            startActivity(intent);
        });


        findViewById(R.id.btnMejora).setOnClickListener(v -> {
            startActivity(new Intent(this, PiezasActivity.class));
        });

        findViewById(R.id.btnConfiguracion).setOnClickListener(v -> {
            startActivity(new Intent(this, CompraActivity.class));
        });

        findViewById(R.id.btnVolver).setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Toast.makeText(this, "Preferencias reseteadas", Toast.LENGTH_SHORT).show();
        });
    }

    private void mostrarCocheSeleccionado() {
        nombre.setText(cocheSeleccionado.getNombre());
        Picasso.get()
                .load(cocheSeleccionado.getImagen())
                .placeholder(R.drawable.imagen_predeterminada)
                .error(R.drawable.imagen_predeterminada)
                .into(imageView);
    }

    private void mostrarCocheDeMarca(List<Coche> coches) {
        for (Coche coche : coches) {
            if (coche.getMarca().equalsIgnoreCase(marcaNombre)) {
                nombre.setText(coche.getNombre());
                Picasso.get()
                        .load(coche.getImagen())
                        .placeholder(R.drawable.imagen_predeterminada)
                        .error(R.drawable.imagen_predeterminada)
                        .into(imageView);
                break;
            }
        }
    }

    private void actualizarNivel() {
        int nivel = prefs.getInt("nivelCoche", 1);
        nivelText.setText("Nivel: " + nivel);
    }

    private void actualizarDinero() {
        double dinero = Double.longBitsToDouble(prefs.getLong("dinero", Double.doubleToLongBits(5000)));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        dineroText.setText("Dinero: " + decimalFormat.format(dinero) + "€");
    }
}