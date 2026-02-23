package com.example.primera_entrega;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lista_productos);

        RecyclerView recycler = findViewById(R.id.recyclerProductos);

        // tipo de lista
        recycler.setLayoutManager(new LinearLayoutManager(this));

        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto(R.drawable.zapatillas, "49€"));
        lista.add(new Producto(R.drawable.camiseta, "19€"));

        productoAdapter adapter = new productoAdapter(lista);
        recycler.setAdapter(adapter);

    }
}