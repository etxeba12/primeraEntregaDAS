package com.example.primera_entrega;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BD extends SQLiteOpenHelper {
    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override  //para activar el FOREIGN KEY
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Usuario (\n" +
                "    id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    email TEXT NOT NULL UNIQUE,\n" +
                "    contrasena TEXT NOT NULL,\n" +
                "    nombre_de_usuario TEXT NOT NULL,\n" +
                "    foto_de_perfil TEXT,\n" +
                "    activa INTEGER NOT NULL DEFAULT 1\n" +
                ");");

        sqLiteDatabase.execSQL("CREATE TABLE Producto (" +
                "id_producto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_producto TEXT NOT NULL," +
                "descripcion TEXT," +
                "precio REAL NOT NULL," +
                "imagen TEXT," +
                "estado INTEGER NOT NULL DEFAULT 1," +
                "id_vendedor INTEGER NOT NULL," +
                "FOREIGN KEY (id_vendedor) REFERENCES Usuario(id_usuario) ON DELETE RESTRICT" +
                ");");

        sqLiteDatabase.execSQL("CREATE TABLE Favoritos (" +
                "id_producto INTEGER NOT NULL," +
                "id_usuario INTEGER NOT NULL," +
                "PRIMARY KEY (id_producto, id_usuario)," +
                "FOREIGN KEY (id_producto) REFERENCES Producto(id_producto) ON DELETE CASCADE," +
                "FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario) ON DELETE CASCADE" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
