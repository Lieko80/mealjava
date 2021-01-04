package org.isa.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class MealwithDB {
    private final String url = "jdbc:mysql://localhost:3306/mealwith";
    private final String user = "root";
    private final String password = "";
    public Connection conn = null;

    public MealwithDB() throws SQLException {
    }

    public Connection getConn() throws SQLException {
        conn = DriverManager.getConnection(url, user, password);

        return conn;
    }
}
