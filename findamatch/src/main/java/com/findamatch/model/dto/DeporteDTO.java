package com.findamatch.model.dto;

public class DeporteDTO {

    private int id;
    private String nombre;
    private int cantMinJugadores;
    private int cantMaxJugadores;
    private String descripcion;

    // Constructor

    public DeporteDTO(
            String nombre,
            int cantMinJugadores,
            int cantMaxJugadores,
            String descripcion) {
        this.nombre = nombre;
        this.cantMinJugadores = cantMinJugadores;
        this.cantMaxJugadores = cantMaxJugadores;
        this.descripcion = descripcion;
    }

    public DeporteDTO(
            int id,
            String nombre,
            int cantMinJugadores,
            int cantMaxJugadores,
            String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.cantMinJugadores = cantMinJugadores;
        this.cantMaxJugadores = cantMaxJugadores;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantMinJugadores() {
        return cantMinJugadores;
    }

    public void setCantMinJugadores(int cantMinJugadores) {
        this.cantMinJugadores = cantMinJugadores;
    }

    public int getCantMaxJugadores() {
        return cantMaxJugadores;
    }

    public void setCantMaxJugadores(int cantMaxJugadores) {
        this.cantMaxJugadores = cantMaxJugadores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // ToString

    @Override
    public String toString() {
        return "DeporteDTO{" +
                "id=" + id +
                ", nombreDeporte=" + nombre +
                ", cantMinDeJugadores=" + cantMinJugadores +
                ", cantMaxDeJugadores=" + cantMaxJugadores +
                ", descripcion='" + descripcion + "}";

    }

}
