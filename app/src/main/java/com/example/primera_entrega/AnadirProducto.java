package com.example.primera_entrega;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class AnadirProducto extends AppCompatActivity{

    private SQLiteDatabase db;
    private String nombre_usuario = "Iker";

    private LinearLayout layout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.anadir_producto);

        BD gestor = BD.getInstance(this);
        db = gestor.getWritableDatabase();


        Spinner spinnerCategoria = findViewById(R.id.spinnerCategoria);
        layout = findViewById(R.id.layoutCamposDinamicos);
        // Configurar adaptador del spinner
        String[] categorias = {"Selecciona una categoría", "Electrónica", "Moda", "Hogar", "Deporte", "Motor", "Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layout.removeAllViews();
                if (position == 0) {
                    return;
                }
                switch (position) {
                    case 1: // Electrónica
                        anadirCampo("Marca");
                        anadirCampo("Modelo");
                        anadirCampo("Estado");
                        break;
                    case 2: // Moda
                        anadirCampo("Talla");
                        anadirCampo("Marca");
                        break;
                    case 3: // Hogar
                        anadirCampo("Material");
                        anadirCampo("Dimensiones");
                        break;
                    case 4: // Deporte
                        anadirCampo("Tipo de deporte");
                        anadirCampo("Marca");
                        break;
                    case 5: // Motor
                        anadirCampo("Marca");
                        anadirCampo("Kilómetros");
                        anadirCampo("Año");
                        break;
                    case 6: // Otros
                        anadirCampo("Descripción");
                        break;
                }
                Log.d("spinner", "Categoría seleccionada: " + parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada si no hay selección
            }
        });

        // BOTON GUARDAR PRODUCTO

        Button btnPublicar = findViewById(R.id.btnPublicar);

        btnPublicar.setOnClickListener(v -> {

            EditText etNombre = findViewById(R.id.etNombreProducto);
            EditText etPrecio = findViewById(R.id.etPrecio);
            Spinner spinner = findViewById(R.id.spinnerCategoria);

            String nombre = etNombre.getText().toString();
            double precio = Double.parseDouble(etPrecio.getText().toString());
            int idCategoria = spinner.getSelectedItemPosition();

            if (idCategoria <= 0) {
                Toast.makeText(this, "Selecciona una categoría válida", Toast.LENGTH_LONG).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put("nombre_producto", nombre);
            values.put("precio", precio);
            values.put("id_vendedor", 1); // Iker
            values.put("id_categoria", idCategoria);

            Log.d("ANDREWPIÑA",  "HE ENTRADO EN FASE 1: " + nombre + precio + idCategoria);


            long idProducto = db.insert("Producto", null, values);

            Log.d("ANDREWPIÑA",  "salida: " + idProducto);


            guardarCamposDinamicos(idProducto);
        });

        // Resto de tus botones (home, favoritos, perfil) igual
        ImageButton btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(v -> {
            Intent i = new Intent(AnadirProducto.this, MainActivity.class);
            startActivity(i);
        });

        ImageButton btn_favoritos = findViewById(R.id.btn_favoritos);
        btn_favoritos.setOnClickListener(v -> {
            Intent i = new Intent(AnadirProducto.this, Favoritos.class);
            startActivity(i);
        });

        Button btn_subir_producto = findViewById(R.id.btnSeleccionarImagen);
        btn_subir_producto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        ImageButton btn_perfil = findViewById(R.id.btn_perfil);
        btn_perfil.setOnClickListener(v -> {
            Intent i = new Intent(AnadirProducto.this, SettingsActivity.class);
            startActivity(i);
        });
    }

    private void anadirCampo(String hint) {
        EditText et = new EditText(this);
        et.setHint(hint);
        layout.addView(et);
    }

    private void guardarCamposDinamicos(long idProducto) {

        for (int i = 0; i < layout.getChildCount(); i++) {

            View view = layout.getChildAt(i);

            if (view instanceof EditText) {
                EditText et = (EditText) view;

                String nombreAtributo = et.getHint().toString();
                String valor = et.getText().toString();

                ContentValues values = new ContentValues();
                values.put("id_producto", idProducto);
                values.put("nombre_atributo", nombreAtributo);
                values.put("valor", valor);

                Log.d("ANDREWPIÑA",  "HE ENTRADO EN FASE 3: " + idProducto + nombreAtributo + valor);

                db.insert("AtributoProducto", null, values);
            }
        }
    }

}