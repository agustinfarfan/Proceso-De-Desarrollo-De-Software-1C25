package com.findamatch.model.dto;

public class DeporteDTO {
    private String nombreDeporte;
    private int cantMinDeJugadores;
    private int cantMaxDeJugadores;
    private String descripcion;

    // Constructor
    public DeporteDTO() {}

    public DeporteDTO(String nombreDeporte, int cantMinDeJugadores, int cantMaxDeJugadores, String descripcion) {
        this.nombreDeporte = nombreDeporte;
        this.cantMinDeJugadores = cantMinDeJugadores;
        this.cantMaxDeJugadores = cantMaxDeJugadores;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getNombreDeporte() {
        return nombreDeporte;
    }

    public void setNombreDeporte(String nombreDeporte) {
        this.nombreDeporte = nombreDeporte;
    }

    public int getCantMinDeJugadores() {
        return cantMinDeJugadores;
    }

    public void setCantMinDeJugadores(int cantMinDeJugadores) {
        this.cantMinDeJugadores = cantMinDeJugadores;
    }

    public int getCantMaxDeJugadores() {
        return cantMaxDeJugadores;
    }

    public void setCantMaxDeJugadores(int cantMaxDeJugadores) {
        this.cantMaxDeJugadores = cantMaxDeJugadores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
