package com.findamatch.controller;

import com.findamatch.model.Deporte;
import com.findamatch.model.dto.DeporteDTO;

import java.util.List;
import java.util.ArrayList;

public class DeporteController {

    Deporte deporte;
    private static DeporteController instance = null;

    // Constructor

    private DeporteController() {
        this.deporte = new Deporte();
    }

    public static DeporteController getInstance() {
        if (instance == null) {
            instance = new DeporteController();
        }
        return instance;
    }

    // CRUD

    public List<DeporteDTO> getAllDeportesDTOs() {

        List<Deporte> deportes = deporte.findAllDeportes();
        List<DeporteDTO> deportesDto = new ArrayList<DeporteDTO>();

        for (Deporte deporte : deportes) {
            deportesDto.add(deporteToDTO(deporte));
        }

        return deportesDto;
    }

    public DeporteDTO getDeporteDTOById(int id) {

        Deporte deporteEncontrado = deporte.findDeporteById(id);
        DeporteDTO deporteDto = deporteToDTO(deporteEncontrado);

        return deporteDto;
    }

    public void createDeporte(DeporteDTO deporteDTO) {
        Deporte nuevoDeporte = dtoToDeporte(deporteDTO);
        this.deporte.saveDeporte(nuevoDeporte);
    }

    public void updateDeporte(DeporteDTO deporteDTO) {
        Deporte deporteEncontrado = deporte.findDeporteById(deporteDTO.getId());
        deporteEncontrado.setNombre(deporteDTO.getNombre());
        deporteEncontrado.setCantMinJugadores(deporteDTO.getCantMinJugadores());
        deporteEncontrado.setCantMaxJugadores(deporteDTO.getCantMaxJugadores());
        deporteEncontrado.setDescripcion(deporteDTO.getDescripcion());
        deporte.updateDeporte(deporteEncontrado);
    }

    public void deleteDeporte(int id) {
        deporte.deleteDeporte(id);
    }

    // Auxiliar

    public DeporteDTO deporteToDTO(Deporte deporte) {
        DeporteDTO deporteDTO = new DeporteDTO(
                deporte.getId(),
                deporte.getNombre(),
                deporte.getCantMinJugadores(),
                deporte.getCantMaxJugadores(),
                deporte.getDescripcion());
        return deporteDTO;
    }

    public Deporte dtoToDeporte(DeporteDTO deporteDTO) {
        Deporte deporte = new Deporte(
                deporteDTO.getId(),
                deporteDTO.getNombre(),
                deporteDTO.getCantMinJugadores(),
                deporteDTO.getCantMaxJugadores(),
                deporteDTO.getDescripcion());
        return deporte;
    }
}
