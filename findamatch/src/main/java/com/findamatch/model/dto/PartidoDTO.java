package com.findamatch.model.dto;

import java.util.Date;

public class PartidoDTO {
    private String tipoDeporte;
    private String ubicacion;
    private Date fecha;
    private int duracion;
    private int horario;

    // Constructor
    public PartidoDTO() {}

    public PartidoDTO(String tipoDeporte, String ubicacion, Date fecha, int duracion, int horario) {
        this.tipoDeporte = tipoDeporte;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.duracion = duracion;
        this.horario = horario;
    }

    // Getters y Setters
    public String getTipoDeporte() {
        return tipoDeporte;
    }

    public void setTipoDeporte(String tipoDeporte) {
        this.tipoDeporte = tipoDeporte;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getHorario() {
        return horario;
    }

    public void setHorario(int horario) {
        this.horario = horario;
    }
}
