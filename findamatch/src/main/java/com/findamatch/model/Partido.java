package com.findamatch.model;

import com.findamatch.dao.PartidoDAO;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.estado.EstadoCreado;
import com.findamatch.model.estado.IEstadoPartido;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Partido {
    private int id;
    private Deporte deporte;
    private List<Usuario> jugadores;
    private Usuario creador;
    private Ubicacion ubicacion;
    private LocalDateTime fecha;
    private int duracion;
    private IEstadoPartido estado;
    private IEstrategiaEmparejamiento estrategiaEmparejamiento;

    PartidoDAO partidoDAO = PartidoDAO.getInstance();

    public Partido() {
    }

    public Partido(
            Deporte deporte,
            Usuario creador,
            Ubicacion ubicacion,
            LocalDateTime fecha,
            int duracionMinutos) {
        this.deporte = deporte;
        this.jugadores = new ArrayList<Usuario>();
        this.jugadores.add(creador);
        this.creador = creador;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.duracion = duracionMinutos;
        this.estado = new EstadoCreado(); // Estado inicial
        this.estrategiaEmparejamiento = new PorCercania();
    }

    public Partido(int id, Deporte deporte, Usuario creador, Ubicacion ubicacion,
            LocalDateTime comienzo, int duracion, IEstadoPartido estado, IEstrategiaEmparejamiento estrategia) {
        this.id = id;
        this.deporte = deporte;
        this.creador = creador;
        this.ubicacion = ubicacion;
        this.fecha = comienzo;
        this.duracion = duracion;
        this.estado = estado;
        this.estrategiaEmparejamiento = estrategia;
    }

    //
  
    public void confirmarPartido() {
        estado.confirmar(this);
    }

    public void cancelarPartido() {
        estado.cancelar(this);
    }

    public void finalizarPartido() {
        estado.finalizar(this);
      
     
    // comenzar partido?


    // CRUD

    public List<Partido> findAllPartidos() throws Exception {
        List<Partido> partidos = new ArrayList<Partido>();
        partidos = PartidoDAO.getInstance().findAllPartidos();
        return partidos;
    }

    public Partido findPartidoById(int id) throws Exception {
        Partido partido = null;
        partido = partidoDAO.findPartidoById(id);
        return partido;

    }

    public int savePartido(Partido partido) {
        try {
            int id = partidoDAO.savePartido(partido);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public void updatePartido(Partido partido) {

        try {
            partidoDAO.updatePartido(partido);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deletePartido(int id) {

        try {
            partidoDAO.deletePartido(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
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

    public IEstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(IEstadoPartido estado) {
        this.estado = estado;
    }

    public IEstrategiaEmparejamiento getEstrategiaEmparejamiento() {
        return estrategiaEmparejamiento;
    }

    public void setEstrategiaEmparejamiento(IEstrategiaEmparejamiento estrategiaEmparejamiento) {
        this.estrategiaEmparejamiento = estrategiaEmparejamiento;
    }

    // ToString
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

}
