package com.findamatch;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.findamatch.controller.DeporteController;
import com.findamatch.controller.PartidoController;
import com.findamatch.controller.UsuarioController;
import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.dto.DeporteDTO;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.enums.Nivel;
import com.findamatch.model.estado.EstadoConfirmado;
import com.findamatch.model.notificacion.Notificacion;
import com.findamatch.model.notificacion.NotificacionEmail;
import com.findamatch.model.notificacion.NotificacionPush;
import com.findamatch.model.notificacion.interfaces.INotificacion;

public class App {
    public static void main(String[] args) throws Exception {
        DeporteController dc = DeporteController.getInstance();
        UsuarioController uc = UsuarioController.getInstance();
        PartidoController pc = PartidoController.getInstance();
        /*
         * 
         */
        int id = uc.createUsuario(new UsuarioDTO(
                "usuario.nuevo2",
                "mail",
                "123",
                "Av. Cabildo 2272, Belgrano, CABA"));

        if (id != -1) {
            UsuarioDTO usuarioCreado = uc.getUsuarioByIdDTO(id);
            System.out.println(usuarioCreado.toString());
        } else {
            System.out.println("Error al crear el usuario.");
        }
        /*
         * List<UsuarioDeporte> usuarioDeportes = uc.getUsuarioDeportes();
         * for (UsuarioDeporte ud : usuarioDeportes) {
         * System.out.println(ud.toString());
         * }
         */

        /*
         * pc.createPartido(new PartidoDTO(
         * 1,
         * 40,
         * "Av. Cabildo 2272, Belgrano, CABA",
         * LocalDateTime.now(),
         * 60));
         */

        Deporte futbol = new Deporte(1, "Fútbol", 5, 10, "Partido 5 vs 5");
        Usuario mathias = new Usuario(1, "mathias", "mathias@mail.com", "1234", "Calle 123");
        Ubicacion ubicacion = new Ubicacion("Plaza Mitre");

        Partido partido = new Partido(
                futbol,
                mathias,
                ubicacion,
                LocalDateTime.now().plusDays(1),
                60);

        partido.getJugadores().add(new Usuario(2, "Facu", "facu@mail.com", "abcd", "Av 123"));

        partido.agregarEstrategiaNotificacion(new NotificacionEmail());
        partido.agregarEstrategiaNotificacion(new NotificacionPush());

        partido.cancelarPartido();
        partido.confirmarPartido();
    }
}
