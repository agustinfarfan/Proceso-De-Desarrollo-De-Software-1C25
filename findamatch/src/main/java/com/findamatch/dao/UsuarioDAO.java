package com.findamatch.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.findamatch.model.Deporte;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.enums.Nivel;

public class UsuarioDAO {

    private static UsuarioDAO instance = null;

    private UsuarioDAO() {
    }

    public static UsuarioDAO getInstance() {
        if (instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }

    // Habria que implementar el metodo en una misma clase para no repetirlo en
    // todos los dao

    private Connection conectar() throws SQLException {
        String url = "jdbc:postgresql://dpg-d1498ifdiees73d2f170-a.oregon-postgres.render.com:5432/findamatch?sslmode=require";
        String user = "dbo";
        String password = "lNht7nfjCEH9hmQ03eTT7Z3k4yeVoKZL";
        return DriverManager.getConnection(url, user, password);
    }

    public List<Usuario> findAllUsuarios() throws SQLException {
        Connection con = conectar();
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombreUsuario = rs.getString("nombre_usuario");
                String mail = rs.getString("mail");
                String contrasena = rs.getString("contrasena");
                String ubicacion = rs.getString("domicilio");
                Usuario usuario = new Usuario(id, nombreUsuario, mail, contrasena, ubicacion);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return usuarios;
    }

    public Usuario findUsuarioById(int id) throws SQLException {
        Connection con = conectar();
        String sql = "SELECT * FROM usuario WHERE id =?";
        Usuario usuario = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idUsuario = rs.getInt("id");
                String nombreUsuario = rs.getString("nombre_usuario");
                String mail = rs.getString("mail");
                String contrasena = rs.getString("contrasena");
                String ubicacion = rs.getString("domicilio");
                usuario = new Usuario(idUsuario, nombreUsuario, mail, contrasena, ubicacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return usuario;
    }

    public int saveUsuario(Usuario usuario) throws SQLException {
        Connection con = conectar();
        String sql = "INSERT INTO usuario (nombre_usuario, mail, contrasena, domicilio) VALUES (?,?,?,?) RETURNING id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getMail());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getUbicacion());

            // Obtener el ID generado
            int usuarioId = -1;
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuarioId = rs.getInt("id");
                } else {
                    throw new SQLException("No se pudo obtener el ID del usuario insertado.");
                }
            }

            // CREAMOS LAS RELACIONES CON DEPORTE DEFAULTS
            String insertRelaciones = """
                        INSERT INTO usuariodeporte (usuario_id, deporte_id)
                        SELECT ?, id FROM deporte
                    """;
            try (PreparedStatement psRelacion = con.prepareStatement(insertRelaciones)) {
                psRelacion.setInt(1, usuarioId);
                psRelacion.executeUpdate();
            }

            return usuarioId;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            con.close();
        }
    }

    public void updateUsuario(Usuario usuario) throws SQLException {
        Connection con = conectar();
        String sql = "UPDATE usuario SET nombre_usuario =?, mail =?, contrasena =?, domicilio =? WHERE id =?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getMail());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getUbicacion());
            ps.setInt(5, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public void deleteUsuario(int id) throws SQLException {
        Connection con = conectar();
        String sql = "DELETE FROM usuario WHERE id =?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public List<UsuarioDeporte> getUsuarioDeportes() throws SQLException {
        List<UsuarioDeporte> lista = new ArrayList<>();
        Connection con = conectar();

        String sql = "SELECT usuario_id, deporte_id, nivelJuego, esFavorito FROM usuariodeporte";

        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int usuarioId = rs.getInt("usuario_id");
                int deporteId = rs.getInt("deporte_id");
                String nivelJuego = rs.getString("nivelJuego");
                boolean esFavorito = rs.getBoolean("esFavorito");

                // Obtener Usuario y Deporte completos
                Usuario usuario = findUsuarioById(usuarioId); // asumido existente
                DeporteDAO deporteDAO = DeporteDAO.getInstance();
                Deporte deporte = deporteDAO.findDeporteById(deporteId); // asumido existente

                // Crear UsuarioDeporte
                UsuarioDeporte ud = new UsuarioDeporte(usuario, deporte, Nivel.valueOf(nivelJuego), esFavorito);
                lista.add(ud);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }

        return lista;
    }

    public void updateUsuarioDeporte(UsuarioDeporte usuarioDeporte) throws SQLException {
        Connection con = conectar();
        String sql = "UPDATE usuariodeporte " +
                "SET nivelJuego = ?, esFavorito = ? " +
                "WHERE usuario_id = ? AND deporte_id = ?";

        try (
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuarioDeporte.getNivelJuego().name()); // suponiendo enum
            ps.setBoolean(2, usuarioDeporte.isEsFavorito());
            ps.setInt(3, usuarioDeporte.getUsuario().getId());
            ps.setInt(4, usuarioDeporte.getDeporte().getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

}
