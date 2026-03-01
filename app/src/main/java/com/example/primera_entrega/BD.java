package com.example.primera_entrega;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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
                "favorito boolean," +
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

        insertarUsuariosPrueba(sqLiteDatabase);
        insertarProductosPrueba(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Favoritos");
        db.execSQL("DROP TABLE IF EXISTS Producto");
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        onCreate(db);
    }

    public List<Producto> listaProductos(SQLiteDatabase db,String nombre_usuario){
        List<Producto> productos = new ArrayList<>(); //lista donde guardaremos los productos

        // para filtar por nombre_de_usuario
        String query = "SELECT P.favorito, P.precio, P.imagen " +
                "FROM Producto P " +
                "INNER JOIN Usuario U ON P.id_vendedor = U.id_usuario " +
                "WHERE U.nombre_de_usuario != ?";

        String[] selectionArgs = {nombre_usuario};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()){
            do{
                boolean favorito = cursor.getInt(0) == 1;
                String precio = cursor.getString(1);
                String imagen = cursor.getString(2);

                productos.add(new Producto(imagen,precio, favorito));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productos;
    }

    public void insertarUsuariosPrueba(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // Usuario 1: Iker
        values.put("email", "iker@gmail.com");
        values.put("contrasena", "1234"); // en producción deberías hashear
        values.put("nombre_de_usuario", "Iker");
        values.put("foto_de_perfil", "perfil_iker"); // nombre de drawable o URL
        values.put("activa", 1);
        db.insert("Usuario", null, values);

        // Usuario 2: Ander
        values.clear();
        values.put("email", "ander@gmail.com");
        values.put("contrasena", "1234");
        values.put("nombre_de_usuario", "Ander");
        values.put("foto_de_perfil", "perfil_ander");
        values.put("activa", 1);
        db.insert("Usuario", null, values);

        // Usuario 3: Andrea
        values.clear();
        values.put("email", "andrea@gmail.com");
        values.put("contrasena", "1234");
        values.put("nombre_de_usuario", "Andrea");
        values.put("foto_de_perfil", "perfil_andrea");
        values.put("activa", 1);
        db.insert("Usuario", null, values);
    }

    public void insertarProductosPrueba(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        // --------------------
        // Producto de Iker
        // --------------------
        values.put("nombre_producto", "Zapatillas rojas");
        values.put("descripcion", "Zapatillas nuevas, talla 42");
        values.put("precio", 49.99);
        values.put("imagen", "zapatillas"); // nombre de drawable
        values.put("estado", 1); // 1 = en venta
        values.put("id_vendedor", 1); // Iker
        db.insert("Producto", null, values);

        // --------------------
        // Productos de Andrea
        // --------------------
        values.clear();
        values.put("nombre_producto", "Virgen");
        values.put("descripcion", "Figura de virgen decorativa");
        values.put("precio", 25.50);
        values.put("imagen", "zapatillas");
        values.put("estado", 1);
        values.put("id_vendedor", 3); // Andrea
        db.insert("Producto", null, values);

        values.clear();
        values.put("nombre_producto", "Bolso vintage");
        values.put("descripcion", "Bolso de segunda mano, buen estado");
        values.put("precio", 30.00);
        values.put("imagen", "zapatillas");
        values.put("estado", 1);
        values.put("id_vendedor", 3); // Andrea
        db.insert("Producto", null, values);

        // --------------------
        // Productos de Ander
        // --------------------
        values.clear();
        values.put("nombre_producto", "Peluca rubia");
        values.put("descripcion", "Peluca sintética, casi nueva");
        values.put("precio", 15.00);
        values.put("imagen", "zapatillas");
        values.put("estado", 1);
        values.put("id_vendedor", 2); // Ander
        db.insert("Producto", null, values);

        values.clear();
        values.put("nombre_producto", "Camiseta negra");
        values.put("descripcion", "Camiseta talla L, usada una vez");
        values.put("precio", 12.00);
        values.put("imagen", "zapatillas");
        values.put("estado", 1);
        values.put("id_vendedor", 2);
        db.insert("Producto", null, values);

        values.clear();
        values.put("nombre_producto", "Chaqueta vaquera");
        values.put("descripcion", "Chaqueta en perfecto estado");
        values.put("precio", 40.00);
        values.put("imagen", "zapatillas");
        values.put("estado", 1);
        values.put("id_vendedor", 2);
        db.insert("Producto", null, values);

        values.clear();
        values.put("nombre_producto", "Reloj deportivo");
        values.put("descripcion", "Reloj con cronómetro, usado");
        values.put("precio", 20.00);
        values.put("imagen", "zapatillas");
        values.put("estado", 1);
        values.put("id_vendedor", 2);
        db.insert("Producto", null, values);
    }

}
