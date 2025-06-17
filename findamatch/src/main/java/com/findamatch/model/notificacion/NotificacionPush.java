package com.findamatch.model.notificacion;

import com.findamatch.model.notificacion.interfaces.INotificacion;

public class NotificacionPush implements INotificacion {
    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        System.out.println("[Notificación Push] A: " + notificacion.getUsuario().getNombreUsuario()
                + " | Mensaje: " + notificacion.getMensaje()
                + " | Partido ID: " + notificacion.getPartido().getId());
    }
}
