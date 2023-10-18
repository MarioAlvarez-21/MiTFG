package com.example.kisupercars;

public class Caracteristicas {
    private String anio, descripcion, imagenModelo, motor, nombreModelo, parMotor, potencia, tiempo
            ,velocidad, nombreMarca;

    public Caracteristicas() {
    }

    public Caracteristicas(String anio, String descripcion, String imagenModelo, String motor, String nombreModelo, String parMotor, String potencia, String tiempo, String velocidad, String nombreMarca) {
        this.anio = anio;
        this.descripcion = descripcion;
        this.imagenModelo = imagenModelo;
        this.motor = motor;
        this.nombreModelo = nombreModelo;
        this.parMotor = parMotor;
        this.potencia = potencia;
        this.tiempo = tiempo;
        this.velocidad = velocidad;
        this.nombreMarca = nombreMarca;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenModelo() {
        return imagenModelo;
    }

    public void setImagenModelo(String imagenModelo) {
        this.imagenModelo = imagenModelo;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }

    public String getParMotor() {
        return parMotor;
    }

    public void setParMotor(String parMotor) {
        this.parMotor = parMotor;
    }

    public String getPotencia() {
        return potencia;
    }

    public void setPotencia(String potencia) {
        this.potencia = potencia;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    @Override
    public String toString() {
        return "Caracteristicas{" +
                "anio='" + anio + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagenModelo='" + imagenModelo + '\'' +
                ", motor='" + motor + '\'' +
                ", nombreModelo='" + nombreModelo + '\'' +
                ", parMotor='" + parMotor + '\'' +
                ", potencia='" + potencia + '\'' +
                ", tiempo='" + tiempo + '\'' +
                ", velocidad='" + velocidad + '\'' +
                ", nombreMarca='" + nombreMarca + '\'' +
                '}';
    }
}
