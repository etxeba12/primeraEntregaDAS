package com.example.primera_entrega;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Favoritos extends AppCompatActivity{

    private SQLiteDatabase db;
    private String nombre_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // para poner el idioma guardado en preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma", "es");

        cambiarIdioma(idioma);

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nombre_usuario = extras.getString("nombre");
        }

        setContentView(R.layout.favoritos);

        BD GestorDB = BD.getInstance(this);
        db = GestorDB.getWritableDatabase();

        // boton para ir a la pantalla principal del usuario
        ImageButton btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Favoritos.this, MainActivity.class);
                i.putExtra("nombre",nombre_usuario);
                startActivity(i);
                finish();
            }
        });

        // boton para ir a la pantalla de añadior producto
        ImageButton btn_anadir_producto = findViewById(R.id.btn_añadir_producto);
        btn_anadir_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Favoritos.this, AnadirProducto.class);
                i.putExtra("nombre",nombre_usuario);
                startActivity(i);
                finish();
            }
        });

        // boton para ir a las preferencias del usuario
        ImageButton btn_perfil = findViewById(R.id.btn_perfil);
        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Favoritos.this, SettingsActivity.class);
                i.putExtra("nombre",nombre_usuario);
                //i.putExtra("idioma",idioma);
                startActivity(i);
                finish();
            }
        });

        int numColumnas = 2;

        List<Producto> lista = GestorDB.listaProductosFavoritos(db,nombre_usuario);

        RecyclerView recycler = findViewById(R.id.recyclerProductos);
        recycler.setLayoutManager(new GridLayoutManager(this, numColumnas));

        int idUsuario = GestorDB.obtenerIdUsuario(db, nombre_usuario);
        productoAdapter adapter = new productoAdapter(lista, idUsuario);
        recycler.setAdapter(adapter);

    }

    protected void cambiarIdioma(String idioma){
        Locale nuevaloc = new Locale(idioma);
        Locale.setDefault(nuevaloc);

        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

}