package com.example.primera_entrega;

import static java.sql.Types.NULL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class Login extends AppCompatActivity {

    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {

        // para poner el idioma guardado en preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma", "es");

        cambiarIdioma(idioma);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        BD GestorDB = BD.getInstance(this);
        db = GestorDB.getWritableDatabase();

        if(GestorDB.tablaEstaVacia(db,"Categoria")){
            db.execSQL("INSERT INTO Categoria (nombre) VALUES " +
                    "('Electrónica'), " +
                    "('Moda'), " +
                    "('Hogar'), " +
                    "('Deporte'), " +
                    "('Motor'), " +
                    "('Otros')");
        }
        if(GestorDB.tablaEstaVacia(db,"Usuario")){
            db.execSQL("INSERT INTO Usuario (email, contrasena, nombre_de_usuario, foto_de_perfil, activa) VALUES " +
                    "('iker@gmail.com','1234','Iker','perfil_iker',1), " +
                    "('ander@gmail.com','1234','Ander','perfil_ander',1), " +
                    "('andrea@gmail.com','1234','Andrea','perfil_andrea',1)");
        }
        if(GestorDB.tablaEstaVacia(db,"Producto")){
            db.execSQL("INSERT INTO Producto (nombre_producto, descripcion, precio, imagen, estado, id_vendedor, id_categoria) VALUES " +
                    "('Zapatillas rojas','Zapatillas nuevas, talla 42',49.99,'zapatillas',1,1,2), " +
                    "('Virgen','Figura decorativa',25.50,'zapatillas',1,3,3), " +
                    "('Bolso vintage','Bolso buen estado',30.00,'zapatillas',1,3,2), " +
                    "('Peluca rubia','Peluca sintética',15.00,'zapatillas',1,2,2), " +
                    "('Camiseta negra','Talla L',12.00,'zapatillas',1,2,2), " +
                    "('Chaqueta vaquera','Perfecto estado',40.00,'zapatillas',1,2,2), " +
                    "('Reloj deportivo','Con cronómetro',20.00,'zapatillas',1,2,4)");
        }
        if(GestorDB.tablaEstaVacia(db,"Favoritos")){
            db.execSQL("INSERT INTO Favoritos (id_usuario, id_producto) VALUES " +
                    "(1,2), " +
                    "(1,4), " +
                    "(1,6)");
        }

        Button btLogin = findViewById(R.id.LoginBoton);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usuario = findViewById(R.id.NombreUsuarioMeter);
                EditText contra = findViewById(R.id.meterContra);
                String valido = GestorDB.comprobarUsuario(db,usuario.getText().toString());
                if (valido != null){
                    if(GestorDB.comprobarContraseña(db,usuario.getText().toString(),contra.getText().toString())) {
                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.putExtra("nombre",usuario.getText().toString());
                        startActivity(i);
                        finish();
                    }
                }else{
                    dialogoAlerta dialogo = new dialogoAlerta();
                    dialogo.setMensaje("No existe ese usuario");
                    dialogo.show(getSupportFragmentManager(), "etiqueta1");
                    usuario.setText("");
                    contra.setText("");
                }
            }
        });

        Button btRegistro = findViewById(R.id.Registro);
        btRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registro.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
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