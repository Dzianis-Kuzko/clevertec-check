package ru.clevertec.check.dao.db.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatabaseConnectionFactoryTest {
    @Mock
    private static DatabaseConfig databaseConfigMock;
    @Mock
    private static Connection connectionMock;

    @BeforeEach
    void configureMocks() {
        when(databaseConfigMock.getUrl()).thenReturn("jdbc:postgresql://localhost:5432/testdb");
        when(databaseConfigMock.getUsername()).thenReturn("testuser");
        when(databaseConfigMock.getPassword()).thenReturn("testpass");
    }

    @Test
    void testGetConnection() throws SQLException {
        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {

            driverManagerMock.when(() ->
                    DriverManager.getConnection(any(String.class), any(Properties.class))
            ).thenReturn(connectionMock);

            Connection connection = DatabaseConnectionFactory.getConnection(databaseConfigMock);
            assertEquals(connectionMock, connection);

            Properties expectedProperties = new Properties();
            expectedProperties.setProperty("user", "testuser");
            expectedProperties.setProperty("password", "testpass");

            driverManagerMock.verify(() ->
                    DriverManager.getConnection(eq("jdbc:postgresql://localhost:5432/testdb"), eq(expectedProperties))
            );
        }
    }
}