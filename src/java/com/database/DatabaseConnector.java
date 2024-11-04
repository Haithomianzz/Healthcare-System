package com.database;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides methods to connect to the database.
 */
public class DatabaseConnector {

    private static final String CONN_STRING = "your_connection_string";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    private static MysqlDataSource dataSource = new MysqlDataSource();

    static {
        dataSource.setURL(CONN_STRING);
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
    }

    /**
     * Connects to the database and returns the connection.
     *
     * @return the database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection connect() throws SQLException {
        return dataSource.getConnection();
    }
}