package com.example.primera_entrega;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class productoAdapter extends RecyclerView.Adapter<productoAdapter.ProductoViewHolder>{
    private List<Producto> lista;

    private int idUsuario;

    public productoAdapter(List<Producto> lista, int idUsuario) {
        this.lista = lista;
        this.idUsuario = idUsuario;
    }
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto, imgLike;
        TextView txtPrecio;
        Button btnComprar;

        public ProductoViewHolder(View itemView) {
            super(itemView);

            imgProducto = itemView.findViewById(R.id.foto);
            txtPrecio = itemView.findViewById(R.id.texto);
            imgLike = itemView.findViewById(R.id.imgLike);
            btnComprar = itemView.findViewById(R.id.botonComprar);
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

        int imagenId = holder.itemView.getContext().getResources().getIdentifier(
                producto.getImagen(),   // "zapatillas", "peluca", etc.
                "drawable",             // carpeta drawable
                holder.itemView.getContext().getPackageName()
        );

        holder.imgProducto.setImageResource(imagenId);
        holder.txtPrecio.setText(producto.getPrecio());

        holder.imgLike.setImageResource(
                producto.getFavorito() ?
                        R.drawable.baseline_favorite_24 :
                        R.drawable.outline_favorite_24
        );

        holder.imgLike.setOnClickListener(v -> {

            BD gestorDB = BD.getInstance(v.getContext());
            SQLiteDatabase db = gestorDB.getWritableDatabase();

            if(producto.getFavorito()){
                gestorDB.quitarFavorito(db, producto.getId(), idUsuario);
                producto.setFavorito(false);
            } else {
                gestorDB.anadirFavorito(db, producto.getId(), idUsuario);
                producto.setFavorito(true);
            }

            notifyItemChanged(holder.getAdapterPosition());
        });

        holder.btnComprar.setOnClickListener(v -> {
            Producto Producto = lista.get(holder.getAdapterPosition());
            Log.d("comprar", "id" + holder.getAdapterPosition());

            BD gestorDB = BD.getInstance(v.getContext());
            SQLiteDatabase db = gestorDB.getWritableDatabase();

            gestorDB.comprarProducto(db, Producto.getId());

            lista.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());


        });
    }


    @Override
    public int getItemCount() {
        return lista.size();
    }
}
