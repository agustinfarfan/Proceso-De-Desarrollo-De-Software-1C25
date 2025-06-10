package com.findamatch;

import java.time.LocalDateTime;
import java.util.List;

import com.findamatch.controller.DeporteController;
import com.findamatch.controller.PartidoController;
import com.findamatch.controller.UsuarioController;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.dto.DeporteDTO;
import com.findamatch.model.dto.PartidoDTO;
import com.findamatch.model.dto.UsuarioDTO;
import com.findamatch.model.enums.Nivel;

public class App {
    public static void main(String[] args) throws Exception {
        DeporteController dc = DeporteController.getInstance();
        UsuarioController uc = UsuarioController.getInstance();
        PartidoController pc = PartidoController.getInstance();
        /*
         * 
         * int id = uc.createUsuario(new UsuarioDTO(
         * "usuario.nuevo",
         * "mail",
         * "123",
         * "Av. Cabildo 2272, Belgrano, CABA"));
         * 
         * if (id != -1) {
         * UsuarioDTO usuarioCreado = uc.getUsuarioByIdDTO(id);
         * System.out.println(usuarioCreado.toString());
         * } else {
         * System.out.println("Error al crear el usuario.");
         * }
         * 
         * List<UsuarioDeporte> usuarioDeportes = uc.getUsuarioDeportes();
         * for (UsuarioDeporte ud : usuarioDeportes) {
         * System.out.println(ud.toString());
         * }
         */

        pc.createPartido(new PartidoDTO(
                1,
                40,
                "Av. Cabildo 2272, Belgrano, CABA",
                LocalDateTime.now(),
                60));

    }
}
