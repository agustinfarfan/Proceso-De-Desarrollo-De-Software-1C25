package com.findamatch.model.dto;

import com.findamatch.model.enums.Nivel;

public class UsuarioDeporteDTO {

    private UsuarioDTO usuario;
    private DeporteDTO deporte;
    private Nivel nivel;
    private boolean esFavorito;

    public UsuarioDeporteDTO(UsuarioDTO usuario, DeporteDTO deporte, Nivel nivel, boolean esFavorito) {
        this.usuario = usuario;
        this.deporte = deporte;
        this.nivel = nivel;
        this.esFavorito = esFavorito;
    }

    // Getters and setters

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public DeporteDTO getDeporte() {
        return deporte;
    }

    public void setDeporte(DeporteDTO deporte) {
        this.deporte = deporte;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public boolean isEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(boolean esFavorito) {
        this.esFavorito = esFavorito;
    }

    // ToString

    @Override
    public String toString() {
        return "UsuarioDeporteDTO{" +
                "usuario=" + usuario.getId() +
                ", deporte='" + deporte.getNombre() + '\'' +
                ", nivel='" + nivel + '\'' +
                ", esFavorito='" + esFavorito + '\'' +
                '}';
    }

}
