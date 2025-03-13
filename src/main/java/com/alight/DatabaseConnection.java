package com.alight;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/BANK?useSSL=false";
    private static final String USER = "alight";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() throws SQLException {
        Driver driver = new com.mysql.cj.jdbc.Driver();
        DriverManager.registerDriver(driver);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
