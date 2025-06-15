package com.findamatch.model.notificacion;

import java.util.ArrayList;
import java.util.List;

import com.findamatch.model.Usuario;
import com.findamatch.model.Partido;
import com.findamatch.model.notificacion.interfaces.INotificacion;

public class Notificador {

    private final List<INotificacion> estrategias = new ArrayList<>();

    public void agregarEstrategia(INotificacion estrategia) {
        estrategias.add(estrategia);
    }

    public void quitarEstrategia(INotificacion estrategia) {
        estrategias.remove(estrategia);
    }

    public void notificar(Partido partido) {
        String mensaje = "El partido cambió al estado: " + partido.getEstado().getNombre();

        for (Usuario jugador : partido.getJugadores()) {
            Notificacion noti = new Notificacion(mensaje,
                    java.time.LocalDateTime.now(),
                    jugador,
                    partido);
            for (INotificacion estrategia : estrategias) {
                estrategia.enviarNotificacion(noti);
            }
        }
    }
}