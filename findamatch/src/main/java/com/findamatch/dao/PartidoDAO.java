package com.findamatch.dao;

import com.findamatch.model.*;
import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.estado.IEstadoPartido;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartidoDAO {

    private static PartidoDAO instance = null;

    private PartidoDAO() {
    }

    public static PartidoDAO getInstance() {
        if (instance == null) {
            instance = new PartidoDAO();
        }
        return instance;
    }

    private Connection conectar() throws SQLException {
        String url = "jdbc:postgresql://dpg-d1498ifdiees73d2f170-a.oregon-postgres.render.com:5432/findamatch?sslmode=require";
        String user = "dbo";
        String password = "lNht7nfjCEH9hmQ03eTT7Z3k4yeVoKZL";
        return DriverManager.getConnection(url, user, password);
    }

    public List<Partido> findAllPartidos() throws Exception {
        Connection con = conectar();
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
                IEstrategiaEmparejamiento estrategia = EstrategiaDAO.getInstance().findEstrategiaById(idEstrategia);

                Partido partido = new Partido(id, deporte, creador, ubicacion, comienzo, duracion, estado, estrategia);
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
        Connection con = conectar();
        String sql = "SELECT * FROM partido WHERE id = ?";
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
                int idEstado = rs.getInt("id_estado");
                int idEstrategia = rs.getInt("id_estrategia");

                Deporte deporte = DeporteDAO.getInstance().findDeporteById(idDeporte);
                Usuario creador = UsuarioDAO.getInstance().findUsuarioById(idCreador);
                Ubicacion ubicacion = new Ubicacion(ubicacionStr);
                IEstadoPartido estado = EstadoDAO.getInstance().findEstadoById(idEstado);
                IEstrategiaEmparejamiento estrategia = EstrategiaDAO.getInstance().findEstrategiaById(idEstrategia);

                partido = new Partido(id, deporte, creador, ubicacion, comienzo, duracion, estado, estrategia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return partido;
    }

    public int savePartido(Partido partido) throws SQLException {
        Connection con = conectar();
        String sql = "INSERT INTO partido (id_deporte, id_creador, ubicacion, comienzo, duracion, id_estado, id_estrategia) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, partido.getDeporte().getId());
            ps.setInt(2, partido.getCreador().getId());
            ps.setString(3, partido.getUbicacion().toString());
            ps.setTimestamp(4, Timestamp.valueOf(partido.getFecha()));
            ps.setInt(5, partido.getDuracion());
            ps.setInt(6, 1);// El estado inicial siempre va a ser 1
            ps.setInt(7, 1); // , partido.getEstrategiaEmparejamiento().getId()); // VER COMO HACER ESTA
                             // PARTE

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("No se pudo obtener el ID del partido insertado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            con.close();
        }
    }

    public void updatePartido(Partido partido) throws SQLException {
        Connection con = conectar();
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
        Connection con = conectar();
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
