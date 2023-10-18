package com.example.kisupercars;

public class Marcas {

    private String nombreMarca, imagenUrl;

    public Marcas() {
    }

    public Marcas(String nombreMarca, String imagenUrl) {
        this.nombreMarca = nombreMarca;
        this.imagenUrl = imagenUrl;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    @Override
    public String toString() {
        return "Marcas{" +
                "nombreMarca='" + nombreMarca + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                '}';
    }
}
