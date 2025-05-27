package com.findamatch.model;

import java.util.*;
import com.findamatch.model.dto.UsuarioDTO;

public class Usuario {
    private String nombreUsuario;
    private String mail;
    private String contraseña;
    private List<UsuarioDeporte> deportes;

    public Usuario(String nombreUsuario, String mail, String contraseña) {
        this.nombreUsuario = nombreUsuario;
        this.mail = mail;
        this.contraseña = contraseña;
        this.deportes = new ArrayList<>();
    }

    public void registrarUsuario(UsuarioDTO usuarioDTO) {

    }

    public boolean loginUsuario(UsuarioDTO usuarioDTO) {

        return true;
    }

    public void actualizarDatosDelUsuario(UsuarioDTO usuarioDTO) {

    }

    public void eliminarUsuario() {
    }

}