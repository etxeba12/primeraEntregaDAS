package com.example.primera_entrega;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        // Cargar imagen desde archivo interno si existe
        String rutaImagen = producto.getImagen();
        if (rutaImagen != null && !rutaImagen.equals("")) {
            File imgFile = new File(rutaImagen);
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imgProducto.setImageBitmap(bitmap);
            } else {
                holder.imgProducto.setImageResource(R.drawable.imagen_por_defecto);
            }
        } else {
            holder.imgProducto.setImageResource(R.drawable.imagen_por_defecto);
        }

        // Precio
        holder.txtPrecio.setText(producto.getPrecio());

        // Favorito
        holder.imgLike.setImageResource(
                producto.getFavorito() ?
                        R.drawable.baseline_favorite_24 :
                        R.drawable.outline_favorite_24
        );

        // Click en like
        holder.imgLike.setOnClickListener(v -> {
            BD gestorDB = BD.getInstance(v.getContext());
            SQLiteDatabase db = gestorDB.getWritableDatabase();

            if (producto.getFavorito()) {
                gestorDB.quitarFavorito(db, producto.getId(), idUsuario);
                producto.setFavorito(false);
            } else {
                gestorDB.anadirFavorito(db, producto.getId(), idUsuario);
                producto.setFavorito(true);
            }

            notifyItemChanged(holder.getAdapterPosition());
        });

        // Click en comprar
        holder.btnComprar.setOnClickListener(v -> {
            Producto Producto = lista.get(holder.getAdapterPosition());
            BD gestorDB = BD.getInstance(v.getContext());
            SQLiteDatabase db = gestorDB.getWritableDatabase();

            gestorDB.comprarProducto(db, Producto.getId());

            // Guardar compra en fichero
            guardarCompraEnFichero(v.getContext(),producto.getId(), producto.getPrecio());

            lista.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });

    }

    private void guardarCompraEnFichero(Context context, int idProducto, String precio) {
        String nombreFichero = "compras.txt";

        // Hora actual
        String hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String contenido = "ID: " + idProducto + ", Precio: " + precio + ", Hora: " + hora + "\n";

        try {
            FileOutputStream fos = context.openFileOutput(nombreFichero, Context.MODE_APPEND);
            fos.write(contenido.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return lista.size();
    }
}
