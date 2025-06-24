package com.findamatch.model.emparejamiento.estrategias;

import com.findamatch.dao.PartidoDAO;
import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.enums.Nivel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PorNivel implements IEstrategiaEmparejamiento {

    private final String nombre = "NIVEL";

    @Override
    public String getNombre() {
        return nombre; // ID correspondiente a esta estrategia
    }

    @Override
    public List<Partido> buscarEmparejamiento(Usuario usuario) {
        List<Partido> partidosEmparejados = new ArrayList<>();
        PartidoDAO partidoDAO = PartidoDAO.getInstance();
        List<Partido> todosLosPartidos;

        try {
            todosLosPartidos = partidoDAO.findAllPartidos(); // puede tirar Exception
        } catch (Exception e) {
            e.printStackTrace();
            return partidosEmparejados;
        }

        // Iteramos por cada partido y vemos si coincide en nivel con el usuario
        for (Partido partido : todosLosPartidos) {
            // 👇 Filtro por estado "ARMADO"
            if (!"ARMADO".equals(partido.getEstado().getNombre()))
                continue;

            Deporte deporte = partido.getDeporte();
            Nivel nivelUsuario = usuario.getNivelPorDeporte(deporte);
            Usuario creador = partido.getCreador();
            Nivel nivelCreador = creador.getNivelPorDeporte(deporte);

            if (nivelUsuario != null && nivelUsuario.equals(nivelCreador)) {
                // Verifica que no esté ya inscripto
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
