package com.findamatch.model.estado;

import java.util.List;

import com.findamatch.model.Partido;

public interface IEstadoPartido {
    String getNombre();
    List<String> getTransicionesValidas();
    void confirmar(Partido partido);

    void cancelar(Partido partido);

    void finalizar(Partido partido);

    void comenzar(Partido partido);
}