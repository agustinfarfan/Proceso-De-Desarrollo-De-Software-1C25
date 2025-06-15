package com.findamatch.model.emparejamiento.estrategias;

import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.dao.PartidoDAO;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PorCercania implements IEstrategiaEmparejamiento {

    @Override
    public int getId() {
        return 2; // ID de estrategia
    }

    @Override
    public List<Partido> buscarEmparejamiento(Usuario usuario) {
        List<Partido> partidosEmparejados = new ArrayList<>();
        PartidoDAO partidoDAO = PartidoDAO.getInstance();
        List<Partido> todosLosPartidos;

        try {
            todosLosPartidos = partidoDAO.findAllPartidos();
        } catch (Exception e) {
            e.printStackTrace();
            return partidosEmparejados;
        }

    Ubicacion ubicacionUsuario;
        try {
            ubicacionUsuario = new Ubicacion(usuario.getUbicacion()); // crea el objeto y geolocaliza
        } catch (Exception e) {
            e.printStackTrace();
            return partidosEmparejados; // no se pudo obtener coordenadas
}

        double varianzaMaxima = ubicacionUsuario.getVarianza(); // radio de búsqueda

        List<PartidoDistancia> candidatos = new ArrayList<>();

       for (Partido partido : todosLosPartidos) {
            if (partido.getCreador().getId() == usuario.getId()) continue; // omitir propios

            // 👇 FILTRO POR ESTADO
            if (!"ARMADO".equals(partido.getEstado().getNombre())) continue;

            Ubicacion ubicacionPartido = partido.getUbicacion();
            if (ubicacionPartido == null) continue;

            double distancia = calcularDistancia(ubicacionUsuario, ubicacionPartido);
            if (distancia <= varianzaMaxima) {
        candidatos.add(new PartidoDistancia(partido, distancia));
    }
}

        candidatos.sort(Comparator.comparingDouble(PartidoDistancia::getDistancia));

        return candidatos.stream()
                .map(PartidoDistancia::getPartido)
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

    // Clase auxiliar para ordenar por distancia
    private static class PartidoDistancia {
        private final Partido partido;
        private final double distancia;

        public PartidoDistancia(Partido partido, double distancia) {
            this.partido = partido;
            this.distancia = distancia;
        }

        public Partido getPartido() {
            return partido;
        }

        public double getDistancia() {
            return distancia;
        }
    }
}
