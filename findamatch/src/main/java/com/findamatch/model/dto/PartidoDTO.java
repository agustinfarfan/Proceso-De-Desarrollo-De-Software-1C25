package com.findamatch.model.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class PartidoDTO {
    private int id;
    private DeporteDTO deporte;
    private UsuarioDTO creador;
    private String ubicacion;
    private LocalDateTime comienzo;
    private int duracion;
    private String estado;
    private List<UsuarioDTO> jugadores;
    private int minimoPartidosJugados;

    // Constructor
    public PartidoDTO() {
    }

    public PartidoDTO(DeporteDTO deporte, UsuarioDTO creador, String ubicacion, LocalDateTime comienzo, int duracion) {
        this.deporte = deporte;
        this.creador = creador;
        this.ubicacion = ubicacion;
        this.duracion = duracion;
        this.comienzo = comienzo;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DeporteDTO getDeporte() {
        return deporte;
    }

    public void setDeporte(DeporteDTO deporte) {
        this.deporte = deporte;
    }

    public UsuarioDTO getCreador() {
        return creador;
    }

    public void setCreador(UsuarioDTO creador) {
        this.creador = creador;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getComienzo() {
        return comienzo;
    }

    public void setComienzo(LocalDateTime comienzo) {
        this.comienzo = comienzo;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<UsuarioDTO> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<UsuarioDTO> jugadores) {
        this.jugadores = jugadores;
    }
    
    public int getMinimoPartidosJugados() {
    return minimoPartidosJugados;
    }

    public void setMinimoPartidosJugados(int minimoPartidosJugados) {
        this.minimoPartidosJugados = minimoPartidosJugados;
    }

    // ToString

    @Override
    public String toString() {
        return "PartidoDTO{" +
                "id=" + id +
                ", deporte=" + deporte +
                ", creador=" + creador +
                ", ubicacion='" + ubicacion + '\'' +
                ", comienzo=" + comienzo +
                ", duracion=" + duracion +
                ", estado='" + estado + '\'' +
                ", jugadores=" + jugadores +
                '}'
                + ", minimoPartidosJugados=" + minimoPartidosJugados ;
    }

}
