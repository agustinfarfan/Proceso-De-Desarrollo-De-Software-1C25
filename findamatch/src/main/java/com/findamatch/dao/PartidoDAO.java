package com.findamatch.dao;

import com.findamatch.model.*;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.estado.FactoryEstado;
import com.findamatch.model.estado.IEstadoPartido;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "SELECT * FROM partido";
        List<Partido> partidos = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int idDeporte = rs.getInt("id_deporte");
                int idCreador = rs.getInt("id_creador");
                String ubicacionStr = rs.getString("ubicacion");
                LocalDateTime comienzo = rs.getTimestamp("comienzo").toLocalDateTime();
                int duracion = rs.getInt("duracion");
                int idEstado = rs.getInt("id_estado");
                int idEstrategia = rs.getInt("id_estrategia");

                Deporte deporte = DeporteDAO.getInstance().findDeporteById(idDeporte);
                Usuario creador = UsuarioDAO.getInstance().findUsuarioById(idCreador);
                Ubicacion ubicacion = new Ubicacion(ubicacionStr);
                IEstadoPartido estado = EstadoDAO.getInstance().findEstadoById(idEstado);

                Partido partido = new Partido(id, deporte, creador, ubicacion, comienzo, duracion, estado);
                partidos.add(partido);
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
        String sql = "SELECT id_deporte, id_creador, ubicacion, comienzo, duracion, nombre FROM partido p JOIN estado e ON p.id_estado = e.id WHERE p.id = ? ";
        Partido partido = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idDeporte = rs.getInt("id_deporte");
                int idCreador = rs.getInt("id_creador");
                String ubicacionStr = rs.getString("ubicacion");
                LocalDateTime comienzo = rs.getTimestamp("comienzo").toLocalDateTime();
                int duracion = rs.getInt("duracion");
                String nombreEstado = rs.getString("nombre");

                Deporte deporte = DeporteDAO.getInstance().findDeporteById(idDeporte);
                Usuario creador = UsuarioDAO.getInstance().findUsuarioById(idCreador);
                Ubicacion ubicacion = new Ubicacion(ubicacionStr);
                IEstadoPartido estado = FactoryEstado.getEstadoByName(nombreEstado);

                partido = new Partido(id, deporte, creador, ubicacion, comienzo, duracion, estado);
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

            String sql = "INSERT INTO partido (id_deporte, id_creador, ubicacion, comienzo, duracion, id_estado) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

            int partidoId;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, partido.getDeporte().getId());
                ps.setInt(2, partido.getCreador().getId());
                ps.setString(3, partido.getUbicacion().getDireccion());
                ps.setTimestamp(4, Timestamp.valueOf(partido.getFecha()));
                ps.setInt(5, partido.getDuracion());
                ps.setInt(6, idEstado); // Estado dinámico según nombre

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        partidoId = rs.getInt("id");
                    } else {
                        throw new SQLException("No se pudo obtener el ID del partido insertado.");
                    }
                }
            }

            // Metemos al creador en la tabla UsuarioPartido
            String insertUsuarioPartido = "INSERT INTO UsuarioPartido (usuario_id, partido_id, confirmado) VALUES (?, ?, ?)";
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
        String sql = "UPDATE partido SET id_deporte = ?, id_creador = ?, ubicacion = ?, comienzo = ?, duracion = ?, id_estado = ?, id_estrategia = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, partido.getDeporte().getId());
            ps.setInt(2, partido.getCreador().getId());
            ps.setString(3, partido.getUbicacion().toString());
            ps.setTimestamp(4, Timestamp.valueOf(partido.getFecha()));
            ps.setInt(5, partido.getDuracion());
            ps.setInt(6, 1);// partido.getEstado().getId()); // SWITCH PARA AGARRAR EL ID POR EL NOMBRE |
                            // PREGUNTAR AL PROFE
                            // POR ID
            ps.setInt(7, 1); // partido.getEstrategiaEmparejamiento().getId()); MISMO PROBLEMA
            ps.setInt(8, partido.getId());
            ps.executeUpdate();
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
}
