package com.findamatch.model.estado;

import com.findamatch.model.Partido;

public class EstadoConfirmado implements IEstadoPartido {

    @Override
    public void confirmar(Partido partido) {
        System.out.println("El partido ya está confirmado.");
    }

    @Override
    public void cancelar(Partido partido) {
        partido.setEstado(new EstadoCancelado());
        System.out.println("Partido cancelado luego de ser confirmado.");
    }

    @Override
    public void finalizar(Partido partido) {
        partido.setEstado(new EstadoFinalizado());
        System.out.println("Partido finalizado con éxito.");
    }

    @Override
    public String nombre() {
        return "Confirmado";
    }
}
