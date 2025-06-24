package com.findamatch.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.findamatch.model.emparejamiento.IEstrategiaEmparejamiento;
import com.findamatch.model.emparejamiento.estrategias.PorCercania;
import com.findamatch.model.emparejamiento.estrategias.PorHistorial;
import com.findamatch.model.emparejamiento.estrategias.PorNivel;

public class EstrategiaDAO {

    ConexionDAO conexionDAO = ConexionDAO.getInstance();
    private static EstrategiaDAO instance = null;

    private EstrategiaDAO() {
    }

    public static EstrategiaDAO getInstance() {
        if (instance == null) {
            instance = new EstrategiaDAO();
        }
        return instance;
    }

    public static int findIDEstrategiaByName(String name) throws SQLException {
        Connection con = ConexionDAO.conectar();
        String sql = "SELECT * FROM estrategia WHERE nombre = ?";
        int idEstrategia = -1;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idEstrategia = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return idEstrategia;
    }

}
