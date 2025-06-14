package com.findamatch.model.emparejamiento.estrategias;

import java.util.List;

import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;

public class PorCercania implements IEstrategiaEmparejamiento {
    private int id;

    public PorCercania() {

    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public List<Usuario> buscarEmparejamiento(Partido partido) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarEmparejamiento'");
    }
}
