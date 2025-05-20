package com.findamatch.model;

public class Deporte {
    private String nombre;
    private int cantMinJugadores;
    private int cantMaxJugadores;

    public Deporte(String nombre, int cantMinJugadores, int cantMaxJugadores) {
        this.nombre = nombre;
        this.cantMinJugadores = cantMinJugadores;
        this.cantMaxJugadores = cantMaxJugadores;
    }

    public String toString() {
        return "Deporte: " + this.nombre + " - Cantidad de jugadores: " + this.cantMinJugadores + " - "
                + this.cantMaxJugadores;
    }
}
