package com.findamatch.model.notificacion;

import com.findamatch.model.notificacion.Notificacion;
import com.findamatch.model.notificacion.interfaces.INotificacion;
import com.findamatch.model.notificacion.adapter.IAdaptadorMail;
import com.findamatch.model.notificacion.adapter.JavaMail;

public class NotificacionEmail implements INotificacion {

    private IAdaptadorMail adaptadorMail;

    public NotificacionEmail() {
        this.adaptadorMail = new JavaMail(); // Por ahora, hardcodeado
    }

    // Si querés inyectarlo por constructor:
    // public NotificacionEmail(IAdaptadorMail adaptador) { this.adaptadorMail =
    // adaptador; }

    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        adaptadorMail.enviarNotificacion(notificacion);
    }
}