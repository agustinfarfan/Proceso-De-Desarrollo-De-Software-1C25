package com.findamatch.model.emparejamiento.estrategias;

import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;
import java.util.List;

public class PorNivel implements IEstrategiaEmparejamiento {
    private int id;

    public PorNivel() {
    }

    @Override
    public List<Usuario> buscarEmparejamiento(Partido partido) {
        return null;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
