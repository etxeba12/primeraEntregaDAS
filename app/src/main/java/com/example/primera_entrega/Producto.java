package com.example.primera_entrega;

public class Producto {

    private int id;
    private String imagen;
    private String precio;
    private boolean favorito;

    public Producto(int id, String imagen, String precio, boolean favorito) {
        this.id = id;
        this.imagen = imagen;
        this.precio = precio;
        this.favorito = favorito;
    }

    public int getId() {
        return id;
    }

    public String getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

    public boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(boolean pFavorito){
        this.favorito = pFavorito;
    }
}