package com.example.primera_entrega;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Favoritos extends AppCompatActivity{

    private SQLiteDatabase db;
    private String nombre_usuario = "Iker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.favoritos);

        this.deleteDatabase("Tabla");
        BD GestorDB = BD.getInstance(this);
        db = GestorDB.getWritableDatabase();

        // boton para ir a la pantalla principal del usuario
        ImageButton btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Favoritos.this, MainActivity.class);
                startActivity(i);
            }
        });

        // boton para ir a la pantalla de añadior producto
        ImageButton btn_anadir_producto = findViewById(R.id.btn_añadir_producto);
        btn_anadir_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Favoritos.this, AnadirProducto.class);
                startActivity(i);
            }
        });

        // boton para ir a las preferencias del usuario
        ImageButton btn_perfil = findViewById(R.id.btn_perfil);
        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Favoritos.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        int numColumnas = 2;

        List<Producto> lista = GestorDB.listaProductosFavoritos(db,nombre_usuario);

        RecyclerView recycler = findViewById(R.id.recyclerProductos);
        recycler.setLayoutManager(new GridLayoutManager(this, numColumnas));

        productoAdapter adapter = new productoAdapter(lista);
        recycler.setAdapter(adapter);

    }

}