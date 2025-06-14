package com.findamatch.model.estado;

import com.findamatch.model.Partido;

public class EstadoFinalizado implements IEstadoPartido {

    private final String nombre = "FINALIZADO";

    @Override
    public void confirmar(Partido partido) {
        System.out.println("No se puede confirmar un partido ya finalizado.");
    }

    @Override
    public void cancelar(Partido partido) {
        System.out.println("No se puede cancelar un partido ya finalizado.");
    }

    @Override
    public void finalizar(Partido partido) {
        System.out.println("El partido ya ha sido finalizado.");
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public void comenzar(Partido partido) {
        System.out.println("No se puede comenzar un partido ya finalizado.");
    }
}
