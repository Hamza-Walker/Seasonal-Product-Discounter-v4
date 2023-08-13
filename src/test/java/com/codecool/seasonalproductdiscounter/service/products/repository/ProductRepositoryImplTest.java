package com.codecool.seasonalproductdiscounter.service.products.repository;

import com.codecool.seasonalproductdiscounter.model.enums.Color;
import com.codecool.seasonalproductdiscounter.model.enums.Season;
import com.codecool.seasonalproductdiscounter.model.products.Product;
import com.codecool.seasonalproductdiscounter.service.logger.Logger;
import com.codecool.seasonalproductdiscounter.service.persistence.SqliteConnector;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRepositoryImplTest {

    private ProductRepositoryImpl productRepository;

    @Mock
    private SqliteConnector mockSqliteConnector;

    @Mock
    private Logger mockLogger;

    @BeforeEach
    void setUp() {
        mockSqliteConnector = Mockito.mock(SqliteConnector.class);
        mockLogger = Mockito.mock(Logger.class);
        productRepository = new ProductRepositoryImpl(mockSqliteConnector, mockLogger);
    }

    @Test
    void testGetAvailableProducts() {
        // Mocking database connection
        when(mockSqliteConnector.getConnection()).thenReturn(null);

        List<Product> availableProducts = productRepository.getAvailableProducts();
        assertNotNull(availableProducts);
        assertEquals(0, availableProducts.size());
    }

    @Test
    void testAddProducts() {
        // Mocking database connection
        when(mockSqliteConnector.getConnection()).thenReturn(null);

        List<Product> products = new ArrayList<>();
        Product product = new Product(1, "Test Product", Color.RED, Season.SUMMER, 19.99, false);
        products.add(product);

        assertTrue(productRepository.addProducts(products));
    }

    @Test
    void testSetProductAsSold() {
        // Mocking database connection
        when(mockSqliteConnector.getConnection()).thenReturn(null);

        Product product = new Product(1, "Test Product", Color.RED, Season.SUMMER, 19.99, false);

        assertTrue(productRepository.setProductAsSold(product));
    }
}
