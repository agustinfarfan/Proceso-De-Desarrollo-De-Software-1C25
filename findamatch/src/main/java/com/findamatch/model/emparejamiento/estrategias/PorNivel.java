package com.findamatch.model.emparejamiento.estrategias;

import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.enums.*;
import com.findamatch.dao.UsuarioDAO;
import com.findamatch.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PorNivel implements IEstrategiaEmparejamiento {
@Override
    public int getId() {
        return 3; // el ID que corresponda a esta estrategia
    }
    @Override
    public List<Usuario> buscarEmparejamiento(Partido partido) {
        List<Usuario> emparejados = new ArrayList<>();
        Deporte deportePartido = partido.getDeporte();
        Usuario creador = partido.getCreador();

        Nivel nivelCreador = creador.getNivelPorDeporte(deportePartido);
        if (nivelCreador == null) return emparejados; // Si el creador no tiene nivel, no se puede comparar

        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
        List<Usuario> todos;
        try {
            todos = usuarioDAO.findAllUsuarios();
        } catch (SQLException e) {
            e.printStackTrace();
            return emparejados;
        }

        for (Usuario u : todos) {
            if (u.getId() == creador.getId()) continue;

            Nivel nivel = u.getNivelPorDeporte(deportePartido);
            if (nivel != null && nivel.equals(nivelCreador)) {
                emparejados.add(u);
            }
        }

        return emparejados;
    }
}

