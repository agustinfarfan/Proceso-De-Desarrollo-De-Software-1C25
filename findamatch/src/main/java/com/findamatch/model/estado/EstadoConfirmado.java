package com.findamatch.model.estado;

import java.util.Arrays;
import java.util.List;

import com.findamatch.model.Partido;

public class EstadoConfirmado implements IEstadoPartido {

    private final String nombre = "CONFIRMADO";

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
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public void comenzar(Partido partido) {
        partido.setEstado(new EstadoEnCurso());
        System.out.println("El partido ha comenzado.");
    }

    @Override
    public List<String> getTransicionesValidas() {
        return Arrays.asList("EN_CURSO");
    }
}
