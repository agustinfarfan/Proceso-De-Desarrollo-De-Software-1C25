package com.findamatch.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;

public class PartidoController {
    private List<Partido> partidos;

    public PartidoController() {
        // SINGLETON
        partidos = new ArrayList<>();
    }

    public void crearPartido(
            Deporte deporte,
            // List<Usuario> jugadores,
            Ubicacion ubicacion,
            LocalDateTime comienzo,
            int duracionMinutos) {
        // String estado,
        // String estrategiaEmparejamiento)

        Partido partido = new Partido(
                deporte,
                // jugadores,
                ubicacion,
                comienzo,
                duracionMinutos
        // estado,
        // estrategiaEmparejamiento
        );

        partidos.add(partido);

    }

    public void mostrarPartidos() {
        for (Partido partido : partidos) {
            System.out.println(partido);
        }
    }
public void confirmarPartido(int index) {
        if (index >= 0 && index < partidos.size()) {
            partidos.get(index).confirmarPartido();
        }
    }
public void cancelarPartido(int index) {
        if (index >= 0 && index < partidos.size()) {
            partidos.get(index).cancelarPartido();
        }
    }
public void finalizarPartido(int index) {
        if (index >= 0 && index < partidos.size()) {
            partidos.get(index).finalizarPartido();
        }
    }
    public void buscarPartido(int index) {
        if (index >= 0 && index < partidos.size()) {
            partidos.get(index).buscarPartido(); // esto usará la estrategia
        }
    }
}
