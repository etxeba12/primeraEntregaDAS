package com.example.primera_entrega;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnadirProducto extends AppCompatActivity{

    private SQLiteDatabase db;
    private String nombre_usuario;

    private ImageView imageViewProducto;

    private LinearLayout layout ;
    private static final int PICK_IMAGE = 101;
    private Uri imagenSeleccionada;

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
            values.put("id_vendedor",gestor.obtenerIdUsuario(db,nombre_usuario));
            values.put("id_categoria", idCategoria);

            Log.d("ANDREWPIÑA",  "HE ENTRADO EN FASE 1: " + nombre + precio + idCategoria);

            if (imagenSeleccionada != null) {
                String rutaImagen = guardarImagenInterna(imagenSeleccionada);
                values.put("imagen", rutaImagen);
            } else {
                values.put("imagen", "imagen_por_defecto"); // en caso de que no suba ninguna
            }

            long idProducto = db.insert("Producto", null, values);

            Log.d("ANDREWPIÑA",  "salida: " + idProducto);


            guardarCamposDinamicos(idProducto);
        });

        // Resto de tus botones
        ImageButton btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(v -> {
            Intent i = new Intent(AnadirProducto.this, MainActivity.class);
            i.putExtra("nombre",nombre_usuario);
            //i.putExtra("idioma",idioma);
            startActivity(i);
            finish();
        });

        ImageButton btn_favoritos = findViewById(R.id.btn_favoritos);
        btn_favoritos.setOnClickListener(v -> {
            Intent i = new Intent(AnadirProducto.this, Favoritos.class);
            i.putExtra("nombre",nombre_usuario);
            //i.putExtra("idioma",idioma);
            startActivity(i);
            finish();
        });

        Button btn_subir_producto = findViewById(R.id.btnSeleccionarImagen);
        btn_subir_producto.setOnClickListener(v -> abrirGaleria());

        ImageButton btn_perfil = findViewById(R.id.btn_perfil);
        btn_perfil.setOnClickListener(v -> {
            Intent i = new Intent(AnadirProducto.this, SettingsActivity.class);
            i.putExtra("nombre",nombre_usuario);
            //i.putExtra("idioma",idioma);
            startActivity(i);
            finish();
        });

        imageViewProducto = findViewById(R.id.imgPerfil);
    }

    private void anadirCampo(String hint) {
        EditText et = new EditText(this);
        et.setHint(hint);
        layout.addView(et);
    }

    private String guardarImagenInterna(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            String nombreArchivo = "producto_" + System.currentTimeMillis() + ".jpg";
            File archivo = new File(getFilesDir(), nombreArchivo);
            OutputStream output = new FileOutputStream(archivo);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.close();
            input.close();
            return archivo.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

                mostrarNotificacionProducto();

                Intent in = new Intent(this, MainActivity.class);
                in.putExtra("nombre", nombre_usuario);
                startActivity(in);
                finish();

            }
        }
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

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imagenSeleccionada = data.getData();
            imageViewProducto.setImageURI(imagenSeleccionada);
        }
    }

    private void mostrarNotificacionProducto() {

        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(elCanal);
        }

        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Producto subido")
                .setContentText("El producto se ha subido correctamente a la aplicación!")
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);

        elManager.notify(1, elBuilder.build());
    }

}