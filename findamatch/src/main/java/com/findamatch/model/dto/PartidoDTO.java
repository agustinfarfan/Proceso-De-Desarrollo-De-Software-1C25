package com.findamatch.model.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class PartidoDTO {
    private int id;
    private int idDeporte;
    private int idCreador;
    private String ubicacion;
    private LocalDateTime comienzo;
    private int duracion;
    private int estado;
    private int estrategia;

    // Constructor
    public PartidoDTO() {
    }

    public PartidoDTO(int idDeporte, int idCreador, String ubicacion, LocalDateTime comienzo, int duracion) {
        this.idDeporte = idDeporte;
        this.idCreador = idCreador;
        this.ubicacion = ubicacion;
        this.duracion = duracion;
        this.comienzo = comienzo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDeporte() {
        return idDeporte;
    }

    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }

    public int getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(int idCreador) {
        this.idCreador = idCreador;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(int estrategia) {
        this.estrategia = estrategia;
    }

    // Getters y Setters

}
