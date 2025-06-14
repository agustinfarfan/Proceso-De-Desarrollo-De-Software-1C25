package com.findamatch.model.emparejamiento.estrategias;

import com.findamatch.controller.UsuarioController;
import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PorCercania implements IEstrategiaEmparejamiento {

    @Override
    public int getId() {
        return 2; // ID correspondiente a esta estrategia
    }

    @Override
    public List<Usuario> buscarEmparejamiento(Partido partido) {
        List<Usuario> todosLosUsuarios = UsuarioController.getInstance()
                .getAllUsuariosDTO()
                .stream()
                .map(dto -> {
                    try {
                        return UsuarioController.getInstance().dtoToUsuario(dto);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(u -> u != null && u.getId() != partido.getCreador().getId())
                .toList();

        Ubicacion ubicacionPartido = partido.getUbicacion();
        double varianzaMaxima = ubicacionPartido.getVarianza(); // distancia máxima permitida

        List<UsuarioDistancia> candidatos = new ArrayList<>();

        for (Usuario usuario : todosLosUsuarios) {
            Ubicacion ubicacionUsuario = usuario.getUbicacion();
            double distancia = calcularDistancia(ubicacionPartido, ubicacionUsuario);

            if (distancia <= varianzaMaxima) {
                candidatos.add(new UsuarioDistancia(usuario, distancia));
            }
        }

        // Ordenar por distancia creciente
        candidatos.sort(Comparator.comparingDouble(UsuarioDistancia::getDistancia));

        // Retornar solo los usuarios
        return candidatos.stream()
                .map(UsuarioDistancia::getUsuario)
                .toList();
    }

    private double calcularDistancia(Ubicacion u1, Ubicacion u2) {
        final int RADIO_TIERRA_KM = 6371;
        double lat1 = Math.toRadians(u1.getLatitud());
        double lon1 = Math.toRadians(u1.getLongitud());
        double lat2 = Math.toRadians(u2.getLatitud());
        double lon2 = Math.toRadians(u2.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));
        return RADIO_TIERRA_KM * c;
    }

    // Clase auxiliar interna para ordenar usuarios por distancia
    private static class UsuarioDistancia {
        private final Usuario usuario;
        private final double distancia;

        public UsuarioDistancia(Usuario usuario, double distancia) {
            this.usuario = usuario;
            this.distancia = distancia;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public double getDistancia() {
            return distancia;
        }
    }
}
