package com.codecool.seasonalproductdiscounter.service.products.repository;

import com.codecool.seasonalproductdiscounter.model.enums.Color;
import com.codecool.seasonalproductdiscounter.model.enums.Season;
import com.codecool.seasonalproductdiscounter.model.products.Product;
import com.codecool.seasonalproductdiscounter.service.persistence.SqliteConnector;
import com.codecool.seasonalproductdiscounter.service.logger.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {

    private final SqliteConnector sqliteConnector;
    private final Logger logger;

    public ProductRepositoryImpl(SqliteConnector sqliteConnector, Logger logger) {
        this.sqliteConnector = sqliteConnector;
        this.logger = logger;
    }

    @Override
    public List<Product> getAvailableProducts() {
        List<Product> availableProducts = new ArrayList<>();
        Connection connection = sqliteConnector.getConnection();

        if (connection == null) {
            logger.logError("Failed to obtain a database connection.");
            return availableProducts;
        }

        String query = "SELECT id, name, color, season, price, sold FROM products";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String colorString = resultSet.getString("color");
                Color color = Color.valueOf(colorString);
                String seasonString = resultSet.getString("season");
                Season season = Season.valueOf(seasonString);
                double price = resultSet.getDouble("price");
                boolean sold = resultSet.getBoolean("sold");

                Product product = new Product(id, name, color, season, price, sold);
                availableProducts.add(product);
            }
        } catch (SQLException e) {
            logger.logError("Error fetching available products: " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.logError("Error closing database connection: " + e.getMessage());
            }
        }

        return availableProducts;
    }

    @Override
    public boolean addProducts(List<Product> products) {
        Connection connection = sqliteConnector.getConnection();

        if (connection == null) {
            logger.logError("Failed to obtain a database connection.");
            return false;
        }

        String query = "INSERT INTO products (name, color, season, price, sold) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (Product product : products) {
                statement.setString(1, product.name());
                statement.setString(2, product.color().name());
                statement.setString(3, product.season().name());
                statement.setDouble(4, product.price());
                statement.setBoolean(5, product.sold());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            logger.logError("Error adding products: " + e.getMessage());
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

    @Override
    public boolean setProductAsSold(Product product) {
        Connection connection = sqliteConnector.getConnection();

        if (connection == null) {
            logger.logError("Failed to obtain a database connection.");
            return false;
        }

        String query = "UPDATE products SET sold = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, true);
            statement.setInt(2, product.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.logError("Error setting product as sold: " + e.getMessage());
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
