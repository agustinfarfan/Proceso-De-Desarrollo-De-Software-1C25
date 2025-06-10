package com.findamatch.model;

import java.util.List;

import com.findamatch.dao.DeporteDAO;

public class Deporte {

    private int id;
    private String nombre;
    private int cantMinJugadores;
    private int cantMaxJugadores;
    private String descripcion;

    DeporteDAO deporteDAO = DeporteDAO.getInstance();

    public Deporte(int id, String nombre, int cantMinJugadores, int cantMaxJugadores, String descripcion) {

        this.id = id;
        this.nombre = nombre;
        this.cantMinJugadores = cantMinJugadores;
        this.cantMaxJugadores = cantMaxJugadores;
        this.descripcion = descripcion;
    }

    public Deporte() {

    }

    // CRUD

    public List<Deporte> findAllDeportes() {

        List<Deporte> deportes = deporteDAO.findAllDeportes();

        return deportes;

    }

    public Deporte findDeporteById(int id) {

        Deporte deporte = null;

        deporte = deporteDAO.findDeporteById(id);

        return deporte;

    }

    public void saveDeporte(Deporte deporte) {

        deporteDAO.saveDeporte(deporte);

    }

    public void updateDeporte(Deporte deporte) {
        deporteDAO.updateDeporte(deporte);
    }

    public void deleteDeporte(int id) {
        deporteDAO.deleteDeporte(id);
    }

    // Setters & Getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantMinJugadores() {
        return cantMinJugadores;
    }

    public void setCantMinJugadores(int cantMinJugadores) {
        this.cantMinJugadores = cantMinJugadores;
    }

    public int getCantMaxJugadores() {
        return cantMaxJugadores;
    }

    public void setCantMaxJugadores(int cantMaxJugadores) {
        this.cantMaxJugadores = cantMaxJugadores;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Deporte{" +
                "id=" + id +
                ", nombreDeporte=" + nombre +
                ", cantMinDeJugadores=" + cantMinJugadores +
                ", cantMaxDeJugadores=" + cantMaxJugadores +
                ", descripcion='" + descripcion + "}";

    }
}
