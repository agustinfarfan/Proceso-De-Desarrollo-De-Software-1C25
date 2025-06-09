package com.findamatch.dao;

import java.sql.*;

import com.findamatch.model.estado.IEstadoPartido;

public class EstadoDAO {

    private static EstadoDAO instance = null;

    private EstadoDAO() {
    }

    public static EstadoDAO getInstance() {
        if (instance == null) {
            instance = new EstadoDAO();
        }
        return instance;
    }

    private Connection conectar() throws SQLException {
        String url = "jdbc:postgresql://db.fecjpibxphahwlqmcssv.supabase.co:5432/postgres";
        String user = "postgres";
        String password = "findamatchuade";
        return DriverManager.getConnection(url, user, password);
    }

    public IEstadoPartido findEstadoById(int id) throws SQLException {
        Connection con = conectar();
        String sql = "SELECT * FROM estado WHERE id = ?";
        IEstadoPartido estado = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                // estado = new Estado(id, nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        // return estado;

        return new IEstadoPartido() {
            @Override
            public int getId() {
                return id;
            }

        };
    }
}
