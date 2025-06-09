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

    private static EstrategiaDAO instance = null;

    private EstrategiaDAO() {
    }

    public static EstrategiaDAO getInstance() {
        if (instance == null) {
            instance = new EstrategiaDAO();
        }
        return instance;
    }

    private Connection conectar() throws SQLException {
        String url = "jdbc:postgresql://db.fecjpibxphahwlqmcssv.supabase.co:5432/postgres";
        String user = "postgres";
        String password = "findamatchuade";
        return DriverManager.getConnection(url, user, password);
    }

    public IEstrategiaEmparejamiento findEstrategiaById(int id) throws SQLException {
        Connection con = conectar();
        String sql = "SELECT * FROM estrategia WHERE id = ?";
        IEstrategiaEmparejamiento estrategia = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");

                switch (nombre.toUpperCase()) {
                    case "CERCANIA":
                        estrategia = new PorCercania();
                        break;
                    case "HISTORIAL":
                        estrategia = new PorHistorial();
                        break;
                    case "NIVEL":
                        estrategia = new PorNivel();
                        break;
                    default:
                        throw new IllegalArgumentException("Estrategia desconocida: " + nombre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        return estrategia;
    }

}
