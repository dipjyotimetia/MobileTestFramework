package com.core;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.DriverManager;
import java.sql.*;

public class DataActions<T> extends ApiActions<T> {

    private Logger logger = LogManager.getLogger(DataActions.class);

    private static String JDBC_URL = "jdbc:sqlserver://wgtuatdbs1;databaseName=TBS;integratedSecurity=true";

    /**
     * Get DB connection
     */
    private void getDbConnection() {
        ResultSet rs = null;
        Connection connObj = null;
        Statement statement = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL);
            if (connObj != null) {
                statement = connObj.createStatement();
                String queryString = "select TOP 10 * from TBS.dbo.Event e where e.EventTypeID='1';";
                rs = statement.executeQuery(queryString);
                while (rs.next()) {
                    logger.info(rs.getString(2));
                }
            }
        } catch (Exception sqlException) {
            logger.error(sqlException);
        } finally {
            finallyBlock(rs, connObj, statement);
        }
    }

    /**
     * Execute query
     * @param query query
     * @return result
     */
    private String ExecuteQuery(String query) {
        String resultValue = "";
        String columnName = "";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
        try (Connection connection = java.sql.DriverManager.getConnection(JDBC_URL)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            try {
                                if (rs.getString(i).toString() == null && i != columnCount) {
                                }
                            } catch (NullPointerException e) {
                                resultValue = "NULL";
                                logger.info("column name:" + columnName + "|" + "Column value:" + resultValue);
                                continue;
                            }
                            columnName = rsmd.getColumnName(i);
                            resultValue = rs.getString(i).toString();
                            logger.info("column name:" + columnName + "|" + "Column value:" + resultValue);
                        }
                    }
                }
                connection.close();
            } catch (SQLException sq) {
                logger.error(sq);
            }
        } catch (SQLException sq) {
            logger.error(sq);
        }
        return resultValue;
    }

    private void displayRow(String title, ResultSet rs) {
        try {
            while (rs.next()) {
                logger.info(rs.getString(title));
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void finallyBlock(ResultSet rs, Connection connObj, Statement stmt) {
        if (rs != null) try {
            rs.close();
        } catch (Exception e) {
            logger.error(e);
        }
        if (connObj != null) try {
            connObj.close();
        } catch (Exception e) {
            logger.error(e);
        }
        if (stmt != null) try {
            stmt.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Query executer
     * @param query query
     * @return resultset
     */
    private ResultSet query(String query) {
        Connection connObj = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL);
            if (connObj != null) {
                Statement statement = connObj.createStatement();
                ResultSet rs = statement.executeQuery(query);
                return rs;
            }
        } catch (Exception sqlException) {
            logger.error(sqlException);
        }
        return null;
    }

}
