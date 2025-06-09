package com.findamatch.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.findamatch.model.Deporte;

public class DeporteDAO {

    private static DeporteDAO instance = null;

    private DeporteDAO() {
    }

    public static DeporteDAO getInstance() {
        if (instance == null) {
            instance = new DeporteDAO();
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

    public List<Deporte> findAllDeportes() {
        List<Deporte> deportes = new ArrayList<>();

        String sql = "SELECT id, nombre, minJugadores, maxJugadores, descripcion FROM Deporte";

        try (Connection conn = conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Deporte deporte = new Deporte(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("minJugadores"),
                        rs.getInt("maxJugadores"),
                        rs.getString("descripcion"));
                deportes.add(deporte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deportes;
    }

    public Deporte findDeporteById(int id) {
        Deporte deporte = null;
        String sql = "SELECT id, nombre, minJugadores, maxJugadores, descripcion FROM Deporte WHERE id = ?";

        try (Connection conn = conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // ← FALTA ESTO
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    deporte = new Deporte(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getInt("minJugadores"),
                            rs.getInt("maxJugadores"),
                            rs.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deporte;
    }

    public void saveDeporte(Deporte deporte) {
        String sql = "INSERT INTO Deporte (nombre, minJugadores, maxJugadores, descripcion) VALUES (?,?,?,?)";

        try (Connection conn = conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, deporte.getNombre());
            stmt.setInt(2, deporte.getCantMinJugadores());
            stmt.setInt(3, deporte.getCantMaxJugadores());
            stmt.setString(4, deporte.getDescripcion());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDeporte(Deporte deporte) {
        String sql = "UPDATE Deporte SET nombre = ?, minJugadores = ?, maxJugadores = ?, descripcion = ? WHERE id = ?";

        try (Connection conn = conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, deporte.getNombre());
            stmt.setInt(2, deporte.getCantMinJugadores());
            stmt.setInt(3, deporte.getCantMaxJugadores());
            stmt.setString(4, deporte.getDescripcion());
            stmt.setInt(5, deporte.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDeporte(int id) {
        String sql = "DELETE FROM Deporte WHERE id = ?";

        try (Connection conn = conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
