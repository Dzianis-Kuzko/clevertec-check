package ru.clevertec.check.dao.db.ds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionFactory {
    private static final String DB_DRIVER = "org.postgresql.Driver";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private DatabaseConnectionFactory() {
    }

    public static Connection getConnection(DatabaseConfig databaseConfig) throws SQLException {
        String url = databaseConfig.getUrl();
        Properties props = new Properties();
        props.setProperty("user", databaseConfig.getUsername());
        props.setProperty("password", databaseConfig.getPassword());
        return DriverManager.getConnection(url, props);
    }
}
