package com.findamatch.model.emparejamiento.estrategias;

import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;

public class FactoryEstrategia {

    public static IEstrategiaEmparejamiento getEstrategiaByName(String estado) {
        switch (estado) {
            case "NIVEL":
                return new PorNivel();
            case "CERCANIA":
                return new PorCercania();
            case "HISTORIAL":
                return new PorHistorial();

        }
        return null;
    }

}
