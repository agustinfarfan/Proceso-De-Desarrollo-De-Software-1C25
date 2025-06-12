package com.findamatch.model.notificacion;

import java.time.LocalDateTime;
import java.util.Date;

import com.findamatch.model.Partido;
import com.findamatch.model.Usuario;

public class Notificacion {
    private String mensaje;
    private LocalDateTime fecha;
    private Usuario usuario;
    private Partido partido;

    public Notificacion(String mensaje, LocalDateTime fecha, Usuario usuario, Partido partido) {
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.usuario = usuario;
        this.partido = partido;
    }

    // Getters
    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Partido getPartido() {
        return partido;
    }
}
