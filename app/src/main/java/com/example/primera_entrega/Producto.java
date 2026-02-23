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

        int imagen;
        String precio;
        boolean liked;
        public Producto(int imagen, String precio) {
            this.imagen = imagen;
            this.precio = precio;
            this.liked = false;
        }
}



