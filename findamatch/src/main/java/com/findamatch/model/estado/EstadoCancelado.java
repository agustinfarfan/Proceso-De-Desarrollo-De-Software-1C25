package com.findamatch.model.estado;

import com.findamatch.model.Partido;

public class EstadoCancelado implements IEstadoPartido {

    private final String nombre = "CANCELADO";

    @Override
    public void confirmar(Partido partido) {
        System.out.println("No se puede confirmar un partido cancelado.");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("El partido ya está cancelado.");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("No se puede finalizar un partido cancelado.");
    }

    @Override
    public String nombre() {
        return this.nombre;
    }
    @Override
    public void comenzar(Partido partido) {
    System.out.println("No se puede comenzar un partido cancelado.");
}
}
