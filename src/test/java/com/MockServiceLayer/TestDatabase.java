package com.MockServiceLayer;

import org.testng.annotations.Test;

import java.sql.*;

public class TestDatabase {

    @Test
    public void TestNewDB() {
        ResultSet rs = null;
        Statement statement = null;
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:input/sqlite/testdata.db";
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                statement = conn.createStatement();
                String query = "select * from data";
                rs = statement.executeQuery((query));
                while (rs.next()) {
                    System.out.println(rs.getString(2));
                }
            }
            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
