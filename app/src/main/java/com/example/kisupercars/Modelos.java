package com.example.kisupercars;

public class Modelos {

    String imagenModelo, nombreModelo, nombreMarca;

    public Modelos() {
    }

    public Modelos(String imagenModelo, String nombreModelo, String nombreMarca) {
        this.imagenModelo = imagenModelo;
        this.nombreModelo = nombreModelo;
        this.nombreMarca = nombreMarca;
    }

    public String getImagenModelo() {
        return imagenModelo;
    }

    public void setImagenModelo(String imagenModelo) {
        this.imagenModelo = imagenModelo;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    @Override
    public String toString() {
        return "Modelos{" +
                "imagenModelo='" + imagenModelo + '\'' +
                ", nombreModelo='" + nombreModelo + '\'' +
                ", nombreMarca='" + nombreMarca + '\'' +
                '}';
    }
}
