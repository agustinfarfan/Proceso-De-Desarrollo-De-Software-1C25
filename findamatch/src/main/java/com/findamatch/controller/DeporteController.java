package com.findamatch.controller;

import com.findamatch.model.Deporte;
import com.findamatch.model.dto.DeporteDTO;

import java.util.List;
import java.util.ArrayList;

public class DeporteController {

    Deporte deporte;

    public DeporteController() {

        this.deporte = new Deporte();

    }

    public void crearDeporte(DeporteDTO deporteDTO) {
        Deporte nuevoDeporte = new Deporte(deporteDTO.getNombreDeporte(), cantMinJugadores, cantMaxJmugadores,
                descricpcion);

        this.deporte.saveDeporte(nuevoDeporte);
    }

    public List<DeporteDTO> listarDeportes() {

        List<Deporte> deportes = deporte.findDeportes();

        // Mapeo de deporte a deporteDTO

        return deportes;
    }

    public void mostrarDeportes() {
        for (Deporte deporte : deportes) {
            System.out.println(deporte.toString());
        }
    }

}
