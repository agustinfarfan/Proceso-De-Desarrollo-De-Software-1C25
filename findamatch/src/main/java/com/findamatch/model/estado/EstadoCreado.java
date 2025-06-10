package com.findamatch.model.estado;

import com.findamatch.model.Partido;

public class EstadoCreado implements IEstadoPartido {

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
        return "Creado";
    }
}
