package com.findamatch.model;

import java.sql.SQLException;
import java.util.*;

import com.findamatch.dao.DeporteDAO;
import com.findamatch.dao.UsuarioDAO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.enums.Nivel;

public class Usuario {

    private int id;
    private String nombreUsuario;
    private String mail;
    private String contrasena;
    private String ubicacion;
    private List<UsuarioDeporte> deportes = new ArrayList<>();
    private List<Partido> partidos = new ArrayList<>();

    UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
    DeporteDAO deporteDAO = DeporteDAO.getInstance();

    public Usuario(int id, String nombreUsuario, String mail, String contrasena, String ubicacion,
            List<UsuarioDeporte> deportes) {

        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.mail = mail;
        this.contrasena = contrasena;
        this.ubicacion = ubicacion;
        this.deportes = deportes;
    }

    public Usuario() {
        // TODO Auto-generated constructor stub
    }

    // CRUD

    public List<Usuario> findAllUsuarios() throws Exception {

        List<Usuario> usuarios = new ArrayList<>();

        try {
            usuarios = usuarioDAO.findAllUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;

    }

    public Usuario findUsuarioById(int id) throws Exception {

        Usuario usuario = null;

        try {
            usuario = usuarioDAO.findUsuarioById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public int saveUsuario(Usuario usuario) {

        try {
            int id = usuarioDAO.saveUsuario(usuario);
            return id;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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

    public List<UsuarioDeporte> getUsuarioDeportes() throws Exception {
        List<UsuarioDeporte> usuarioDeportes = new ArrayList<>();
        try {
            usuarioDeportes = usuarioDAO.getUsuarioDeportes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarioDeportes;

    }

    public void updateUsuarioDeporte(UsuarioDeporte usuarioDeporte) throws SQLException {
        usuarioDAO.updateUsuarioDeporte(usuarioDeporte);

    }

    public Nivel getNivelPorDeporte(Deporte deporte) {
        if (deportes == null)
            return null;

        for (UsuarioDeporte ud : deportes) {
            if (ud.getDeporte().getId() == deporte.getId()) {
                return ud.getNivelJuego();
            }
        }

        return null;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<UsuarioDeporte> getDeportes() {
        return this.deportes;
    }

    public void setDeportes(List<UsuarioDeporte> deportes) {
        this.deportes = deportes;
    }

    public void addUsuarioDeporte(UsuarioDeporte ud) {
        deportes.add(ud);
    }

    public List<Partido> getPartidos() {
        return this.partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    // ToString
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", mail='" + mail + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", deportes='" + deportes + '\'' +
                ", partidos='" + partidos + '\'' +
                '}';
    }

}