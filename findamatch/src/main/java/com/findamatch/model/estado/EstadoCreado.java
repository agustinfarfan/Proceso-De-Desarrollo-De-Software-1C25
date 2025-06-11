package com.findamatch.model.estado;

import com.findamatch.model.Partido;

public class EstadoCreado implements IEstadoPartido {

    private final String nombre = "CREADO";

    @Override
    public void confirmar(Partido partido) {
        partido.setEstado(new EstadoConfirmado());
        System.out.println("Partido confirmado.");
    }

    @Override
    public void cancelar(Partido partido) {
        partido.setEstado(new EstadoCancelado());
        System.out.println("Partido cancelado.");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("No se puede finalizar un partido que aún no ha sido confirmado.");
    }

    @Override
    public String nombre() {
        return this.nombre;
    }
    @Override
    public void comenzar(Partido partido) {
    System.out.println("El partido debe ser confirmado antes de comenzar.");
    }
}
