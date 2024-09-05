package ru.clevertec.check.dao.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.core.dto.CreateDiscountCardDTO;
import ru.clevertec.check.core.dto.DiscountCardDTO;
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
class DiscountCardJDBCDaoTest {
    @Mock
    private DatabaseConfig databaseConfigMock;
    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private ResultSet resultSetMock;
    @InjectMocks
    private DiscountCardJDBCDao discountCardJDBCDao;


    @BeforeAll
    static void setUpStatic() {
        mockStatic(DatabaseConnectionFactory.class);

    }

    @BeforeEach
    void setUp() throws SQLException {
        when(DatabaseConnectionFactory.getConnection(databaseConfigMock)).thenReturn(connectionMock);
    }

    @Test
    void testGetByIDSuccess() throws SQLException {
        int discountCardId = 1;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("id")).thenReturn(discountCardId);
        when(resultSetMock.getString("number")).thenReturn("1111");
        when(resultSetMock.getInt("amount")).thenReturn(3);

        DiscountCardDTO expectedDC = getDiscountCardDTO();

        DiscountCardDTO actualDC = discountCardJDBCDao.get(discountCardId);

        assertEquals(expectedDC, actualDC);
    }

    @Test
    void testCreateSuccess() throws SQLException {
        long discountCardId = 1;
        CreateDiscountCardDTO createDiscountCardDTO = getCreateDiscountCardDTO();

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getLong("id")).thenReturn(discountCardId);

        DiscountCardDTO expectedDC = getDiscountCardDTO();

        DiscountCardDTO actualDC = discountCardJDBCDao.create(createDiscountCardDTO);

        assertEquals(expectedDC, actualDC);

    }

    @Test
    void testUpdateSuccess() throws SQLException {
        DiscountCardDTO discountCardDTO = getDiscountCardDTO();

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        discountCardJDBCDao.update(discountCardDTO);

        verify(preparedStatementMock).setInt(1, Integer.parseInt(discountCardDTO.getNumber()));
        verify(preparedStatementMock).setInt(2, discountCardDTO.getDiscountAmount());
        verify(preparedStatementMock).setLong(3, discountCardDTO.getId());
        verify(preparedStatementMock).executeUpdate();

    }

    @Test
    void testDeleteSuccess() throws SQLException {
        long discountCardId = 1;
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        discountCardJDBCDao.delete(discountCardId);

        verify(preparedStatementMock).setLong(1, discountCardId);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    void testGetByNumber() throws SQLException {
        String discountCardNumber = "1111";

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("id")).thenReturn(1);
        when(resultSetMock.getString("number")).thenReturn(discountCardNumber);
        when(resultSetMock.getInt("amount")).thenReturn(3);

        DiscountCardDTO expectedDC = getDiscountCardDTO();

        DiscountCardDTO actualDC = discountCardJDBCDao.get(discountCardNumber);

        assertEquals(expectedDC, actualDC);
    }

    @AfterAll
    static void tearDown() {
        framework().clearInlineMocks();
    }

    private DiscountCardDTO getDiscountCardDTO() {
        DiscountCardDTO dto = new DiscountCardDTO();
        dto.setId(1);
        dto.setNumber("1111");
        dto.setDiscountAmount(3);
        return dto;
    }

    private CreateDiscountCardDTO getCreateDiscountCardDTO() {
        CreateDiscountCardDTO dto = new CreateDiscountCardDTO();
        dto.setNumber("1111");
        dto.setDiscountAmount(3);
        return dto;
    }

}