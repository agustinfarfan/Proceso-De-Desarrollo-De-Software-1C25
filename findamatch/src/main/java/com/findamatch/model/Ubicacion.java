package com.findamatch.model;

import com.findamatch.model.geocoding.Geocoder;
import com.findamatch.model.geocoding.GoogleGeocoderAdapter;

public class Ubicacion {
    private String direccion;
    private double latitud;
    private double longitud;
    private double varianza;

    private Geocoder geocoder;

    public Ubicacion(String direccion) {
        this.direccion = direccion;
    }
    // Getters y setters

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getVarianza() {
        return varianza;
    }

    public void setVarianza(double varianza) {
        this.varianza = varianza;
    }

    // ToString

    @Override
    public String toString() {
        return "Ubicacion{" +
                "direccion='" + direccion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", varianza=" + varianza +
                '}';
    }

}