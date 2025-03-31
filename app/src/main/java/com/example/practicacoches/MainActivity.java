package com.example.practicacoches;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ImageButton btnPrevious, btnNext;
    private final int[] imagenes = {R.drawable.vw, R.drawable.bmw, R.drawable.audi};
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        btnPrevious = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_forward);

        imageView.setOnClickListener(v -> {
            String marca = getResources().getResourceEntryName(imagenes[currentIndex]);

            Intent intent = new Intent(MainActivity.this, CocheActivity.class);
            //Aqui paso los datos que necesite
            intent.putExtra("marca", marca);

            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        updateImage();

        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                updateImage();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentIndex < imagenes.length - 1) {
                currentIndex++;
                updateImage();
            }
        });
    }

    private void updateImage() {
        imageView.setImageResource(imagenes[currentIndex]);
    }
}