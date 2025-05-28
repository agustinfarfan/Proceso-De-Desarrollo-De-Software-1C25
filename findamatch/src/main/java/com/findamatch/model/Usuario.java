package com.findamatch.model;

import java.sql.SQLException;
import java.util.*;

import com.findamatch.dao.UsuarioDAO;
import com.findamatch.model.dto.UsuarioDTO;

public class Usuario {

    private int id;
    private String nombreUsuario;
    private String mail;
    private String contrasena;
    private int edad;
    private String ubicacion;
    private List<UsuarioDeporte> deportes;

    UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();

    public Usuario(int id, String nombreUsuario, String mail, String contrasena, int edad, String ubicacion) {

        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.mail = mail;
        this.contrasena = contrasena;
        this.edad = edad;
        this.ubicacion = ubicacion;

        this.deportes = new ArrayList<>();
    }

    public Usuario() {
        // TODO Auto-generated constructor stub
    }

    // CRUD

    public List<Usuario> findAllUsuarios() {

        List<Usuario> usuarios = new ArrayList<>();

        try {
            usuarios = usuarioDAO.findAllUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;

    }

    public Usuario findUsuarioById(int id) {

        Usuario usuario = null;

        try {
            usuario = usuarioDAO.findUsuarioById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;

    }

    public void saveUsuario(Usuario usuario) {

        try {
            usuarioDAO.saveUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateUsuario(Usuario usuario) {

        try {
            usuarioDAO.updateUsuario(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteUsuario(int id) {

        try {
            usuarioDAO.deleteUsuario(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    // ToString
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", mail='" + mail + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", edad=" + edad +
                ", ubicacion='" + ubicacion + '\'' +
                '}';
    }

}