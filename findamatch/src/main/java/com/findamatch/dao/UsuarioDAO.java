package com.findamatch.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.findamatch.model.Deporte;
import com.findamatch.model.Partido;
import com.findamatch.model.Ubicacion;
import com.findamatch.model.Usuario;
import com.findamatch.model.UsuarioDeporte;
import com.findamatch.model.emparejamiento.estrategias.FactoryEstrategia;
import com.findamatch.model.enums.Nivel;
import com.findamatch.model.estado.FactoryEstado;

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

    public List<Usuario> findAllUsuarios() throws Exception {
        System.out.println("COMIENZO");
        Connection con = ConexionDAO.conectar();
        String sql = """
                    SELECT
                        u.id AS usuario_id,
                        u.nombre_usuario,
                        u.mail,
                        u.contrasena,
                        u.domicilio,
                        e.nombre as nombreEstrategia ,
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
                    JOIN estrategia e on E.id = U.id_estrategia
                    ORDER BY u.id
                """;

        Map<Integer, Usuario> mapaUsuarios = new LinkedHashMap<>();

        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println("COMIENZO USUARIO");
                int usuarioId = rs.getInt("usuario_id");

                Usuario usuario = mapaUsuarios.get(usuarioId);
                if (usuario == null) {
                    usuario = new Usuario();

                    usuario.setId(usuarioId);
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setMail(rs.getString("mail"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setUbicacion(rs.getString("domicilio"));
                    usuario.setEstrategia(FactoryEstrategia.getEstrategiaByName(rs.getString("nombreEstrategia")));

                    mapaUsuarios.put(usuarioId, usuario);

                    // Partidos

                    List<Partido> partidos = new ArrayList<>();

                    String sqlPartidos = "SELECT partido_id FROM usuariopartido WHERE usuario_id = ?";
                    try (PreparedStatement psPartidos = con.prepareStatement(sqlPartidos)) {
                        psPartidos.setInt(1, usuarioId);
                        ResultSet rsPartidos = psPartidos.executeQuery();

                        while (rsPartidos.next()) {
                            System.out.println("COMIENZO PARTIDO");
                            int partidoId = rsPartidos.getInt("partido_id");
                            // Busco partido
                            String sqlPartido = "SELECT id_deporte, ubicacion, comienzo, duracion, nombre FROM partido p JOIN estado e ON p.id_estado = e.id WHERE p.id = ? ";
                            try (PreparedStatement psPartido = con.prepareStatement(sqlPartido)) {
                                psPartido.setInt(1, partidoId);
                                ResultSet rsPartido = psPartido.executeQuery();
                                while (rsPartido.next()) {
                                    Partido partido = new Partido();
                                    partido.setId(partidoId);
                                    partido.setDeporte(
                                            DeporteDAO.getInstance().findDeporteById(rsPartido.getInt("id_deporte")));
                                    partido.setUbicacion(new Ubicacion(rsPartido.getString("ubicacion")));
                                    partido.setFecha(rsPartido.getTimestamp("comienzo").toLocalDateTime());
                                    partido.setDuracion(rsPartido.getInt("duracion"));
                                    partido.setEstado(FactoryEstado.getEstadoByName(rsPartido.getString("nombre")));

                                    partidos.add(partido);
                                }

                            }

                            usuario.setPartidos(partidos);

                        }
                    }
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
                usuario.addUsuarioDeporte(ud);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }

        return new ArrayList<>(mapaUsuarios.values());
    }

    public Usuario findUsuarioById(int id) throws Exception {
        Connection con = ConexionDAO.conectar();

        String sqlUsuario = """
                    SELECT
                        u.id AS usuario_id,
                        u.nombre_usuario,
                        u.mail,
                        u.contrasena,
                        u.domicilio,
                        e.nombre as nombreEstrategia ,
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
                    JOIN estrategia e on E.id = U.id_estrategia
                    WHERE u.id = ?
                """;

        Usuario usuario = null;

        try (PreparedStatement ps = con.prepareStatement(sqlUsuario)) {
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
                    usuario.setEstrategia(FactoryEstrategia.getEstrategiaByName(rs.getString("nombreEstrategia")));

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
                usuario.addUsuarioDeporte(ud);
            }
        }

        if (usuario != null) {
            String sqlPartidos = """
                        SELECT
                            p.id,
                            p.id_creador,
                            p.id_deporte,
                            p.ubicacion,
                            p.comienzo,
                            p.duracion,
                            e.nombre AS estado_nombre
                        FROM partido p
                        JOIN usuariopartido up ON p.id = up.partido_id
                        JOIN estado e ON p.id_estado = e.id
                        WHERE up.usuario_id = ?
                    """;

            try (PreparedStatement ps = con.prepareStatement(sqlPartidos)) {
                ps.setInt(1, usuario.getId());
                ResultSet rs = ps.executeQuery();

                List<Partido> partidos = new ArrayList<>();
                while (rs.next()) {
                    Partido partido = new Partido();
                    partido.setId(rs.getInt("id"));
                    Usuario creador = new Usuario();
                    creador.setId(rs.getInt("id_creador"));
                    partido.setCreador(creador);
                    partido.setDeporte(DeporteDAO.getInstance().findDeporteById(rs.getInt("id_deporte")));
                    partido.setUbicacion(new Ubicacion(rs.getString("ubicacion")));
                    partido.setFecha(rs.getTimestamp("comienzo").toLocalDateTime());
                    partido.setDuracion(rs.getInt("duracion"));
                    partido.setEstado(FactoryEstado.getEstadoByName(rs.getString("estado_nombre")));
                    partidos.add(partido);
                }

                usuario.setPartidos(partidos);
            }
        }

        con.close();
        return usuario;
    }

    public Usuario findUsuarioByUsuario(String username) throws Exception {
        Connection con = ConexionDAO.conectar();

        String sqlUsuario = """
                    SELECT
                        u.id AS usuario_id,
                        u.nombre_usuario,
                        u.mail,
                        u.contrasena,
                        u.domicilio,
                        e.nombre as nombreEstrategia ,
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
                    JOIN estrategia e on E.id = U.id_estrategia
                    WHERE u.nombre_usuario = ?
                """;

        Usuario usuario = null;

        try (PreparedStatement ps = con.prepareStatement(sqlUsuario)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("usuario_id"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setMail(rs.getString("mail"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setUbicacion(rs.getString("domicilio"));
                    usuario.setEstrategia(FactoryEstrategia.getEstrategiaByName(rs.getString("nombreEstrategia")));

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
                usuario.addUsuarioDeporte(ud);
            }
        }

        if (usuario != null) {
            String sqlPartidos = """
                    SELECT
                        p.id,
                        p.id_creador,
                        p.id_deporte,
                        d.nombre AS deporte_nombre,
                        d.minJugadores,
                        d.maxJugadores,
                        d.descripcion AS deporte_descripcion,
                        p.ubicacion,
                        p.comienzo,
                        p.duracion,
                        e.nombre AS estado_nombre
                    FROM partido p
                    JOIN usuariopartido up ON p.id = up.partido_id
                    JOIN estado e ON p.id_estado = e.id
                    JOIN deporte d ON p.id_deporte = d.id
                    WHERE up.usuario_id = ?
                    """;

            try (PreparedStatement ps = con.prepareStatement(sqlPartidos)) {
                ps.setInt(1, usuario.getId());
                ResultSet rs = ps.executeQuery();

                List<Partido> partidos = new ArrayList<>();
                while (rs.next()) {
                    Partido partido = new Partido();
                    partido.setId(rs.getInt("id"));
                    Usuario creador = new Usuario();
                    creador.setId(rs.getInt("id_creador"));
                    partido.setCreador(creador);
                    Deporte deporte = new Deporte(
                            rs.getInt("id_deporte"),
                            rs.getString("deporte_nombre"),
                            rs.getInt("minJugadores"),
                            rs.getInt("maxJugadores"),
                            rs.getString("deporte_descripcion"));
                    partido.setDeporte(deporte);
                    partido.setUbicacion(new Ubicacion(rs.getString("ubicacion")));
                    partido.setFecha(rs.getTimestamp("comienzo").toLocalDateTime());
                    partido.setDuracion(rs.getInt("duracion"));
                    partido.setEstado(FactoryEstado.getEstadoByName(rs.getString("estado_nombre")));
                    partidos.add(partido);
                }

                usuario.setPartidos(partidos);
            }
        }

        con.close();
        return usuario;
    }

    public int saveUsuario(Usuario usuario) throws SQLException {
        Connection con = ConexionDAO.conectar();
        String sql = "INSERT INTO usuario (nombre_usuario, mail, contrasena, domicilio, id_estrategia) VALUES (?,?,?,?, ?) RETURNING id";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getMail());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getUbicacion());
            ps.setInt(5, EstrategiaDAO.findIDEstrategiaByName(usuario.getEstrategia().getNombre()));

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

    /*
     * 
     */
    public void updateUsuario(Usuario usuario) throws SQLException {
        Connection con = ConexionDAO.conectar();

        // Actualiza datos del usuario
        String sqlUsuario = "UPDATE usuario SET nombre_usuario = ?, mail = ?, contrasena = ?, domicilio = ?, id_estrategia = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlUsuario)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getMail());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getUbicacion());
            ps.setInt(5, EstrategiaDAO.findIDEstrategiaByName(usuario.getEstrategia().getNombre()));
            ps.setInt(6, usuario.getId());
            ps.executeUpdate();
        }

        // Actualiza nivelJuego y esFavorito en usuariodeporte
        String sqlUsuarioDeporte = """
                UPDATE usuariodeporte
                SET nivelJuego = ?, esFavorito = ?
                WHERE usuario_id = ? AND deporte_id = ?
                """;
        try (PreparedStatement ps = con.prepareStatement(sqlUsuarioDeporte)) {
            for (UsuarioDeporte ud : usuario.getDeportes()) {
                ps.setString(1, ud.getNivelJuego().toString());
                ps.setBoolean(2, ud.isFavorito());
                ps.setInt(3, usuario.getId());
                ps.setInt(4, ud.getDeporte().getId());
                ps.executeUpdate();
            }
        }

        con.close();
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

    public List<UsuarioDeporte> getUsuarioDeportes() throws Exception {
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
            ps.setBoolean(2, usuarioDeporte.isFavorito());
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
