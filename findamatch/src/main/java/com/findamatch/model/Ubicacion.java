package com.findamatch.model;

public class Ubicacion {
    private double latitud;
    private double longitud;
    private double varianza;

    public Ubicacion(double latitud, double longitud, double varianza) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.varianza = varianza;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getVarianza() {
        return varianza;
    }

    public void obtenerCoordenadas(String lugar) {
    }
}