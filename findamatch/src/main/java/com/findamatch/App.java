package com.findamatch;

import java.util.List;

import com.findamatch.controller.DeporteController;
import com.findamatch.model.dto.DeporteDTO;

public class App {
    public static void main(String[] args) {
        DeporteController dc = DeporteController.getInstance();

        /*
         * 
         * 
         * dc.createDeporte(deporteDTO);
         * 
         * DeporteDTO deporteDTO2 = dc.getDeporteDTOById(11);
         * System.out.println(deporteDTO2.toString());
         * 
         * DeporteDTO deporteDTO = new DeporteDTO(11, "Fútbol 11", 22, 11,
         * "Fútbol tradicional MODIFICADO");
         * dc.updateDeporte(deporteDTO);
         * 
         * deporteDTO2 = dc.getDeporteDTOById(11);
         * System.out.println(deporteDTO2.toString());
         */

        List<DeporteDTO> deportesDTOs = dc.getAllDeportesDTOs();
        for (DeporteDTO deporteDTO : deportesDTOs) {
            System.out.println(deporteDTO.toString());
        }
        dc.deleteDeporte(11);

        deportesDTOs = dc.getAllDeportesDTOs();

        for (DeporteDTO deporteDTO : deportesDTOs) {
            System.out.println(deporteDTO.toString());
        }

    }
}
