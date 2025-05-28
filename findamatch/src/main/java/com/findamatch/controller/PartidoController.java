package com.findamatch.controller;

import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

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

}
