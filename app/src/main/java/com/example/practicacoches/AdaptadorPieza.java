package com.example.practicacoches;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdaptadorPieza extends RecyclerView.Adapter<AdaptadorPieza.ViewHolder> {

    private List<Pieza> lista;
    private Set<Integer> seleccionadas = new HashSet<>();

    public AdaptadorPieza(List<Pieza> lista) {
        this.lista = lista;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        ImageView imgPieza;
        CheckBox cbSeleccionada;
        Button btnVender;
        ImageButton btnFavorito;

        ViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            imgPieza = itemView.findViewById(R.id.imgPieza);
            cbSeleccionada = itemView.findViewById(R.id.cbSeleccionada);
            btnFavorito = itemView.findViewById(R.id.btnFavorito);
        }
    }

    public ArrayList<Pieza> getPiezasSeleccionadas() {
        ArrayList<Pieza> resultado = new ArrayList<>();
        for (Integer index : seleccionadas) {
            resultado.add(lista.get(index));
        }
        return resultado;
    }

    public void marcarSeleccionadas(List<Pieza> piezasMarcadas) {
        seleccionadas.clear();
        for (int i = 0; i < lista.size(); i++) {
            for (Pieza piezaMarcada : piezasMarcadas) {
                if (lista.get(i).getId().equals(piezaMarcada.getId())) {
                    seleccionadas.add(i);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void actualizarPiezas(List<Pieza> piezas) {
        this.lista = piezas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pieza, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pieza pieza = lista.get(position);
        holder.tvNombre.setText(pieza.getNombre());
        holder.tvPrecio.setText(pieza.getPrecio() + "€");

        String imagenUrl = pieza.getImagenUrl();
        if (imagenUrl == null || imagenUrl.isEmpty()) {
            holder.imgPieza.setImageResource(R.drawable.imagen_predeterminada);
        } else {
            Picasso.get()
                    .load(imagenUrl)
                    .placeholder(R.drawable.imagen_predeterminada)
                    .error(R.drawable.imagen_predeterminada)
                    .into(holder.imgPieza);
        }

        if (pieza.isFavorito()) {
            holder.btnFavorito.setImageResource(R.drawable.ic_star);
        } else {
            holder.btnFavorito.setImageResource(R.drawable.ic_star_outline);
        }

        holder.btnFavorito.setOnClickListener(v -> {
            boolean nuevoEstado = !pieza.isFavorito();
            pieza.setFavorito(nuevoEstado);

            if (nuevoEstado) {
                holder.btnFavorito.setImageResource(R.drawable.ic_star);
            } else {
                holder.btnFavorito.setImageResource(R.drawable.ic_star_outline);
            }

            notifyItemChanged(position);
        });

        holder.cbSeleccionada.setOnCheckedChangeListener(null);
        holder.cbSeleccionada.setChecked(seleccionadas.contains(position));

        holder.cbSeleccionada.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            if (isChecked) {
                if (seleccionadas.size() < 5) {
                    seleccionadas.add(pos);
                } else {
                    holder.cbSeleccionada.setChecked(false);
                    Toast.makeText(holder.itemView.getContext(), "Máximo 5 piezas permitidas", Toast.LENGTH_SHORT).show();
                }
            } else {
                seleccionadas.remove(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}