package com.findamatch.model.emparejamiento;

import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;

import java.util.List;

public interface IEstrategiaEmparejamiento {
    String getNombre();

    List<Partido> buscarEmparejamiento(Usuario usuario); // Nuevo enfoque
}