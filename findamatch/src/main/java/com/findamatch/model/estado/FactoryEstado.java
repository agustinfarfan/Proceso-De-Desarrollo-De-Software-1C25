package com.findamatch.model.estado;

public class FactoryEstado {

    public static IEstadoPartido getEstadoByName(String estado) {
        switch (estado) {
            case "ARMADO":
                return new EstadoCreado();
            case "CONFIRMADO":
                return new EstadoConfirmado();
            case "CANCELADO":
                return new EstadoCancelado();
            case "EN_JUEGO":
                return new EstadoEnCurso();
            case "FINALIZADO":
                return new EstadoFinalizado();
        }

        return null;
    }

}
