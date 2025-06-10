package com.findamatch.model.estado;

import com.findamatch.model.Partido;

public interface IEstadoPartido {

    int getId();
    void confirmar(Partido partido);
    void cancelar(Partido partido);
    void finalizar(Partido partido);
    String nombre();

}
