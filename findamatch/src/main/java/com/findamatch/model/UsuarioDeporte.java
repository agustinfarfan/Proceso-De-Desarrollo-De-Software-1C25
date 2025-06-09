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

}
