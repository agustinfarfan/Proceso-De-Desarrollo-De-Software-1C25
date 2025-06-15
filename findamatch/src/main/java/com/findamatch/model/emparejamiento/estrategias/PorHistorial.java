package com.findamatch.model.emparejamiento.estrategias;

import java.util.ArrayList;
import java.util.List;

import com.findamatch.dao.PartidoDAO;
import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;

public class PorHistorial implements IEstrategiaEmparejamiento {
    private int id;

    @Override
    public int getId() {
        return 1; // ID correspondiente a esta estrategia
    }
    @Override
    public List<Partido> buscarEmparejamiento(Usuario usuario) {
        List<Partido> partidosEmparejados = new ArrayList<>();
        PartidoDAO partidoDAO = PartidoDAO.getInstance();
        List<Partido> todosLosPartidos;

        try {
            todosLosPartidos = partidoDAO.findAllPartidos();
        } catch (Exception e) {
            e.printStackTrace();
            return partidosEmparejados;
        }

        // Calcular cantidad de partidos FINALIZADOS en los que el usuario participó
        long cantidadJugados = todosLosPartidos.stream()
            .filter(p -> p.getEstado().getNombre().equals("FINALIZADO"))
            .filter(p -> p.getJugadores().stream()
                    .anyMatch(j -> j.getId() == usuario.getId()))
            .count();

        for (Partido partido : todosLosPartidos) {
            if (!partido.getEstado().getNombre().equals("ARMADO")) continue;

            // Si cumple con el mínimo de historial requerido
            if (cantidadJugados >= partido.getMinimoPartidosJugados()) {
                boolean yaInscripto = partido.getJugadores().stream()
                        .anyMatch(j -> j.getId() == usuario.getId());
                if (!yaInscripto) {
                    partidosEmparejados.add(partido);
                }
            }
        }

        return partidosEmparejados;
    }

}
