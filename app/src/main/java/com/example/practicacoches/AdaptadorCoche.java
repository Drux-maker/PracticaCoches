package com.example.practicacoches;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorCoche extends RecyclerView.Adapter<AdaptadorCoche.CocheViewHolder> {

    private List<Coche> coches;

    public AdaptadorCoche(List<Coche> coches) {
        this.coches = coches;
    }

    //Aqui añadimos tantos items de coche como coches hay
    @NonNull
    @Override
    public CocheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_coche, parent, false);
        return new CocheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CocheViewHolder holder, int position) {
        Coche coche = coches.get(position);
        holder.imgCoche.setImageResource(coche.getImagenResId());
        holder.tvModelo.setText(coche.getModelo());
        holder.tvDescripcion.setText(coche.getDescripcion());
    }

    @Override
    public int getItemCount() { return coches.size(); }

    public static class CocheViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCoche;
        TextView tvModelo;
        TextView tvDescripcion;


        public CocheViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCoche = itemView.findViewById(R.id.imgCoche);
            tvModelo = itemView.findViewById(R.id.tvModelo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }
    }
}
