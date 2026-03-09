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
    private static BD instancia;

    public static synchronized BD getInstance(Context context) {
        if (instancia == null) {
            instancia = new BD(context.getApplicationContext(), "Tabla", null, 2);
        }
        return instancia;
    }
    BD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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

        sqLiteDatabase.execSQL("CREATE TABLE Categoria (" +
                "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL UNIQUE" +
                ");");

        sqLiteDatabase.execSQL("CREATE TABLE AtributoProducto (" +
                "id_atributo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_producto INTEGER NOT NULL," +
                "nombre_atributo TEXT NOT NULL," +
                "valor TEXT NOT NULL," +
                "FOREIGN KEY (id_producto) REFERENCES Producto(id_producto) ON DELETE CASCADE" +
                ");");

        sqLiteDatabase.execSQL("CREATE TABLE Producto (" +
                "id_producto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre_producto TEXT NOT NULL," +
                "descripcion TEXT," +
                "precio REAL NOT NULL," +
                "imagen TEXT DEFAULT 'zapatillas'," +
                "favorito INTEGER DEFAULT 0," +
                "estado INTEGER NOT NULL DEFAULT 1," +
                "id_vendedor INTEGER NOT NULL," +
                "id_categoria INTEGER NOT NULL," +
                "FOREIGN KEY (id_vendedor) REFERENCES Usuario(id_usuario) ON DELETE RESTRICT," +
                "FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria) ON DELETE RESTRICT" +
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
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS Favoritos");
        db.execSQL("DROP TABLE IF EXISTS AtributoProducto");
        db.execSQL("DROP TABLE IF EXISTS Producto");
        db.execSQL("DROP TABLE IF EXISTS Categoria");
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        onCreate(db);
    }

    public boolean comprobarExisteUsuario(SQLiteDatabase db,String usu) {
        String query = "SELECT * FROM Usuario WHERE nombre_de_usuario = ?";
        String[] selectionArgs = {usu};
        boolean existe = false;
        Cursor c = db.rawQuery(query, selectionArgs);
        if (c.getCount() != 0) {
            existe = true;
        }
        c.close(); // Cerramos el cursor después de usarlo
        return existe;
    }
    public String comprobarUsuario(SQLiteDatabase db,String usu) {
        String query = "SELECT * FROM Usuario WHERE nombre_de_usuario = ?";
        String[] selectionArgs = {usu};
        Cursor c = db.rawQuery(query, selectionArgs);
        if (c.getCount() != 0) {
            if (c.moveToFirst()) {
                c.close();
                return usu;
            }
        }
        c.close(); // Cerramos el cursor después de usarlo
        return null;
    }

    public int obtenerIdUsuario(SQLiteDatabase db, String nombreUsuario) {
        int id = -1;

        String query = "SELECT id_usuario FROM Usuario WHERE nombre_de_usuario = ?";
        String[] selectionArgs = {nombreUsuario};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        cursor.close();
        return id;
    }

    public boolean comprobarContraseña(SQLiteDatabase db,String usu,String contra) {
        String query = "SELECT * FROM Usuario WHERE nombre_de_usuario = ? AND contrasena = ?";
        String[] selectionArgs = {usu,contra};
        boolean existe = false;
        Cursor c = db.rawQuery(query, selectionArgs);
        if (c.getCount() != 0) {
            existe = true;
        }
        c.close(); // Cerramos el cursor después de usarlo
        return existe;
    }

    public void meterUsuario(SQLiteDatabase db,String nombreUsuario,String pCorreo, String pContrasena){
        //Agregar un usuario a la base datos
        ContentValues values = new ContentValues();
        values.put("nombre_de_usuario", nombreUsuario);
        values.put("email", pCorreo);
        values.put("contrasena", pContrasena);

        // Ejecutar la consulta parametrizada
        db.insert("Usuario", null, values);
    }

    public boolean tablaEstaVacia(SQLiteDatabase db, String nombreTabla) {
        boolean tablaVacia = true;
        Cursor cursor = null;
        String query = "SELECT COUNT(*) FROM " + nombreTabla;
        cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int rowCount = cursor.getInt(0);
            // Verificar si el número de filas es cero
            tablaVacia = (rowCount == 0);
            cursor.close(); // Cerrar el cursor después de usarlo
        }
        return tablaVacia;
    }

    public List<Producto> listaProductos(SQLiteDatabase db,String nombre_usuario){
        List<Producto> productos = new ArrayList<>(); //lista donde guardaremos los productos

        // para filtar por nombre_de_usuario
        String query = "SELECT P.id_producto, " +
                "CASE WHEN F.id_producto IS NULL THEN 0 ELSE 1 END AS favorito, " +
                "P.precio, P.imagen " +
                "FROM Producto P " +
                "INNER JOIN Usuario U ON P.id_vendedor = U.id_usuario " +
                "LEFT JOIN Favoritos F ON P.id_producto = F.id_producto " +
                "AND F.id_usuario = (SELECT id_usuario FROM Usuario WHERE nombre_de_usuario = ?) " +
                "WHERE U.nombre_de_usuario != ? AND P.estado = 1";

        //para saber si ese producto está en favoritos del usuario actual y para no mostrar productos vendidos por ese usuario
        String[] selectionArgs = {nombre_usuario, nombre_usuario};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()){
            do{
                int idProducto = cursor.getInt(0);
                boolean favorito = cursor.getInt(1) == 1;
                String precio = cursor.getString(2);
                String imagen = cursor.getString(3);

                productos.add(new Producto(idProducto, imagen, precio, favorito));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return productos;
    }

    public void comprarProducto(SQLiteDatabase db, int idProducto){

        ContentValues values = new ContentValues();
        values.put("estado", 0);

        db.update(
                "Producto",
                values,
                "id_producto = ?",
                new String[]{String.valueOf(idProducto)}
        );
    }

    public List<Producto> listaProductosFavoritos(SQLiteDatabase db,String nombre_usuario){
        List<Producto> productos = new ArrayList<>(); //lista donde guardaremos los productos

        // para filtar por nombre_de_usuario
        String query = "SELECT P.id_producto,P.precio, P.imagen " +
                "FROM Producto P " +
                "INNER JOIN Favoritos F ON P.id_producto = F.id_producto " +
                "INNER JOIN Usuario U ON F.id_usuario = U.id_usuario " +
                "WHERE U.nombre_de_usuario = ? ";

        String[] selectionArgs = {nombre_usuario};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()){
            do{
                int idProducto = cursor.getInt(0);
                String precio = cursor.getString(1);
                String imagen = cursor.getString(2);

                productos.add(new Producto(idProducto,imagen, precio, true));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productos;
    }

    public void anadirFavorito(SQLiteDatabase db, int idProducto, int idUsuario){

        ContentValues values = new ContentValues();
        values.put("id_producto", idProducto);
        values.put("id_usuario", idUsuario);

        db.insert("Favoritos", null, values);
    }

    public void quitarFavorito(SQLiteDatabase db, int idProducto, int idUsuario){

        db.delete(
                "Favoritos",
                "id_producto = ? AND id_usuario = ?",
                new String[]{String.valueOf(idProducto), String.valueOf(idUsuario)}
        );
    }

}
