package com.findamatch.model.notificacion;

import com.findamatch.model.notificacion.interfaces.INotificacion;

public class NotificacionPush implements INotificacion {
    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        System.out.println("[Notificación Push]");
        System.out.println("A: " + notificacion.getUsuario().getNombreUsuario());
        System.out.println("Mensaje: " + notificacion.getMensaje());
        System.out.println("Partido ID: " + notificacion.getPartido().getId());
    }
}
