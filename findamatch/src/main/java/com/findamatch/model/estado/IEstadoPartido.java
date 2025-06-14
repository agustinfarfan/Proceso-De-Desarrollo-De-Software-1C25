package com.findamatch.model.estado;

import com.findamatch.model.Partido;

public interface IEstadoPartido {
    String getNombre();

    void confirmar(Partido partido);

    void cancelar(Partido partido);

    void finalizar(Partido partido);

    void comenzar(Partido partido); // ← Nuevo método
}