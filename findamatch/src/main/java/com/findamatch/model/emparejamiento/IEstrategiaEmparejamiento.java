package com.findamatch.model.emparejamiento;

import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;

import java.util.List;

public interface IEstrategiaEmparejamiento {
    int getId();

    List<Partido> buscarEmparejamiento(Usuario usuario); // Nuevo enfoque
}