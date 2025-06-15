package com.findamatch.controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.findamatch.model.Deporte;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.dto.DeporteDTO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.enums.Nivel;

public class UsuarioController {

    Usuario usuario;
    UsuarioDeporte usuarioDeporte;

    DeporteController dc = DeporteController.getInstance();

    private static UsuarioController instance = null;

    // Constructor
    private UsuarioController() {
        this.usuario = new Usuario();
        this.usuarioDeporte = new UsuarioDeporte();
    }

    public static UsuarioController getInstance() {
        if (instance == null) {
            instance = new UsuarioController();
        }
        return instance;
    }

    // CRUD
    public List<UsuarioDTO> getAllUsuariosDTO() {

        List<Usuario> usuarios = usuario.findAllUsuarios();
        List<UsuarioDTO> usuariosDTO = new ArrayList<UsuarioDTO>();

        for (Usuario u : usuarios) {
            UsuarioDTO usuarioDTO = usuarioToDto(u);
            usuariosDTO.add(usuarioDTO);
        }
        return usuariosDTO;
    }

    public UsuarioDTO getUsuarioByIdDTO(int id) {
        Usuario usuarioNuevo = usuario.findUsuarioById(id);
        UsuarioDTO usuarioDTO = usuarioToDto(usuarioNuevo);
        return usuarioDTO;
    }

    public int createUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuarioNuevo = dtoToUsuario(usuarioDTO);
        int id = usuario.saveUsuario(usuarioNuevo);
        return id;
    }

    public void updateUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuarioNuevo = dtoToUsuario(usuarioDTO);
        usuario.updateUsuario(usuarioNuevo);
    }

    public void deleteUsuario(int id) {
        usuario.deleteUsuario(id);
    }

    public List<UsuarioDeporte> getUsuarioDeportes() {
        List<UsuarioDeporte> deportesUsuario = usuario.getUsuarioDeportes(); // deberia existir un dao?
        return deportesUsuario;
    }

    public void updateUsuarioDeporte(UsuarioDTO usuarioDTO, DeporteDTO deporteDTO, Nivel nivelJuego,
            boolean esFavorito) throws SQLException {

        Usuario usuario = dtoToUsuario(usuarioDTO);
        Deporte deporte = dc.dtoToDeporte(deporteDTO);
        UsuarioDeporte usuarioDeporte = new UsuarioDeporte(usuario, deporte, nivelJuego, esFavorito);
        usuario.updateUsuarioDeporte(usuarioDeporte);
    }

    // Auxiliar

    private UsuarioDTO usuarioToDto(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombreUsuario(usuario.getNombreUsuario());
        usuarioDTO.setMail(usuario.getMail());
        usuarioDTO.setContrasena(usuario.getContrasena());
        usuarioDTO.setUbicacion(usuario.getUbicacion());
        return usuarioDTO;
    }

    private Usuario dtoToUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setId(usuarioDTO.getId());
        usuarioNuevo.setNombreUsuario(usuarioDTO.getNombreUsuario());
        usuarioNuevo.setMail(usuarioDTO.getMail());
        usuarioNuevo.setContrasena(usuarioDTO.getContrasena());
        usuarioNuevo.setUbicacion(usuarioDTO.getUbicacion());
        return usuarioNuevo;
    }
}
