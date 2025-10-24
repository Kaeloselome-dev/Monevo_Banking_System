package com.examplemonevo.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connection {
    private static final String URL = "jdbc:sqlite:C:src/data/Monevo_Banking_System.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}