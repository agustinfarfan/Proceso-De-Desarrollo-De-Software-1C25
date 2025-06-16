package com.findamatch.controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.dto.DeporteDTO;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.dto.UsuarioDeporteDTO;
import com.findamatch.model.enums.Nivel;

public class UsuarioController {

    Usuario usuario;
    UsuarioDeporte usuarioDeporte;

    DeporteController dc = DeporteController.getInstance();
    PartidoController pc = null;

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

    public PartidoController getPartidoController() {
        if (pc == null) {
            pc = PartidoController.getInstance();
        }
        return pc;
    }

    // CRUD

    public List<UsuarioDTO> getAllUsuariosDTO() throws Exception {

        pc = getPartidoController();


        List<Usuario> usuarios = usuario.findAllUsuarios();
        List<UsuarioDTO> usuariosDTO = new ArrayList<UsuarioDTO>();

        for (Usuario u : usuarios) {
            UsuarioDTO usuarioDTO = usuarioToDto(u);
            usuarioDTO.setDeportes(usuarioDeporteToDto(u.getDeportes()));

            List<PartidoDTO> partidosDTO = new ArrayList<PartidoDTO>();
            List<Partido> partidos = u.getPartidos();

            for (Partido p : partidos) {
                PartidoDTO partidoDTO = pc.partidoToDTO(p);
                partidosDTO.add(partidoDTO);
            }

            usuarioDTO.setPartidos(partidosDTO);
            usuariosDTO.add(usuarioDTO);
        }
        return usuariosDTO;
    }

    public UsuarioDTO getUsuarioByIdDTO(int id) throws Exception {

        this.pc = PartidoController.getInstance();

        Usuario usuarioEncontrado = usuario.findUsuarioById(id);

        UsuarioDTO usuarioDTO = usuarioToDto(usuarioEncontrado);

        List<UsuarioDeporteDTO> usuariosDeporteDTO = usuarioDeporteToDto(usuarioEncontrado.getDeportes());
        usuarioDTO.setDeportes(usuariosDeporteDTO);

        List<PartidoDTO> partidosDTO = new ArrayList<PartidoDTO>();
        List<Partido> partidos = usuarioEncontrado.getPartidos();

        for (Partido p : partidos) {
            PartidoDTO partidoDTO = pc.partidoToDTO(p);
            partidosDTO.add(partidoDTO);
        }

        usuarioDTO.setPartidos(partidosDTO);

        return usuarioDTO;
    }

    public UsuarioDTO getUsuarioByUsernameDTO(String username) throws Exception {
        this.pc = PartidoController.getInstance();

        Usuario usuarioEncontrado = usuario.findUsuarioByUsername(username);

        if (usuarioEncontrado == null) {
            return null;
        }
        
        UsuarioDTO usuarioDTO = usuarioToDto(usuarioEncontrado);

        List<UsuarioDeporteDTO> usuariosDeporteDTO = usuarioDeporteToDto(usuarioEncontrado.getDeportes());
        usuarioDTO.setDeportes(usuariosDeporteDTO);

        List<PartidoDTO> partidosDTO = new ArrayList<>();
        for (Partido p : usuarioEncontrado.getPartidos()) {
            PartidoDTO partidoDTO = pc.partidoToDTO(p);
            partidosDTO.add(partidoDTO);
        }
        usuarioDTO.setPartidos(partidosDTO);

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

    public void updateUsuarioDeporte(UsuarioDTO usuarioDTO, DeporteDTO deporteDTO, Nivel nivelJuego,
            boolean esFavorito) throws SQLException {

        Usuario usuario = dtoToUsuario(usuarioDTO);
        Deporte deporte = dc.dtoToDeporte(deporteDTO);
        UsuarioDeporte usuarioDeporte = new UsuarioDeporte(usuario, deporte, nivelJuego, esFavorito);
        usuario.updateUsuarioDeporte(usuarioDeporte);
    }

    // Auxiliar

    public UsuarioDTO usuarioToDto(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombreUsuario(usuario.getNombreUsuario());
        usuarioDTO.setMail(usuario.getMail());
        usuarioDTO.setContrasena(usuario.getContrasena());
        usuarioDTO.setUbicacion(usuario.getUbicacion());

        return usuarioDTO;
    }

    public Usuario dtoToUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setId(usuarioDTO.getId());
        usuarioNuevo.setNombreUsuario(usuarioDTO.getNombreUsuario());
        usuarioNuevo.setMail(usuarioDTO.getMail());
        usuarioNuevo.setContrasena(usuarioDTO.getContrasena());
        usuarioNuevo.setUbicacion(usuarioDTO.getUbicacion());
        return usuarioNuevo;
    }

    private List<UsuarioDeporte> dtoToUsuarioDeporte(List<UsuarioDeporteDTO> deportesDTO) {
        List<UsuarioDeporte> deportes = new ArrayList<UsuarioDeporte>();
        for (UsuarioDeporteDTO deporteDTO : deportesDTO) {
            UsuarioDeporte usuarioDeporte = new UsuarioDeporte(
                    dtoToUsuario(deporteDTO.getUsuario()),
                    dc.dtoToDeporte(deporteDTO.getDeporte()),
                    deporteDTO.getNivel(),
                    deporteDTO.isEsFavorito());
            deportes.add(usuarioDeporte);
        }
        return deportes;
    }

    private List<UsuarioDeporteDTO> usuarioDeporteToDto(List<UsuarioDeporte> deportes) {
        List<UsuarioDeporteDTO> deportesDTO = new ArrayList<UsuarioDeporteDTO>();
        for (UsuarioDeporte deporte : deportes) {
            UsuarioDeporteDTO usuarioDeporteDTO = new UsuarioDeporteDTO(
                    usuarioToDto(deporte.getUsuario()),
                    dc.deporteToDTO(deporte.getDeporte()),
                    deporte.getNivelJuego(),
                    deporte.isEsFavorito());
            deportesDTO.add(usuarioDeporteDTO);
        }
        return deportesDTO;
    }

}
