package com.findamatch.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Partido {
    private Deporte deporte;
    private List<Usuario> jugadores;
    private int duracion;
    private Ubicacion ubicacion;
    private LocalDateTime fecha;
    // private IEstadoPartido estado;
    // private IEstrategiaEmparejamiento estrategiaEmparejamiento;

    public Partido(
            Deporte deporte,
            // List<Usuario> jugadores,
            Ubicacion ubicacion,
            LocalDateTime fecha,
            int duracionMinutos) {
        // String estado,
        // String estrategiaEmparejamiento) {
        this.deporte = deporte;
        // this.jugadores = jugadores;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.duracion = duracionMinutos;
        // this.estado = estado;
        // this.estrategiaEmparejamiento = estrategiaEmparejamiento;
    }

    public String toString() {
        return "Partido{" +
                "deporte=" + deporte +
                // ", jugadores=" + jugadores +
                ", ubicacion='" + ubicacion + '\'' +
                ", comienzo=" + fecha +
                ", duracionMinutos=" + duracion +
                // ", estado='" + estado + '\'' +
                // ", estrategiaEmparejamiento='" + estrategiaEmparejamiento + '\'' +
                '}';
    }

    // public void cambiarEstado(IEstadoPartido nuevoEstado) {}

    // public void cambiarEstrategia(IEstrategiaEmparejamiento estrategia) {}

    public void crearPartido() {

    }

    public void buscarPartido() {

    }

    public void agregarPartido() {

    }

    public void cancelarPartido() {

    }

    public void confirmarPartido() {

    }
}
