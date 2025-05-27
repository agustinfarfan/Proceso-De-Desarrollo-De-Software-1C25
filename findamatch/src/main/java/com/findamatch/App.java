package com.findamatch;

import com.findamatch.controller.DeporteController;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        DeporteController dc = new DeporteController();
        dc.crearDeporte("Futbol", 5, 5);
        dc.mostrarDeportes();

    }
}
