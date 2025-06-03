package com.findamatch.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.estado.EstadoCreado;
import com.findamatch.model.estado.IEstadoPartido;

public class Partido {
    private Deporte deporte;
    private List<Usuario> jugadores;
    private int duracion;
    private Ubicacion ubicacion;
    private LocalDateTime fecha;
    private IEstadoPartido estado;
    private IEstrategiaEmparejamiento estrategiaEmparejamiento;

    public Partido(
            Deporte deporte,
            Ubicacion ubicacion,
            LocalDateTime fecha,
            int duracionMinutos) {

        this.deporte = deporte;
        this.jugadores = new ArrayList<>();
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.duracion = duracionMinutos;
        this.estado = new EstadoCreado(); // Estado inicial
    }

    // --------- MÉTODOS STATE ----------
    public void confirmarPartido() {
        estado.confirmar(this);
    }

    public void cancelarPartido() {
        estado.cancelar(this);
    }

    public void finalizarPartido() {
        estado.finalizar(this);
    }

    public void setEstado(IEstadoPartido estado) {
        this.estado = estado;
    }

    public IEstadoPartido getEstado() {
        return estado;
    }

    // --------- MÉTODOS STRATEGY ----------
    public void setEstrategiaEmparejamiento(IEstrategiaEmparejamiento estrategia) {
        this.estrategiaEmparejamiento = estrategia;
    }

    public void buscarPartido() {
       
    }

    // --------- GETTERS / SETTERS ----------
    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public List<Usuario> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Usuario> jugadores) {
        this.jugadores = jugadores;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    // --------- TO STRING ----------
    @Override
    public String toString() {
        return "Partido{" +
                "deporte=" + deporte +
                ", ubicacion=" + ubicacion +
                ", comienzo=" + fecha +
                ", duracionMinutos=" + duracion +
                ", estado=" + (estado != null ? estado.nombre() : "Sin estado") +
                '}';
    }

    // --------- OTROS MÉTODOS VACÍOS ----------
    public void crearPartido() {}

    public void agregarPartido() {}
}
