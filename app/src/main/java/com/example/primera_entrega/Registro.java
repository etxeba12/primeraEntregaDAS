package com.example.primera_entrega;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


import java.util.Locale;

public class Registro extends AppCompatActivity {
    private SQLiteDatabase db; //base de datos de la tabla usuarios

    private int tema;

    protected void onCreate(Bundle savedInstanceState) {

        // para poner el idioma guardado en preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String idioma = prefs.getString("idioma", "es");

        cambiarIdioma(idioma);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        BD GestorDB = BD.getInstance(this);
        db = GestorDB.getWritableDatabase();

        Button registrobt = findViewById(R.id.Registro);

        registrobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombreUsu = findViewById(R.id.NombreUsuarioMeter);
                EditText correo = findViewById(R.id.correoMeter);
                EditText contra1 = findViewById(R.id.meterContra);
                EditText contra2 = findViewById(R.id.meterContra2);
                if(!nombreUsu.getText().toString().equals("")){
                    if(!correo.getText().toString().equals("")){
                        if(!contra1.getText().toString().equals("") && !contra2.getText().toString().equals("")){
                            if(contra1.getText().toString().equals(contra2.getText().toString())){
                                if(!GestorDB.comprobarExisteUsuario(db, nombreUsu.getText().toString())) {
                                    GestorDB.meterUsuario(db,nombreUsu.getText().toString(),correo.getText().toString(),contra1.getText().toString());
                                    Intent i = new Intent(Registro.this, Login.class);
                                    i.putExtra("tema",tema);
                                    i.putExtra("idioma",idioma);
                                    startActivity(i);
                                    finish();
                                }else{
                                    dialogoAlerta dialogo = new dialogoAlerta();
                                    dialogo.setMensaje("Usuario existente, pruebe otro");
                                    dialogo.show(getSupportFragmentManager(), "etiqueta0");
                                }
                            }else{
                                //las contraseñas no coinciden
                                dialogoAlerta dialogo = new dialogoAlerta();
                                dialogo.setMensaje("Las contraseñas no coinciden");
                                dialogo.show(getSupportFragmentManager(), "etiqueta2");
                            }
                        }else{
                            //hacer que salga una pantallita diciendo que las contraseñas no puede estar vacias
                            dialogoAlerta dialogo = new dialogoAlerta();
                            dialogo.setMensaje("Las contraseñas no pueden estar vacias");
                            dialogo.show(getSupportFragmentManager(), "etiqueta3");
                        }
                    }else{
                        //hacer que salga una pantallita diciendo que las contraseñas no puede estar vacias
                        dialogoAlerta dialogo = new dialogoAlerta();
                        dialogo.setMensaje("El correo no pueden estar vacio");
                        dialogo.show(getSupportFragmentManager(), "etiqueta4");
                    }
                }else{
                    //hacer que salga una pantallita diciendo que el nombre usuario no puede estar vacio
                    dialogoAlerta dialogo = new dialogoAlerta();
                    dialogo.setMensaje("El nombre de usuario no puede estar vacio");
                    dialogo.show(getSupportFragmentManager(), "etiqueta5");
                }
                nombreUsu.setText("");
                contra1.setText("");
                contra2.setText("");
            }
        });

        Button loginbt = findViewById(R.id.LoginBoton);

        loginbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registro.this,Login.class);
                startActivity(i);
                finish();
            }
        });

    }
    public void onDestroy() {
        super.onDestroy();
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