package com.findamatch.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.emparejamiento.estrategias.FactoryEstrategia;
import com.findamatch.model.estado.FactoryEstado;
import com.findamatch.model.estado.IEstadoPartido;
import com.findamatch.model.geocoding.GoogleGeocoderAdapter;
import com.findamatch.model.notificacion.NotificacionEmail;
import com.findamatch.model.notificacion.NotificacionPush;

public class PartidoController {
    Partido partido;
    Deporte deporte;
    Usuario usuario;

    private DeporteController dc = DeporteController.getInstance();
    private UsuarioController uc = null;
    private static PartidoController instance = null;

    // Constructor
    private PartidoController() {
        this.partido = new Partido();
        this.deporte = new Deporte();
        this.usuario = new Usuario();
    }

    public static PartidoController getInstance() {
        if (instance == null) {
            instance = new PartidoController();
        }
        return instance;
    }

    public UsuarioController getUsuarioController() {
        if (uc == null) {
            uc = UsuarioController.getInstance();
        }
        return uc;
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
        IEstadoPartido estado = FactoryEstado.getEstadoByName("ARMADO");
        partido.setEstado(estado);

        // Geocoding - obtener latitud y longitud
        GoogleGeocoderAdapter geocoder = new GoogleGeocoderAdapter();
        Ubicacion ubicacionGeocodificada = geocoder.getUbicacion(partido.getUbicacion());
        partido.setUbicacion(ubicacionGeocodificada);

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

    public void confirmarPartido(int id) throws Exception {
        Partido partidoEncontrado = partido.findPartidoById(id);
        partidoEncontrado.agregarEstrategiaNotificacion(new NotificacionEmail());
        partidoEncontrado.agregarEstrategiaNotificacion(new NotificacionPush());
        partidoEncontrado.confirmarPartido();
        partido.updatePartido(partidoEncontrado);
    }

    public void cancelarPartido(int id) throws Exception {

        Partido partidoEncontrado = partido.findPartidoById(id);
        partidoEncontrado.agregarEstrategiaNotificacion(new NotificacionEmail());
        partidoEncontrado.agregarEstrategiaNotificacion(new NotificacionPush());
        partidoEncontrado.cancelarPartido();
        partido.updatePartido(partidoEncontrado);
    }

    public void comenzarPartido(int id) throws Exception {

        Partido partidoEncontrado = partido.findPartidoById(id);
        partidoEncontrado.agregarEstrategiaNotificacion(new NotificacionEmail());
        partidoEncontrado.agregarEstrategiaNotificacion(new NotificacionPush());
        partidoEncontrado.comenzarPartido();
        partido.updatePartido(partidoEncontrado);
    }

    public void finalizarPartido(int id) throws Exception {
        Partido partidoEncontrado = partido.findPartidoById(id);
        partidoEncontrado.agregarEstrategiaNotificacion(new NotificacionEmail());
        partidoEncontrado.agregarEstrategiaNotificacion(new NotificacionPush());
        partidoEncontrado.finalizarPartido();
        partido.updatePartido(partidoEncontrado);
    }

    // Conversiones DTO - Partido
    public PartidoDTO partidoToDTO(Partido partido) {

        uc = getUsuarioController();

        PartidoDTO partidoDTO = new PartidoDTO();
        partidoDTO.setId(partido.getId());
        partidoDTO.setDeporte(dc.deporteToDTO(partido.getDeporte()));
        if (partido.getCreador() != null) {
            partidoDTO.setCreador(uc.usuarioToDto(partido.getCreador()));
        }
        partidoDTO.setUbicacion(partido.getUbicacion().getDireccion());
        partidoDTO.setComienzo(partido.getFecha());
        partidoDTO.setDuracion(partido.getDuracion());
        partidoDTO.setEstado(partido.getEstado().getNombre());

        List<Usuario> jugadores = partido.getJugadores();
        List<UsuarioDTO> jugadoresDTO = new ArrayList<>();

        if (jugadores != null) {
            for (Usuario u : jugadores) {
                jugadoresDTO.add(uc.usuarioToDto(u));
            }

            partidoDTO.setJugadores(jugadoresDTO);
        }
        partidoDTO.setMinimoPartidosJugados(partido.getMinimoPartidosJugados());

        return partidoDTO;
    }

    public Partido dtoToPartido(PartidoDTO partidoDTO) throws Exception {

        uc = getUsuarioController();

        Partido partido = new Partido();
        partido.setId(partidoDTO.getId());
        partido.setDeporte(dc.dtoToDeporte(partidoDTO.getDeporte()));
        partido.setCreador(uc.dtoToUsuario(partidoDTO.getCreador()));
        partido.setUbicacion(new Ubicacion(partidoDTO.getUbicacion()));
        partido.setFecha(partidoDTO.getComienzo());
        partido.setDuracion(partidoDTO.getDuracion());
        partido.setEstado(FactoryEstado.getEstadoByName(partidoDTO.getEstado()));

        List<UsuarioDTO> jugadoresDTO = partidoDTO.getJugadores();
        List<Usuario> jugadores = new ArrayList<>();

        for (UsuarioDTO u : jugadoresDTO) {
            jugadores.add(uc.dtoToUsuario(u));
        }

        partido.setJugadores(jugadores);

        partido.setMinimoPartidosJugados(partidoDTO.getMinimoPartidosJugados());

        return partido;
    }

    public List<PartidoDTO> getPartidosDeUsuarioDTO(int usuarioId) {
        List<Partido> partidos = partido.getPartidosDeUsuario(usuarioId);
        return partidos.stream()
                .map(this::partidoToDTO)
                .collect(Collectors.toList());
    }

    public void agregarJugadorAPartido(PartidoDTO partidoDTO, UsuarioDTO jugadorDTO) throws Exception {
        int id = partidoDTO.getId();
        Partido partido = new Partido().findPartidoById(id);
        Usuario jugador = UsuarioController.getInstance().dtoToUsuario(jugadorDTO); // Convertimos DTO a entidad

        List<Usuario> jugadores = partido.getJugadores();
        if (!jugadores.contains(jugador)) {
            jugadores.add(jugador);
            partido.setJugadores(jugadores);
            partido.updatePartido(partido);
        } else {
            throw new Exception("Ya estás anotado en este partido.");
        }
    }

    public List<PartidoDTO> buscarPartidos(UsuarioDTO usuarioDTO) {
        IEstrategiaEmparejamiento estrategia = FactoryEstrategia.getEstrategiaByName(usuarioDTO.getEstrategia());
        Usuario u = UsuarioController.getInstance().dtoToUsuario(usuarioDTO);
        List<Partido> partidosEncontrados = estrategia.buscarEmparejamiento(u);

        List<PartidoDTO> partidosDTO = new ArrayList<>();

        for (Partido p : partidosEncontrados) {
            partidosDTO.add(PartidoController.getInstance().partidoToDTO(p));
        }

        return partidosDTO;

    }

}
