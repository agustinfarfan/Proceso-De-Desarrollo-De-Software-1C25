package com.findamatch.model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Partido {
    private Deporte deporte;
    private List<Usuario> jugadores;
    private String ubicacion; // Deberia cambiar, quizas una clase o algo que ayude a la busqueda por cercania
    private LocalDateTime comienzo;
    private int duracionMinutos;
    private String estado; // CAMBIAR A STATE
    private String estrategiaEmparejamiento; // CAMBIAR A ESTRATEGIA DE EMPAREJAMIENTO

    public Partido(
            Deporte deporte,
            // List<Usuario> jugadores,
            String ubicacion,
            LocalDateTime comienzo,
            int duracionMinutos) {
        // String estado,
        // String estrategiaEmparejamiento) {
        this.deporte = deporte;
        // this.jugadores = jugadores;
        this.ubicacion = ubicacion;
        this.comienzo = comienzo;
        this.duracionMinutos = duracionMinutos;
        // this.estado = estado;
        // this.estrategiaEmparejamiento = estrategiaEmparejamiento;
    }

    public String toString() {
        return "Partido{" +
                "deporte=" + deporte +
                // ", jugadores=" + jugadores +
                ", ubicacion='" + ubicacion + '\'' +
                ", comienzo=" + comienzo +
                ", duracionMinutos=" + duracionMinutos +
                // ", estado='" + estado + '\'' +
                // ", estrategiaEmparejamiento='" + estrategiaEmparejamiento + '\'' +
                '}';
    }

}
