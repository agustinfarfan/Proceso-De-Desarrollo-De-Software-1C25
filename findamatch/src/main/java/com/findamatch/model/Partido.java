package com.findamatch.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Partido {
    private Deporte tipoDeporte;
    private List<Usuario> jugadores;
    private int duracion;
    private Ubicacion ubicacion;
    private Date fecha;
    private IEstadoPartido estado;
    private IEstrategiaEmparejamiento estrategiaEmparejamiento;

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
                "deporte=" + tipoDeporte +
                // ", jugadores=" + jugadores +
                ", ubicacion='" + ubicacion + '\'' +
                ", comienzo=" + comienzo +
                ", duracionMinutos=" + duracionMinutos +
                // ", estado='" + estado + '\'' +
                // ", estrategiaEmparejamiento='" + estrategiaEmparejamiento + '\'' +
                '}';
    }


    public void cambiarEstado(IEstadoPartido nuevoEstado) {

    }
    public void cambiarEstrategia(IEstrategiaEmparejamiento estrategia) {

    }
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
