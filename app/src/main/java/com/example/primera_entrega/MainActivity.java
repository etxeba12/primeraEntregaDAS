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

public class MainActivity extends AppCompatActivity{

    private SQLiteDatabase db;
    private String nombre_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nombre_usuario = extras.getString("nombre");
            //idioma = extras.getString("idioma");
        }

        setContentView(R.layout.lista_productos);

        BD GestorDB = BD.getInstance(this);
        db = GestorDB.getWritableDatabase();

        // boton para ir a la pantalla de favortios del usuario
        ImageButton btn_favoritos = findViewById(R.id.btn_favoritos);
        btn_favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Favoritos.class);
                i.putExtra("nombre",nombre_usuario);
                //i.putExtra("idioma",idioma);
                startActivity(i);
                finish();
            }
        });

        // boton para ir a la pantalla de añadior producto
        ImageButton btn_anadir_producto = findViewById(R.id.btn_añadir_producto);
        btn_anadir_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AnadirProducto.class);
                i.putExtra("nombre",nombre_usuario);
                //i.putExtra("idioma",idioma);
                startActivity(i);
                finish();
            }
        });

        // boton para ir a las preferencias del usuario
        ImageButton btn_perfil = findViewById(R.id.btn_perfil);
        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                i.putExtra("nombre",nombre_usuario);
                //i.putExtra("idioma",idioma);
                startActivity(i);
                finish();
            }
        });

        int numColumnas = 2;

        List<Producto> lista = GestorDB.listaProductos(db,nombre_usuario);

        RecyclerView recycler = findViewById(R.id.recyclerProductos);
        recycler.setLayoutManager(new GridLayoutManager(this, numColumnas));

        productoAdapter adapter = new productoAdapter(lista);
        recycler.setAdapter(adapter);

    }

    public void aplicarModoOscuro(boolean activado) {
        if (activado) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }
}