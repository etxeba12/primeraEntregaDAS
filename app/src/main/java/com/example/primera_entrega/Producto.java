package com.example.primera_entrega;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public  class Producto {

        String imagen;
        String precio;
        boolean favorito;
    public Producto( String imagen,String precio, boolean favorito) {
        this.precio = precio;
        this.imagen = imagen;
        this.favorito = favorito;
    }

    public String getPrecio() { return precio; }
    public String getImagen() { return imagen; }
    public boolean getFavorito() { return favorito; }
}



