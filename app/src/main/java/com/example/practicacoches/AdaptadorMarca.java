package com.example.practicacoches;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdaptadorMarca extends RecyclerView.Adapter<AdaptadorMarca.MarcaViewHolder> {

    private List<Marca> listaMarcas;
    private Context context;

    public AdaptadorMarca(List<Marca> listaMarcas, Context context) {
        this.listaMarcas = listaMarcas;
        this.context = context;
    }

    @Override
    public MarcaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_marca, parent, false);
        return new MarcaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarcaViewHolder holder, int position) {
        Marca marca = listaMarcas.get(position);
        holder.txtMarca.setText(marca.getNombre());

        String imagen = marca.getImagen();
        if (imagen == null || imagen.isEmpty()) {
            holder.imgMarca.setImageResource(R.drawable.imagen_predeterminada);
        } else {
            Picasso.get()
                    .load(imagen)
                    .placeholder(R.drawable.imagen_predeterminada)
                    .error(R.drawable.imagen_predeterminada)
                    .into(holder.imgMarca);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CocheActivity.class);
            intent.putExtra("marcaNombre", marca.getNombre());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaMarcas.size();
    }

    public static class MarcaViewHolder extends RecyclerView.ViewHolder {
        TextView txtMarca;
        ImageView imgMarca;

        public MarcaViewHolder(View itemView) {
            super(itemView);
            txtMarca = itemView.findViewById(R.id.txtMarca);
            imgMarca = itemView.findViewById(R.id.imgMarca);
        }
    }
}
