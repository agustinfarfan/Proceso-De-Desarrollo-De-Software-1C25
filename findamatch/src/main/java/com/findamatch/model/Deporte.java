package com.findamatch.model;

import java.util.List;

import com.findamatch.dao.DeporteDAO;
import com.findamatch.model.dto.DeporteDTO;

public class Deporte {
    private String nombre;
    private int cantMinJugadores;
    private int cantMaxJugadores;
    private String descripcion;

    DeporteDAO deporteDAO;

    public Deporte(String nombre, int cantMinJugadores, int cantMaxJugadores, String descripcion) {
        this.nombre = nombre;
        this.cantMinJugadores = cantMinJugadores;
        this.cantMaxJugadores = cantMaxJugadores;
        this.descripcion = descripcion;

        deporteDAO = new DeporteDAO();
    }

    public Deporte() {

    }

    public String toString() {
        return "Deporte: " + this.nombre + " - Cantidad de jugadores: " + this.cantMinJugadores + " - "
                + this.cantMaxJugadores;
    }

    public void saveDeporte(Deporte deporte) {

        // Persiste en base de datos

    }

    public List<Deporte> findDeportes() {

        List<Deporte> deportes = deporteDAO.obtenerDeportes();

        return deportes;

    }

    public void updateDeporte() {
    }

    public void deleteDeporte() {
    }

}
