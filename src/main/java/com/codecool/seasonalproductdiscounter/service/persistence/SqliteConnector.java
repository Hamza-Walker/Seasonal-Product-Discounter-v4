package com.codecool.seasonalproductdiscounter.service.persistence;

import com.codecool.seasonalproductdiscounter.service.logger.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnector {

    private final String dbFile;
    private final Logger logger;

    public SqliteConnector(String dbFile, Logger logger) {
        this.dbFile = dbFile;
        this.logger = logger;
    }

    public Connection getConnection() {
        Connection conn;
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create a connection to the database
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

            logger.logInfo("Connected to SQLite database: " + dbFile);
        } catch (ClassNotFoundException e) {
            logger.logError("SQLite JDBC driver not found.");
        } catch (SQLException e) {
            logger.logError("Error connecting to SQLite database: " + e.getMessage());
        }


        return null;
    }
}
