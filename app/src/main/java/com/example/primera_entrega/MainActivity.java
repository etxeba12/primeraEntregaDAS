package com.example.primera_entrega;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lista_productos);

        ImageButton btn_perfil = findViewById(R.id.btn_perfil);
        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });

        int numColumnas = 2;
        RecyclerView recycler = findViewById(R.id.recyclerProductos);
        recycler.setLayoutManager(new GridLayoutManager(this, numColumnas));

        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto(R.drawable.zapatillas, "49€"));
        lista.add(new Producto(R.drawable.camiseta, "19€"));
        lista.add(new Producto(R.drawable.zapatillas, "49€"));
        lista.add(new Producto(R.drawable.camiseta, "19€"));
        lista.add(new Producto(R.drawable.zapatillas, "49€"));
        lista.add(new Producto(R.drawable.camiseta, "19€"));
        lista.add(new Producto(R.drawable.zapatillas, "49€"));
        lista.add(new Producto(R.drawable.camiseta, "19€"));


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