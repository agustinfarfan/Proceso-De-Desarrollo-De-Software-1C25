package com.findamatch.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.findamatch.model.Usuario;

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
        String url = "jdbc:postgresql://db.fecjpibxphahwlqmcssv.supabase.co:5432/postgres";
        String user = "postgres";
        String password = "findamatchuade";

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
                int edad = rs.getInt("edad");
                String ubicacion = rs.getString("ubicacion");
                Usuario usuario = new Usuario(id, nombreUsuario, mail, contrasena, edad, ubicacion);
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
                int edad = rs.getInt("edad");
                String ubicacion = rs.getString("ubicacion");
                usuario = new Usuario(idUsuario, nombreUsuario, mail, contrasena, edad, ubicacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return usuario;
    }

    public void saveUsuario(Usuario usuario) throws SQLException {
        Connection con = conectar();
        String sql = "INSERT INTO usuario (id, nombre_usuario, mail, contrasena, edad, ubicacion) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuario.getId());
            ps.setString(2, usuario.getNombreUsuario());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getContrasena());
            ps.setInt(5, usuario.getEdad());
            ps.setString(6, usuario.getUbicacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }

    public void updateUsuario(Usuario usuario) throws SQLException {
        Connection con = conectar();
        String sql = "UPDATE usuario SET nombre_usuario =?, mail =?, contrasena =?, edad =?, ubicacion =? WHERE id =?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getMail());
            ps.setString(3, usuario.getContrasena());
            ps.setInt(4, usuario.getEdad());
            ps.setString(5, usuario.getUbicacion());
            ps.setInt(6, usuario.getId());
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

}
