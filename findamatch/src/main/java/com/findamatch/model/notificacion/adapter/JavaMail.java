package com.findamatch.model.notificacion.adapter;

import com.findamatch.model.notificacion.Notificacion;

public class JavaMail implements IAdaptadorMail {
    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        System.out.println("[JavaMail: Email] A: " + notificacion.getUsuario().getMail()
                + " | Mensaje: " + notificacion.getMensaje()
                + " | Partido ID: " + notificacion.getPartido().getId());
    }
}