package com.findamatch.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.findamatch.model.Deporte;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.enums.Nivel;

public class UsuarioDAO {

    ConexionDAO conexionDAO = ConexionDAO.getInstance();
    private static UsuarioDAO instance = null;

    private UsuarioDAO() {
    }

    public static UsuarioDAO getInstance() {
        if (instance == null) {
            instance = new UsuarioDAO();
        }
        return instance;
    }

    public List<Usuario> findAllUsuarios() throws SQLException {
        Connection con = ConexionDAO.conectar();
        String sql = """
                    SELECT
                        u.id AS usuario_id,
                        u.nombre_usuario,
                        u.mail,
                        u.contrasena,
                        u.domicilio,
                        d.id AS deporte_id,
                        d.nombre,
                        d.minJugadores,
                        d.maxJugadores,
                        d.descripcion,
                        ud.nivelJuego,
                        ud.esFavorito
                    FROM usuario u
                    JOIN usuariodeporte ud ON u.id = ud.usuario_id
                    JOIN deporte d ON ud.deporte_id = d.id
                    ORDER BY u.id
                """;

        Map<Integer, Usuario> mapaUsuarios = new LinkedHashMap<>();

        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int usuarioId = rs.getInt("usuario_id");

                Usuario usuario = mapaUsuarios.get(usuarioId);
                if (usuario == null) {
                    usuario = new Usuario();

                    usuario.setId(usuarioId);
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setMail(rs.getString("mail"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setUbicacion(rs.getString("domicilio"));

                    mapaUsuarios.put(usuarioId, usuario);
                }

                // Crear Deporte
                Deporte deporte = new Deporte(
                        rs.getInt("deporte_id"),
                        rs.getString("nombre"),
                        rs.getInt("minJugadores"),
                        rs.getInt("maxJugadores"),
                        rs.getString("descripcion"));

                // Crear UsuarioDeporte
                Nivel nivel = Nivel.valueOf(rs.getString("nivelJuego"));
                boolean esFavorito = rs.getBoolean("esFavorito");

                UsuarioDeporte ud = new UsuarioDeporte(usuario, deporte, nivel, esFavorito);
                usuario.addUsuarioDeporte(ud); // Asegurate de tener este método en la clase Usuario
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }

        return new ArrayList<>(mapaUsuarios.values());
    }

    public Usuario findUsuarioById(int id) throws SQLException {
        Connection con = ConexionDAO.conectar();
        String sql = """
                    SELECT
                        u.id AS usuario_id,
                        u.nombre_usuario,
                        u.mail,
                        u.contrasena,
                        u.domicilio,
                        d.id AS deporte_id,
                        d.nombre,
                        d.minJugadores,
                        d.maxJugadores,
                        d.descripcion,
                        ud.nivelJuego,
                        ud.esFavorito
                    FROM usuario u
                    JOIN usuariodeporte ud ON u.id = ud.usuario_id
                    JOIN deporte d ON ud.deporte_id = d.id
                    WHERE u.id = ?
                """;

        Usuario usuario = null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario_id"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setMail(rs.getString("mail"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setUbicacion(rs.getString("domicilio"));
                }

                Deporte deporte = new Deporte(
                        rs.getInt("deporte_id"),
                        rs.getString("nombre"),
                        rs.getInt("minJugadores"),
                        rs.getInt("maxJugadores"),
                        rs.getString("descripcion"));

                Nivel nivel = Nivel.valueOf(rs.getString("nivelJuego"));
                boolean esFavorito = rs.getBoolean("esFavorito");

                UsuarioDeporte ud = new UsuarioDeporte(usuario, deporte, nivel, esFavorito);
                usuario.addUsuarioDeporte(ud); // ← volvemos a usar este método

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }

        return usuario;
    }

    public int saveUsuario(Usuario usuario) throws SQLException {
        Connection con = ConexionDAO.conectar();
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
        Connection con = ConexionDAO.conectar();
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
        Connection con = ConexionDAO.conectar();
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
        Connection con = ConexionDAO.conectar();

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
        Connection con = ConexionDAO.conectar();
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
