package com.findamatch;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.findamatch.controller.DeporteController;
import com.findamatch.controller.PartidoController;
import com.findamatch.controller.UsuarioController;
import com.findamatch.dao.EstrategiaDAO;
import com.findamatch.dao.UsuarioDAO;
import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.dto.DeporteDTO;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.dto.UsuarioDeporteDTO;
import com.findamatch.model.enums.Nivel;
import com.findamatch.model.estado.EstadoConfirmado;
import com.findamatch.model.estado.FactoryEstado;
import com.findamatch.model.notificacion.Notificacion;
import com.findamatch.model.notificacion.NotificacionEmail;
import com.findamatch.model.notificacion.NotificacionPush;
import com.findamatch.model.notificacion.interfaces.INotificacion;

public class App2 {
    public static void main(String[] args) throws Exception {
        DeporteController dc = DeporteController.getInstance();
        UsuarioController uc = UsuarioController.getInstance();
        PartidoController pc = PartidoController.getInstance();

        // List<UsuarioDTO> usuarios = uc.getAllUsuariosDTO();

        // for (UsuarioDTO u : usuarios) {
        // System.out.println("=========" + u.getNombreUsuario() + "=========");
        // System.out.println(u);
        // }

        UsuarioDTO usuario = uc.getUsuarioByIdDTO(18);
        // usuario.setEstrategia("CERCANIA");

        List<PartidoDTO> PARTIDOS = pc.buscarPartidos(usuario);
        // uc.updateUsuario(usuario);
    }
}
