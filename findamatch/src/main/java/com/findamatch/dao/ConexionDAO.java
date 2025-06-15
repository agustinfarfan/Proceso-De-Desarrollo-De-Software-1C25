package com.findamatch.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDAO {

    private static final String URL = "jdbc:postgresql://dpg-d1498ifdiees73d2f170-a.oregon-postgres.render.com:5432/findamatch?sslmode=require";
    private static final String USER = "dbo";
    private static final String PASSWORD = "lNht7nfjCEH9hmQ03eTT7Z3k4yeVoKZL";

    // private static final String URL =
    // "jdbc:postgresql://db.fecjpibxphahwlqmcssv.supabase.co:5432/postgres?sslmode=require";
    // private static final String USER = "postgres";
    // private static final String PASSWORD = "findamatchuade"; // ← tu contraseña
    // real

    private static ConexionDAO instance = null;

    private ConexionDAO() {
    }

    public static ConexionDAO getInstance() {
        if (instance == null) {
            instance = new ConexionDAO();
        }
        return instance;
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
