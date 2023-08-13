package com.codecool.seasonalproductdiscounter.service.users;

import com.codecool.seasonalproductdiscounter.model.users.User;
import com.codecool.seasonalproductdiscounter.service.logger.Logger;
import com.codecool.seasonalproductdiscounter.service.persistence.SqliteConnector;
import com.codecool.seasonalproductdiscounter.service.users.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final SqliteConnector sqliteConnector;
    private final Logger logger;

    public UserRepositoryImpl(SqliteConnector sqliteConnector, Logger logger) {
        this.sqliteConnector = sqliteConnector;
        this.logger = logger;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = sqliteConnector.getConnection();

        if (connection == null) {
            logger.logError("Failed to obtain a database connection.");
            return users;
        }

        String query = "SELECT id, username, email FROM users";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");

                User user = new User(id, username, email);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.logError("Error fetching users: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.logError("Error closing database connection: " + e.getMessage());
            }
        }

        return users;
    }

    @Override
    public boolean addUser(User user) {
        Connection connection = sqliteConnector.getConnection();

        if (connection == null) {
            logger.logError("Failed to obtain a database connection.");
            return false;
        }

        String query = "INSERT INTO users (username, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.userName());
            statement.setString(2, user.password());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error adding user: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.logError("Error closing database connection: " + e.getMessage());
            }
        }

        return true;
    }
}
