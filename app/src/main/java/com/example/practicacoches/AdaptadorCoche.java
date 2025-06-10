package com.example.practicacoches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorCoche extends RecyclerView.Adapter<AdaptadorCoche.CocheViewHolder> {
    private List<Coche> coches;

    public AdaptadorCoche(List<Coche> coches) {
        this.coches = coches;
    }

    @NonNull
    @Override
    public CocheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coche, parent, false);
        return new CocheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocheViewHolder holder, int position) {
        Coche coche = coches.get(position);

        String imagen = coche.getImagen();
        if (imagen == null || imagen.isEmpty()) {
            holder.imgCoche.setImageResource(R.drawable.imagen_predeterminada);
        } else {
            Picasso.get()
                    .load(imagen)
                    .placeholder(R.drawable.imagen_predeterminada)
                    .error(R.drawable.imagen_predeterminada)
                    .into(holder.imgCoche);
        }

        holder.tvModelo.setText(coche.getNombre());
    }

    @Override
    public int getItemCount() {
        return coches.size();
    }

    public static class CocheViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCoche;
        TextView tvModelo, tvId;

        public CocheViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCoche = itemView.findViewById(R.id.imgCoche);
            tvModelo = itemView.findViewById(R.id.tvModelo);
            tvId = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}
