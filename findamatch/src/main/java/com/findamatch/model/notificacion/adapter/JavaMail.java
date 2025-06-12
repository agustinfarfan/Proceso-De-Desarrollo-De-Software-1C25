package com.findamatch.model.notificacion.adapter;

import com.findamatch.model.notificacion.Notificacion;

public class JavaMail implements IAdaptadorMail {
    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        System.out.println("[JavaMail: Email]");
        System.out.println("A: " + notificacion.getUsuario().getMail());
        System.out.println("Mensaje: " + notificacion.getMensaje());
        System.out.println("Partido ID: " + notificacion.getPartido().getId());
    }
}