package com.example.primera_entrega;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class productoAdapter extends RecyclerView.Adapter<productoAdapter.ProductoViewHolder>{
    private List<Producto> lista;

    public productoAdapter(List<Producto> lista) {
        this.lista = lista;
    }
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto, imgLike;
        TextView txtPrecio;

        public ProductoViewHolder(View itemView) {
            super(itemView);

            imgProducto = itemView.findViewById(R.id.foto);
            txtPrecio = itemView.findViewById(R.id.texto);
            imgLike = itemView.findViewById(R.id.imgLike);
        }
    }

    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elLayoutDeCadaItem = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.producto, parent, false);
        ProductoViewHolder evh = new ProductoViewHolder(elLayoutDeCadaItem);
        return evh;
    }


    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = lista.get(position);

        holder.imgProducto.setImageResource(producto.imagen);
        holder.txtPrecio.setText(producto.precio);

        holder.imgLike.setImageResource(
                producto.liked ?
                        R.drawable.baseline_favorite_24 :
                        R.drawable.outline_favorite_24
        );

        holder.imgLike.setOnClickListener(v -> {
            producto.liked = !producto.liked;
            notifyItemChanged(position);
        });
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }
}
