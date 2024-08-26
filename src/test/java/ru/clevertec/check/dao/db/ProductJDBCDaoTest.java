package ru.clevertec.check.dao.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.core.dto.CreateProductDTO;
import ru.clevertec.check.core.dto.ProductDTO;
import ru.clevertec.check.dao.db.ds.DatabaseConfig;
import ru.clevertec.check.dao.db.ds.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.framework;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductJDBCDaoTest {
    @Mock
    private DatabaseConfig databaseConfigMock;
    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private ResultSet resultSetMock;
    @InjectMocks
    private ProductJDBCDao productJDBCDao;


    @BeforeAll
    static void setUpStatic() {
        mockStatic(DatabaseConnectionFactory.class);
    }

    @BeforeEach
    void setUp() throws SQLException {

        when(DatabaseConnectionFactory.getConnection(databaseConfigMock)).thenReturn(connectionMock);

    }

    @Test
    void testGetSuccess() throws SQLException {
        long productId = 1L;

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getLong("id")).thenReturn(productId);
        when(resultSetMock.getString("description")).thenReturn("Milk");
        when(resultSetMock.getDouble("price")).thenReturn(1.00);
        when(resultSetMock.getInt("quantity_in_stock")).thenReturn(4);
        when(resultSetMock.getBoolean("wholesale_product")).thenReturn(true);

        ProductDTO expectedProductDTO = getProductDTO();

        ProductDTO actualProduct = productJDBCDao.get(productId);

        assertEquals(expectedProductDTO, actualProduct);

    }

    @Test
    void testCreateSuccess() throws SQLException {
        long productId = 1L;
        CreateProductDTO createProductDTO = getCreateProductDTO();

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getLong("id")).thenReturn(productId);

        ProductDTO expectedProductDTO = getProductDTO();
        ProductDTO actualProductDTO = productJDBCDao.create(createProductDTO);

        assertEquals(expectedProductDTO, actualProductDTO);

    }

    @Test
    void testUpdateSuccess() throws SQLException {
        ProductDTO productDTO = getProductDTO();

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        productJDBCDao.update(productDTO);

        verify(preparedStatementMock).setString(1, productDTO.getDescription());
        verify(preparedStatementMock).setDouble(2, productDTO.getPrice());
        verify(preparedStatementMock).setInt(3, productDTO.getQuantityInStock());
        verify(preparedStatementMock).setBoolean(4, productDTO.isWholesaleProduct());
        verify(preparedStatementMock).setLong(5, productDTO.getId());
        verify(preparedStatementMock).executeUpdate();

    }

    @Test
    void testDeleteSuccess() throws SQLException {
        long productId = 1L;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        productJDBCDao.delete(productId);

        verify(preparedStatementMock).setLong(1, productId);
        verify(preparedStatementMock).executeUpdate();

    }

    @AfterAll
    static void tearDown() {
        framework().clearInlineMocks();
    }

    private ProductDTO getProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1);
        dto.setDescription("Milk");
        dto.setPrice(1.0);
        dto.setQuantityInStock(4);
        dto.setWholesaleProduct(true);
        return dto;
    }

    private CreateProductDTO getCreateProductDTO() {
        CreateProductDTO dto = new CreateProductDTO();
        dto.setDescription("Milk");
        dto.setPrice(1.00);
        dto.setQuantityInStock(4);
        dto.setWholesaleProduct(true);
        return dto;
    }
}