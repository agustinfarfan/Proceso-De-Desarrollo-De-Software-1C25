package com.findamatch.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.notificacion.NotificacionEmail;
import com.findamatch.model.notificacion.NotificacionPush;

public class PartidoController {
    Partido partido;
    Deporte deporte;
    Usuario usuario;

    private static PartidoController instance = null;
    
    // Constructor
    private PartidoController() {
        partido = new Partido();
        deporte = new Deporte();
        usuario = new Usuario();
    }

    public static PartidoController getInstance() {
        if (instance == null) {
            instance = new PartidoController();
        }
        return instance;
    }

    // CRUD

    public List<PartidoDTO> getAllPartidosDTO() throws Exception {
        List<Partido> partidos = partido.findAllPartidos();
        List<PartidoDTO> partidosDTO = new ArrayList<>();

        for (Partido p : partidos) {
            PartidoDTO partidoDTO = partidoToDTO(p);
            partidosDTO.add(partidoDTO);
        }

        return partidosDTO;
    }

    public PartidoDTO getPartidoDTOById(int id) throws Exception {
        Partido partidoEncontrado = partido.findPartidoById(id);
        PartidoDTO partidoDTO = partidoToDTO(partidoEncontrado);
        return partidoDTO;
    }

    public int createPartido(PartidoDTO partidoDTO) throws Exception {
        Partido partido = dtoToPartido(partidoDTO);
        partido.agregarEstrategiaNotificacion(new NotificacionEmail());
        partido.agregarEstrategiaNotificacion(new NotificacionPush());
        int id = partido.savePartido(partido);

        return id;
    }

    public void updatePartido(PartidoDTO partidoDTO) throws Exception {
        Partido partido = dtoToPartido(partidoDTO);
        partido.agregarEstrategiaNotificacion(new NotificacionEmail());
        partido.agregarEstrategiaNotificacion(new NotificacionPush());
        partido.updatePartido(partido);
    }

    public void deletePartido(int id) {
        partido.deletePartido(id);
    }

    // Conversiones DTO - Partido
    public PartidoDTO partidoToDTO(Partido partido) {
        PartidoDTO partidoDTO = new PartidoDTO();
        partidoDTO.setId(partido.getId());
        partidoDTO.setIdDeporte(partido.getDeporte().getId());
        partidoDTO.setIdCreador(partido.getCreador().getId());
        partidoDTO.setUbicacion(partido.getUbicacion().getDireccion());
        partidoDTO.setComienzo(partido.getFecha());
        partidoDTO.setDuracion(partido.getDuracion());
        // REVISAR TEMA IDS CON EL PROFE
        // partidoDTO.setEstado(partido.getEstado().getId());
        partidoDTO.setEstrategia(partido.getEstrategiaEmparejamiento().getId());
        return partidoDTO;
    }

    public Partido dtoToPartido(PartidoDTO partidoDTO) throws Exception {
        Partido partido = new Partido();
        partido.setId(partidoDTO.getId());
        partido.setDeporte(deporte.findDeporteById(partidoDTO.getIdDeporte()));
        partido.setCreador(usuario.findUsuarioById(partidoDTO.getIdCreador()));
        partido.setUbicacion(new Ubicacion(partidoDTO.getUbicacion()));
        partido.setFecha(partidoDTO.getComienzo());
        partido.setDuracion(partidoDTO.getDuracion());
        // REVISAR TEMA IDS CON EL PROFE
        // partido.setEstado(partido.getEstado().findById(partidoDTO.getEstado())); //
        // partido.setEstrategiaEmparejamiento(IEstrategiaEmparejamiento.findById(partidoDTO.getEstrategia()));
        return partido;
    }

    public void confirmarPartido(int id) throws Exception {
        Partido partidoEncontrado = partido.findPartidoById(id);
        partido.agregarEstrategiaNotificacion(new NotificacionEmail());
        partido.agregarEstrategiaNotificacion(new NotificacionPush());
        partidoEncontrado.confirmarPartido();
        partido.updatePartido(partidoEncontrado);
    }

    public void cancelarPartido(int id) throws Exception {

        Partido partidoEncontrado = partido.findPartidoById(id);
        partido.agregarEstrategiaNotificacion(new NotificacionEmail());
        partido.agregarEstrategiaNotificacion(new NotificacionPush());
        partidoEncontrado.cancelarPartido();
        partido.updatePartido(partidoEncontrado);
    }

    public void finalizarPartido(int id) throws Exception {
        Partido partidoEncontrado = partido.findPartidoById(id);
        partido.agregarEstrategiaNotificacion(new NotificacionEmail());
        partido.agregarEstrategiaNotificacion(new NotificacionPush());
        partidoEncontrado.finalizarPartido();
        partido.updatePartido(partidoEncontrado);
    }
}
