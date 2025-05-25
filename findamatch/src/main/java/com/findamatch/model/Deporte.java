package com.findamatch.model;

public class Deporte {
    private String nombre;
    private int cantMinJugadores;
    private int cantMaxJugadores;
    private String descripcion;

    public Deporte(String nombre, int cantMinJugadores, int cantMaxJugadores,String descripcion) {
        this.nombre = nombre;
        this.cantMinJugadores = cantMinJugadores;
        this.cantMaxJugadores = cantMaxJugadores;
        this.descripcion = descripcion;
    }

    public String toString() {
        return "Deporte: " + this.nombre + " - Cantidad de jugadores: " + this.cantMinJugadores + " - "
                + this.cantMaxJugadores;
    }

    public void crearDeporte() {
    }

    public void actualizarDeporte() {
    }

    public void eliminarDeporte() {
    }

}
