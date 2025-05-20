package com.findamatch.controller;

import com.findamatch.model.Deporte;
import java.util.List;
import java.util.ArrayList;

public class DeporteController {

    private List<Deporte> deportes;

    public DeporteController() {
        this.deportes = new ArrayList<>(deportes);
    }

    public void crearDeporte(String nombre, int cantMinJugadores, int cantMaxJugadores) {
        Deporte deporte = new Deporte(nombre, cantMinJugadores, cantMaxJugadores);
        deportes.add(deporte);
    }

    public void mostrarDeportes() {
        for (Deporte deporte : deportes) {
            System.out.println(deporte.toString());
        }
    }

}
