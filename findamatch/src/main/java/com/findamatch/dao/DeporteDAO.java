package com.findamatch.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.findamatch.model.dto.DeporteDTO;

public class DeporteDAO {

    private Connection conectar() throws SQLException {
        String url = "jdbc:postgresql://db.fecjpibxphahwlqmcssv.supabase.co:5432/postgres";
        String user = "postgres";
        String password = "findamatchuade";

        return DriverManager.getConnection(url, user, password);
    }

    public List<Deporte> obtenerDeportes() {
        List<DeporteDTO> deportes = new ArrayList<>();

        String sql = "SELECT nombre, minJugadores, maxJugadores, descripcion FROM Deporte";

        try (Connection conn = conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DeporteDTO dto = new DeporteDTO(
                        rs.getString("nombre"),
                        rs.getInt("minJugadores"),
                        rs.getInt("maxJugadores"),
                        rs.getString("descripcion"));
                deportes.add(dto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deportes;
    }
}
