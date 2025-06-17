package com.findamatch.model.estado;

import java.util.Arrays;
import java.util.List;

import com.findamatch.model.Partido;

public class EstadoEnCurso implements IEstadoPartido {

    private final String nombre = "EN_CURSO";

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void confirmar(Partido partido) {
        System.out.println("El partido ya comenzó. No se puede confirmar.");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("No se puede cancelar un partido en curso.");
    }

    @Override
    public void finalizar(Partido partido) {
        partido.setEstado(new EstadoFinalizado());
        System.out.println("Partido finalizado correctamente desde el estado EN_CURSO.");
    }

    @Override
    public void comenzar(Partido partido) {
        System.out.println("El partido ya está en curso.");
    }

    @Override
    public List<String> getTransicionesValidas() {
        return Arrays.asList("FINALIZADO");
    }
}