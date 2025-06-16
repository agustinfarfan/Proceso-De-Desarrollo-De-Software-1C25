package com.findamatch.dao;

import com.findamatch.model.*;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.estado.FactoryEstado;
import com.findamatch.model.estado.IEstadoPartido;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartidoDAO {

    ConexionDAO conexionDAO = ConexionDAO.getInstance();
    private static PartidoDAO instance = null;

    private PartidoDAO() {
    }

    public static PartidoDAO getInstance() {
        if (instance == null) {
            instance = new PartidoDAO();
        }
        return instance;
    }

public List<Partido> findAllPartidos() throws Exception {
    Connection con = ConexionDAO.conectar();

    String sqlPartidos = """
        SELECT p.id, p.id_deporte, p.id_creador, p.ubicacion, p.comienzo, p.duracion,
               p.minimo_partidos_jugados, e.nombre AS estado_nombre,
               u.nombre_usuario AS creador_nombre, u.mail, u.domicilio
        FROM partido p
        JOIN estado e ON p.id_estado = e.id
        JOIN usuario u ON p.id_creador = u.id
    """;

    String sqlJugadores = """
        SELECT up.partido_id, u.id, u.nombre_usuario, u.mail, u.domicilio
        FROM usuariopartido up
        JOIN usuario u ON up.usuario_id = u.id
    """;

    List<Partido> partidos = new ArrayList<>();
    Map<Integer, List<Usuario>> jugadoresPorPartido = new HashMap<>();

    try {
        try (PreparedStatement psJugadores = con.prepareStatement(sqlJugadores)) {
            ResultSet rsJugadores = psJugadores.executeQuery();
            while (rsJugadores.next()) {
                int partidoId = rsJugadores.getInt("partido_id");
                Usuario jugador = new Usuario();
                jugador.setId(rsJugadores.getInt("id"));
                jugador.setNombreUsuario(rsJugadores.getString("nombre_usuario"));
                jugador.setMail(rsJugadores.getString("mail"));
                jugador.setUbicacion(rsJugadores.getString("domicilio"));

                jugadoresPorPartido
                    .computeIfAbsent(partidoId, k -> new ArrayList<>())
                    .add(jugador);
            }
        }

        try (PreparedStatement ps = con.prepareStatement(sqlPartidos)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                Deporte deporte = DeporteDAO.getInstance().findDeporteById(rs.getInt("id_deporte"));

                Usuario creador = new Usuario();
                creador.setId(rs.getInt("id_creador"));
                creador.setNombreUsuario(rs.getString("creador_nombre"));
                creador.setMail(rs.getString("mail"));
                creador.setUbicacion(rs.getString("domicilio"));

                Partido partido = new Partido(
                    id,
                    deporte,
                    creador,
                    new Ubicacion(rs.getString("ubicacion")),
                    rs.getTimestamp("comienzo").toLocalDateTime(),
                    rs.getInt("duracion"),
                    FactoryEstado.getEstadoByName(rs.getString("estado_nombre"))
                );
                partido.setMinimoPartidosJugados(rs.getInt("minimo_partidos_jugados"));
                partido.setJugadores(jugadoresPorPartido.getOrDefault(id, new ArrayList<>()));

                partidos.add(partido);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        con.close();
    }

    return partidos;
}


    public Partido findPartidoById(int id) throws Exception {
        Connection con = ConexionDAO.conectar();
        String sql = "SELECT id_deporte, id_creador, ubicacion, comienzo, duracion, nombre, minimo_partidos_jugados FROM partido p JOIN estado e ON p.id_estado = e.id WHERE p.id = ?";
        Partido partido = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idDeporte = rs.getInt("id_deporte");
                int idCreador = rs.getInt("id_creador");
                String ubicacionStr = rs.getString("ubicacion");
                String nombreEstado = rs.getString("nombre");
                int minimo = rs.getInt("minimo_partidos_jugados");

                Deporte deporte = DeporteDAO.getInstance().findDeporteById(idDeporte);
                Usuario creador = UsuarioDAO.getInstance().findUsuarioById(idCreador);
                Ubicacion ubicacion = new Ubicacion(ubicacionStr);
                LocalDateTime comienzo = rs.getTimestamp("comienzo").toLocalDateTime();
                int duracion = rs.getInt("duracion");
                IEstadoPartido estado = FactoryEstado.getEstadoByName(nombreEstado);

                // Jugadores

                String sqlJugadores = "SELECT usuario_id FROM UsuarioPartido WHERE partido_id = ?";
                List<Usuario> jugadores = new ArrayList<>();
                try (PreparedStatement psJugadores = con.prepareStatement(sqlJugadores)) {
                    psJugadores.setInt(1, id);
                    try (ResultSet rsJugadores = psJugadores.executeQuery()) {
                        while (rsJugadores.next()) {
                            int idJugador = rsJugadores.getInt("usuario_id");
                            Usuario jugador = UsuarioDAO.getInstance().findUsuarioById(idJugador);
                            jugadores.add(jugador);
                        }
                    }
                }

                partido = new Partido(id, deporte, creador, ubicacion, comienzo, duracion, estado);
                partido.setJugadores(jugadores);

                partido.setMinimoPartidosJugados(minimo);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return partido;
    }


    public int savePartido(Partido partido) throws SQLException {
        Connection con = ConexionDAO.conectar();
        try {
            String estadoNombre = partido.getEstado().getNombre();
            String sqlEstado = "SELECT id FROM estado WHERE nombre = ?";
            int idEstado;
            try (PreparedStatement psEstado = con.prepareStatement(sqlEstado)) {
                psEstado.setString(1, estadoNombre);
                try (ResultSet rs = psEstado.executeQuery()) {
                    if (rs.next()) {
                        idEstado = rs.getInt("id");
                    } else {
                        throw new SQLException("No se encontró el estado con nombre: " + estadoNombre);
                    }
                }
            }

            String sql = "INSERT INTO partido (id_deporte, id_creador, ubicacion, comienzo, duracion, id_estado, minimo_partidos_jugados) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ? ) RETURNING id";

            int partidoId;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, partido.getDeporte().getId());
                ps.setInt(2, partido.getCreador().getId());
                ps.setString(3, partido.getUbicacion().getDireccion());
                ps.setTimestamp(4, Timestamp.valueOf(partido.getFecha()));
                ps.setInt(5, partido.getDuracion());
                ps.setInt(6, idEstado);
                ps.setInt(7, partido.getMinimoPartidosJugados());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        partidoId = rs.getInt("id");
                    } else {
                        throw new SQLException("No se pudo obtener el ID del partido insertado.");
                    }
                }
            }

            String insertUsuarioPartido = "INSERT INTO UsuarioPartido (usuario_id, partido_id, notificado) VALUES (?, ?, ?)";
            try (PreparedStatement psRelacion = con.prepareStatement(insertUsuarioPartido)) {
                psRelacion.setInt(1, partido.getCreador().getId());
                psRelacion.setInt(2, partidoId);
                psRelacion.setBoolean(3, true);
                psRelacion.executeUpdate();
            }

            return partidoId;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            con.close();
        }
    }


    public void updatePartido(Partido partido) throws SQLException {
        Connection con = ConexionDAO.conectar();

        String sql = "UPDATE partido SET id_deporte = ?, id_creador = ?, ubicacion = ?, comienzo = ?, duracion = ?, id_estado = ?, minimo_partidos_jugados = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            String estadoNombre = partido.getEstado().getNombre();
            String sqlEstado = "SELECT id FROM estado WHERE nombre = ?";
            int idEstado;
            try (PreparedStatement psEstado = con.prepareStatement(sqlEstado)) {
                psEstado.setString(1, estadoNombre);
                try (ResultSet rs = psEstado.executeQuery()) {
                    if (rs.next()) {
                        idEstado = rs.getInt("id");
                    } else {
                        throw new SQLException("No se encontró el estado con nombre: " + estadoNombre);
                    }
                }
            }

            ps.setInt(1, partido.getDeporte().getId());
            ps.setInt(2, partido.getCreador().getId());
            ps.setString(3, partido.getUbicacion().getDireccion());
            ps.setTimestamp(4, Timestamp.valueOf(partido.getFecha()));
            ps.setInt(5, partido.getDuracion());
            ps.setInt(6, idEstado); // Estado dinámico según nombre
            ps.setInt(7, partido.getId());
            ps.setInt(7, partido.getMinimoPartidosJugados());
            ps.setInt(8, partido.getId());

            ps.executeUpdate();

            // Actualizamos lista de jugadores

            String deleteJugadores = "DELETE FROM UsuarioPartido WHERE partido_id = ?";
            try (PreparedStatement psDeleteJugadores = con.prepareStatement(deleteJugadores)) {
                psDeleteJugadores.setInt(1, partido.getId());
                psDeleteJugadores.executeUpdate();
            }

            String insertUsuarioPartido = "INSERT INTO UsuarioPartido (usuario_id, partido_id, notificado) VALUES (?, ?, ?)";
            try (PreparedStatement psInsertJugadores = con.prepareStatement(insertUsuarioPartido)) {
                for (Usuario jugador : partido.getJugadores()) {
                    psInsertJugadores.setInt(1, jugador.getId());
                    psInsertJugadores.setInt(2, partido.getId());
                    psInsertJugadores.setBoolean(3, false);
                    psInsertJugadores.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public void deletePartido(int id) throws SQLException {
        Connection con = ConexionDAO.conectar();
        String sql = "DELETE FROM partido WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public List<Partido> getPartidosDeUsuario(int usuarioId) {
    List<Partido> partidos = new ArrayList<>();
    Connection con = null;

    String sql = """
        SELECT p.id, p.id_deporte, p.id_creador, p.ubicacion, p.comienzo, p.duracion,
               e.nombre AS estado_nombre, p.minimo_partidos_jugados
        FROM partido p
        JOIN UsuarioPartido up ON p.id = up.partido_id
        JOIN estado e ON p.id_estado = e.id
        WHERE up.usuario_id = ?
    """;

    try {
        con = ConexionDAO.conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idDeporte = rs.getInt("id_deporte");
                    int idCreador = rs.getInt("id_creador");
                    String ubicacionStr = rs.getString("ubicacion");
                    LocalDateTime comienzo = rs.getTimestamp("comienzo").toLocalDateTime();
                    int duracion = rs.getInt("duracion");
                    String estadoNombre = rs.getString("estado_nombre");
                    int minimo = rs.getInt("minimo_partidos_jugados");

                    Deporte deporte = DeporteDAO.getInstance().findDeporteById(idDeporte);
                    Usuario creador = UsuarioDAO.getInstance().findUsuarioById(idCreador);
                    Ubicacion ubicacion = new Ubicacion(ubicacionStr);
                    IEstadoPartido estado = FactoryEstado.getEstadoByName(estadoNombre);

                    Partido partido = new Partido(id, deporte, creador, ubicacion, comienzo, duracion, estado);
                    partido.setMinimoPartidosJugados(minimo);

                    // Cargar jugadores
                    String sqlJugadores = "SELECT usuario_id FROM UsuarioPartido WHERE partido_id = ?";
                    List<Usuario> jugadores = new ArrayList<>();
                    try (PreparedStatement psJugadores = con.prepareStatement(sqlJugadores)) {
                        psJugadores.setInt(1, id);
                        try (ResultSet rsJugadores = psJugadores.executeQuery()) {
                            while (rsJugadores.next()) {
                                int idJugador = rsJugadores.getInt("usuario_id");
                                Usuario jugador = UsuarioDAO.getInstance().findUsuarioById(idJugador);
                                jugadores.add(jugador);
                            }
                        }
                    }

                    partido.setJugadores(jugadores);
                    partidos.add(partido);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return partidos;
    }
}
