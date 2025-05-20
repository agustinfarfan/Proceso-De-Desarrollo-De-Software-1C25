package com.findamatch.model;

import java.util.List;
import java.util.ArrayList;

public class Usuario {

    private String nombreUsuario;
    private String mail;
    private String contrasena;
    // private List<UsuarioDeporteFavorito> deportesFavoritos;

    public Usuario(String nombreUsuario, String mail, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.mail = mail;
        this.contrasena = contrasena;
    }

}