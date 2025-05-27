package com.findamatch.model.emparejamiento.estrategias;

import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;
import java.util.List;

public class PorNivel implements IEstrategiaEmparejamiento {
    private final int nivelMinimo;
    private final int nivelMaximo;

    public PorNivel(int nivelMinimo, int nivelMaximo) {
        this.nivelMinimo = nivelMinimo;
        this.nivelMaximo = nivelMaximo;
    }

    @Override
    public List<Usuario> buscarEmparejamiento(Partido partido) {
        return null;
    }
}
