package com.findamatch.model;

import com.findamatch.model.enums.Nivel;

public class UsuarioDeporte {
    private Usuario usuario;
    private Deporte deporte;
    private Nivel nivelJuego;
    private boolean esFavorito;

    public UsuarioDeporte(Usuario usuario, Deporte deporte, Nivel nivelJuego, boolean esFavorito) {
        this.usuario = usuario;
        this.deporte = deporte;
        this.nivelJuego = nivelJuego;
        this.esFavorito = esFavorito;
    }

    public UsuarioDeporte() {

    }

    // Getters y setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public Nivel getNivelJuego() {
        return nivelJuego;
    }

    public void setNivelJuego(Nivel nivelJuego) {
        this.nivelJuego = nivelJuego;
    }

    public boolean isEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(boolean esFavorito) {
        this.esFavorito = esFavorito;
    }

    // To String
    @Override
    public String toString() {
        return "UsuarioDeporte{" +
                "usuario=" + usuario.getNombreUsuario() +
                ", deporte=" + deporte.getNombre() +
                ", nivelJuego=" + nivelJuego +
                ", esFavorito=" + esFavorito +
                '}';
    }

}
